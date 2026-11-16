# Parte 3 — Deadlock

O relatório completo desta parte (como executar, logs, diagnóstico com jstack, condições de Coffman e correção) está no [README principal](../README.md#parte-3--deadlock).

```bash
javac DeadlockTravado.java DeadlockCorrigido.java
java DeadlockTravado     # trava em deadlock; encerre com Ctrl + C
java DeadlockCorrigido   # termina normalmente
```
