package jdbc.builder;

import java.util.Map;

public class MySQLURLBuilder extends JDBCURLBuilder {
    public MySQLURLBuilder() {
        setDB("mysql");
    }

    @Override
    public String getURL() {
        //assemble a connection URL like this:
        //jdbc:mysql://localhost:3306/redditreader?serverTimezone=UTC&useUnicode=true
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(JDBC);
        urlBuilder.append(":");
        urlBuilder.append(dbType);
        urlBuilder.append("://");
        urlBuilder.append(hostAddress); 
        urlBuilder.append(":");
        urlBuilder.append(portNumber);
        urlBuilder.append("/");
        urlBuilder.append(catalogName);

        if (!properties.isEmpty()) {
            urlBuilder.append("?");
            //loop through the property to get each pair of key and value
            //concatenate key and value with format "key=value"
            //and put "&" after each value to connect multiple properties
            for (Map.Entry<String, String> property : properties.entrySet()) {
                urlBuilder.append(property.getKey());
                urlBuilder.append("=");
                urlBuilder.append(property.getValue());
                urlBuilder.append("&");
            }
            //get rid of the last "&" after the value in the last property
            urlBuilder.setLength(urlBuilder.length() - 1);
        }

        return urlBuilder.toString();

    }

}
