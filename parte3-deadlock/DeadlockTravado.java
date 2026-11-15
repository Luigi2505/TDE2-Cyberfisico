public class DeadlockTravado {
    
private static final Object LOCK_A = new Object();
private static final Object LOCK_B = new Object();

   public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            synchronized (LOCK_A) {
                System.out.println("[Thread-1] Adquiriu LOCK_A");
                dormir(50); // garante que a Thread-2 já pegou o LOCK_B
                System.out.println("[Thread-1] Tentando adquirir LOCK_B...");
                synchronized (LOCK_B) {
                    System.out.println("[Thread-1] Adquiriu LOCK_B (nunca chega aqui)");
                }
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            synchronized (LOCK_B) {
                System.out.println("[Thread-2] Adquiriu LOCK_B");
                dormir(50);
                System.out.println("[Thread-2] Tentando adquirir LOCK_A...");
                synchronized (LOCK_A) {
                    System.out.println("[Thread-2] Adquiriu LOCK_A (nunca chega aqui)");
                }
            }
        }, "Thread-2");

        t1.start();
        t2.start();
    }

    private static void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

