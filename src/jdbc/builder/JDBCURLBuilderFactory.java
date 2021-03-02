package jdbc.builder;

/**
 * JDBCURLBuilderFactory class
 * Create a factory to get access to concrete instances of JDBCURLBuilder
 * Finished by Juan Ni on Feb 15, 2021
 * 
 * @author Juan Ni
 *
 */
public class JDBCURLBuilderFactory {
    public static JDBCURLBuilder create() {
        return new MySQLURLBuilder();
    }
}
