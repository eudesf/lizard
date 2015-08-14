package org.coode.dlquery.lizard;

import static org.coode.dlquery.lizard.ResultsSection.DIRECT_SUB_CLASSES;
import static org.coode.dlquery.lizard.ResultsSection.DIRECT_SUPER_CLASSES;
import static org.coode.dlquery.lizard.ResultsSection.EQUIVALENT_CLASSES;
import static org.coode.dlquery.lizard.ResultsSection.INSTANCES;
import static org.coode.dlquery.lizard.ResultsSection.SUB_CLASSES;
import static org.coode.dlquery.lizard.ResultsSection.SUPER_CLASSES;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.aksw.owl2sparql.OWLClassExpressionToSPARQLConverter;
import org.apache.log4j.Logger;
import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.owl.model.cache.OWLExpressionUserCache;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.inference.ReasonerUtilities;
import org.protege.editor.owl.ui.clsdescriptioneditor.ExpressionEditor;
import org.protege.editor.owl.ui.clsdescriptioneditor.OWLExpressionChecker;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLException;

import br.ufpe.cin.aac3.gryphon.Gryphon;
import br.ufpe.cin.aac3.gryphon.Gryphon.ResultFormat;
import br.ufpe.cin.aac3.gryphon.GryphonConfig;
import br.ufpe.cin.aac3.gryphon.model.Ontology;
import cin.ufpe.lizard.LizardPreferencesPane;
import cin.ufpe.lizard.config.DatabaseConfig;
import cin.ufpe.lizard.config.OntologyURIConfig;
import cin.ufpe.lizard.config.SourceConfig;

