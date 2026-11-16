import java.util.concurrent.Semaphore;

public class Garfo {
    private final int id;
    private final Semaphore semaforo;

    public Garfo(int id) {
        this.id = id;
        this.semaforo = new Semaphore(1, true);
    }

    public int getId() {
        return id;
    }

    public void pegar() throws InterruptedException {
        semaforo.acquire();
    }

    public void soltar() {
        semaforo.release();
    }
}
