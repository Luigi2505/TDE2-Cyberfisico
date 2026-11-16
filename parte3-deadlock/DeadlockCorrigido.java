public class DeadlockCorrigido {

    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();

    public static void main(String[] args) throws InterruptedException {

        
        Thread t1 = new Thread(() -> {
            synchronized (LOCK_A) {
                System.out.println("[Thread-1] Adquiriu LOCK_A");
                dormir(50);
                System.out.println("[Thread-1] Tentando adquirir LOCK_B...");
                synchronized (LOCK_B) {
                    System.out.println("[Thread-1] Adquiriu LOCK_B, trabalho concluído");
                }
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            synchronized (LOCK_A) { 
                System.out.println("[Thread-2] Adquiriu LOCK_A");
                dormir(50);
                System.out.println("[Thread-2] Tentando adquirir LOCK_B...");
                synchronized (LOCK_B) {
                    System.out.println("[Thread-2] Adquiriu LOCK_B, trabalho concluído");
                }
            }
        }, "Thread-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Programa finalizado sem deadlock.");
    }

    private static void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}