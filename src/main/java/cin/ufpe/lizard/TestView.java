package cin.ufpe.lizard;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.selection.OWLSelectionModel;
import org.protege.editor.owl.model.selection.OWLSelectionModelListener;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLEntity;

public class TestView extends AbstractOWLViewComponent {

	private static final long serialVersionUID = 7426862941373706522L;
	private Logger logger = Logger.getLogger(TestView.class);
	private JLabel label;
	private OWLSelectionModel selectionModel;
	private OWLSelectionModelListener listener = new OWLSelectionModelListener() {
		
		@Override
		public void selectionChanged() throws Exception {
			updateView(getOWLWorkspace().getOWLSelectionModel().getSelectedEntity());
		}

	};
	
	private void updateView(OWLEntity selectedEntity) {
		if (selectedEntity != null) {
			String entityName = getOWLModelManager().getRendering(selectedEntity);
			label.setText("Hello World! Selected entity = " + entityName);
		} else {
			label.setText("Hello World!");
		}
	}
	
	@Override
	protected void initialiseOWLView() throws Exception {
		logger.info("Initializing test view");
		label = new JLabel("Hello World");
		setLayout(new BorderLayout());
		add(label, BorderLayout.CENTER);
		selectionModel = getOWLWorkspace().getOWLSelectionModel();
		selectionModel.addListener(listener);
	}

	@Override
	protected void disposeOWLView() {
		selectionModel.removeListener(listener);
	}

}
