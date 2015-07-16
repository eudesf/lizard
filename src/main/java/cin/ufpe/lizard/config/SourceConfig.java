package cin.ufpe.lizard.config;

import java.io.Serializable;

public abstract class SourceConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private static int counter;
	
	private int id;
	private boolean global;

	public SourceConfig() {
		id = counter;
		counter++;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public abstract String getName();
	public abstract String getDescription();
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SourceConfig other = (SourceConfig) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