import com.hp.hpl.jena.query.Query;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 22-Aug-2006<br><br>
 *
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLClassExpressionEditorViewComponent extends AbstractOWLViewComponent {
    private static final long serialVersionUID = 8268241587271333587L;

    private Logger log = Logger.getLogger(OWLClassExpressionEditorViewComponent.class);

    private ExpressionEditor<OWLClassExpression> owlDescriptionEditor;

    private ResultsList resultsList;

    private JCheckBox showDirectSuperClassesCheckBox;

    private JCheckBox showSuperClassesCheckBox;

    private JCheckBox showEquivalentClassesCheckBox;

    private JCheckBox showDirectSubClassesCheckBox;

    private JCheckBox showSubClassesCheckBox;

    private JCheckBox showIndividualsCheckBox;

    private JButton queryGryphonButton;
    
    private JButton convertToSparqlButton;

//    private JLabel statusLabel;
    
    private JTextArea resultTextArea;

    private OWLModelManagerListener listener;

    private boolean requiresRefresh = false;
    
    private List<Ontology> ontologies;
    
    protected void initialiseOWLView() throws Exception {
        setLayout(new BorderLayout(10, 10));

        JComponent editorPanel = createQueryPanel();
        JComponent resultsPanel = createTextResultsPanel();
        // JComponent optionsBox = createOptionsBox();
        // resultsPanel.add(optionsBox, BorderLayout.EAST);

        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, resultsPanel);
        splitter.setDividerLocation(0.3);

        add(splitter, BorderLayout.CENTER);

//        updateGUI();

        listener = new OWLModelManagerListener() {
            public void handleChange(OWLModelManagerChangeEvent event) {
                if (event.isType(EventType.ONTOLOGY_CLASSIFIED)) {
                    doQuery();
                }
            }
        };

        getOWLModelManager().addListener(listener);

        addHierarchyListener(new HierarchyListener(){
            public void hierarchyChanged(HierarchyEvent event) {
                if (requiresRefresh && isShowing()){
                    doQuery();
                }
            }
        });
    }

    private String readFile(File file) {
    	StringBuilder result = new StringBuilder();
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader(file));
	    	try {
		    	String line;
		    	while ((line = reader.readLine()) != null) {
		    		result.append(line);
		    		result.append(System.lineSeparator());
		    	}
	    	} finally {
				reader.close();
	    	}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
	    return result.toString();
    }
    
	private void initGryphon() {
		GryphonConfig.setWorkingDirectory(new File("integrationMScExperiment"));
		GryphonConfig.setLogEnabled(true);
		GryphonConfig.setShowLogo(true);
		Gryphon.init();
	}
	
	private void loadGryphonSources() {
		initGryphon();
		log.info("Loading Gryphon Sources...");
		ontologies = new ArrayList<>();
		List<SourceConfig> sourceConfigList = LizardPreferencesPane.loadSourceConfigList();
		for (SourceConfig sc : sourceConfigList) {
			if (sc instanceof OntologyURIConfig) {
				OntologyURIConfig config = (OntologyURIConfig) sc;
				
				if (config.isGlobal()) {
					Gryphon.setGlobalOntology(config.getOntology());
				} else {
					Gryphon.addLocalOntology(config.getOntology());
				}
				ontologies.add(config.getOntology());
			} else if (sc instanceof DatabaseConfig) {
				Gryphon.addLocalDatabase(((DatabaseConfig) sc).getDatabase());
			}
		}
	}
	
	private JComponent createTextResultsPanel() {
		JPanel editorPanel = new JPanel(new BorderLayout());
		resultTextArea = new JTextArea();
		editorPanel.add(resultTextArea);
		return editorPanel;
	}

	private JComponent createQueryPanel() {
        JPanel editorPanel = new JPanel(new BorderLayout());

        final OWLExpressionChecker<OWLClassExpression> checker = getOWLModelManager().getOWLExpressionCheckerFactory().getOWLClassExpressionChecker();
        owlDescriptionEditor = new ExpressionEditor<OWLClassExpression>(getOWLEditorKit(), checker);
        owlDescriptionEditor.addStatusChangedListener(new InputVerificationStatusChangedListener(){
            public void verifiedStatusChanged(boolean newState) {
                queryGryphonButton.setEnabled(newState);
                convertToSparqlButton.setEnabled(newState);
            }
        });
        owlDescriptionEditor.setPreferredSize(new Dimension(100, 50));

        editorPanel.add(ComponentFactory.createScrollPane(owlDescriptionEditor), BorderLayout.CENTER);
        JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        queryGryphonButton = new JButton(new AbstractAction("Query Gryphon") {
            private static final long serialVersionUID = -1833321282125901561L;

            public void actionPerformed(ActionEvent e) {
            	doQuery();
            }
        });
        convertToSparqlButton = new JButton(new AbstractAction("Convert to SPARQL") {
            private static final long serialVersionUID = -1833321282125901561L;

            public void actionPerformed(ActionEvent e) {
            	resultTextArea.setText(convertToSparql().toString());
            }

        });

        queryGryphonButton.setEnabled(false);
        convertToSparqlButton.setEnabled(false);
        buttonHolder.add(queryGryphonButton);
        buttonHolder.add(convertToSparqlButton);
//        buttonHolder.add(statusLabel);

        editorPanel.add(buttonHolder, BorderLayout.SOUTH);
        editorPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(
                Color.LIGHT_GRAY), "Query (class expression)"), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        
//		TODO disabled source alignment...
    	//
//    	log.info("Aligning Sources");
//    	statusLabel = new JLabel("Aligning Sources...");
//    	new SwingWorker<Void, Object>() {
//
//			@Override
//			protected Void doInBackground() throws Exception {
//				Gryphon.alignAndMap();
//				return null;
//			}
//
//			@Override
//			protected void done() {
//				statusLabel.setText("Sources aligned.");
//				executeButton.setEnabled(true);
//				log.info("Sources aligned.");
//			}
//    		
//    	}.execute();
    	
        return editorPanel;
    }


    private JComponent createResultsPanel() {
        JComponent resultsPanel = new JPanel(new BorderLayout(10, 10));
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(
                Color.LIGHT_GRAY), "Query results"), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        resultsList = new ResultsList(getOWLEditorKit());
