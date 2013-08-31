package template;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.selection.OWLSelectionModel;
import org.protege.editor.owl.model.selection.OWLSelectionModelListener;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * 
 * This plugin doesn't really do much and is intended to be deleted when a developer creates his own content.
 * 
 * It really should be a AbstractOWLSelectionViewComponent but I forgot some step and it didn't work.
 * 
 * @author redmond
 *
 */
public class TemplateView extends AbstractOWLViewComponent {
	private static final long serialVersionUID = 1505057428784911280L;
	private Logger logger = Logger.getLogger(TemplateView.class);
	private JLabel label;
	private OWLSelectionModel selectionModel;
	private OWLSelectionModelListener listener = new OWLSelectionModelListener() {
		
		@Override
		public void selectionChanged() throws Exception {
			OWLEntity entity = getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
			updateView(entity);
		}
	};
	
	@Override
	protected void initialiseOWLView() throws Exception {
		logger.info("Initializing test view");
		label = new JLabel("Hello world");
		setLayout(new BorderLayout());
		add(label, BorderLayout.CENTER);
		selectionModel = getOWLWorkspace().getOWLSelectionModel();
		selectionModel.addListener(listener);
	}
	@Override
	protected void disposeOWLView() {
		selectionModel.removeListener(listener);
	}
	
	private void updateView(OWLEntity e) {
		if (e != null) {
			String entityName = getOWLModelManager().getRendering(e);
			label.setText("Hello World! Selected entity = " +  entityName);
		}
		else {
			label.setText("Hello World!");
		}
	}

}
