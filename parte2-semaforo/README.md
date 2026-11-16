# Parte 2 — Threads e Semáforos: Contador Concorrente

Demonstração de uma condição de corrida em um contador compartilhado e sua correção com um semáforo binário (`java.util.concurrent.Semaphore`).

## Arquivos

| Arquivo | Descrição |
|---|---|
| `ContadorSemSincronizacao.java` | Versão 1: T threads incrementam `count` sem nenhuma sincronização. |
| `ContadorComSemaforo.java` | Versão 2: cada incremento é protegido por `Semaphore(1, true)` (binário e justo/FIFO). |

Parâmetros usados: **T = 8 threads** e **M = 200.000 incrementos por thread**, valor esperado **T × M = 1.600.000**.

## Como executar

Requer JDK 17+. Dentro da pasta `parte2-semaforo`:

```bash
javac ContadorSemSincronizacao.java ContadorComSemaforo.java
java ContadorSemSincronizacao
java ContadorComSemaforo
```

## Resultados

Ambiente: Windows 11, Java HotSpot 17.0.9.

### Versão 1 — sem sincronização

| Execução | Valor Esperado | Valor Obtido | Tempo Gasto |

| 1 | 1.600.000 | 448.846 | 77,04 ms |
| 2 | 1.600.000 | 464.021 | 104,14 ms |
| 3 | 1.600.000 | 1.338.842 | 51,46 ms |

### Versão 2 — com semáforo binário (justo)

| Execução | Valor Esperado | Valor Obtido | Tempo Gasto |

| 1 | 1.600.000 | 1.600.000 | 15.798,44 ms |
| 2 | 1.600.000 | 1.600.000 | 14.184,89 ms |
| 3 | 1.600.000 | 1.600.000 | 18.550,07 ms |

A versão sem sincronização perdeu entre 16% e 72% dos incrementos, com um resultado diferente a cada execução. A versão com semáforo chegou ao valor exato nas três execuções, mas foi cerca de **200 vezes mais lenta**.

## Discussão técnica

### 1. Por que a versão sem sincronização perde incrementos

A operação `count++` parece única, mas não é atômica. Ela é executada em três passos:

1. **ler** o valor atual de `count` da memória;
2. **somar 1** no registrador;
3. **gravar** o resultado de volta na memória.

Como as 8 threads rodam ao mesmo tempo em núcleos diferentes, duas delas podem ler o mesmo valor (por exemplo, 100) antes que qualquer uma grave. As duas calculam 101 e gravam 101: foram feitos dois incrementos, mas o contador só avançou uma vez. Esse é o *lost update*, típico de uma condição de corrida. O resultado depende de como o escalonador intercala as threads, por isso muda a cada execução (de 448.846 a 1.338.842 nos testes).

Além disso, sem sincronização o compilador JIT pode manter `count` em um registrador durante vários incrementos e só gravar depois, o que aumenta ainda mais as perdas.

### 2. Por que a versão com semáforo é correta

O semáforo é criado com **uma única permissão**, então funciona como exclusão mútua (mutex). Uma thread só executa `count++` depois de obter a permissão com `acquire()`, e as outras ficam bloqueadas até ela chamar `release()`. Assim, os três passos (ler, somar, gravar) são executados por uma thread de cada vez, sem intercalação, e nenhum incremento se perde.

O `release()` fica em um bloco `finally` para que a permissão seja sempre devolvida, mesmo se ocorrer uma exceção. Caso contrário, as outras threads ficariam bloqueadas para sempre.

### 3. Trade-off de throughput

A correção tem um custo alto: de 75 ms para 16 s. Os motivos:

- **Serialização:** a seção crítica é praticamente todo o trabalho. Com o semáforo, as 8 threads executam em fila e o paralelismo desaparece. O programa fica mais lento até que uma versão com uma única thread, porque ainda paga o custo de coordenação.
- **Contenção:** são 1.600.000 operações de `acquire()`/`release()` disputando a mesma permissão, e quase todo `acquire()` encontra o semáforo ocupado.
- **Trocas de contexto:** a thread que não consegue a permissão é estacionada (*park*) pelo sistema operacional e depois precisa ser acordada (*unpark*). Cada transição custa microssegundos, muito mais que o próprio `count++`, que leva nanossegundos.
- **Fairness (justiça):** com `new Semaphore(1, true)` as permissões são entregues em ordem FIFO, então nenhuma thread sofre *starvation*. O custo é que a thread que acabou de liberar não pode readquirir a permissão logo em seguida, mesmo ainda estando em execução: ela precisa passar a vez para a próxima da fila, que tem de ser acordada. Isso força uma troca de contexto praticamente a cada incremento. Um semáforo não justo (`new Semaphore(1)`) permite que a thread "fure a fila" (*barging*), o que costuma dar um throughput bem maior, mas sem garantia de ordem.

Para um contador simples, alternativas como `AtomicInteger` (instrução CAS do hardware) ou acumular localmente e somar só no final seriam muito mais eficientes. O semáforo compensa quando a seção crítica é maior e o custo da sincronização fica pequeno perto do trabalho realizado.

### 4. Visibilidade e ordenação (happens-before)

Além da atomicidade, existe o problema da **visibilidade**. Cada núcleo tem seus caches e registradores, e o compilador e o processador podem reordenar instruções. Sem sincronização, o Java Memory Model não garante que uma thread veja o valor que outra gravou em `count`.

O `Semaphore` resolve isso porque a especificação do `java.util.concurrent` garante que as ações de uma thread antes de chamar `release()` **acontecem antes (happens-before)** das ações de outra thread após um `acquire()` bem-sucedido. Na prática:

- tudo o que a thread A gravou antes de `release()` (incluindo o novo valor de `count`) se torna visível para a thread B depois do `acquire()`;
- o valor não fica preso no cache ou registrador local de A;
- o compilador e o processador não podem mover a escrita de `count` para fora da seção crítica.

Somado à regra de que `Thread.join()` também estabelece happens-before, isso garante que a thread `main` lê o valor final correto de `count` ao imprimir o resultado.