//        resultsList.setResultsSectionVisible(SUB_CLASSES, showSubClassesCheckBox.isSelected());
        resultsPanel.add(ComponentFactory.createScrollPane(resultsList));
        return resultsPanel;
    }


    private JComponent createOptionsBox() {
        Box optionsBox = new Box(BoxLayout.Y_AXIS);
        showDirectSuperClassesCheckBox = new JCheckBox(new AbstractAction(DIRECT_SUPER_CLASSES.getDisplayName()) {
            /**
             * 
             */
            private static final long serialVersionUID = 1531417504526875891L;

            public void actionPerformed(ActionEvent e) {
                resultsList.setResultsSectionVisible(DIRECT_SUPER_CLASSES, showDirectSuperClassesCheckBox.isSelected());
                doQuery();
            }
        });
        optionsBox.add(showDirectSuperClassesCheckBox);
        optionsBox.add(Box.createVerticalStrut(3));

        showSuperClassesCheckBox = new JCheckBox(new AbstractAction(SUPER_CLASSES.getDisplayName()) {
            /**
             * 
             */
            private static final long serialVersionUID = 4603049796331219219L;

            public void actionPerformed(ActionEvent e) {
                resultsList.setResultsSectionVisible(SUPER_CLASSES, showSuperClassesCheckBox.isSelected());
                doQuery();
            }
        });
        showSuperClassesCheckBox.setSelected(false);
        optionsBox.add(showSuperClassesCheckBox);
        optionsBox.add(Box.createVerticalStrut(3));

        showEquivalentClassesCheckBox = new JCheckBox(new AbstractAction(EQUIVALENT_CLASSES.getDisplayName()) {
            /**
             * 
             */
            private static final long serialVersionUID = -3766966095409342054L;

            public void actionPerformed(ActionEvent e) {
                resultsList.setResultsSectionVisible(EQUIVALENT_CLASSES, showEquivalentClassesCheckBox.isSelected());
                doQuery();
            }
        });
        optionsBox.add(showEquivalentClassesCheckBox);
        optionsBox.add(Box.createVerticalStrut(3));

        showDirectSubClassesCheckBox = new JCheckBox(new AbstractAction(DIRECT_SUB_CLASSES.getDisplayName()) {
            private static final long serialVersionUID = 696913194074753412L;

            public void actionPerformed(ActionEvent e) {
                resultsList.setResultsSectionVisible(DIRECT_SUB_CLASSES, showDirectSubClassesCheckBox.isSelected());
                doQuery();
            }
        });
        optionsBox.add(showDirectSubClassesCheckBox);
        optionsBox.add(Box.createVerticalStrut(3));

        showSubClassesCheckBox = new JCheckBox(new AbstractAction(SUB_CLASSES.getDisplayName()) {
            private static final long serialVersionUID = -3418802363566640471L;

            public void actionPerformed(ActionEvent e) {
                resultsList.setResultsSectionVisible(SUB_CLASSES, showSubClassesCheckBox.isSelected());
                doQuery();
            }
        });
        showSubClassesCheckBox.setSelected(false);
        optionsBox.add(showSubClassesCheckBox);
        optionsBox.add(Box.createVerticalStrut(3));

        showIndividualsCheckBox = new JCheckBox(new AbstractAction(INSTANCES.getDisplayName()) {
            /**
             * 
             */
            private static final long serialVersionUID = -7727032635999833150L;

            public void actionPerformed(ActionEvent e) {
                resultsList.setResultsSectionVisible(INSTANCES, showIndividualsCheckBox.isSelected());
                doQuery();
            }
        });
        optionsBox.add(showIndividualsCheckBox);

        return optionsBox;
    }


    protected void disposeOWLView() {
        getOWLModelManager().removeListener(listener);
    }

    private Query convertToSparql() {
    	try {
    		OWLClassExpressionToSPARQLConverter converter = new OWLClassExpressionToSPARQLConverter();
			return converter.asQuery("?x", owlDescriptionEditor.createObject());
		} catch (OWLException e) {
			throw new RuntimeException(e);
		}
    }
    
    private void doQuery() {
        if (isShowing()){
        	loadGryphonSources();
        	String sparqlQuery = convertToSparql().toString();
    		log.info("**** QUERY ****\n" + sparqlQuery + "\n****");
    		Gryphon.query(sparqlQuery, ResultFormat.JSON);
    		
    		File resultFolder = Gryphon.getResultFolder();
    		if (resultFolder.exists()) {
    			if (resultFolder.listFiles().length > 0) {
    				resultTextArea.setText(readFile(resultFolder.listFiles()[0]));
    			}
    		}
            requiresRefresh = false;
        }
        else{
            requiresRefresh = true;
        }
    }


   
}
