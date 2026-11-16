import java.util.Random;

public class FilosofoIngenuo implements Runnable {
    private final int id;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;
    private final Random random = new Random();

    public FilosofoIngenuo(int id, Garfo garfoEsquerdo, Garfo garfoDireito) {
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
    }

    private void pensar() throws InterruptedException {
        System.out.println("Filosofo " + id + " esta pensando.");
        Thread.sleep(random.nextInt(50) + 10);
    }

    private void comer() throws InterruptedException {
        System.out.println("Filosofo " + id + " esta comendo.");
        Thread.sleep(random.nextInt(100) + 50);
    }

    @Override
    public void run() {
        try {
            while (true) {
                pensar();

                System.out.println("Filosofo " + id + " esta com fome.");

                // 1. Pega sempre o garfo esquerdo primeiro
                garfoEsquerdo.pegar();
                System.out.println("Filosofo " + id + " pegou o garfo esquerdo (" + garfoEsquerdo.getId() + ").");

                // Pausa crucial: enquanto este filosofo segura o garfo esquerdo,
                // os vizinhos tambem acordam e pegam seus respectivos garfos esquerdos.
                Thread.sleep(200);

                // 2. Tenta pegar o garfo direito (todos encontram ocupado -> DEADLOCK)
                System.out.println("Filosofo " + id + " tentando pegar o garfo direito (" + garfoDireito.getId() + ")...");
                garfoDireito.pegar();
                System.out.println("Filosofo " + id + " pegou o garfo direito (" + garfoDireito.getId() + ").");

                comer();

                System.out.println("Filosofo " + id + " soltou os garfos (" + garfoEsquerdo.getId() + " e " + garfoDireito.getId() + ").");
                garfoDireito.soltar();
                garfoEsquerdo.soltar();
            }
        } catch (InterruptedException e) {
            System.out.println("Filosofo " + id + " foi interrompido.");
        }
    }
}
