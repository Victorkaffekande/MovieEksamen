package dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

    private static final String PROP_FILE = "src/database.settings";
    private SQLServerDataSource ds;

    /**
     * Logger ind på vores databse ved brug af informationerne omkring
     * vores login i et .settings fil
     * @throws IOException
     */

    public DatabaseConnector() throws IOException
    {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(PROP_FILE));
        ds = new SQLServerDataSource();
        ds.setServerName(databaseProperties.getProperty("Server"));
        ds.setDatabaseName(databaseProperties.getProperty("Database"));
        ds.setUser(databaseProperties.getProperty("User"));
        ds.setPassword(databaseProperties.getProperty("Password"));
    }


    /**
     * Danner en forbindelse med vores database
     * @return forbindelsen
     * @throws SQLServerException
     */

    public Connection getConnection() throws SQLServerException
    {
        return ds.getConnection();
    }

    /**
     * Tester til at se om den åbner
     * @param args
     * @throws IOException
     * @throws SQLException
     */

    public static void main(String[] args) throws IOException, SQLException {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        Connection connection = databaseConnector.getConnection();
        System.out.println("Is it open" + !connection.isClosed());
        connection.close();
    }
}
