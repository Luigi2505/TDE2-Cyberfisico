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
        System.out.println("Filosofo " + id + " ta pensando");
        Thread.sleep(random.nextInt(100) + 50);
    }

    private void comer() throws InterruptedException {
        System.out.println("Filosofo " + id + " ta comendo");
        Thread.sleep(random.nextInt(200) + 100);
    }

    @Override
    public void run() {
        try {
            while (true) {
                pensar();

                System.out.println("Filosofo " + id + " ta com fome");

                garfoEsquerdo.pegar();
                System.out.println("Filosofo " + id + " pegou o garfo esquerdo (" + garfoEsquerdo.getId() + ").");

                Thread.sleep(50);

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
