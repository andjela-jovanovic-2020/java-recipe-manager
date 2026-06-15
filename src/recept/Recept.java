
package recept;


public class Recept {
    private int id;
    private String naziv;
    private int vremePripreme;
    private String opis;
    private String slika;
    private Kategorija kategorija;
    private boolean omiljeni;
    
    public Recept(){
        
    }

    public Recept(String naziv, int vremePripreme, String opis, Kategorija kategorija) {
        this.naziv = naziv;
        this.vremePripreme = vremePripreme;
        this.opis = opis;
        this.kategorija = kategorija;
    }
    
    
    
    public Recept(String naziv, int vremePripreme, String opis, String slika, Kategorija kategorija){
        this.naziv = naziv;
        this.vremePripreme = vremePripreme;
        this.opis = opis;
        this.slika = slika;
        this.kategorija = kategorija;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getVremePripreme() {
        return vremePripreme;
    }

    public String getOpis() {
        return opis;
    }

    public String getSlika() {
        return slika;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setVremePripreme(int vremePripreme) {
        this.vremePripreme = vremePripreme;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    public boolean isOmiljeni() {
        return omiljeni;
    }

    public void setOmiljeni(boolean omiljeni) {
        this.omiljeni = omiljeni;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    @Override
    public String toString() {
        return "Recept{" + "naziv= " + naziv + ", vremePripreme= " + vremePripreme + ", opis= " + opis + ", slika= " + slika + ", kategorija= " + kategorija.getNazivKategorija()+ ", omiljeni= " + omiljeni + '}';
    }
    
    
    
    
}
