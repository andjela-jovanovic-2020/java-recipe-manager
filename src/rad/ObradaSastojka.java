
package rad;

import baza.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import recept.Recept;
import recept.ReceptSastojak;
import recept.Sastojak;


public class ObradaSastojka {
    
    
    
    //ubacivanje sastojaka u bazu
    public void dodajSastojak(ReceptSastojak rs){
        String obradjen = obradaSastojka(rs.getSastojak().getSastojak());//uzima naziv sastojka i sredi ga za dalju proveru
        if(obradjen == null){
            return;
        }
        Boolean izvrsenje = postojiSastojak(obradjen);
        
        
        
        if(izvrsenje){//ako vec ima taj sastojak u bazi samo ga ubacujem u spojenu tabelu
            postaviIDsastojka(rs);//uzimam id iz baze i postavljam ga u objekat recept sastojak 
            ubacivanjeReceptSastojak(rs);//ubacivanje sastojaka i ostalog u tabelu
        }else{//ako nema sastojka u bazi prvo da ubaci u svoju tabelu pa tek onda u zajednicku
            dodajNoviSastojak(obradjen);
            postaviIDsastojka(rs);
            ubacivanjeReceptSastojak(rs);
        }
        
    }
    
    
    //dodatne metode za metodu dodavanje sastojaka
    
    //trazi id sastojka da se ubaci u recept sastojak
    public void postaviIDsastojka(ReceptSastojak rs){
        String sql = "SELECT id FROM Sastojak WHERE naziv = ?";
            
            try(Connection con = Database.connect();
                PreparedStatement pr = con.prepareStatement(sql)){
                
                pr.setString(1, obradaSastojka(rs.getSastojak().getSastojak()));
                
                ResultSet res = pr.executeQuery();
                
                if(res.next()){
                    rs.getSastojak().setIdSastojka(res.getInt("id"));
                }
            }catch(Exception e){
                System.out.println("Greska kod postavljanja id: " + e.getMessage());
            }
    }
    
    //sredjuje da su su prva slova velika ostalo malo
    public static String obradaSastojka(String sastojak){
        String rec = sastojak.trim();
        if(rec.isEmpty()){
            return null;
        }else{
            String rr = rec.toLowerCase();
            String obrada = rr.substring(0,1).toUpperCase() + rr.substring(1);
           
            return obrada;
        } 
    }
    
    //proverava da li vec postoji sastojak u tabeli
    public boolean postojiSastojak(String sastojak){
        String sas = "";
        if(obradaSastojka(sastojak) != null){
            sas = obradaSastojka(sastojak);
        }
        
        String sql = "SELECT * FROM Sastojak WHERE naziv = ?";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql);){
            
            pr.setString(1, sas);
            
            ResultSet res = pr.executeQuery();
            
            if(res.next()){
                return true;
            }
            
        }catch(Exception e){
            System.out.println("Greska za proveru sastojka: " + e.getMessage());
        }
        
        return false;
    }
    
    //ako nije do sada ubacen sastojak ubacujem ga
    public void dodajNoviSastojak(String sastojak){
        String sql = "INSERT INTO Sastojak(naziv) VALUES(?)";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql);){
            
            pr.setString(1, obradaSastojka(sastojak));
            
            pr.executeUpdate();
            
        }catch(Exception e){
            System.out.println("Greska kod dodavanja novog sastojka: " + e.getMessage());
        }
    }
    
    //popunjava tabelu Recept Sastojak
    public void ubacivanjeReceptSastojak(ReceptSastojak rs){
        
        if(postojiReceptSastojak(rs.getRecept().getId(), rs.getSastojak().getIdSastojka())){
            return;
        }
        
        String sql = "INSERT INTO ReceptSastojak(recept_id, sastojak_id, kolicina, jedinica) VALUES(?,?,?,?)";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql);){
            
            pr.setInt(1, rs.getRecept().getId());
            pr.setInt(2, rs.getSastojak().getIdSastojka());
            pr.setInt(3, rs.getKolicina());
            pr.setString(4, rs.getJedinica());
            
            pr.executeUpdate();
            
        }catch(Exception e){
            System.out.println("Greska kod ubacivanje recepta i sastojka u spojnu tabelu: " + e.getMessage());
        }
    }
    
    //proverava da li vec postoji kombinacija u spojenoj tabeli
    public boolean postojiReceptSastojak(int recept_id, int sastojak_id){
        
        String sql = "SELECT recept_id, sastojak_id FROM ReceptSastojak WHERE recept_id = ? AND sastojak_id = ? ";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
            
            pr.setInt(1, recept_id);
            pr.setInt(2, sastojak_id);
            
            ResultSet res = pr.executeQuery();
            
            if(res.next()){
                return true;
            }
            
        }catch(Exception e){
            System.out.println("Greska kod provere spojne tabele recepta i sastojka: " + e.getMessage());
        }
        
        return false;
    }
    
    //sledece uraditi
    //brise red tabele u ReceptSastojak
    public void obrisiSastojak(ReceptSastojak rs){
        String sql = "DELETE FROM ReceptSastojak WHERE recept_id = ? AND sastojak_id = ?";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
            
            pr.setInt(1, rs.getRecept().getId());
            pr.setInt(2, rs.getSastojak().getIdSastojka());
            
            pr.executeUpdate();
            
        }catch(Exception e){
            System.out.println("Greska pri brisanju sastojka iz recepta: " + e.getMessage());
        }
    }
    //brise sve
    public void obrisiSveSastojke(Recept recept){
        String sql = "DELETE FROM ReceptSastojak WHERE recept_id = ?";
        
        try(Connection con = Database.connect();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setInt(1, recept.getId());
            ps.executeUpdate();
            
        }catch(Exception e){
            System.out.println("Greska prilikom brisanja sastojaka  " + e.getMessage());
        }
    }
    
    //sledece uraditi
    public ArrayList<ReceptSastojak> vratiSastojke(Recept recept){
        
        ArrayList<ReceptSastojak> listaSastojaka = new ArrayList<>();
        
        String sql = "SELECT ReceptSastojak.kolicina, ReceptSastojak.jedinica, Sastojak.naziv, Sastojak.id"
                + " FROM ReceptSastojak JOIN Sastojak ON ReceptSastojak.sastojak_id = Sastojak.id WHERE ReceptSastojak.recept_id = ?";
        
        try(Connection con = Database.connect();
            PreparedStatement pr = con.prepareStatement(sql)){
            
            pr.setInt(1, recept.getId());
            ResultSet res = pr.executeQuery();
            while(res.next()){
                ReceptSastojak rs = new ReceptSastojak();
                Sastojak sastojak = new Sastojak();
                
                sastojak.setIdSastojka(res.getInt("id"));
                sastojak.setSastojak(res.getString("naziv"));
                
                rs.setRecept(recept);
                rs.setSastojak(sastojak);
                rs.setKolicina(res.getInt("kolicina"));
                rs.setJedinica(res.getString("jedinica"));
                
                listaSastojaka.add(rs);
            }
            
            
        }catch(Exception e){
            System.out.println("Greska prilikom trazenja sastojaka: " + e.getMessage());
        }
        
        return listaSastojaka;
    }

    
}
