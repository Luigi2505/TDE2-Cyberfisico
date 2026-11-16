public class MainIngenuo {
    private static final int NUM_FILOSOFOS = 5;

    public static void main(String[] args) {
        Garfo[] garfos = new Garfo[NUM_FILOSOFOS];
        FilosofoIngenuo[] filosofos = new FilosofoIngenuo[NUM_FILOSOFOS];
        Thread[] threads = new Thread[NUM_FILOSOFOS];

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Garfo(i);
        }

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Garfo garfoEsquerdo = garfos[i];
            Garfo garfoDireito = garfos[(i + 1) % NUM_FILOSOFOS];

            filosofos[i] = new FilosofoIngenuo(i, garfoEsquerdo, garfoDireito);
            threads[i] = new Thread(filosofos[i]);
        }

        System.out.println("Versao ingenua com deadlock");

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            threads[i].start();
        }
    }
}
