

package baza;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    
    public static Connection connect(){
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:receptiBaza.db");
            //System.out.println("Uspesno povezano sa bazom.");
            return conn;
        } catch (Exception e) {
            System.out.println("Greska sa konekcijom : " + e.getMessage());
            return null;
        }
    }
}
