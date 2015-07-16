package cin.ufpe.lizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class OntologyDialog extends LizardDialog {

	private static final long serialVersionUID = 1L;

	private JTextField nameField = new JTextField();
	private JTextField uriField = new JTextField();
	
	public OntologyDialog() {
		super();
		setSize(300, 200);
		setLocationRelativeTo(null);
		setTitle("Adicionar ontologia");
		
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
		buttonBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBox.add(okButton);
		buttonBox.add(cancelButton);
		
		Box formBox = new Box(BoxLayout.Y_AXIS);
		formBox.setBorder(buttonBox.getBorder());
		formBox.add(newLeftLabel("Nome:"));
		formBox.add(nameField);
		formBox.add(newLeftLabel("URI:"));
		formBox.add(uriField);
		add(formBox, BorderLayout.CENTER);
		add(buttonBox, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				OntologyDialog dialog = new OntologyDialog();
				dialog.setVisible(true);
				if (dialog.isOk()) {
					JOptionPane.showMessageDialog(dialog, "Save here!");
				}
			}

		});
	}

	public JTextField getNameField() {
		return nameField;
	}

	public JTextField getUriField() {
		return uriField;
	}

}
