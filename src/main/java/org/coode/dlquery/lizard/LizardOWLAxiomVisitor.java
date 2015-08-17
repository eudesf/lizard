package org.coode.dlquery.lizard;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

public class LizardOWLAxiomVisitor extends OWLAxiomVisitorAdapter {
	
	private StringBuilder resultText = new StringBuilder();
	
	public String getResultText() {
		return resultText.toString();
	}
	
	@Override
	protected void handleDefault(OWLAxiom axiom) {
		resultText.append("Classes:\n");
		for (OWLClass owlClass : axiom.getClassesInSignature()) {
			resultText.append(" -   ");
			resultText.append(owlClass.getIRI());
			resultText.append("\n");
		}
		
		resultText.append("\nAxiomType:");
		resultText.append(axiom.getAxiomType().getName());
	}
}
