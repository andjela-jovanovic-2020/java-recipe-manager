/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rad;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author djoki
 */
public class ObradaSlike {
    
    public String uzmiEkstSlike(String putanja) {
        if (putanja == null) {
            return null;
        }

        int tac = putanja.lastIndexOf(".");
        if (tac >= 0) {
            return putanja.substring(tac);
        }

        return "";
    }
    
    public boolean proveraFajla(File fajl) {
        if (fajl == null || !fajl.exists()) {
            return false;
        }

        String ekst = uzmiEkstSlike(fajl.getName());
        if (ekst == null || ekst.equals("")) {
            return false;
        }

        if (!ekst.equals(".jpg") && !ekst.equals(".png") && !ekst.equals(".jpeg")) {
            return false;
        }

        return true;
    }
    
    public void kopirajSliku(File fajl, String naziv) {
        String nazivSlike="";
        try {
            //nazivSlike = String.valueOf(id) + uzmiEkstSlike(fajl.getName());
            nazivSlike = naziv;
            Path folder = Paths.get("slike");
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }

            Path odrediste = folder.resolve(nazivSlike);
            Files.copy(fajl.toPath(), odrediste, StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            System.out.println("Greska prilikom kopiranja slike u folder " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public String kreirajNazivSlike(File fajl, int id){
        String naziv = String.valueOf(id) + uzmiEkstSlike(fajl.getName());
        return naziv;
    }
    
    public void brisiSliku(String naziv){
        String nazivS = naziv;
        
        if(nazivS != null && !nazivS.isEmpty() && !nazivS.equals("zamena.jpg")){
            try{
                Path put = Paths.get("slike", nazivS);
                Files.deleteIfExists(put);
            }catch(Exception e){
                System.out.println("Greska prilikom brisanja slike iz foldera " + e.getMessage());
            }
        }
        
    }
}
