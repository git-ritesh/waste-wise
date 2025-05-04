package gui;

import dao.AssignmentDAO;
import models.Assignment;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dashboard for Collector users
 */
public class CollectorDashboard extends JFrame {
    
    private User currentUser;
    
    private JTabbedPane tabbedPane;
    private JTable assignmentsTable;
    private DefaultTableModel assignmentsTableModel;
    private JButton updateStatusButton;
    private JButton refreshButton;
    private JButton logoutButton;
    
    /**
     * Constructor
     * @param user Current user
     */
    public CollectorDashboard(User user) {
        this.currentUser = user;
        initComponents();
        setupLayout();
        setupListeners();
        loadAssignments();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setTitle("Waste Wise - Collector Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Assignments table
        String[] assignmentsColumns = {"ID", "Request ID", "Request Details", "Assigned Date", "Status"};
        assignmentsTableModel = new DefaultTableModel(assignmentsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        assignmentsTable = new JTable(assignmentsTableModel);
        
        // Buttons
        updateStatusButton = new JButton("Update Status");
        refreshButton = new JButton("Refresh");
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
        
        // Assignments panel
        JPanel assignmentsPanel = new JPanel(new BorderLayout(10, 10));
        
        JScrollPane assignmentsScrollPane = new JScrollPane(assignmentsTable);
        assignmentsTable.setFillsViewportHeight(true);
        
        JPanel assignmentsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        assignmentsButtonPanel.add(updateStatusButton);
        assignmentsButtonPanel.add(refreshButton);
        
        assignmentsPanel.add(assignmentsScrollPane, BorderLayout.CENTER);
        assignmentsPanel.add(assignmentsButtonPanel, BorderLayout.SOUTH);
        
        // Add tabs
        tabbedPane.addTab("My Assignments", assignmentsPanel);
        
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
        updateStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAssignmentStatus();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAssignments();
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
     * Load assignments for the current collector
     */
    private void loadAssignments() {
        // Clear table
        assignmentsTableModel.setRowCount(0);
        
        // Get assignments for current collector
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        List<Assignment> assignments = assignmentDAO.getAssignmentsByCollectorId(currentUser.getUserId());
        
        // Add assignments to table
        for (Assignment assignment : assignments) {
            Object[] row = {
                assignment.getAssignmentId(),
                assignment.getRequestId(),
                assignment.getRequestDetails(),
                assignment.getAssignedDate(),
                assignment.getStatus()
            };
            assignmentsTableModel.addRow(row);
        }
    }
    
    /**
     * Update the status of an assignment
     */
    private void updateAssignmentStatus() {
        // Check if a row is selected
        int selectedRow = assignmentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an assignment", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the selected assignment ID and current status
        int assignmentId = (int) assignmentsTable.getValueAt(selectedRow, 0);
        String currentStatus = (String) assignmentsTable.getValueAt(selectedRow, 4);
        
        // Create status options based on current status
        String[] statusOptions;
        if (currentStatus.equals("Assigned")) {
            statusOptions = new String[]{"In Progress", "Completed"};
        } else if (currentStatus.equals("In Progress")) {
            statusOptions = new String[]{"Completed"};
        } else {
            JOptionPane.showMessageDialog(this, "This assignment is already completed", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Show status selection dialog
        String newStatus = (String) JOptionPane.showInputDialog(
            this,
            "Select new status:",
            "Update Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statusOptions,
            statusOptions[0]
        );
        
        if (newStatus != null) {
            // Update status in database
            AssignmentDAO assignmentDAO = new AssignmentDAO();
            boolean success = assignmentDAO.updateAssignmentStatus(assignmentId, newStatus);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Status updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAssignments(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update status", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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