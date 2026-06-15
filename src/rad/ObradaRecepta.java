
package rad;
import  baza.Database;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import recept.Kategorija;
import recept.Recept;

public class ObradaRecepta {
    
    public void izmeniRecept(Recept recept){
        String sql = "UPDATE Recept SET naziv = ? , vreme_pripreme = ? , opis = ? , kategorija_id = ? WHERE id = ?";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
            
            pr.setString(1, recept.getNaziv());
            pr.setInt(2, recept.getVremePripreme());
            pr.setString(3, recept.getOpis());
            pr.setInt(4, recept.getKategorija().getIdKategorije());
            pr.setInt(5, recept.getId());
            
            pr.executeUpdate();
        }catch(Exception e){
            System.out.println("Ne radi ti nesto kod izmene recepta: " + e.getMessage());
        }
    }
    
    //brisanje recepta
    public void brisanjeRecepta(int idBrisanog){
        String sql = "DELETE FROM Recept WHERE id = ?";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
            
            pr.setInt(1, idBrisanog);
            pr.executeUpdate();
            
        }catch(Exception e){
            System.out.println("Ne radi ti nesto: " + e.getMessage());
        }
        
    }
    
    //ubacivanje recepta, doraditi
    public void dodajRecept(Recept recept){
        String sql = "INSERT INTO Recept(naziv, vreme_pripreme, opis, slika, kategorija_id) VALUES(?,?,?,?,?)";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);){
            
            pr.setString(1, recept.getNaziv());
            pr.setInt(2, recept.getVremePripreme());
            pr.setString(3, recept.getOpis());
            pr.setString(4, recept.getSlika());
            pr.setInt(5, recept.getKategorija().getIdKategorije());
            
            pr.executeUpdate();
            
            ResultSet rs = pr.getGeneratedKeys();
            if(rs.next()){
                recept.setId(rs.getInt(1));
            }
        }catch(Exception e){
            System.out.println("Ne radi ti nesto: " + e.getMessage());
        }
    }
    
    public void azurirajSliku(Recept recept, String nazivSlike) {
        String sql = "UPDATE Recept SET slika = ? WHERE id = ?";

        try (Connection con = Database.connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nazivSlike);
            ps.setInt(2, recept.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Greska prilikom ubacivanja putanje slike " + e.getMessage());
        }
    }
    
    
    //vraca listu recepata
    public ArrayList<Recept> vratiSveRecepte(){
        ArrayList<Recept> recepti = new ArrayList<>();
        String sql = "SELECT Recept.id AS recept_id, Recept.naziv AS naziv_recepta, "
                + "Recept.vreme_pripreme, Recept.opis, Recept.slika, Recept.omiljeni, Recept.kategorija_id, "
                + "Kategorija.id AS kat_id, Kategorija.naziv AS naziv_kategorije "
                + "FROM Recept JOIN Kategorija ON Recept.kategorija_id = Kategorija.id";
        
        
        try(Connection cn = Database.connect();
            PreparedStatement pr = cn.prepareStatement(sql);
            ResultSet res = pr.executeQuery()){
            while(res.next()){
                Recept recept = new Recept();
                Kategorija kategorija = new Kategorija();
                
                recept.setId(res.getInt("recept_id"));
                recept.setNaziv(res.getString("naziv_recepta"));
                recept.setVremePripreme(res.getInt("vreme_pripreme"));
                recept.setOpis(res.getString("opis"));
                recept.setOmiljeni(res.getBoolean("omiljeni"));
                recept.setSlika(res.getString("slika"));
                
                kategorija.setIdKategorije(res.getInt("kat_id"));
                kategorija.setNazivKategorije(res.getString("naziv_kategorije"));
                recept.setKategorija(kategorija);
                
                /*
                int id = res.getInt("kategorija_id");
                kategorija.setIdKategorije(id);
                recept.setKategorija(kategorija);
                */
                recepti.add(recept);
            }
        }catch(Exception e){
            System.out.println("Ne radi ti nesto: " + e.getMessage());
        }
        
        return recepti;
    }
    
    //koristim za prikazivanje detalja recepta
    public Recept nadjiRecept(int id){
        Recept recept = new Recept();
        String sql = "SELECT Recept.id AS recept_id, Recept.naziv AS naziv_recepta, Recept.vreme_pripreme, Recept.opis, Recept.slika, Recept.omiljeni, "
                    + "Kategorija.naziv AS naziv_kategorije, Kategorija.id AS id_kategorije "
                    + "FROM Recept JOIN Kategorija "
                    + "ON Recept.kategorija_id = Kategorija.id "
                    + "WHERE Recept.id = ?";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
            
            pr.setInt(1, id);
            ResultSet res = pr.executeQuery();
            
            if(res.next()){
                recept.setId(res.getInt("recept_id"));
                recept.setNaziv(res.getString("naziv_recepta").substring(0,1).toUpperCase() + res.getString("naziv_recepta").substring(1));
                recept.setVremePripreme(res.getInt("vreme_pripreme"));
                recept.setOpis(res.getString("opis"));
                recept.setSlika(res.getString("slika"));
                recept.setOmiljeni(res.getBoolean("omiljeni"));
                
                Kategorija kategorija = new Kategorija();
                kategorija.setIdKategorije(res.getInt("id_kategorije"));
                kategorija.setNazivKategorije(res.getString("naziv_kategorije"));
                
                recept.setKategorija(kategorija);
            }
            
        }catch(Exception e){
            System.out.println("Greska prilikom uzimanja recepta iz baze: " + e.getMessage());
        }
        
        return recept;
    }
    
    
    //filtriranje sastojka
    //vraca recepte po odredjenom sastojku
    public ArrayList<Recept> vratiReceptePoSastojku(String sastojak){
        
        String sas = ObradaSastojka.obradaSastojka(sastojak);
        ArrayList<Recept> recepti = new ArrayList<>();
        
        String sql = "SELECT Recept.id AS id_recepta, Recept.naziv AS naziv_recepta, Recept.vreme_pripreme"
                + " FROM Sastojak JOIN ReceptSastojak ON Sastojak.id = ReceptSastojak.sastojak_id "
                + "JOIN Recept ON ReceptSastojak.recept_id = Recept.id "
                + "WHERE Sastojak.naziv = ?";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
            
            pr.setString(1, sas);
            
            ResultSet rs = pr.executeQuery();
            
            while(rs.next()){
                Recept recept = new Recept();
                  
                recept.setId(rs.getInt("id_recepta"));
                recept.setNaziv(rs.getString("naziv_recepta"));
                recept.setVremePripreme(rs.getInt("vreme_pripreme"));
                
                recepti.add(recept);
                
            }
            
            
            
        }catch(Exception e){
             System.out.println("Greska pri pretrazi recepta po sastojku: " + e.getMessage());
        }
        
        
        return recepti;
    }
    
    public ArrayList<Recept> pretragaRecepta(String rec){
        ArrayList<Recept> lista = new ArrayList<>();
        
        String sql = "SELECT id, naziv, vreme_pripreme FROM Recept WHERE naziv LIKE ?";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
            
            pr.setString(1, "%" + rec + "%");
            ResultSet rs = pr.executeQuery();
            while(rs.next()){
                Recept recept = new Recept();
                recept.setId(rs.getInt("id"));
                recept.setNaziv(rs.getString("naziv"));
                recept.setVremePripreme(rs.getInt("vreme_pripreme"));
                
                lista.add(recept);
            }
            
        }catch(Exception e){
            System.out.println("Greska prilikom trazenja recepta za pretragu");
        }
        
        return lista;
    }
    
    //vraca recepte po kategoriji
    public ArrayList<Recept> vratiReceptePoKategoriji(Kategorija ka){
        ArrayList<Recept> recepti = new ArrayList<>();
        
        String sql = "SELECT Recept.id, Recept.naziv, Recept.vreme_pripreme"
                + " FROM Recept JOIN Kategorija ON Recept.kategorija_id = Kategorija.id"
                + " WHERE Kategorija.id = ?  ";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
            
            pr.setInt(1, ka.getIdKategorije());
            
            ResultSet res = pr.executeQuery();
            
            while(res.next()){
                Recept recept = new Recept();
                recept.setId(res.getInt("id"));
                recept.setNaziv(res.getString("naziv"));
                recept.setVremePripreme(res.getInt("vreme_pripreme"));
                
                recepti.add(recept);
            }
        }catch(Exception e){
            System.out.println("Greska prilikom filtriranja po kategoriji: " + e.getMessage());
        }
        
        return recepti;
    }
    
    //vraca omiljene recepte
    public ArrayList<Recept> vratiOmiljeneRecepte(){
        ArrayList<Recept> recepti = new ArrayList<>();
        
        String sql = "SELECT Recept.id, Recept.naziv, Recept.vreme_pripreme "
                    + "FROM Recept "
                    + "WHERE Recept.omiljeni = 1";
        
        try(Connection con = Database.connect();
           PreparedStatement pr = con.prepareStatement(sql);
           ResultSet res = pr.executeQuery()){
           
           while(res.next()){
               Recept recept = new Recept();
               recept.setId(res.getInt("id"));
               recept.setNaziv(res.getString("naziv"));
               recept.setVremePripreme(res.getInt("vreme_pripreme"));
               
               recepti.add(recept);
           }
           
        }catch(Exception e){
            System.out.println("Greska prilikom vracanja omiljenih recepata: " + e.getMessage());
        }
        
        return recepti;
    }
    
    
    //regulisanje omiljenih
    //postavlja kao omiljeni
    public void dodajOmiljeni(Recept recept){
        String sql = "UPDATE Recept SET omiljeni = 1 WHERE id = ?";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
            
            pr.setInt(1, recept.getId());
            
            pr.executeUpdate(); 
        }catch(Exception e){
            System.out.println("Greska prilikom dodavanja omiljenog: " + e.getMessage());
        }
    }
    
    //uklanja iz omiljenih
    public void ukloniOmiljeni(Recept recept){
        String sql = "UPDATE Recept SET omiljeni = 0 WHERE id = ?";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
                        
            pr.setInt(1, recept.getId());
            
            pr.executeUpdate();
        }catch(Exception e){
            System.out.println("Greska prilikom uklanjanja omiljenog: " + e.getMessage());
        }
    }
    
    public static String obradaNaslova(String naziv){
        String rec = naziv.trim();
        
        if(rec.isEmpty()){
            return null;
        }else{
            rec.toLowerCase();
            String obradjen = rec.substring(0, 1).toUpperCase() + rec.substring(1);
            return obradjen;
        }
    }
}
