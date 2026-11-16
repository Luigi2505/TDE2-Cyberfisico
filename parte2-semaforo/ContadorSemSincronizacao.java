public class ContadorSemSincronizacao {
    private static final int NUM_THREADS = 8;
    private static final int INCREMENTOS = 200_000;

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        long inicio = System.nanoTime();

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(ContadorSemSincronizacao::tarefa, "Worker-" + i);
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long fim = System.nanoTime();

        int esperado = NUM_THREADS * INCREMENTOS;
        System.out.println("Versao 1 - sem sincronizacao");
        System.out.printf("Esperado: %d%n", esperado);
        System.out.printf("Obtido:   %d%n", count);
        System.out.printf("Perdidos: %d%n", esperado - count);
        System.out.printf("Tempo:    %.3f ms%n", (fim - inicio) / 1_000_000.0);
    }

    private static void tarefa() {
        for (int i = 0; i < INCREMENTOS; i++) {
            count++; // leitura, soma e escrita nao atomicas: condicao de corrida
        }
    }
}
