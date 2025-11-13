import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceStationGUI extends JFrame {

    private JTextArea logArea;
    private JTextField baysField, carsField, waitingField;
    private JButton startButton;

    // Shared data
    private Queue<Integer> waitingQueue;
    private Semaphore empty, full, mutex, pumps;
    private AtomicInteger servedCars;
    private int totalCars;

    public ServiceStationGUI() {
        setTitle("Car Wash Simulator");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 247, 250));

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Simulation Setup"));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.add(new JLabel("Service Bays:"));
        baysField = new JTextField("2");
        inputPanel.add(baysField);

        inputPanel.add(new JLabel("Arriving Cars:"));
        carsField = new JTextField("5");
        inputPanel.add(carsField);

        inputPanel.add(new JLabel("Waiting Area Capacity:"));
        waitingField = new JTextField("3");
        inputPanel.add(waitingField);

        startButton = new JButton("Start Simulation");
        startButton.setBackground(new Color(0, 153, 153));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inputPanel.add(startButton);

        add(inputPanel, BorderLayout.NORTH);

        // Output panel
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.CYAN);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Console Log"));
        add(scrollPane, BorderLayout.CENTER);

        // --- Button Action ---
        startButton.addActionListener(e -> new Thread(this::startSimulation).start());
    }

    private void startSimulation() {
        logArea.setText("");

        try {
            int numberOfPumps = Integer.parseInt(baysField.getText());
            int waitingAreaCapacity = Integer.parseInt(waitingField.getText());
            totalCars = Integer.parseInt(carsField.getText());

            if (numberOfPumps <= 0 || waitingAreaCapacity <= 0 || totalCars <= 0) {
                appendLog("All inputs must be positive numbers!");
                return;
            }

            appendLog("Simulation started");
            appendLog("Waiting Area Capacity: " + waitingAreaCapacity);
            appendLog("Number of Pumps: " + numberOfPumps);
            appendLog("Total Cars: " + totalCars + "\n");

            waitingQueue = new LinkedList<>();
            empty = new Semaphore(waitingAreaCapacity);
            full = new Semaphore(0);
            mutex = new Semaphore(1);
            pumps = new Semaphore(numberOfPumps);
            servedCars = new AtomicInteger(0);

            // Start pump threads
            for (int i = 1; i <= numberOfPumps; i++) {
                Thread pumpThread = new Thread(new Pump(waitingQueue, empty, full, pumps, mutex, i, servedCars, totalCars) {
                    @Override
                    public void displayMessage(String msg) {
                        appendLog(msg);
                    }
                });
                pumpThread.start();
            }

            // Start car threads
            for (int i = 1; i <= totalCars; i++) {
                Thread car = new Thread(new Car(i, waitingQueue, empty, full, mutex, pumps) {
                    @Override
                    public void displayMessage(String msg) {
                        appendLog(msg);
                    }
                });
                car.start();
                Thread.sleep(300);
            }

        
        } catch (NumberFormatException ex) {
            appendLog("Please enter valid numeric values.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            appendLog("Simulation was interrupted.");
        }
    }

    // Helper function
    public synchronized void appendLog(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServiceStationGUI().setVisible(true));
    }
}
