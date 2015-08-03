package cin.ufpe.lizard.config;

import br.ufpe.cin.aac3.gryphon.model.Database;



public class DatabaseConfig extends SourceConfig {
	
	private static final long serialVersionUID = -5949924208805480731L;
	
	private String host;
	private int port;
	private String userName;
	private char[] password;
	private String databaseName;
	private Database.DBMS dbms;
	
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public char[] getPassword() {
		return password;
	}
	public void setPassword(char[] password) {
		this.password = password;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public Database.DBMS getDbms() {
		return dbms;
	}
	public void setDbms(Database.DBMS dbms) {
		this.dbms = dbms;
	}
	
	@Override
	public String getName() {
		return getDatabaseName();
	}
	@Override
	public String getDescription() {
		return String.format("%s:%s@%s:%s/%s", getUserName(), new String(password), getHost(), getPort(), getDatabaseName());
	}
	@Override
	public void setGlobal(boolean global) {
		// Database source could not be global
	}
	
	public Database getDatabase() {
		return new Database(host, port, userName, new String(password), databaseName, dbms);
	}
}
