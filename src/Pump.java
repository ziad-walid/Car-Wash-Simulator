import java.util.Queue;

public class Pump implements Runnable {

    private Queue<Integer> queue;
    private Semaphore Empty , Full , Pumps , Mutex;
    private int pumpId;
    // inquire is a request to enter
    // release is for exiting
    // queue is for cars waiting for fuel (like a waiting area)
    // Empty is for empty spots in the queue (available spots for cars to wait)
    // Full is for full spots in the queue (ready cars to be served (enter a pump))
    // Pumps is for available pumps (available service bays)
    // Mutex is for mutual exclusion (to protect shared cars)

    public Pump(Queue<Integer> queue, Semaphore Empty, Semaphore Full, Semaphore Pumps, Semaphore Mutex , int pumpId) {
        this.queue = queue;
        this.Empty = Empty;
        this.Full = Full;
        this.Pumps = Pumps;
        this.Mutex = Mutex;
        this.pumpId = pumpId;
    }

    @Override
    public void run() {
        try {
            Full.acquire();
            Mutex.acquire();
            int car = queue.poll();
            Mutex.release();
            Empty.release();
            Pumps.acquire();
            System.out.println("Pump" + pumpId + ": C" + car + " Occupied");
            System.out.println("Pump" + pumpId + ": C" + car + " Login");
            System.out.println("Pump" + pumpId + ": C" + car + " begins service at Bay " + pumpId);
            Thread.sleep(1000);
            System.out.println("Pump" + pumpId + ": C" + car + " Finishes service");
            Pumps.release();
            System.out.println("Bay " + pumpId + " is free now");
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}