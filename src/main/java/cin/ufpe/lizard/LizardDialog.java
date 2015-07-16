package cin.ufpe.lizard;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LizardDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;

	private boolean ok;
	
	public LizardDialog() {
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	protected JComponent newLeftLabel(String text) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel(text));
		return panel;
	}

	public boolean isOk() {
		return ok;
	}

	protected void setOk(boolean ok) {
		this.ok = ok;
	}
	
	
	
}
