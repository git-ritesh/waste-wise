package gui;

import dao.UserDAO;
import models.User;
import utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Registration frame for new user registration
 */
public class RegistrationFrame extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton cancelButton;
    
    private JFrame parentFrame;
    
    /**
     * Constructor
     * @param parentFrame Parent frame (login frame)
     */
    public RegistrationFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
        setupLayout();
        setupListeners();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setTitle("Waste Wise - Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        fullNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        
        String[] roles = {"Resident", "Collector"};
        roleComboBox = new JComboBox<>(roles);
        
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");
    }
    
    /**
     * Set up the layout
     */
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("User Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(confirmPasswordField, gbc);
        
        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Full Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(fullNameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(phoneField, gbc);
        
        // Role
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Role:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(roleComboBox, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set main panel as content pane
        setContentPane(mainPanel);
    }
    
    /**
     * Set up event listeners
     */
    private void setupListeners() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
    }
    
    /**
     * Handle registration
     */
    private void register() {
        // Get input values
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String role = (String) roleComboBox.getSelectedItem();
        
        // Validate input
        if (!validateInput(username, password, confirmPassword, fullName, email, phone)) {
            return;
        }
        
        // Check if username or email already exists
        UserDAO userDAO = new UserDAO();
        if (userDAO.usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userDAO.emailExists(email)) {
            JOptionPane.showMessageDialog(this, "Email already exists", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create user object
        User user = new User(username, password, fullName, role, email, phone);
        
        // Add user to database
        boolean success = userDAO.addUser(user);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            cancel(); // Go back to login screen
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate input fields
     * @param username Username
     * @param password Password
     * @param confirmPassword Confirm password
     * @param fullName Full name
     * @param email Email
     * @param phone Phone
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInput(String username, String password, String confirmPassword, String fullName, String email, String phone) {
        if (!ValidationUtils.isValidUsername(username)) {
            JOptionPane.showMessageDialog(this, "Username must be between 4 and 20 characters", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!ValidationUtils.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!ValidationUtils.isValidName(fullName)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid full name", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!phone.isEmpty() && !ValidationUtils.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid phone number", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * Handle cancel button click
     */
    private void cancel() {
        parentFrame.setVisible(true);
        dispose();
    }
}