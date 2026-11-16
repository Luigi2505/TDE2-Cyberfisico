import java.util.Random;

public class Filosofo implements Runnable {
    private final int id;
    private final Garfo primeiroGarfo;
    private final Garfo segundoGarfo;
    private final Random random = new Random();
    private int refeicoes = 0;

    public Filosofo(int id, Garfo garfoEsquerdo, Garfo garfoDireito) {
        this.id = id;

        if (garfoEsquerdo.getId() < garfoDireito.getId()) {
            this.primeiroGarfo = garfoEsquerdo;
            this.segundoGarfo = garfoDireito;
        } else {
            this.primeiroGarfo = garfoDireito;
            this.segundoGarfo = garfoEsquerdo;
        }
    }

    private void pensar() throws InterruptedException {
        System.out.println("Filosofo " + id + " ta pensando");
        Thread.sleep(random.nextInt(1000) + 500);
    }

    private void comer() throws InterruptedException {
        refeicoes++;
        System.out.println("Filosofo " + id + " ta comendo (Refeicao " + refeicoes + ")");
        Thread.sleep(random.nextInt(1000) + 500);
    }

    @Override
    public void run() {
        try {
            while (true) {
                pensar();

                System.out.println("Filosofo " + id + " ta com fome");

                primeiroGarfo.pegar();
                System.out.println("Filosofo " + id + " pegou o garfo " + primeiroGarfo.getId() + ".");

                segundoGarfo.pegar();
                System.out.println("Filosofo " + id + " pegou o garfo " + segundoGarfo.getId() + ".");

                comer();

                System.out.println("Filosofo " + id + " soltou os garfos " + primeiroGarfo.getId() + " e " + segundoGarfo.getId() + ".");
                segundoGarfo.soltar();
                primeiroGarfo.soltar();
            }
        } catch (InterruptedException e) {
            System.out.println("Filosofo " + id + " foi interrompido.");
        }
    }
}
