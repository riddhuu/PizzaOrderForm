import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDishCrust;
    private JComboBox<String> sizeComboBox;
    private JCheckBox[] toppings;
    private JTextArea orderTextArea;
    private final double TAX_RATE = 0.07;
    private final double[] SIZES_PRICES = {8.00, 12.00, 16.00, 20.00};

    public PizzaGUIFrame() {
        setTitle("Pizza Order System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crust Panel
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(BorderFactory.createTitledBorder("Crust Type"));
        ButtonGroup crustGroup = new ButtonGroup();
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-dish");
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);
        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDishCrust);

        // Size Panel
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));
        String[] sizes = {"Small", "Medium", "Large", "Super"};
        sizeComboBox = new JComboBox<>(sizes);
        sizePanel.add(sizeComboBox);

        // Toppings Panel
        JPanel toppingsPanel = new JPanel(new GridLayout(3, 2));
        toppingsPanel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        toppings = new JCheckBox[6];
        String[] toppingNames = {"Cheese", "Pepperoni", "Mushrooms", "Olives", "Onions", "Sausage"};
        for (int i = 0; i < toppings.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            toppingsPanel.add(toppings[i]);
        }

        // Order Display Panel
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        orderTextArea = new JTextArea(10, 30);
        orderTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderTextArea);
        orderPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton orderButton = new JButton("Order");
        JButton clearButton = new JButton("Clear");
        JButton quitButton = new JButton("Quit");
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        // Add panels to frame
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);
        topPanel.add(toppingsPanel);
        add(topPanel, BorderLayout.NORTH);
        add(orderPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Listeners
        orderButton.addActionListener(e -> displayOrder());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to quit?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void displayOrder() {
        if (!validateOrder()) {
            JOptionPane.showMessageDialog(this, "Please select a crust type, size, and at least one topping.", "Incomplete Order", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder order = new StringBuilder();
        double total = 0;

        // Crust and Size
        String crust = thinCrust.isSelected() ? "Thin" : (regularCrust.isSelected() ? "Regular" : "Deep-dish");
        String size = (String) sizeComboBox.getSelectedItem();
        double sizePrice = SIZES_PRICES[sizeComboBox.getSelectedIndex()];
        total += sizePrice;

        order.append("=========================================\n");
        order.append(String.format("%-20s $%.2f\n", crust + " Crust, " + size, sizePrice));

        // Toppings
        for (JCheckBox topping : toppings) {
            if (topping.isSelected()) {
                order.append(String.format("%-20s $1.00\n", topping.getText()));
                total += 1.00;
            }
        }

        // Calculations
        double tax = total * TAX_RATE;
        double grandTotal = total + tax;

        // Format and append the rest of the order
        DecimalFormat df = new DecimalFormat("#0.00");
        order.append("Sub-total:           $").append(df.format(total)).append("\n");
        order.append("Tax:                 $").append(df.format(tax)).append("\n");
        order.append("---------------------------------------------\n");
        order.append("Total:               $").append(df.format(grandTotal)).append("\n");
        order.append("=========================================\n");

        orderTextArea.setText(order.toString());
    }

    private boolean validateOrder() {
        boolean crustSelected = thinCrust.isSelected() || regularCrust.isSelected() || deepDishCrust.isSelected();
        boolean toppingSelected = false;
        for (JCheckBox topping : toppings) {
            if (topping.isSelected()) {
                toppingSelected = true;
                break;
            }
        }
        return crustSelected && toppingSelected;
    }

    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDishCrust.setSelected(false);
        sizeComboBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        orderTextArea.setText("");
    }}

