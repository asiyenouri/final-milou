package milou;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class GraphicalUI {
    private final Services services;
    private JFrame mainFrame;
    private User currentUser;

    public GraphicalUI(Services services) {
        this.services = services;
        initializeUI();
    }

    private void initializeUI() {
        mainFrame = new JFrame("Milou Email Client");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(700, 500);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(new Color(245, 245, 245));
        showAuthPanel();
        mainFrame.setVisible(true);
    }

    private void showAuthPanel() {
        JPanel authPanel = new JPanel();
        authPanel.setLayout(new BoxLayout(authPanel, BoxLayout.Y_AXIS));
        authPanel.setBackground(new Color(50, 50, 50));
        authPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel welcomeLabel = new JLabel("Milou Email Client");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton loginButton = createStyledButton("Login", e -> showLoginPanel());
        JButton signupButton = createStyledButton("Sign Up", e -> showSignupPanel());
        JButton quitButton = createStyledButton("Quit", e -> System.exit(0));

        authPanel.add(Box.createVerticalGlue());
        authPanel.add(welcomeLabel);
        authPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        authPanel.add(loginButton);
        authPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        authPanel.add(signupButton);
        authPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        authPanel.add(quitButton);
        authPanel.add(Box.createVerticalGlue());

        mainFrame.getContentPane().removeAll();
        mainFrame.add(authPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(new Color(50, 50, 50));
        loginPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailField.setMaximumSize(new Dimension(300, 30));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setMaximumSize(new Dimension(300, 30));

        JButton loginButton = createStyledButton("Login", e -> {
            try {
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                currentUser = services.login(email, password);
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(mainFrame, "Welcome back, " + currentUser.getName() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                showUnreadEmailsDialog();
                showMainPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = createStyledButton("Back", e -> showAuthPanel());

        loginPanel.add(Box.createVerticalGlue());
        loginPanel.add(emailLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(emailField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(passwordLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(passwordField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(backButton);
        loginPanel.add(Box.createVerticalGlue());

        mainFrame.getContentPane().removeAll();
        mainFrame.add(loginPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showSignupPanel() {
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new BoxLayout(signupPanel, BoxLayout.Y_AXIS));
        signupPanel.setBackground(new Color(50, 50, 50));
        signupPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameField.setMaximumSize(new Dimension(300, 30));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailField.setMaximumSize(new Dimension(300, 30));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setMaximumSize(new Dimension(300, 30));

        JButton signupButton = createStyledButton("Sign Up", e -> {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                services.signUp(name, email, password);
                JOptionPane.showMessageDialog(mainFrame, "Your new account is created.\nGo ahead and login!", "Success", JOptionPane.INFORMATION_MESSAGE);
                showAuthPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Signup Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = createStyledButton("Back", e -> showAuthPanel());

        signupPanel.add(Box.createVerticalGlue());
        signupPanel.add(nameLabel);
        signupPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        signupPanel.add(nameField);
        signupPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        signupPanel.add(emailLabel);
        signupPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        signupPanel.add(emailField);
        signupPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        signupPanel.add(passwordLabel);
        signupPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        signupPanel.add(passwordField);
        signupPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        signupPanel.add(signupButton);
        signupPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        signupPanel.add(backButton);
        signupPanel.add(Box.createVerticalGlue());

        mainFrame.getContentPane().removeAll();
        mainFrame.add(signupPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showUnreadEmailsDialog() {
        List<Email> unreadEmails = services.getUnreadEmails(currentUser.getEmail());
        StringBuilder message = new StringBuilder();
        message.append("Unread Emails:\n\n");
        message.append(unreadEmails.size()).append(" unread emails:\n");
        for (Email email : unreadEmails) {
            message.append("+ ").append(email.getSender())
                    .append(" - ").append(email.getFormattedSubject())
                    .append(" (").append(email.getId()).append(")\n");
        }
        JOptionPane.showMessageDialog(mainFrame, message.toString(), "Unread Emails", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton sendButton = createStyledButton("Send", e -> showSendPanel());
        JButton viewButton = createStyledButton("View", e -> showViewPanel());
        JButton replyButton = createStyledButton("Reply", e -> showReplyPanel());
        JButton forwardButton = createStyledButton("Forward", e -> showForwardPanel());
        JButton logoutButton = createStyledButton("Logout", e -> {
            currentUser = null;
            showAuthPanel();
        });

        buttonPanel.add(sendButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(replyButton);
        buttonPanel.add(forwardButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainFrame.getContentPane().removeAll();
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showSendPanel() {
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.Y_AXIS));
        sendPanel.setBackground(new Color(50, 50, 50));
        sendPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel recipientsLabel = new JLabel("Recipient(s) (comma separated):");
        recipientsLabel.setForeground(Color.WHITE);
        recipientsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        recipientsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField recipientsField = new JTextField();
        recipientsField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        recipientsField.setMaximumSize(new Dimension(300, 30));

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setForeground(Color.WHITE);
        subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subjectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField subjectField = new JTextField();
        subjectField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subjectField.setMaximumSize(new Dimension(300, 30));

        JLabel bodyLabel = new JLabel("Body:");
        bodyLabel.setForeground(Color.WHITE);
        bodyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bodyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea bodyArea = new JTextArea();
        bodyArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        JScrollPane bodyScroll = new JScrollPane(bodyArea);
        bodyScroll.setMaximumSize(new Dimension(300, 150));

        JButton sendButton = createStyledButton("Send", e -> {
            try {
                String recipientsInput = recipientsField.getText().trim();
                List<String> recipients = Arrays.asList(recipientsInput.split("\\s*,\\s*"));
                String subject = subjectField.getText().trim();
                String body = bodyArea.getText().trim();
                Email email = services.sendEmail(currentUser.getEmail(), recipients, subject, body);
                JOptionPane.showMessageDialog(mainFrame, "Successfully sent your email.\nCode: " + email.getId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                showMainPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Send Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = createStyledButton("Back", e -> showMainPanel());

        sendPanel.add(Box.createVerticalGlue());
        sendPanel.add(recipientsLabel);
        sendPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sendPanel.add(recipientsField);
        sendPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sendPanel.add(subjectLabel);
        sendPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sendPanel.add(subjectField);
        sendPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sendPanel.add(bodyLabel);
        sendPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sendPanel.add(bodyScroll);
        sendPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sendPanel.add(sendButton);
        sendPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sendPanel.add(backButton);
        sendPanel.add(Box.createVerticalGlue());

        mainFrame.getContentPane().removeAll();
        mainFrame.add(sendPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showViewPanel() {
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
        viewPanel.setBackground(new Color(50, 50, 50));
        viewPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JButton allEmailsButton = createStyledButton("All Emails", e -> {
            List<Email> emails = services.getAllEmails(currentUser.getEmail());
            showEmailsDialog(emails, "All Emails");
        });
        JButton unreadEmailsButton = createStyledButton("Unread Emails", e -> {
            List<Email> emails = services.getUnreadEmails(currentUser.getEmail());
            showEmailsDialog(emails, "Unread Emails");
        });
        JButton sentEmailsButton = createStyledButton("Sent Emails", e -> {
            List<Email> emails = services.getSentEmails(currentUser.getEmail());
            showSentEmailsDialog(emails);
        });
        JButton codeButton = createStyledButton("Read by Code", e -> showReadByCodePanel());
        JButton backButton = createStyledButton("Back", e -> showMainPanel());

        viewPanel.add(Box.createVerticalGlue());
        viewPanel.add(allEmailsButton);
        viewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        viewPanel.add(unreadEmailsButton);
        viewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        viewPanel.add(sentEmailsButton);
        viewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        viewPanel.add(codeButton);
        viewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        viewPanel.add(backButton);
        viewPanel.add(Box.createVerticalGlue());

        mainFrame.getContentPane().removeAll();
        mainFrame.add(viewPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showEmailsDialog(List<Email> emails, String title) {
        StringBuilder message = new StringBuilder();
        message.append(title).append(":\n\n");
        for (Email email : emails) {
            message.append("+ ").append(email.getSender())
                    .append(" - ").append(email.getFormattedSubject())
                    .append(" (").append(email.getId()).append(")\n");
        }
        JOptionPane.showMessageDialog(mainFrame, message.toString(), title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSentEmailsDialog(List<Email> emails) {
        StringBuilder message = new StringBuilder();
        message.append("Sent Emails:\n\n");
        for (Email email : emails) {
            message.append("+ ").append(email.getRecipientsAsString())
                    .append(" - ").append(email.getFormattedSubject())
                    .append(" (").append(email.getId()).append(")\n");
        }
        JOptionPane.showMessageDialog(mainFrame, message.toString(), "Sent Emails", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showReadByCodePanel() {
        JPanel codePanel = new JPanel();
        codePanel.setLayout(new BoxLayout(codePanel, BoxLayout.Y_AXIS));
        codePanel.setBackground(new Color(50, 50, 50));
        codePanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel codeLabel = new JLabel("Email Code:");
        codeLabel.setForeground(Color.WHITE);
        codeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField codeField = new JTextField();
        codeField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        codeField.setMaximumSize(new Dimension(300, 30));

        JButton readButton = createStyledButton("Read", e -> {
            try {
                String code = codeField.getText().trim();
                Email email = services.getEmailByCode(code, currentUser.getEmail());
                String message = String.format("Code: %s\nRecipient(s): %s\nSubject: %s\nDate: %s\n\n%s",
                        email.getId(), email.getRecipientsAsString(), email.getFormattedSubject(),
                        Utils.formatDate(email.getDate()), email.getBody());
                JOptionPane.showMessageDialog(mainFrame, message, "Email Details", JOptionPane.INFORMATION_MESSAGE);
                showMainPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Read Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        String code = JOptionPane.showInputDialog(null, "Enter email ID:");
        if (code == null || code.trim().isEmpty()) {
            return;
        }

        try {
            Email email = services.getEmailByCode(code, currentUser.getEmail());
            String message = String.format(
                    "From: %s\nTo: %s\nSubject: %s\nDate: %s\n\n%s",
                    email.getSender(),
                    email.getRecipientsAsString(),
                    email.getFormattedSubject(),
                    Utils.formatDate(email.getDate()),
                    email.getBody()
            );
            JOptionPane.showMessageDialog(null, message, "Email Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error: " + e.getMessage(),
                    "Read Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        JButton backButton = createStyledButton("Back", e -> showViewPanel());

        codePanel.add(Box.createVerticalGlue());
        codePanel.add(codeLabel);
        codePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        codePanel.add(codeField);
        codePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        codePanel.add(readButton);
        codePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        codePanel.add(backButton);
        codePanel.add(Box.createVerticalGlue());

        mainFrame.getContentPane().removeAll();
        mainFrame.add(codePanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showReplyPanel() {
        JPanel replyPanel = new JPanel();
        replyPanel.setLayout(new BoxLayout(replyPanel, BoxLayout.Y_AXIS));
        replyPanel.setBackground(new Color(50, 50, 50));
        replyPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel codeLabel = new JLabel("Email Code:");
        codeLabel.setForeground(Color.WHITE);
        codeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField codeField = new JTextField();
        codeField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        codeField.setMaximumSize(new Dimension(300, 30));

        JLabel bodyLabel = new JLabel("Body:");
        bodyLabel.setForeground(Color.WHITE);
        bodyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bodyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea bodyArea = new JTextArea();
        bodyArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        JScrollPane bodyScroll = new JScrollPane(bodyArea);
        bodyScroll.setMaximumSize(new Dimension(300, 150));

        JButton replyButton = createStyledButton("Reply", e -> {
            try {
                String code = codeField.getText().trim();
                String body = bodyArea.getText().trim();
                Email reply = services.replyToEmail(currentUser.getEmail(), code, body);
                JOptionPane.showMessageDialog(mainFrame, "Successfully sent your reply to email " + code + ".\nCode: " + reply.getId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                showMainPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Reply Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = createStyledButton("Back", e -> showMainPanel());

        replyPanel.add(Box.createVerticalGlue());
        replyPanel.add(codeLabel);
        replyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        replyPanel.add(codeField);
        replyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        replyPanel.add(bodyLabel);
        replyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        replyPanel.add(bodyScroll);
        replyPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        replyPanel.add(replyButton);
        replyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        replyPanel.add(backButton);
        replyPanel.add(Box.createVerticalGlue());

        mainFrame.getContentPane().removeAll();
        mainFrame.add(replyPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showForwardPanel() {
        JPanel forwardPanel = new JPanel();
        forwardPanel.setLayout(new BoxLayout(forwardPanel, BoxLayout.Y_AXIS));
        forwardPanel.setBackground(new Color(50, 50, 50));
        forwardPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel codeLabel = new JLabel("Email Code:");
        codeLabel.setForeground(Color.WHITE);
        codeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField codeField = new JTextField();
        codeField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        codeField.setMaximumSize(new Dimension(300, 30));

        JLabel recipientsLabel = new JLabel("Recipient(s) (comma separated):");
        recipientsLabel.setForeground(Color.WHITE);
        recipientsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        recipientsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField recipientsField = new JTextField();
        recipientsField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        recipientsField.setMaximumSize(new Dimension(300, 30));

        JButton forwardButton = createStyledButton("Forward", e -> {
            try {
                String code = codeField.getText().trim();
                String recipientsInput = recipientsField.getText().trim();
                List<String> recipients = Arrays.asList(recipientsInput.split("\\s*,\\s*"));
                Email forwarded = services.forwardEmail(currentUser.getEmail(), code, recipients);
                JOptionPane.showMessageDialog(mainFrame, "Successfully forwarded your email.\nCode: " + forwarded.getId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                showMainPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Forward Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = createStyledButton("Back", e -> showMainPanel());

        forwardPanel.add(Box.createVerticalGlue());
        forwardPanel.add(codeLabel);
        forwardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        forwardPanel.add(codeField);
        forwardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        forwardPanel.add(recipientsLabel);
        forwardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        forwardPanel.add(recipientsField);
        forwardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        forwardPanel.add(forwardButton);
        forwardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        forwardPanel.add(backButton);
        forwardPanel.add(Box.createVerticalGlue());

        mainFrame.getContentPane().removeAll();
        mainFrame.add(forwardPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(25, 118, 210));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(66, 165, 245));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(25, 118, 210));
            }
        });
        button.addActionListener(action);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        return button;
    }

    public void start() {
        // start UI
    }
}