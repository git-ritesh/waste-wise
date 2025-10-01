package gui;

import dao.*;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.sql.Date;
import java.util.List;

/**
 * Dashboard for Admin users
 */
public class AdminDashboard extends JFrame {
    
    private User currentUser;
    
    private JTabbedPane tabbedPane;
    
    // Requests tab components
    private JTable requestsTable;
    private DefaultTableModel requestsTableModel;
    private JButton assignButton;
    private JButton refreshRequestsButton;
    
    // Users tab components
    private JTable usersTable;
    private DefaultTableModel usersTableModel;
    private JButton addUserButton;
    private JButton editUserButton;
    private JButton deleteUserButton;
    private JButton refreshUsersButton;
    
    // Assignments tab components
    private JTable assignmentsTable;
    private DefaultTableModel assignmentsTableModel;
    private JButton refreshAssignmentsButton;
    
    // Feedback tab components
    private JTable feedbackTable;
    private DefaultTableModel feedbackTableModel;
    private JButton refreshFeedbackButton;
    
    // Reports tab components
    private JPanel reportsPanel;
    
    // Other components
    private JButton logoutButton;
    
    /**
     * Constructor
     * @param user Current user
     */
    public AdminDashboard(User user) {
        this.currentUser = user;
        initComponents();
        setupLayout();
        setupListeners();
        loadData();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setTitle("Waste Wise - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Requests table
        String[] requestsColumns = {"ID", "Resident", "Waste Type", "Quantity (kg)", "Address", "Status", "Requested Date"};
        requestsTableModel = new DefaultTableModel(requestsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        requestsTable = new JTable(requestsTableModel);
        
        // Users table
        String[] usersColumns = {"ID", "Username", "Full Name", "Role", "Email", "Phone"};
        usersTableModel = new DefaultTableModel(usersColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        usersTable = new JTable(usersTableModel);
        
        // Assignments table
        String[] assignmentsColumns = {"ID", "Request ID", "Collector", "Request Details", "Assigned Date", "Status"};
        assignmentsTableModel = new DefaultTableModel(assignmentsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        assignmentsTable = new JTable(assignmentsTableModel);
        
        // Feedback table
        String[] feedbackColumns = {"ID", "Resident", "Request Details", "Rating", "Comments", "Submitted On"};
        feedbackTableModel = new DefaultTableModel(feedbackColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        feedbackTable = new JTable(feedbackTableModel);
        
        // Reports panel
        reportsPanel = new JPanel();
        
        // Buttons
        assignButton = new JButton("Assign to Collector");
        refreshRequestsButton = new JButton("Refresh");
        
        addUserButton = new JButton("Add User");
        editUserButton = new JButton("Edit User");
        deleteUserButton = new JButton("Delete User");
        refreshUsersButton = new JButton("Refresh");
        
        refreshAssignmentsButton = new JButton("Refresh");
        
        refreshFeedbackButton = new JButton("Refresh");
        
        logoutButton = new JButton("Logout");
    }
    
    /**
     * Set up the layout
     */
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JPanel headerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerButtonPanel.add(logoutButton);
        headerPanel.add(headerButtonPanel, BorderLayout.EAST);
        
        // Requests tab
        JPanel requestsPanel = new JPanel(new BorderLayout(10, 10));
        
        JScrollPane requestsScrollPane = new JScrollPane(requestsTable);
        requestsTable.setFillsViewportHeight(true);
        
        JPanel requestsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        requestsButtonPanel.add(assignButton);
        requestsButtonPanel.add(refreshRequestsButton);
        
        requestsPanel.add(requestsScrollPane, BorderLayout.CENTER);
        requestsPanel.add(requestsButtonPanel, BorderLayout.SOUTH);
        
        // Users tab
        JPanel usersPanel = new JPanel(new BorderLayout(10, 10));
        
        JScrollPane usersScrollPane = new JScrollPane(usersTable);
        usersTable.setFillsViewportHeight(true);
        
        JPanel usersButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usersButtonPanel.add(addUserButton);
        usersButtonPanel.add(editUserButton);
        usersButtonPanel.add(deleteUserButton);
        usersButtonPanel.add(refreshUsersButton);
        
        usersPanel.add(usersScrollPane, BorderLayout.CENTER);
        usersPanel.add(usersButtonPanel, BorderLayout.SOUTH);
        
        // Assignments tab
        JPanel assignmentsPanel = new JPanel(new BorderLayout(10, 10));
        
        JScrollPane assignmentsScrollPane = new JScrollPane(assignmentsTable);
        assignmentsTable.setFillsViewportHeight(true);
        
        JPanel assignmentsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        assignmentsButtonPanel.add(refreshAssignmentsButton);
        
        assignmentsPanel.add(assignmentsScrollPane, BorderLayout.CENTER);
        assignmentsPanel.add(assignmentsButtonPanel, BorderLayout.SOUTH);
        
        // Feedback tab
        JPanel feedbackPanel = new JPanel(new BorderLayout(10, 10));
        
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackTable);
        feedbackTable.setFillsViewportHeight(true);
        
        JPanel feedbackButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        feedbackButtonPanel.add(refreshFeedbackButton);
        
        feedbackPanel.add(feedbackScrollPane, BorderLayout.CENTER);
        feedbackPanel.add(feedbackButtonPanel, BorderLayout.SOUTH);
        
        // Reports tab
        setupReportsPanel();
        
        // Add tabs
        tabbedPane.addTab("Waste Requests", requestsPanel);
        tabbedPane.addTab("Users", usersPanel);
        tabbedPane.addTab("Assignments", assignmentsPanel);
        tabbedPane.addTab("Feedback", feedbackPanel);
        tabbedPane.addTab("Reports", reportsPanel);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Set main panel as content pane
        setContentPane(mainPanel);
    }
    
    /**
     * Set up the reports panel
     */
    private void setupReportsPanel() {
        reportsPanel.setLayout(new BorderLayout(10, 10));
        
        JPanel reportButtonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        reportButtonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton wasteByTypeButton = new JButton("Waste Collection by Type");
        JButton requestStatusButton = new JButton("Request Status Summary");
        JButton collectorPerformanceButton = new JButton("Collector Performance");
        
        reportButtonsPanel.add(wasteByTypeButton);
        reportButtonsPanel.add(requestStatusButton);
        reportButtonsPanel.add(collectorPerformanceButton);
        
        JPanel reportContentPanel = new JPanel();
        reportContentPanel.setBorder(BorderFactory.createTitledBorder("Report Results"));
        reportContentPanel.setLayout(new BorderLayout());
        
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        JScrollPane reportScrollPane = new JScrollPane(reportTextArea);
        reportContentPanel.add(reportScrollPane, BorderLayout.CENTER);
        
        reportsPanel.add(reportButtonsPanel, BorderLayout.WEST);
        reportsPanel.add(reportContentPanel, BorderLayout.CENTER);
        
        // Add action listeners for report buttons
        wasteByTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateWasteByTypeReport(reportTextArea);
            }
        });
        
        requestStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRequestStatusReport(reportTextArea);
            }
        });
        
        collectorPerformanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCollectorPerformanceReport(reportTextArea);
            }
        });
    }
    
    /**
     * Set up event listeners
     */
    private void setupListeners() {
        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignRequestToCollector();
            }
        });
        
        refreshRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRequests();
            }
        });
        
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });
        
        editUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editUser();
            }
        });
        
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        
        refreshUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });
        
        refreshAssignmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAssignments();
            }
        });
        
        refreshFeedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFeedback();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }
    
    /**
     * Load all data for the dashboard
     */
    private void loadData() {
        loadRequests();
        loadUsers();
        loadAssignments();
        loadFeedback();
    }
    
    /**
     * Load waste requests
     */
    private void loadRequests() {
        // Clear table
        requestsTableModel.setRowCount(0);
        
        // Get all requests
        WasteRequestDAO requestDAO = new WasteRequestDAO();
        List<WasteRequest> requests = requestDAO.getAllWasteRequests();
        
        // Add requests to table
        for (WasteRequest request : requests) {
            Object[] row = {
                request.getRequestId(),
                request.getUserName(),
                request.getWasteTypeName(),
                request.getQuantity(),
                request.getAddress(),
                request.getStatus(),
                request.getRequestedDate()
            };
            requestsTableModel.addRow(row);
        }
    }
    
    /**
     * Load users
     */
    private void loadUsers() {
        // Clear table
        usersTableModel.setRowCount(0);
        
        // Get all users
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        
        // Add users to table
        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole(),
                user.getEmail(),
                user.getPhone()
            };
            usersTableModel.addRow(row);
        }
    }
    
    /**
     * Load assignments
     */
    private void loadAssignments() {
        // Clear table
        assignmentsTableModel.setRowCount(0);
        
        // Get all assignments
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        List<Assignment> assignments = assignmentDAO.getAllAssignments();
        
        // Add assignments to table
        for (Assignment assignment : assignments) {
            Object[] row = {
                assignment.getAssignmentId(),
                assignment.getRequestId(),
                assignment.getCollectorName(),
                assignment.getRequestDetails(),
                assignment.getAssignedDate(),
                assignment.getStatus()
            };
            assignmentsTableModel.addRow(row);
        }
    }
    
    /**
     * Load feedback
     */
    private void loadFeedback() {
        // Clear table
        feedbackTableModel.setRowCount(0);
        
        // Get all feedback
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        List<Feedback> feedbackList = feedbackDAO.getAllFeedback();
        
        // Add feedback to table
        for (Feedback feedback : feedbackList) {
            Object[] row = {
                feedback.getFeedbackId(),
                feedback.getUserName(),
                feedback.getRequestDetails(),
                feedback.getRating(),
                feedback.getComments(),
                feedback.getSubmittedOn()
            };
            feedbackTableModel.addRow(row);
        }
    }
    
    /**
     * Assign a request to a collector
     */
    private void assignRequestToCollector() {
        // Check if a row is selected
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the selected request ID and status
        int requestId = (int) requestsTable.getValueAt(selectedRow, 0);
        String status = (String) requestsTable.getValueAt(selectedRow, 5);
        
        // Check if the request is already assigned
        if (!status.equals("Pending")) {
            JOptionPane.showMessageDialog(this, "This request is already " + status, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get collectors
        UserDAO userDAO = new UserDAO();
        List<User> collectors = userDAO.getUsersByRole("Collector");
        
        if (collectors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No collectors available", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Show collector selection dialog
        User selectedCollector = (User) JOptionPane.showInputDialog(
            this,
            "Select a collector:",
            "Assign Request",
            JOptionPane.QUESTION_MESSAGE,
            null,
            collectors.toArray(),
            collectors.get(0)
        );
        
        if (selectedCollector != null) {
            // Create assignment
            Assignment assignment = new Assignment(requestId, selectedCollector.getUserId());
            
            // Save to database
            AssignmentDAO assignmentDAO = new AssignmentDAO();
            boolean success = assignmentDAO.addAssignment(assignment);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Request assigned successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadRequests(); // Refresh the requests table
                loadAssignments(); // Refresh the assignments table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to assign request", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Add a new user
     */
    private void addUser() {
        JDialog dialog = new JDialog(this, "Add User", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);
        
        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Full Name:"), gbc);
        
        JTextField fullNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(fullNameField, gbc);
        
        // Role
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Role:"), gbc);
        
        String[] roles = {"Admin", "Resident", "Collector"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(roleComboBox, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Email:"), gbc);
        
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Phone:"), gbc);
        
        JTextField phoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(phoneField, gbc);
        
        // Buttons
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String fullName = fullNameField.getText();
                String role = (String) roleComboBox.getSelectedItem();
                String email = emailField.getText();
                String phone = phoneField.getText();
                
                // Validate input
                if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all required fields", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create user object
                User user = new User(username, password, fullName, role, email, phone);
                
                // Save to database
                UserDAO userDAO = new UserDAO();
                boolean success = userDAO.addUser(user);
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "User added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadUsers(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add user", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Edit an existing user
     */
    private void editUser() {
        // Check if a row is selected
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the selected user ID
        int userId = (int) usersTable.getValueAt(selectedRow, 0);
        
        // Get user from database
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(userId);
        
        if (user == null) {
            JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create edit dialog
        JDialog dialog = new JDialog(this, "Edit User", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        JTextField usernameField = new JTextField(user.getUsername(), 20);
        usernameField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);
        
        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Full Name:"), gbc);
        
        JTextField fullNameField = new JTextField(user.getFullName(), 20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(fullNameField, gbc);
        
        // Role
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Role:"), gbc);
        
        String[] roles = {"Admin", "Resident", "Collector"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setSelectedItem(user.getRole());
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(roleComboBox, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        
        JTextField emailField = new JTextField(user.getEmail(), 20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Phone:"), gbc);
        
        JTextField phoneField = new JTextField(user.getPhone(), 20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(phoneField, gbc);
        
        // Reset Password checkbox
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Reset Password:"), gbc);
        
        JCheckBox resetPasswordCheckBox = new JCheckBox();
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(resetPasswordCheckBox, gbc);
        
        // New Password
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setEnabled(false);
        panel.add(newPasswordLabel, gbc);
        
        JPasswordField newPasswordField = new JPasswordField(20);
        newPasswordField.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(newPasswordField, gbc);
        
        // Add listener to reset password checkbox
        resetPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = resetPasswordCheckBox.isSelected();
                newPasswordLabel.setEnabled(selected);
                newPasswordField.setEnabled(selected);
            }
        });
        
        // Buttons
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String fullName = fullNameField.getText();
                String role = (String) roleComboBox.getSelectedItem();
                String email = emailField.getText();
                String phone = phoneField.getText();
                
                // Validate input
                if (fullName.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all required fields", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Update user object
                user.setFullName(fullName);
                user.setRole(role);
                user.setEmail(email);
                user.setPhone(phone);
                
                // Update in database
                boolean success = userDAO.updateUser(user);
                
                // Reset password if requested
                if (resetPasswordCheckBox.isSelected()) {
                    String newPassword = new String(newPasswordField.getPassword());
                    if (newPassword.length() < 6) {
                        JOptionPane.showMessageDialog(dialog, "Password must be at least 6 characters", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    success = success && userDAO.updatePassword(userId, newPassword);
                }
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "User updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadUsers(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update user", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Delete a user
     */
    private void deleteUser() {
        // Check if a row is selected
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the selected user ID and username
        int userId = (int) usersTable.getValueAt(selectedRow, 0);
        String username = (String) usersTable.getValueAt(selectedRow, 1);
        
        // Confirm deletion
        int option = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete user '" + username + "'?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            // Delete from database
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.deleteUser(userId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "User deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user. The user may have associated records.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Generate waste by type report
     * @param reportTextArea Text area to display the report
     */
    private void generateWasteByTypeReport(JTextArea reportTextArea) {
        StringBuilder report = new StringBuilder();
        report.append("WASTE COLLECTION BY TYPE REPORT\n");
        report.append("===============================\n\n");
        
        WasteRequestDAO requestDAO = new WasteRequestDAO();
        List<WasteRequest> requests = requestDAO.getAllWasteRequests();
        
        // Group by waste type
        java.util.Map<String, Double> wasteByType = new java.util.HashMap<>();
        
        for (WasteRequest request : requests) {
            String wasteType = request.getWasteTypeName();
            double quantity = request.getQuantity();
            
            if (wasteByType.containsKey(wasteType)) {
                wasteByType.put(wasteType, wasteByType.get(wasteType) + quantity);
            } else {
                wasteByType.put(wasteType, quantity);
            }
        }
        
        // Sort by quantity (descending)
        java.util.List<java.util.Map.Entry<String, Double>> sortedEntries = new java.util.ArrayList<>(wasteByType.entrySet());
        java.util.Collections.sort(sortedEntries, new java.util.Comparator<java.util.Map.Entry<String, Double>>() {
            @Override
            public int compare(java.util.Map.Entry<String, Double> e1, java.util.Map.Entry<String, Double> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });
        
        // Calculate total
        double total = 0;
        for (double quantity : wasteByType.values()) {
            total += quantity;
        }
        
        // Format report
        report.append(String.format("%-20s %-15s %-15s\n", "Waste Type", "Quantity (kg)", "Percentage"));
        report.append(String.format("%-20s %-15s %-15s\n", "----------", "------------", "----------"));
        
        for (java.util.Map.Entry<String, Double> entry : sortedEntries) {
            String wasteType = entry.getKey();
            double quantity = entry.getValue();
            double percentage = (quantity / total) * 100;
            
            report.append(String.format("%-20s %-15.2f %-15.2f%%\n", wasteType, quantity, percentage));
        }
        
        report.append(String.format("\n%-20s %-15.2f %-15s\n", "TOTAL", total, "100.00%"));
        
        reportTextArea.setText(report.toString());
    }
    
    /**
     * Generate request status report
     * @param reportTextArea Text area to display the report
     */
    private void generateRequestStatusReport(JTextArea reportTextArea) {
        StringBuilder report = new StringBuilder();
        report.append("REQUEST STATUS SUMMARY REPORT\n");
        report.append("=============================\n\n");
        
        WasteRequestDAO requestDAO = new WasteRequestDAO();
        List<WasteRequest> requests = requestDAO.getAllWasteRequests();
        
        // Count by status
        int pendingCount = 0;
        int assignedCount = 0;
        int inProgressCount = 0;
        int collectedCount = 0;
        
        for (WasteRequest request : requests) {
            String status = request.getStatus();
            
            switch (status) {
                case "Pending":
                    pendingCount++;
                    break;
                case "Assigned":
                    assignedCount++;
                    break;
                case "In Progress":
                    inProgressCount++;
                    break;
                case "Collected":
                    collectedCount++;
                    break;
            }
        }
        
        int totalCount = requests.size();
        
        // Format report
        report.append(String.format("%-20s %-15s %-15s\n", "Status", "Count", "Percentage"));
        report.append(String.format("%-20s %-15s %-15s\n", "------", "-----", "----------"));
        
        report.append(String.format("%-20s %-15d %-15.2f%%\n", "Pending", pendingCount, (double) pendingCount / totalCount * 100));
        report.append(String.format("%-20s %-15d %-15.2f%%\n", "Assigned", assignedCount, (double) assignedCount / totalCount * 100));
        report.append(String.format("%-20s %-15d %-15.2f%%\n", "In Progress", inProgressCount, (double) inProgressCount / totalCount * 100));
        report.append(String.format("%-20s %-15d %-15.2f%%\n", "Collected", collectedCount, (double) collectedCount / totalCount * 100));
        
        report.append(String.format("\n%-20s %-15d %-15s\n", "TOTAL", totalCount, "100.00%"));
        
        reportTextArea.setText(report.toString());
    }
    
    /**
     * Generate collector performance report
     * @param reportTextArea Text area to display the report
     */
    private void generateCollectorPerformanceReport(JTextArea reportTextArea) {
        StringBuilder report = new StringBuilder();
        report.append("COLLECTOR PERFORMANCE REPORT\n");
        report.append("============================\n\n");
        
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        List<Assignment> assignments = assignmentDAO.getAllAssignments();
        
        // Group by collector
        java.util.Map<String, Integer> totalByCollector = new java.util.HashMap<>();
        java.util.Map<String, Integer> completedByCollector = new java.util.HashMap<>();
        
        for (Assignment assignment : assignments) {
            String collectorName = assignment.getCollectorName();
            
            // Count total assignments
            if (totalByCollector.containsKey(collectorName)) {
                totalByCollector.put(collectorName, totalByCollector.get(collectorName) + 1);
            } else {
                totalByCollector.put(collectorName, 1);
            }
            
            // Count completed assignments
            if (assignment.getStatus().equals("Completed")) {
                if (completedByCollector.containsKey(collectorName)) {
                    completedByCollector.put(collectorName, completedByCollector.get(collectorName) + 1);
                } else {
                    completedByCollector.put(collectorName, 1);
                }
            }
        }
        
        // Format report
        report.append(String.format("%-25s %-15s %-15s %-15s\n", "Collector", "Total Assigned", "Completed", "Completion Rate"));
        report.append(String.format("%-25s %-15s %-15s %-15s\n", "---------", "--------------", "---------", "--------------"));
        
        for (String collectorName : totalByCollector.keySet()) {
            int total = totalByCollector.get(collectorName);
            int completed = completedByCollector.getOrDefault(collectorName, 0);
            double completionRate = (double) completed / total * 100;
            
            report.append(String.format("%-25s %-15d %-15d %-15.2f%%\n", collectorName, total, completed, completionRate));
        }
        
        reportTextArea.setText(report.toString());
    }
    
    /**
     * Handle logout
     */
    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}