package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/**
 * Model class
 * Finished by Juan Ni on Feb 15, 2021
 * 
 * @author Juan Ni
 *
 */
public class JDBCModel {
    private List<String> columnNames;
    private List<String> tableNames;
    private Connection connection;
    private String user;
    private String pass;

    JDBCModel() {
        //Initialize the lists
        columnNames = new ArrayList<>();
        tableNames = new ArrayList<>();
    }

    //this method is a setter
    public void setCredential(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    //columnNames getter, not in the UML
    public List<String> getColumnNames() {
        return columnNames;
    }

    //tableNames getter, not in the UML
    public List<String> getTableNames() {
        return tableNames;
    }

    //this method  throw an exception if connection is null or closed
    private void checkConnectionIsValid() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Connection is invalid!");
        }

    }

    private void checkTableNameAndColumnAreValid(String table) throws SQLException {
        //1.1
        Objects.requireNonNull(table, "table name cannot be null");
        //1.2
        table = table.trim();

        if (tableNames.isEmpty()) {
            //1.4
            getAndInitializeTableNames();
        }

        if (columnNames.isEmpty()) {
            //1.5
            getAndInitializeColumnNames(table);
        }

        if (table.isEmpty() || !tableNames.contains(table)) {
            //1.6
            throw new IllegalArgumentException("table name=\"" + table + "\" is not valid");
        }

    }

    public void connectTo(String url) throws SQLException {
        // if the connection is open close it first and then use DriveManager to get a new
        // connection. Use the existing method.
        if (isConnected()) {
            close();
        }
        connection = DriverManager.getConnection(url, user, pass);

        //when connected, get and initialize table from database
        getAndInitializeTableNames();
    }

    public boolean isConnected() throws SQLException {
        //if connection is not null and connection is not closed, the model is connected
        return connection != null && !connection.isClosed();
    }

    public List<String> getAndInitializeColumnNames(String table) throws SQLException {
        //1.1
        checkConnectionIsValid();
        //1.2
        columnNames.clear();
        //1.3 && 1.4
        java.sql.DatabaseMetaData dbMeta = connection.getMetaData();

        try (ResultSet rs = dbMeta.getColumns(connection.getCatalog(), null, table, null)) {
            while (rs.next()) {
                //1.5
                columnNames.add(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //1.6 & 1.7
        List<String> list = Collections.unmodifiableList(columnNames);

        //1.8
        return list;
    }

    public List<String> getAndInitializeTableNames() throws SQLException {
        //1.1
        checkConnectionIsValid();
        //1.2
        tableNames.clear();
        //1.3 && 1.4
        java.sql.DatabaseMetaData dbMeta = connection.getMetaData();

        try (ResultSet rs = dbMeta.getTables(connection.getCatalog(), null, null, new String[] { "TABLE" })) {
            while (rs.next()) {
                //1.5
                tableNames.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //1.6 & 1.7
        List<String> list = Collections.unmodifiableList(tableNames);

        //1.8
        return list;
    }

    //this method is calling search method with not search term
    public List<List<Object>> getAll(String table) throws SQLException {
        return search(table, "");
    }

    public List<List<Object>> search(String table, String searchTerm) throws SQLException {

        checkConnectionIsValid();
        checkTableNameAndColumnAreValid(table);
        List<List<Object>> list = new LinkedList<>();
        String sql = buildSQLSearchQuery(table, true);
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (searchTerm != null) {
                searchTerm = String.format("%%%s%%", searchTerm);
                for (int i = 0; i < columnNames.size(); i++) {
                    ps.setObject(i + 1, searchTerm);
                }
            }
            extractRowsFromResultSet(ps, list);
        }
        return list;

    }

    private String buildSQLSearchQuery(String table, boolean withParameters) {
        StringBuilder sqlBuilder = new StringBuilder("select * from ");
        sqlBuilder.append(table);
        if (!withParameters) {
            return sqlBuilder.toString();
        }
        sqlBuilder.append(" where ");
        for (String column : columnNames) {
            sqlBuilder.append(column);
            sqlBuilder.append(" like ? or ");
        }
        sqlBuilder.setLength(sqlBuilder.length() - 3);

        return sqlBuilder.toString();
    }

    private void extractRowsFromResultSet(PreparedStatement ps, List<List<Object>> list) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                List<Object> row = new LinkedList<>();
                for (String label : columnNames) {
                    Object obj = rs.getObject(label);
                    row.add(obj);
                }
                list.add(row);
            }
        }

    }

    //this method will build an update query to update the database using input from user
    public void update(String table, String firstColValue, String col, String newValue) throws SQLException {
        StringBuilder updateQuery = new StringBuilder("update ");
        updateQuery.append(table);
        updateQuery.append(" set ");
        updateQuery.append(col);
        updateQuery.append("='");
        updateQuery.append(newValue);
        updateQuery.append("' where ");
        updateQuery.append(columnNames.get(0));
        updateQuery.append("=");
        updateQuery.append(firstColValue);
        try (PreparedStatement ps = connection.prepareStatement(updateQuery.toString())) {
            ps.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (isConnected()) {
            connection.close();
        }
    }

}
