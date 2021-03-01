package jdbc;

import java.sql.SQLException;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.builder.JDBCURLBuilder;

public class JDBCController implements AutoCloseable {
    private JDBCURLBuilder builder;
    private JDBCModel model;
    private StringProperty tableUse;
    private ObservableList<String> tableNameList;

    public JDBCController() {
        tableNameList = FXCollections.observableArrayList();
        model = new JDBCModel();
        tableUse = new SimpleStringProperty();
        tableUse.addListener((value, oldValue, newValue) -> {
            try {
                model.getAndInitializeColumnNames(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public StringProperty tableInUseProperty() {
        return tableUse;
    }

    public JDBCController setURLBuilder(JDBCURLBuilder builder) {
        this.builder = builder;
        return this;
    }

    public JDBCController setDataBase(String address, String port, String catalog) {
        builder.setAddress(address);
        builder.setPort(port);
        builder.setCatalog(catalog);
        return this;
    }

    public JDBCController addConnectionURLProperty(String key, String value) {
        builder.addURLProperty(key, value);
        return this;
    }

    public JDBCController setCredentials(String user, String pass) {
        model.setCredential(user, pass);
        return this;
    }

    //connect method will get the url from builder and pass it to connectTo of model
    public JDBCController connect() throws SQLException {
        model.connectTo(builder.getURL());
        return this;
    }

    public boolean isConnected() throws SQLException {
        return model.isConnected();
    }

    public List<String> getColumnNames() throws SQLException {
        return model.getColumnNames();
    }

    public ObservableList<String> getTableNames() throws SQLException {
        //if the model is connected, clear tableNameList
        //then call addAll methods on it and pass to it the result of model::getTableNames
        if (model.isConnected()) {
            tableNameList.clear();
            //change here
            tableNameList.addAll(model.getTableNames());
        }
        return tableNameList;
    }

    public List<List<Object>> getAll() throws SQLException {
        return model.getAll(tableUse.getValue());
    }

    public List<List<Object>> search(String searchTerm) throws SQLException {
        return model.search(tableUse.getValue(), searchTerm);
    }

    public void update(int index, String firstColValue, String col, String newValue) throws SQLException {
        model.update(tableUse.getValue(), index, firstColValue, col, newValue);
    }

    @Override
    public void close() throws Exception {
        model.close();
    }







}
