
package recept;


public class Sastojak {
    private int idSastojka;
    private String sastojak;
    
    public Sastojak(){
        
    }

    public int getIdSastojka() {
        return idSastojka;
    }

    public String getSastojak() {
        return sastojak;
    }

    public void setIdSastojka(int idSastojka) {
        this.idSastojka = idSastojka;
    }

    public void setSastojak(String sastojak) {
        this.sastojak = sastojak;
    }

    @Override
    public String toString() {
        return "Id sastojka: " + idSastojka + ", Naziv sastojka: " + sastojak;
    }
    
    
}
