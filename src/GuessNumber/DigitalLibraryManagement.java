package GuessNumber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class Book {
    int id;
    String title;
    String author;
    boolean isIssued;

    Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }
}

class Library {
    List<Book> books = new ArrayList<>();

    void addBook(Book b) { books.add(b); }
    List<Book> getBooks() { return books; }

    void issueBook(int id) {
        for (Book b : books) {
            if (b.id == id) { b.isIssued = true; return; }
        }
    }

    void returnBook(int id) {
        for (Book b : books) {
            if (b.id == id) { b.isIssued = false; return; }
        }
    }
}

public class DigitalLibraryManagement extends JFrame {
    Library library = new Library();
    DefaultTableModel model;

    DigitalLibraryManagement() {
        setTitle("Digital Library Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Tabs for Admin & User ---
        JTabbedPane tabs = new JTabbedPane();

        // Admin Panel
        JPanel adminPanel = new JPanel(new BorderLayout());
        adminPanel.add(createAdminPanel(), BorderLayout.CENTER);
        tabs.add("Admin", adminPanel);

        // User Panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(createUserPanel(), BorderLayout.CENTER);
        tabs.add("User", userPanel);

        add(tabs);
        setVisible(true);
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table
        model = new DefaultTableModel(new Object[]{"ID", "Title", "Author", "Status"}, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        // Form inputs
        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JButton addBtn = new JButton("Add Book");
        JButton issueBtn = new JButton("Issue Book");
        JButton returnBtn = new JButton("Return Book");
        JButton refreshBtn = new JButton("Refresh List");

        form.add(new JLabel("Book ID:"));
        form.add(idField);
        form.add(new JLabel("Title:"));
        form.add(titleField);
        form.add(new JLabel("Author:"));
        form.add(authorField);
        form.add(addBtn);
        form.add(refreshBtn);

        // Button actions
        addBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String title = titleField.getText();
                String author = authorField.getText();
                library.addBook(new Book(id, title, author));
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        issueBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter Book ID to issue:");
            if (input != null) {
                library.issueBook(Integer.parseInt(input));
                refreshTable();
            }
        });

        returnBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter Book ID to return:");
            if (input != null) {
                library.returnBook(Integer.parseInt(input));
                refreshTable();
            }
        });

        refreshBtn.addActionListener(e -> refreshTable());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(issueBtn);
        buttonPanel.add(returnBtn);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("Search");
        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Search by Title: "), BorderLayout.WEST);
        top.add(searchField, BorderLayout.CENTER);
        top.add(searchBtn, BorderLayout.EAST);

        DefaultTableModel userModel = new DefaultTableModel(new Object[]{"ID", "Title", "Author", "Status"}, 0);
        JTable userTable = new JTable(userModel);
        JScrollPane scroll = new JScrollPane(userTable);

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().toLowerCase();
            userModel.setRowCount(0);
            for (Book b : library.getBooks()) {
                if (b.title.toLowerCase().contains(keyword)) {
                    userModel.addRow(new Object[]{b.id, b.title, b.author, b.isIssued ? "Issued" : "Available"});
                }
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Book b : library.getBooks()) {
            model.addRow(new Object[]{b.id, b.title, b.author, b.isIssued ? "Issued" : "Available"});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DigitalLibraryManagement::new);
    }
}
