package diary;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBController {
    
    private static final DBController dbcontroller = new DBController();
    private static Connection connection;
    private static String DB_PATH;
    
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des JDBC-Treibers");
            e.printStackTrace();
        }
    }
    
    private DBController(){
    }
    
    public Connection GetConnection()
    {
    	return connection;
    }
    
    public static void SetDBPath(String newDBPath) 
    {
    	DB_PATH = newDBPath;
    }
    
    public static String GetDBPath()
    {
    	return DB_PATH;
    }
    
    public static DBController getInstance(String NewDBPath) throws FileNotFoundException, RuntimeException {
        SetDBPath(NewDBPath);
    	dbcontroller.initDBConnection();
    	return dbcontroller;
    }
    
    public static DBController getInstance() throws FileNotFoundException, RuntimeException {
        dbcontroller.initDBConnection();
    	return dbcontroller;
    }
    
    private void initDBConnection() throws FileNotFoundException, RuntimeException 
    {
    	File d_dbfile;
    	if (GetDBPath().length() <=0) {
        	throw new RuntimeException("database path empty!");
        	
        }
        d_dbfile = new File(GetDBPath());
    	if (!d_dbfile.exists()) {
    		throw new FileNotFoundException();
    	}
    	
    	try {
        	
            if (connection != null)
                return;
            System.out.println("Creating Connection to Database...");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            if (!connection.isClosed())
                System.out.println("...Connection established");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (!connection.isClosed() && connection != null) {
                        connection.close();
                        if (connection.isClosed())
                            System.out.println("Connection to Database closed");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
        });
    }
}

