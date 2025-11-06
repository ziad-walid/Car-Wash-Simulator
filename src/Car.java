import java.util.Queue;

public class Car extends Thread {
    private int id;
    private Queue<Integer> queue;
    private Semaphore empty;     // Counts available waiting spaces
    private Semaphore full;      // Counts cars waiting
    private Semaphore mutex;     // Protects queue from race conditions

    public Car(int id, Queue<Integer> queue, Semaphore empty, Semaphore full, Semaphore mutex) {
        this.id = id;
        this.queue = queue;
        this.empty = empty;
        this.full = full;
        this.mutex = mutex;
    }

    @Override
    public void run() {
        System.out.println("Car " + id + " arrives.");

        empty.acquire();    

        if (ServiceStation.pumps.availablePermits() == 0) { // to check if all pumps are busy
            System.out.println("C" + id + "arrived and waiting");
        }
        
        
        mutex.acquire();
        queue.add(id);
        
        mutex.release(); 
        
        full.release();
    }

    public static void main(String[] args) {
    System.out.println("Car class compiled successfully");
}
}

