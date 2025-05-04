package gui;

import dao.UserDAO;
import models.User;
// import utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login frame for user authentication
 */
public class LoginFrame extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    /**
     * Constructor
     */
    public LoginFrame() {
        initComponents();
        setupLayout();
        setupListeners();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setTitle("Waste Wise - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
    }
    
    /**
     * Set up the layout
     */
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel logoLabel = new JLabel("Waste Wise");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(0, 102, 0));
        logoPanel.add(logoLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        // Add panels to main panel
        mainPanel.add(logoPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set main panel as content pane
        setContentPane(mainPanel);
    }
    
    /**
     * Set up event listeners
     */
    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationFrame();
            }
        });
    }
    
    /**
     * Handle login
     */
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticateUser(username, password);
        
        if (user != null) {
            // Login successful
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Open appropriate dashboard based on user role
            openDashboard(user);
            
            // Close login frame
            dispose();
        } else {
            // Login failed
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Open the registration frame
     */
    private void openRegistrationFrame() {
        RegistrationFrame registrationFrame = new RegistrationFrame(this);
        registrationFrame.setVisible(true);
        setVisible(false);
    }
    
    /**
     * Open the appropriate dashboard based on user role
     * @param user Authenticated user
     */
    private void openDashboard(User user) {
        switch (user.getRole()) {
            case "Admin":
                AdminDashboard adminDashboard = new AdminDashboard(user);
                adminDashboard.setVisible(true);
                break;
            case "Resident":
                ResidentDashboard residentDashboard = new ResidentDashboard(user);
                residentDashboard.setVisible(true);
                break;
            case "Collector":
                CollectorDashboard collectorDashboard = new CollectorDashboard(user);
                collectorDashboard.setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown user role", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
    
    /**
     * Main method to start the application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            // Set look and feel to system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}