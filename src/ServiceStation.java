import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceStation {

    public static Semaphore pumps;
    public static int totalCars;
    public static AtomicInteger servedCars = new AtomicInteger(0);

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter waiting area capacity: ");
        int waitingAreaCapacity = input.nextInt();

        System.out.print("Enter number of service bays: ");
        int numberOfPumps = input.nextInt();

        System.out.print("Enter number of arriving cars: ");
        ServiceStation.totalCars = input.nextInt();

        input.close();

        Queue<Integer> waitingQueue = new LinkedList<>();
        Semaphore empty = new Semaphore(waitingAreaCapacity); 
        Semaphore full = new Semaphore(0);                    
        Semaphore mutex = new Semaphore(1);                   
        pumps = new Semaphore(numberOfPumps);       

        System.out.println("Waiting Area Capacity: " + waitingAreaCapacity);
        System.out.println("Number of Pumps: " + numberOfPumps);
        System.out.println("Total Cars: " + totalCars);
        

        for (int i = 1; i <= numberOfPumps; i++) {
            Thread pumpThread = new Thread(new Pump(waitingQueue, empty, full, pumps, mutex, i));
            pumpThread.start();
        }

        for (int i = 1; i <= ServiceStation.totalCars; i++) {
            Car car = new Car(i, waitingQueue, empty, full, mutex);
            car.start();


            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

       
    }
}
