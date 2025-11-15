package GuessNumber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Account {
    private String userId;
    private String userPin;
    private double balance;
    private java.util.List<String> transactions;

    public Account(String userId, String userPin, double balance) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public boolean validate(String userId, String userPin) {
        return this.userId.equals(userId) && this.userPin.equals(userPin);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add("Deposited $" + amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactions.add("Withdrew $" + amount);
            return true;
        }
        return false;
    }

    public boolean transfer(Account receiver, double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            receiver.balance += amount;
            transactions.add("Transferred $" + amount + " to " + receiver.userId);
            receiver.transactions.add("Received $" + amount + " from " + userId);
            return true;
        }
        return false;
    }

    public String getTransactionHistory() {
        if (transactions.isEmpty()) return "No transactions yet.";
        StringBuilder sb = new StringBuilder();
        for (String t : transactions) sb.append(t).append("\n");
        return sb.toString();
    }

    public double getBalance() {
        return balance;
    }
}

public class ATMGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField userIdField;
    private JPasswordField pinField;
    private JLabel messageLabel;
    private Account currentUser;

    private Account user1 = new Account("user123", "1234", 1000);
    private Account user2 = new Account("user456", "5678", 500);

    public ATMGUI() {
        setTitle("ATM Interface");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // --- Login Panel ---
        JPanel loginPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        userIdField = new JTextField();
        pinField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);

        loginPanel.add(new JLabel("User ID:"));
        loginPanel.add(userIdField);
        loginPanel.add(new JLabel("PIN:"));
        loginPanel.add(pinField);
        loginPanel.add(loginBtn);
        loginPanel.add(messageLabel);

        mainPanel.add(loginPanel, "login");

        // --- Menu Panel ---
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        JButton historyBtn = new JButton("Transaction History");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton depositBtn = new JButton("Deposit");
        JButton transferBtn = new JButton("Transfer");
        JButton quitBtn = new JButton("Logout");

        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        menuPanel.add(historyBtn);
        menuPanel.add(withdrawBtn);
        menuPanel.add(depositBtn);
        menuPanel.add(transferBtn);
        menuPanel.add(quitBtn);

        mainPanel.add(menuPanel, "menu");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");

        // --- Button Actions ---
        loginBtn.addActionListener(e -> {
            String id = userIdField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            if (user1.validate(id, pin)) currentUser = user1;
            else if (user2.validate(id, pin)) currentUser = user2;
            else currentUser = null;

            if (currentUser != null) {
                messageLabel.setText("");
                cardLayout.show(mainPanel, "menu");
            } else {
                messageLabel.setText("Invalid credentials!");
            }
        });

        historyBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    currentUser.getTransactionHistory(),
                    "Transaction History",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        withdrawBtn.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
            if (amtStr != null && !amtStr.isEmpty()) {
                double amt = Double.parseDouble(amtStr);
                if (currentUser.withdraw(amt))
                    JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                else
                    JOptionPane.showMessageDialog(this, "Insufficient balance!");
            }
        });

        depositBtn.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
            if (amtStr != null && !amtStr.isEmpty()) {
                double amt = Double.parseDouble(amtStr);
                currentUser.deposit(amt);
                JOptionPane.showMessageDialog(this, "Deposit successful!");
            }
        });

        transferBtn.addActionListener(e -> {
            String receiverId = JOptionPane.showInputDialog(this, "Enter receiver User ID:");
            String amtStr = JOptionPane.showInputDialog(this, "Enter amount to transfer:");

            if (receiverId != null && amtStr != null && !amtStr.isEmpty()) {
                double amt = Double.parseDouble(amtStr);
                Account receiver = receiverId.equals("user123") ? user1 :
                                   receiverId.equals("user456") ? user2 : null;
                if (receiver != null && receiver != currentUser && currentUser.transfer(receiver, amt))
                    JOptionPane.showMessageDialog(this, "Transfer successful!");
                else
                    JOptionPane.showMessageDialog(this, "Transfer failed!");
            }
        });

        quitBtn.addActionListener(e -> {
            currentUser = null;
            userIdField.setText("");
            pinField.setText("");
            cardLayout.show(mainPanel, "login");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMGUI().setVisible(true));
    }
}

