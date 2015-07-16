package cin.ufpe.lizard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

public class DatabaseDialog extends LizardDialog {

	private static final long serialVersionUID = 1L;

	private JTextField nameField = new JTextField();
	private JTextField hostField = new JTextField();
	private JSpinner portField = new JSpinner(new SpinnerNumberModel(3306, 0, 999999, 1));
	private JTextField userNameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JComboBox<String> databaseCombo = new JComboBox<String>();
	
	public DatabaseDialog() {
		super();
		setSize(300, 300);
		setLocationRelativeTo(null);
		setTitle("Adicionar banco de dados");
		
		JButton okButton = new JButton("Adicionar");
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setOk(true);
				dispose();
			}
		});

		JButton cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
		});
		
		Box buttonBox = new Box(BoxLayout.X_AXIS);
		buttonBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		buttonBox.setAlignmentX(Box.RIGHT_ALIGNMENT);
		buttonBox.add(okButton);
		buttonBox.add(cancelButton);
		
		databaseCombo.addItem("MySQL");
		databaseCombo.addItem("PostgreSQL");
		
		Box formBox = new Box(BoxLayout.Y_AXIS);
		formBox.setBorder(buttonBox.getBorder());
		
		formBox.add(newLeftLabel("Nome:"));
		formBox.add(nameField);
		formBox.add(newLeftLabel("Host:"));
		formBox.add(hostField);
		formBox.add(newLeftLabel("Porta:"));
		formBox.add(portField);
		formBox.add(newLeftLabel("Usuário:"));
		formBox.add(userNameField);
		formBox.add(newLeftLabel("Password:"));
		formBox.add(passwordField);
		formBox.add(newLeftLabel("Banco de Dados:"));
		formBox.add(databaseCombo);

		add(formBox, BorderLayout.CENTER);
		add(buttonBox, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new DatabaseDialog().setVisible(true);
			}

		});
	}

	public JTextField getNameField() {
		return nameField;
	}

	public JTextField getHostField() {
		return hostField;
	}

	public JSpinner getPortField() {
		return portField;
	}

	public JTextField getUserNameField() {
		return userNameField;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public JComboBox<String> getDatabaseCombo() {
		return databaseCombo;
	}

}
