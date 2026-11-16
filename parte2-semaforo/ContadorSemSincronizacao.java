public class ContadorSemSincronizacao {
    private static final int NUM_THREADS = 8;
    private static final int INCREMENTOS = 200_000;
    private static final int EXECUCOES = 3;

    private static volatile int count = 0; // evita que o JIT junte o laco; volatile nao torna count++ atomico

    public static void main(String[] args) throws InterruptedException {
        int esperado = NUM_THREADS * INCREMENTOS;

        System.out.println("Versao 1 - sem sincronizacao (T = " + NUM_THREADS + ", M = " + INCREMENTOS + ")");
        Tabela.cabecalho();
        for (int e = 1; e <= EXECUCOES; e++) {
            count = 0;
            double tempoMs = executar();
            Tabela.linha(e, esperado, count, tempoMs);
        }
        Tabela.rodape();
    }

    private static double executar() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        long inicio = System.nanoTime();

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(ContadorSemSincronizacao::tarefa, "Worker-" + i);
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }

        return (System.nanoTime() - inicio) / 1_000_000.0;
    }

    private static void tarefa() {
        for (int i = 0; i < INCREMENTOS; i++) {
            count++; // leitura, soma e escrita nao atomicas: condicao de corrida
        }
    }
}
