package cin.ufpe.lizard.config;

import java.net.URI;
import java.net.URISyntaxException;

import br.ufpe.cin.aac3.gryphon.model.Ontology;

public class OntologyURIConfig extends SourceConfig {

	private static final long serialVersionUID = 6992482073474906282L;
	
	private boolean global;
	private String name;
	private String uri;
	
	public boolean isGlobal() {
		return global;
	}
	public void setGlobal(boolean global) {
		this.global = global;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getURI() {
		return uri;
	}
	public void setURI(String uri) {
		this.uri = uri;
	}
	
	@Override
	public String getDescription() {
		return getURI();
	}

	public Ontology getOntology() {
		try {
			return new Ontology(name, new URI(uri));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}
