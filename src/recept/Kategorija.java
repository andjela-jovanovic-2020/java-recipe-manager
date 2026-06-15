
package recept;


public class Kategorija {
    private int idKategorije;
    private String nazivKategorije;
    
    public Kategorija(){
        
    }
    
    
    public String getNazivKategorija(){
        return this.nazivKategorije;
    }
    
    public int getIdKategorije(){
        return this.idKategorije;
    }

    public void setIdKategorije(int idKategorije) {
        this.idKategorije = idKategorije;
    }

    public void setNazivKategorije(String nazivKategorije) {
        this.nazivKategorije = nazivKategorije;
    }
    
    

    @Override
    public String toString() {
        return nazivKategorije;
    }
    
    
}
