package gui;

import dao.FeedbackDAO;
import dao.WasteRequestDAO;
import dao.WasteTypeDAO;
import models.Feedback;
import models.User;
import models.WasteRequest;
import models.WasteType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;

/**
 * Dashboard for Resident users
 */
public class ResidentDashboard extends JFrame {
    
    private User currentUser;
    
    private JTabbedPane tabbedPane;
    private JTable requestsTable;
    private DefaultTableModel requestsTableModel;
    private JButton newRequestButton;
    private JButton refreshButton;
    private JButton logoutButton;
    private JButton provideFeedbackButton;
    
    /**
     * Constructor
     * @param user Current user
     */
    public ResidentDashboard(User user) {
        this.currentUser = user;
        initComponents();
        setupLayout();
        setupListeners();
        loadRequests();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setTitle("Waste Wise - Resident Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Requests table
        String[] requestsColumns = {"ID", "Waste Type", "Quantity (kg)", "Address", "Status", "Requested Date", "Pickup Date"};
        requestsTableModel = new DefaultTableModel(requestsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        requestsTable = new JTable(requestsTableModel);
        
        // Buttons
        newRequestButton = new JButton("New Request");
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");
        provideFeedbackButton = new JButton("Provide Feedback");
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
        
        // Requests panel
        JPanel requestsPanel = new JPanel(new BorderLayout(10, 10));
        
        JScrollPane requestsScrollPane = new JScrollPane(requestsTable);
        requestsTable.setFillsViewportHeight(true);
        
        JPanel requestsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        requestsButtonPanel.add(newRequestButton);
        requestsButtonPanel.add(refreshButton);
        requestsButtonPanel.add(provideFeedbackButton);
        
        requestsPanel.add(requestsScrollPane, BorderLayout.CENTER);
        requestsPanel.add(requestsButtonPanel, BorderLayout.SOUTH);
        
        // Add tabs
        tabbedPane.addTab("My Requests", requestsPanel);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Set main panel as content pane
        setContentPane(mainPanel);
    }
    
    /**
     * Set up event listeners
     */
    private void setupListeners() {
        newRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNewRequestDialog();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRequests();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        provideFeedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                provideFeedback();
            }
        });
    }
    
    /**
     * Load waste requests for the current user
     */
    private void loadRequests() {
        // Clear table
        requestsTableModel.setRowCount(0);
        
        // Get requests for current user
        WasteRequestDAO requestDAO = new WasteRequestDAO();
        List<WasteRequest> requests = requestDAO.getWasteRequestsByUserId(currentUser.getUserId());
        
        // Add requests to table
        for (WasteRequest request : requests) {
            Object[] row = {
                request.getRequestId(),
                request.getWasteTypeName(),
                request.getQuantity(),
                request.getAddress(),
                request.getStatus(),
                request.getRequestedDate(),
                request.getPickupDate()
            };
            requestsTableModel.addRow(row);
        }
    }
    
    /**
     * Open dialog to create a new waste request
     */
    private void openNewRequestDialog() {
        JDialog dialog = new JDialog(this, "New Waste Pickup Request", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Waste Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Waste Type:"), gbc);
        
        WasteTypeDAO wasteTypeDAO = new WasteTypeDAO();
        List<WasteType> wasteTypes = wasteTypeDAO.getAllWasteTypes();
        JComboBox<WasteType> wasteTypeComboBox = new JComboBox<>();
        for (WasteType wasteType : wasteTypes) {
            wasteTypeComboBox.addItem(wasteType);
        }
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(wasteTypeComboBox, gbc);
        
        // Quantity
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Quantity (kg):"), gbc);
        
        JTextField quantityField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(quantityField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Address:"), gbc);
        
        JTextArea addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(addressScrollPane, gbc);
        
        // Requested Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Requested Date:"), gbc);
        
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(dateSpinner, gbc);
        
        // Buttons
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        // Add action listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate input
                if (wasteTypeComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(dialog, "Please select a waste type", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String quantityStr = quantityField.getText();
                if (!quantityStr.matches("\\d+(\\.\\d+)?")) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a valid quantity", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String address = addressArea.getText();
                if (address.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter an address", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create waste request
                WasteType selectedWasteType = (WasteType) wasteTypeComboBox.getSelectedItem();
                double quantity = Double.parseDouble(quantityStr);
                java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
                Date sqlDate = new Date(utilDate.getTime());
                
                WasteRequest request = new WasteRequest(
                    currentUser.getUserId(),
                    selectedWasteType.getTypeId(),
                    quantity,
                    address,
                    sqlDate
                );
                
                // Save to database
                WasteRequestDAO requestDAO = new WasteRequestDAO();
                boolean success = requestDAO.addWasteRequest(request);
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Request submitted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadRequests(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to submit request", "Error", JOptionPane.ERROR_MESSAGE);
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
     * Open dialog to provide feedback for a completed request
     */
    private void provideFeedback() {
        // Check if a row is selected
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the selected request ID
        int requestId = (int) requestsTable.getValueAt(selectedRow, 0);
        String status = (String) requestsTable.getValueAt(selectedRow, 4);
        
        // Check if the request is completed
        if (!status.equals("Collected")) {
            JOptionPane.showMessageDialog(this, "You can only provide feedback for collected requests", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if feedback already exists
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        Feedback existingFeedback = feedbackDAO.getFeedbackByRequestId(requestId);
        
        if (existingFeedback != null) {
            JOptionPane.showMessageDialog(this, "You have already provided feedback for this request", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create feedback dialog
        JDialog dialog = new JDialog(this, "Provide Feedback", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Rating
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Rating (1-5):"), gbc);
        
        JSpinner ratingSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(ratingSpinner, gbc);
        
        // Comments
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Comments:"), gbc);
        
        JTextArea commentsArea = new JTextArea(5, 20);
        commentsArea.setLineWrap(true);
        JScrollPane commentsScrollPane = new JScrollPane(commentsArea);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(commentsScrollPane, gbc);
        
        // Buttons
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        // Add action listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rating = (int) ratingSpinner.getValue();
                String comments = commentsArea.getText();
                
                if (comments.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter comments", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create feedback
                Feedback feedback = new Feedback(
                    currentUser.getUserId(),
                    requestId,
                    rating,
                    comments
                );
                
                // Save to database
                boolean success = feedbackDAO.addFeedback(feedback);
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Feedback submitted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to submit feedback", "Error", JOptionPane.ERROR_MESSAGE);
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