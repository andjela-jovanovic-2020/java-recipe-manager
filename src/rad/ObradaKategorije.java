
package rad;

import baza.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import recept.Kategorija;


public class ObradaKategorije {
    
    //posle prepravim za prikaz
    
    public ArrayList<Kategorija>  prikaziKategorije(){
        ArrayList<Kategorija> lista = new ArrayList<>();
        String sql = "SELECT * FROM Kategorija";
        
        try{
             Connection conn = Database.connect();
             PreparedStatement pr = conn.prepareStatement(sql);
             ResultSet res = pr.executeQuery();
             
             while(res.next()){
                 Kategorija kat = new Kategorija();
                 kat.setIdKategorije(res.getInt("id"));
                 kat.setNazivKategorije(res.getString("naziv"));
                 lista.add(kat);
             }
             
        }catch(Exception e){
            System.out.println("Nesto ne valja");
        }
        return lista;
    }
}
