package runngun.main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.border.BevelBorder;

import javax.swing.JButton;
import java.awt.SystemColor;

import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.awt.event.ActionEvent;


@SuppressWarnings("serial")
class SecurityPrompt extends JFrame {
	private JPanel contentPane;
	private JTextField sigTextField;
	private JPasswordField passTextField;
	
	SecurityPrompt(Consumer<String> game) {
		setResizable(false);
		setTitle("Security Prompt");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 478, 325);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		IanDRM.verifyNotIan(contentPane);
		
		JButton btnNewButton = new JButton("Start Game");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sigTextField.getText().contentEquals("")) {
					JOptionPane.showMessageDialog(contentPane, "Please sign the non-disclosure agreement");
					return;
				}
				else if(!passTextField.getText().contentEquals("uh question")) {
					JOptionPane.showMessageDialog(contentPane, "Incorrect password");
					return;
				}
				game.accept(sigTextField.getText());
				setVisible(false);
			}
		});
		btnNewButton.setBackground(SystemColor.controlHighlight);
		btnNewButton.setFont(new Font("Arial", Font.PLAIN, 11));
		btnNewButton.setBounds(244, 152, 208, 89);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnNewButton_1.setBackground(SystemColor.controlHighlight);
		btnNewButton_1.setFont(new Font("Arial", Font.PLAIN, 11));
		btnNewButton_1.setBounds(244, 252, 208, 23);
		contentPane.add(btnNewButton_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBounds(10, 11, 224, 264);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Non-disclosure agreement");
		lblNewLabel_1.setBounds(10, 11, 204, 20);
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Arial", Font.ITALIC, 14));
		
		sigTextField = new JTextField();
		sigTextField.setBounds(76, 233, 138, 20);
		panel_1.add(sigTextField);
		sigTextField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Signature: ");
		lblNewLabel.setBounds(10, 236, 71, 14);
		panel_1.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 42, 204, 180);
		panel_1.add(scrollPane_1);
		
		JTextPane txtpnIAgreenyeet = new JTextPane();
		scrollPane_1.setViewportView(txtpnIAgreenyeet);
		txtpnIAgreenyeet.setFont(new Font("Arial", Font.PLAIN, 12));
		txtpnIAgreenyeet.setText("I irrevocably agree that,\r\n\r\nI will not share my copy of the game, or any information or documentation associated with it outside of the company.\r\n\r\nIf my copy of the game is leaked, I take full responsibility and am liable to pay for damages up to the sum of \u00A31,000,000,000.\r\n\r\nI will take the correct precautions to ensure that sensitive information is not shared outside of the company.");
		txtpnIAgreenyeet.setEditable(false);
		txtpnIAgreenyeet.setCaretPosition(0);
		
		JTextPane txtpnAsASecurity = new JTextPane();
		txtpnAsASecurity.setEditable(false);
		txtpnAsASecurity.setFont(new Font("Arial", Font.PLAIN, 12));
		txtpnAsASecurity.setText("As a security precaution, you must enter the password given to you in the email and sign the non-disclosure agreement before starting the game.");
		txtpnAsASecurity.setBackground(SystemColor.control);
		txtpnAsASecurity.setBounds(244, 11, 208, 89);
		contentPane.add(txtpnAsASecurity);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 11));
		lblPassword.setBounds(244, 111, 66, 14);
		contentPane.add(lblPassword);
		
		passTextField = new JPasswordField();
		passTextField.setBounds(309, 108, 143, 20);
		contentPane.add(passTextField);
		passTextField.setColumns(10);
	}
}
