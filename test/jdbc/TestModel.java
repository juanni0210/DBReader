package jdbc;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TestModel {
    private JDBCModel model;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/redditreader?serverTimezone=UTC&useUnicode=true";

    @BeforeEach
    public void setup() {
        model = new JDBCModel();
    }

    @AfterEach
    public void teardown() {
        model = null;
    }

    @Test
    public void normalTestConnectTo() {
        model.setCredential("cst8288", "8288");
        try {
            model.connectTo(DB_URL);
            assertTrue(model.isConnected());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void errorTestConnectTo() {
        //password is wrong
        model.setCredential("cst8288", "1");
        assertThrows(SQLException.class, () -> model.connectTo(DB_URL));
    }

    @Test
    public void testIsConnected() {
        try {
            assertFalse(model.isConnected());

            model.setCredential("cst8288", "8288");
            model.connectTo(DB_URL);
            assertTrue(model.isConnected());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClose() {
        try {
            model.setCredential("cst8288", "8288");
            model.close();
            assertFalse(model.isConnected());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAndInitializeTableNames() {
        try {
            model.setCredential("cst8288", "8288");
            model.connectTo(DB_URL);
            List<String> tableNames = model.getAndInitializeTableNames();
            //there are four tables in the redditreader database
            //account board host image
            assertEquals(4, tableNames.size());
            assertEquals("account", tableNames.get(0));
            assertEquals("board", tableNames.get(1));
            assertEquals("host", tableNames.get(2));
            assertEquals("image", tableNames.get(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAndInitializeColumnNames() {
        try {
            model.setCredential("cst8288", "8288");
            model.connectTo(DB_URL);
            List<String> columnNamesAccount = model.getAndInitializeColumnNames("account");
            //there are four columns in the account table
            //id  nickname  username  passoword
            assertEquals(4, columnNamesAccount.size());
            assertEquals("id", columnNamesAccount.get(0));
            assertEquals("nickname", columnNamesAccount.get(1));
            assertEquals("username", columnNamesAccount.get(2));
            assertEquals("password", columnNamesAccount.get(3));

            List<String> columnNamesBoard = model.getAndInitializeColumnNames("board");
            //there are four columns in the board table
            //id  Host_id  url  name
            assertEquals(4, columnNamesBoard.size());
            assertEquals("id", columnNamesBoard.get(0));
            assertEquals("Host_id", columnNamesBoard.get(1));
            assertEquals("url", columnNamesBoard.get(2));
            assertEquals("name", columnNamesBoard.get(3));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearch() {
        try {
            model.setCredential("cst8288", "8288");
            model.connectTo(DB_URL);
            List<List<Object>> result = model.search("account", "8288");
            List<Object> oneRowResult = result.get(0);
            //oneRowResult will be 
            //id  nickname  username  password
            //2   Shawn     cst8288   8288
            assertEquals(Long.valueOf(2), oneRowResult.get(0));
            assertEquals("Shawn", oneRowResult.get(1));
            assertEquals("cst8288", oneRowResult.get(2));
            assertEquals("8288", oneRowResult.get(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAll() {
        try {
            model.setCredential("cst8288", "8288");
            model.connectTo(DB_URL);
            List<List<Object>> result = model.getAll("account");
            //result will be 
            //id  nickname       username    password
            //1   administrator  admin       admin
            //2   Shawn          cst8288     8288
            List<Object> rowOneResult = result.get(0);
            assertEquals(Long.valueOf(1), rowOneResult.get(0));
            assertEquals("administrator", rowOneResult.get(1));
            assertEquals("admin", rowOneResult.get(2));
            assertEquals("admin", rowOneResult.get(3));

            List<Object> rowTwoResult = result.get(1);
            assertEquals(Long.valueOf(2), rowTwoResult.get(0));
            assertEquals("Shawn", rowTwoResult.get(1));
            assertEquals("cst8288", rowTwoResult.get(2));
            assertEquals("8288", rowTwoResult.get(3));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
