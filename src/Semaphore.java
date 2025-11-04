public class Semaphore {
    private int val;

    public Semaphore(int initVal) {
        if (initVal < 0) {
            throw new IllegalArgumentException("Initial value cannot be negative.");
        }
        this.val = initVal;
    }

    public synchronized void acquire() {
        while (this.val == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle thread interruption
                return;
            }
        }
        this.val--;
    }

    public synchronized void release() {
        this.val++;
        notify();
    }
}
