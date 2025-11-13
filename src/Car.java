import java.util.Queue;

public class Car extends Thread {
    private int id;
    private Queue<Integer> queue;
    private Semaphore empty;     // Counts available waiting spaces
    private Semaphore full;      // Counts cars waiting
    private Semaphore mutex;     // Protects queue from race conditions
    private Semaphore pumps;     // Service bays/pumps

    public Car(int id, Queue<Integer> queue, Semaphore empty, Semaphore full, Semaphore mutex, Semaphore pumps) {
        this.id = id;
        this.queue = queue;
        this.empty = empty;
        this.full = full;
        this.mutex = mutex;
        this.pumps = pumps;
    }

    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public void run() {
        try {
            displayMessage("Car " + id + " arrived");

            empty.acquire();
            mutex.acquire();
            queue.add(id);
            
            // Only display waiting message if no pumps are available
            if (pumps.availablePermits() == 0) {
                displayMessage("C" + id + " Arrived and waiting");
            }
            
            mutex.release();
            full.release();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            displayMessage("Car " + id + " was interrupted");
        }
    }

    public static void main(String[] args) {
        System.out.println("Car class compiled successfully");
    }
}
