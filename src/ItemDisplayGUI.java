import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class ItemDisplayGUI {
    private static JComboBox<String> preferencesComboBox;
    private static List<Building> buildings;

    public static Customer currentCustomer;
    public static Worker overseeingWorker;
    private static String currentPreference = "All";

    public static void main(String[] args) throws Exception {
/*

        buildings = DataInitializer.initializeData();
        currentCustomer = DataInitializer.currentCustomer;
        overseeingWorker = DataInitializer.overseeingWorker;
        ObjectPlus.registerClass(Transaction.class);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("DB/allExtents.ser"))) {
            ObjectPlus.writeExtents(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        File file = new File("DB/allExtents.ser");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ObjectPlus.readExtents(ois);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            buildings = (List<Building>) ObjectPlus.getExtent(Building.class);
            currentCustomer = ObjectPlus.getExtent(Customer.class).iterator().next();
            overseeingWorker = ObjectPlus.getExtent(Worker.class).iterator().next();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ObjectPlus.registerClass(Transaction.class);


        setGlobalFontSize(18);

        JFrame frame = new JFrame("Item Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel shopsHeader = new JLabel("Shops");
        shopsHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel itemsHeader = new JLabel("Items");
        itemsHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel descriptionHeader = new JLabel("Description");
        descriptionHeader.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel shopsPanel = new JPanel(new BorderLayout());
        shopsPanel.add(shopsHeader, BorderLayout.NORTH);
        JList<Building> buildingList = new JList<>(buildings.toArray(new Building[0]));
        buildingList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Building) {
                    Building building = (Building) value;
                    setText(building.getName());
                }
                return c;
            }
        });

        shopsPanel.add(new JScrollPane(buildingList), BorderLayout.CENTER);

        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.add(itemsHeader, BorderLayout.NORTH);
        DefaultListModel<Loot> itemListModel = new DefaultListModel<>();
        JList<Loot> itemList = new JList<>(itemListModel);
        itemList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Loot item) {
                    setText(item.getName());
                }
                return c;
            }
        });
        itemsPanel.add(new JScrollPane(itemList), BorderLayout.CENTER);

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.add(descriptionHeader, BorderLayout.NORTH);
        descriptionPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        JPanel listPanel = new JPanel(new GridLayout(1, 3));
        listPanel.add(shopsPanel);
        listPanel.add(itemsPanel);
        listPanel.add(descriptionPanel);

        frame.add(listPanel, BorderLayout.CENTER);

        preferencesComboBox = new JComboBox<>(new String[]{"Please chose a preference", "Weapon", "Valuable"});
        frame.add(preferencesComboBox, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton purchaseButton = new JButton("Purchase");
        buttonPanel.add(purchaseButton);

        JButton seeTransactionsButton = new JButton("See Transactions");
        buttonPanel.add(seeTransactionsButton);

        JButton checkBalanceButton = new JButton("Check Balance");
        buttonPanel.add(checkBalanceButton);

        JButton seeInventoryButton = new JButton("See Inventory");
        buttonPanel.add(seeInventoryButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        seeInventoryButton.addActionListener(e -> {
            if (currentCustomer.getInventory().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Your inventory is currently empty.", "Inventory", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringBuilder inventoryDetails = new StringBuilder();
            for (Loot l : currentCustomer.getInventory()) {
                inventoryDetails.append(l.toString2()).append("\n\n");
            }
            JOptionPane.showMessageDialog(frame, "Current Inventory: \n\n" + inventoryDetails, "Inventory", JOptionPane.INFORMATION_MESSAGE);
        });

        checkBalanceButton.addActionListener(e -> {
            double balance = currentCustomer.getMoney();
            JOptionPane.showMessageDialog(frame, "Current Balance: " + balance, "Balance", JOptionPane.INFORMATION_MESSAGE);
        });

        seeTransactionsButton.addActionListener(e -> {
            try {

                Iterable<Transaction> transactionsIterable = ObjectPlus.getExtent(Transaction.class);
                List<Transaction> transactions = new ArrayList<>();
                for (Transaction transaction : transactionsIterable) {
                    transactions.add(transaction);
                }

                if (transactions.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No transactions available.", "Transactions", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                StringBuilder transactionDetails = new StringBuilder();
                for (Transaction transaction : transactions) {
                    transactionDetails.append(transaction.toString()).append("\n\n");
                }
                JTextArea textArea = new JTextArea(transactionDetails.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(frame, scrollPane, "Transactions", JOptionPane.INFORMATION_MESSAGE);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }

        });

        purchaseButton.addActionListener(e -> {
            Loot selectedItem = itemList.getSelectedValue();

            if (selectedItem != null) {
                double itemValue = selectedItem.calculateGoldValue();

                if (currentCustomer.getMoney() >= itemValue) {
                    JOptionPane.showMessageDialog(frame, "Congratulations!", "Purchase Successful", JOptionPane.INFORMATION_MESSAGE);
                    Transaction order = new Transaction(itemValue, selectedItem, currentCustomer, overseeingWorker);
                    Building selectedBuilding = buildingList.getSelectedValue();
                    updateItemList(selectedBuilding, itemListModel);
                    try {
                        selectedItem.removeFromPossessions();
                        currentCustomer.decreaseMoney((int)itemValue);
                        System.out.println("Inventory before purchase: " + currentCustomer.getInventory());
                        selectedItem.setIsInPossessionOf(currentCustomer);
                        System.out.println("Inventory after purchase: " + currentCustomer.getInventory());
                        updateItemList(selectedBuilding, itemListModel);
                        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("DB/allExtents.ser"))) {
                            ObjectPlus.writeExtents(out);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        System.out.println("Transactions after purchase: " + ObjectPlus.getExtent(Transaction.class));
                        System.out.println("Shops after purchase: " + ObjectPlus.getExtent(Building.class));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "You do not have enough funds", "Purchase Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buildingList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Building selectedBuilding = buildingList.getSelectedValue();
                updateItemList(selectedBuilding, itemListModel);
            }
        });



        itemList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Loot selectedItem = itemList.getSelectedValue();
                descriptionArea.setText(selectedItem.toString());
            }
        });

        preferencesComboBox.addActionListener(e -> {
            System.out.println("ComboBox selection changed");
            currentPreference = (String) preferencesComboBox.getSelectedItem();
            Building selectedBuilding = buildingList.getSelectedValue();
            updateItemList(selectedBuilding, itemListModel);
            frame.revalidate();
            frame.repaint();
        });


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.width * 0.7), (int) (screenSize.height * 0.7));
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

    }


    private static void setGlobalFontSize(int size) {
        FontUIResource f = new FontUIResource("SansSerif", Font.PLAIN, size);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    private static void updateItemList(Building b, DefaultListModel<Loot> itemListModel) {
        itemListModel.clear();
        List<Loot> sortedLoot = new ArrayList<>(b.getStoredInside());
        sortedLoot.sort((l1, l2) -> compareLootBasedOnPreference(l1, l2, currentPreference));
        for (Loot loot : sortedLoot) {
            itemListModel.addElement(loot);
        }
    }

    private static int compareLootBasedOnPreference(Loot l1, Loot l2, String preference) {
        if (preference.equals("Weapon")) {
            boolean isL1Weapon = l1 instanceof Weapon;
            boolean isL2Weapon = l2 instanceof Weapon;
            return Boolean.compare(isL2Weapon, isL1Weapon);
        } else if (preference.equals("Valuable")) {
            boolean isL1Valuable = l1 instanceof Valuable;
            boolean isL2Valuable = l2 instanceof Valuable;
            return Boolean.compare(isL2Valuable, isL1Valuable);
        }
        return 0;
    }

}
