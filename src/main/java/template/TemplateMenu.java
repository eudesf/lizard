package template;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLWorkspace;
import org.protege.editor.owl.ui.action.ProtegeOWLAction;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * This plugin doesn't really do much and is intended to be deleted when a developer creates his own content.
 * 
 * @author redmond
 *
 */
public class TemplateMenu extends ProtegeOWLAction {
	private static final long serialVersionUID = 749843192372192393L;
	private Logger logger = Logger.getLogger(TemplateMenu.class);

	@Override
	public void initialise() throws Exception {		
	}

	@Override
	public void dispose() throws Exception {		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		logger.info("Template menu plugin invoked");
		OWLWorkspace workspace = getOWLWorkspace();
		OWLEntity selectedEntity = workspace.getOWLSelectionModel().getSelectedEntity();
		String message;
		if (selectedEntity != null) {
			message = "Hello world! Selected entity = " + getOWLModelManager().getRendering(selectedEntity);
		}
		else {
			message = "Hello world";
		}
		JFrame parent = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, workspace);
		JOptionPane.showMessageDialog(parent, message);
	}


}
