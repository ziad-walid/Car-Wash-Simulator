public class Semaphore {
    private int val;

    public Semaphore(int initVal) {
        if (initVal < 0) {
            throw new IllegalArgumentException("Initial value cannot be negative.");
        }
        this.val = initVal;
    }

    public synchronized void acquire() throws InterruptedException {
        while (this.val == 0) {
            wait();
        }
        this.val--;
    }

    public synchronized void release() {
        this.val++;
        notify();
    }
    public synchronized int availablePermits() { // help to check available permits
        return this.val;
    }
}
