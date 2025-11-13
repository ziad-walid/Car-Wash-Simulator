import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Pump implements Runnable {

    private Queue<Integer> queue;
    private Semaphore Empty, Full, Pumps, Mutex;
    private int pumpId;
    private AtomicInteger servedCars;
    private int totalCars;

    public Pump(Queue<Integer> queue, Semaphore Empty, Semaphore Full, Semaphore Pumps, Semaphore Mutex, 
             int pumpId, AtomicInteger servedCars, int totalCars) {
        this.queue = queue;
        this.Empty = Empty;
        this.Full = Full;
        this.Pumps = Pumps;
        this.Mutex = Mutex;
        this.pumpId = pumpId;
        this.servedCars = servedCars;
        this.totalCars = totalCars;
    }

    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Full.acquire();
                Mutex.acquire();
                int car = queue.poll();
                Mutex.release();
                Empty.release();
                Pumps.acquire();

                displayMessage("Pump" + pumpId + ": C" + car + " Occupied");
                displayMessage("Pump" + pumpId + ": C" + car + " Login");
                displayMessage("Pump" + pumpId + ": C" + car + " begins service at Bay " + pumpId);
                Thread.sleep(2000);
                displayMessage("Pump" + pumpId + ": C" + car + " Finishes service");
                Pumps.release();
                displayMessage("Bay " + pumpId + " is free now");
                
                int served = servedCars.incrementAndGet();
                if (served == totalCars) {
                    displayMessage("All cars processed; simulation ends");
                    break; 
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            displayMessage("Pump " + pumpId + " was interrupted");
        }
    }
}
