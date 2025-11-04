public class Semaphore {
    private int value;

    public Semaphore(int initialValue) {
        if (initialValue < 0) {
            throw new IllegalArgumentException("Initial value cannot be negative.");
        }
        this.value = initialValue;
    }

    public synchronized void acquire() {
        while (this.value == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle thread interruption
                return;
            }
        }
        this.value--;
    }

    public synchronized void release() {
        this.value++;
        notify();
    }
}
