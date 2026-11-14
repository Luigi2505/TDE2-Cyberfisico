import java.util.concurrent.Semaphore;

public class Garfo {
    private final int id;
    private final Semaphore semaforo;

    public Garfo(int id) {
        this.id = id;
        this.semaforo = new Semaphore(1); 
    }

    public int getId() {
        return id;
    }

    public Semaphore getSemaforo() {
        return semaforo;
    }

    public void pegar() {
    }

    public void soltar() {
    }
}

