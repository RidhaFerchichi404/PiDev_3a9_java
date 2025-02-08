package Pi.PostEtComment;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connection {
    private static Connection connection;
    private java.sql.Connection cnx;

    public java.sql.Connection getCnx() {
        return cnx;
    }

    private Connection() {
        String url = "jdbc:mysql://localhost:3306/database";
        String user = "root";
        String password = "";
        try {
            cnx = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion établie !");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Connection getInstance(){
        if(connection == null){
            connection= new Connection();
        }
        return connection;

    }
}
