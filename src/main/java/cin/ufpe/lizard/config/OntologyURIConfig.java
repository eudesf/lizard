package cin.ufpe.lizard.config;

public class OntologyURIConfig extends SourceConfig {

	private static final long serialVersionUID = 1L;
	
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

	
}
