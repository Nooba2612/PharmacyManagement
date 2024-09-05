package pharmacy.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.border.*;

import pharmacy.controllers.AuthController;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
	private JLabel pharmacyIcon;
	private JTextField usernameField;
	private JPasswordField passwordField;

	public LoginFrame() {
		setTitle("Pharmacy Management System - Login");
		getContentPane().setBackground(new Color(51, 153, 51));
		setExtendedState(JFrame.MAXIMIZED_BOTH); // window full screen
		setUndecorated(false); // remove window decorations
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		// center the text horizontally
		setVisible(true);
		JLabel label = new JLabel();
		label.setBounds(379, 15, 0, 0);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(821, 239, 471, 408);
		panel.setLayout(null);

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setForeground(new Color(0, 102, 0));
		usernameLabel.setBounds(30, 133, 143, 17);
		usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 19));
		panel.add(usernameLabel);

		usernameField = new JTextField(15);
		usernameField.setBackground(new Color(167, 207, 177));
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		usernameField.setBounds(162, 128, 238, 32);
		panel.add(usernameField);

		passwordField = new JPasswordField(15);
		passwordField.setBackground(new Color(167, 207, 177));
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		passwordField.setBounds(162, 195, 238, 32);
		panel.add(passwordField);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setForeground(new Color(51, 102, 0));
		passwordLabel.setBounds(30, 198, 122, 17);
		passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 19));
		panel.add(passwordLabel);

		JButton loginButton = new JButton("Tiếp tục");
		loginButton.setBounds(202, 263, 143, 29);
		loginButton.setForeground(new Color(255, 255, 255));
		loginButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		loginButton.setBackground(new Color(51, 102, 0));
		panel.add(loginButton);

		JLabel title = new JLabel("Pharmacy Management System");
		title.setBounds(684, 43, 746, 92);
		title.setForeground(new Color(255, 255, 255));
		title.setFont(new Font("Tahoma", Font.BOLD, 45));
		getContentPane().setLayout(null);
		getContentPane().add(label);
		getContentPane().add(panel);

		JLabel loginTitle = new JLabel("Đăng Nhập");
		loginTitle.setHorizontalAlignment(SwingConstants.CENTER);
		loginTitle.setFont(new Font("Tahoma", Font.BOLD, 25));
		loginTitle.setBackground(new Color(255, 255, 255));
		loginTitle.setForeground(new Color(51, 153, 51));
		loginTitle.setBounds(91, 40, 276, 32);
		panel.add(loginTitle);
		getContentPane().add(title);

		JPanel pharmacyIconPanel = new JPanel();
		pharmacyIconPanel.setBorder(new LineBorder(new Color(51, 153, 51), 30, true));
		pharmacyIconPanel.setBackground(new Color(255, 255, 255));
		pharmacyIconPanel.setBounds(0, -11, 611, 816);
		getContentPane().add(pharmacyIconPanel);
		pharmacyIconPanel.setLayout(new BorderLayout(0, 0));

		pharmacyIcon = new JLabel();
		pharmacyIcon.setHorizontalAlignment(SwingConstants.CENTER);
		pharmacyIcon.setBackground(new Color(255, 255, 255));
		pharmacyIcon.setIcon(new ImageIcon(LoginFrame.class.getResource("/images/pharmacy-icon.png")));
		pharmacyIconPanel.add(pharmacyIcon);

		// handle login button click
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					handleLogin();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		usernameField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					handleLogin();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		passwordField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					handleLogin();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	protected void handleLogin() throws SQLException {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		AuthController authController = new AuthController();
		
		if (authController.authenticateUser(username, password)) {
			new DashboardFrame();
			LoginFrame.this.setVisible(false);
		} else {
			JOptionPane.showMessageDialog(LoginFrame.this, "Thông tin người dùng không hợp lệ", "Đăng nhập thất bại",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
