package GuessNumber;
// Username: student01
// Password: 1234

import javax.swing.*;
import java.awt.*;

// ------------------- USER CLASS -------------------
class User {
    private String username;
    private String password;
    private String name;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public boolean validate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void updateProfile(String newName, String newPassword) {
        this.name = newName;
        if (!newPassword.isEmpty()) {
            this.password = newPassword;
        }
    }

    public String getName() {
        return name;
    }
}

// ------------------- MAIN GUI CLASS -------------------
public class OnlineExamGUI extends JFrame {

    private CardLayout layout;
    private JPanel mainPanel;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel timerLabel, questionLabel;
    private ButtonGroup optionsGroup;
    private JRadioButton[] options;
    private JButton nextButton, submitButton;

    private javax.swing.Timer timer; // Swing Timer
    private int timeLeft = 60; // 60 seconds per exam
    private int currentQuestion = 0;
    private int score = 0;

    private User user = new User("student01", "1234", "John Doe");

    private String[][] questions = {
            {"What is the capital of India?", "Delhi", "Mumbai", "Kolkata", "Chennai", "A"},
            {"Java is a ___ language.", "Low-level", "High-level", "Assembly", "Machine", "B"},
            {"Which company developed Java?", "Microsoft", "Oracle", "Apple", "IBM", "B"},
            {"Which keyword is used to inherit a class in Java?", "this", "super", "extends", "implements", "C"},
            {"Which method is used to start a thread?", "init()", "start()", "run()", "begin()", "B"}
    };

    public OnlineExamGUI() {
        setTitle("Online Examination System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        mainPanel = new JPanel(layout);
        add(mainPanel);

        createLoginPanel();
        createProfilePanel();
        createExamPanel();
        createResultPanel();

        layout.show(mainPanel, "login");
    }

    // ------------------- LOGIN PANEL -------------------
    private void createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel title = new JLabel("Online Examination Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginPanel.add(title);
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (user.validate(username, password)) {
                layout.show(mainPanel, "profile");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(loginPanel, "login");
    }

    // ------------------- PROFILE PANEL -------------------
    private void createProfilePanel() {
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Welcome, update your profile or start the exam");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        profilePanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        gbc.gridx = 0;
        profilePanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(nameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField(15);
        profilePanel.add(newPasswordLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(newPasswordField, gbc);

        gbc.gridy++;
        gbc.gridx = 1;
        JButton updateButton = new JButton("Update Profile");
        profilePanel.add(updateButton, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton startExamButton = new JButton("Start Exam");
        profilePanel.add(startExamButton, gbc);

        updateButton.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newPass = new String(newPasswordField.getPassword()).trim();
            user.updateProfile(newName.isEmpty() ? user.getName() : newName, newPass);
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
        });

        startExamButton.addActionListener(e -> {
            startExam();
            layout.show(mainPanel, "exam");
        });

        mainPanel.add(profilePanel, "profile");
    }

    // ------------------- EXAM PANEL -------------------
    private void createExamPanel() {
        JPanel examPanel = new JPanel(new BorderLayout(10, 10));
        examPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        timerLabel = new JLabel("Time left: 60s", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(timerLabel, BorderLayout.EAST);
        examPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        optionsGroup = new ButtonGroup();
        options = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 14));
            options[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            optionsGroup.add(options[i]);
            centerPanel.add(options[i]);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        examPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);
        examPanel.add(buttonPanel, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> nextQuestion());
        submitButton.addActionListener(e -> endExam());

        mainPanel.add(examPanel, "exam");
    }

    // ------------------- RESULT PANEL -------------------
    private void createResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.addActionListener(e -> {
            layout.show(mainPanel, "login");
            usernameField.setText("");
            passwordField.setText("");
        });

        resultPanel.add(resultLabel, BorderLayout.CENTER);
        resultPanel.add(logoutButton, BorderLayout.SOUTH);
        mainPanel.add(resultPanel, "result");

        resultPanel.putClientProperty("label", resultLabel);
    }

    // ------------------- EXAM LOGIC -------------------
    private void startExam() {
        currentQuestion = 0;
        score = 0;
        timeLeft = 60;
        loadQuestion();

        timer = new javax.swing.Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft + "s");
            if (timeLeft <= 0) {
                ((javax.swing.Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Time’s up! Auto-submitting...");
                endExam();
            }
        });
        timer.start();
    }

    private void loadQuestion() {
        if (currentQuestion < questions.length) {
            String[] q = questions[currentQuestion];
            questionLabel.setText("Q" + (currentQuestion + 1) + ": " + q[0]);
            for (int i = 0; i < 4; i++) {
                options[i].setText(q[i + 1]);
                options[i].setSelected(false);
            }
        }
    }

    private void nextQuestion() {
        checkAnswer();
        currentQuestion++;
        if (currentQuestion < questions.length) {
            loadQuestion();
        } else {
            endExam();
        }
    }

    private void checkAnswer() {
        String correct = questions[currentQuestion][5];
        char correctOption = correct.charAt(0);
        int selectedIndex = -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) selectedIndex = i;
        }
        if (selectedIndex == (correctOption - 'A')) score++;
    }

    private void endExam() {
        if (timer != null) timer.stop();
        checkAnswer();

        JPanel resultPanel = (JPanel) mainPanel.getComponent(3);
        JLabel resultLabel = (JLabel) resultPanel.getClientProperty("label");

        double percentage = (score * 100.0) / questions.length;
        String grade;
        if (percentage >= 90) grade = "A";
        else if (percentage >= 75) grade = "B";
        else if (percentage >= 50) grade = "C";
        else grade = "F";

        resultLabel.setText("<html><center>Exam completed!<br>Your score: " + score + "/" + questions.length +
                "<br>Percentage: " + String.format("%.2f", percentage) + "%" +
                "<br>Grade: " + grade + "</center></html>");

        layout.show(mainPanel, "result");
    }

    // ------------------- MAIN -------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OnlineExamGUI().setVisible(true));
    }
}
