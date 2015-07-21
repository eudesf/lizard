package cin.ufpe.lizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;
import org.protege.editor.owl.ui.preferences.OWLPreferencesPanel;

import br.ufpe.cin.aac3.gryphon.Gryphon;
import br.ufpe.cin.aac3.gryphon.GryphonConfig;
import cin.ufpe.lizard.config.DatabaseConfig;
import cin.ufpe.lizard.config.OntologyURIConfig;
import cin.ufpe.lizard.config.SourceConfig;

public class LizardPreferencesPane extends OWLPreferencesPanel {

    private static final long serialVersionUID = 1L;
    
    public static final String PREFERENCES_SET_KEY = "cin.ufpe.lizard";
    public static final String PREFERENCES_KEY = "LizardPrefs";
    
	private JTabbedPane tabPane;
	private List<SourceConfig> sourceConfigList = new ArrayList<>();
	private OntologyConfigTableModel tableModel;
	private JTable table = new JTable();
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				LizardPreferencesPane pane = new LizardPreferencesPane();
				try {
					pane.initialise();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				JFrame mainFrame = new JFrame("Test");
				mainFrame.add(pane);
				mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				mainFrame.setSize(800, 500);
				mainFrame.setLocationRelativeTo(null);
				mainFrame.setVisible(true);
			}
		});
	}
	
	private void initGryphon() {
		GryphonConfig.setWorkingDirectory(new File("integrationExample"));
		GryphonConfig.setLogEnabled(true);
		GryphonConfig.setShowGryphonLogoOnConsole(true);
		Gryphon.init();
	}
	
	@SuppressWarnings("unchecked")
	private void loadPrefs() {
		try {
			byte[] buf = getPreferences().getByteArray(PREFERENCES_KEY, null);
			if (buf != null) {
				ByteArrayInputStream byteIn = new ByteArrayInputStream(buf);
				ObjectInputStream objectIn = new ObjectInputStream(byteIn);
				
				sourceConfigList = (List<SourceConfig>) objectIn.readObject();
			}
			tableModel = new OntologyConfigTableModel(sourceConfigList);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialise() throws Exception {
		initGryphon();
		loadPrefs();
		
		setLayout(new BorderLayout());	

		JButton addOntologyButton = new JButton("Adicionar ontologia...");
		addOntologyButton.setMaximumSize(new Dimension(300, 40));
		addOntologyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OntologyDialog dialog = new OntologyDialog();
				dialog.setVisible(true);
				if (dialog.isOk()) {
					OntologyURIConfig config = new OntologyURIConfig();
					config.setName(dialog.getNameField().getText());
					config.setURI(dialog.getUriField().getText());
					if (sourceConfigList.isEmpty()) {
						config.setGlobal(true);
					}
					sourceConfigList.add(config);
					tableModel.fireTableDataChanged();
					table.updateUI();
				}
			}
			
		});
		
		JButton addDatabaseButton = new JButton("Adicionar banco de dados...");
		addDatabaseButton.setMaximumSize(new Dimension(300, 40));
		addDatabaseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DatabaseDialog dialog = new DatabaseDialog();
				dialog.setVisible(true);
				if (dialog.isOk()) {
					DatabaseConfig config = new DatabaseConfig();
					config.setHost(dialog.getHostField().getText());
					config.setDatabaseName(String.valueOf(dialog.getDatabaseCombo().getSelectedItem()));
					config.setPassword(dialog.getPasswordField().getPassword());
					config.setPort((int) dialog.getPortField().getValue());
					config.setUserName(dialog.getUserNameField().getText());
					sourceConfigList.add(config);
					tableModel.fireTableDataChanged();
					table.updateUI();
				}
			}
		});

		JButton configureGlobalButton = new JButton("Configurar ontologia como global");
		configureGlobalButton.setMaximumSize(new Dimension(300, 40));
		configureGlobalButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(LizardPreferencesPane.this, "Selecione uma fonte para ser configurada como global.");
				} else {
					SourceConfig config = sourceConfigList.get(table.getSelectedRow());
					if (config instanceof DatabaseConfig) {
						JOptionPane.showMessageDialog(LizardPreferencesPane.this, 
								"Não é possível configurar uma fonte de banco de dados como global.");
					} else {
						for (SourceConfig aux : sourceConfigList) {
							aux.setGlobal(false);
						}
						config.setGlobal(true);
						tableModel.fireTableDataChanged();
						table.updateUI();
					}
				}
			}
		});
		
		JButton removeSourceButton = new JButton("Remover fonte");
		removeSourceButton.setMaximumSize(new Dimension(300, 40));
		removeSourceButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(LizardPreferencesPane.this, "Selecione uma fonte para ser removida.");
				} else {
					sourceConfigList.remove(table.getSelectedRow());
					tableModel.fireTableDataChanged();
					table.updateUI();
				}
			}
		});
		
		Box buttonsBox = new Box(BoxLayout.Y_AXIS);
		buttonsBox.setAlignmentX(Box.CENTER_ALIGNMENT);
		buttonsBox.add(addOntologyButton);
		buttonsBox.add(Box.createVerticalStrut(10));
		buttonsBox.add(addDatabaseButton);
		buttonsBox.add(Box.createVerticalStrut(10));
		buttonsBox.add(configureGlobalButton);
		buttonsBox.add(Box.createVerticalStrut(10));
		buttonsBox.add(removeSourceButton);
		buttonsBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(85);
		table.getColumnModel().getColumn(2).setPreferredWidth(5);
		
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(new JLabel("Fontes do Gryphon:"), BorderLayout.NORTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonsBox, BorderLayout.EAST);
		
		tabPane = new JTabbedPane();
		add(tabPane, BorderLayout.NORTH);
		
		tabPane.add("Bases de Conhecimento", mainPanel);
	}
	
	@Override
	public void dispose() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	private static Preferences getPreferences() {
		return PreferencesManager.getInstance().getPreferencesForSet(PREFERENCES_SET_KEY, PREFERENCES_KEY);
	}

	@Override
	public void applyChanges() {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
			
			objectOut.writeObject(sourceConfigList);
			getPreferences().putByteArray(PREFERENCES_KEY, byteOut.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
