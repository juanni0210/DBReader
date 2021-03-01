package jdbc.builder;

public class JDBCURLBuilderFactory {
    public static JDBCURLBuilder create() {
        return new MySQLURLBuilder();
    }
}
