package jdbc.builder;

import java.util.HashMap;
import java.util.Map;

public abstract class JDBCURLBuilder {
    protected static final String JDBC = "jdbc";
    protected Map<String, String> properties;
    protected String dbType;
    protected int portNumber;
    protected String hostAddress;
    protected String catalogName;

    public JDBCURLBuilder() {
        properties = new HashMap<>();
    }

    public void setPort(String port) {
        if (port == null) {
            throw new NullPointerException();
        }
        int portNum = Integer.parseInt(port);
        setPort(portNum);
    }

    public void addURLProperty(String key, String value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        properties.put(key, value);
    }

    protected void setDB(String db) {
        if (db == null) {
            throw new NullPointerException();
        }
        this.dbType = db;

    }

    public abstract String getURL();

    public void setPort(int port) {
        if (port < 0) {
            throw new IllegalArgumentException("Port number cannot be negative!");
        }
        this.portNumber = port;

    }

    public void setAddress(String address) {
        if (address == null) {
            throw new NullPointerException();
        }
        this.hostAddress = address;
    }

    public void setCatalog(String catalog) {
        if (catalog == null) {
            throw new NullPointerException();
        }
        this.catalogName = catalog;
    }


}