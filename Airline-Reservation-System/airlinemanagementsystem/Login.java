package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JButton submit, reset, close, register;
    JTextField tfusername;
    JPasswordField tfpassword;

    public Login() {
        // Set up the frame properties
        setTitle("Login");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Username Label and Field
        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(20, 20, 100, 20);
        add(lblusername);

        tfusername = new JTextField();
        tfusername.setBounds(130, 20, 200, 20);
        add(tfusername);

        // Password Label and Field
        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(20, 60, 100, 20);
        add(lblpassword);

        tfpassword = new JPasswordField();
        tfpassword.setBounds(130, 60, 200, 20);
        add(tfpassword);

        // Reset Button
        reset = new JButton("Reset");
        reset.setBounds(40, 160, 120, 20);
        reset.addActionListener(this);
        add(reset);

        // Submit Button
        submit = new JButton("Submit");
        int submitButtonWidth = 120;
        int frameWidth = 400;
        int centerX = (frameWidth - submitButtonWidth) / 2; // Center the button horizontally
        submit.setBounds(centerX, 120, submitButtonWidth, 20);
        submit.addActionListener(this);
        add(submit);

        // Register Button
        register = new JButton("New User");
        register.setBounds(220, 160, 120, 20);
        register.addActionListener(this);
        add(register);

        // Close Button
        close = new JButton("Close");
        close.setBounds(centerX, 200, 120, 20);
        close.addActionListener(this);
        add(close);

        // Frame settings
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ensures the application exits when closed
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            handleLogin();
        } else if (ae.getSource() == register) {
            handleRegister();
        } else if (ae.getSource() == close) {
            dispose(); // Close the application
        } else if (ae.getSource() == reset) {
            tfusername.setText("");
            tfpassword.setText("");
        }
    }

    private void handleLogin() {
        String username = tfusername.getText();
        String password = new String(tfpassword.getPassword()); // Securely get password

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.getConnection(); // Get the connection

            if (conn != null) { // Check if the connection is successful
                String query = "SELECT * FROM login WHERE username = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(query);

                // Set parameters
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    new Home(); // Redirect to Home screen on successful login
                    dispose(); // Close login window
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Login Error", JOptionPane.ERROR_MESSAGE);
                    tfusername.setText("");
                    tfpassword.setText("");
                }

                // Close resources
                rs.close();
                stmt.close();
                conn.close(); // Close the database connection

            } else {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String username = tfusername.getText();
        String password = new String(tfpassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.getConnection();

            if (conn != null) {
                // Check if username already exists
                String checkQuery = "SELECT * FROM login WHERE username = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, username);
                ResultSet checkRs = checkStmt.executeQuery();

                if (checkRs.next()) {
                    JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Insert new user into the database
                    String query = "INSERT INTO login (username, password) VALUES (?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, username);
                    stmt.setString(2, password);

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Registration Successful! Welcome to the Airline Management System.");
                        new Home(); // Redirect to Home screen after successful registration
                        dispose(); // Close the registration/login window
                    }

                    stmt.close();
                }

                checkRs.close();
                checkStmt.close();
                conn.close();
            } else {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new); // Use invokeLater for thread safety
    }
}
