
package recept;


public class ReceptSastojak {
    private Recept recept;
    private Sastojak sastojak;
    private int kolicina;
    private String jedinica;

    public ReceptSastojak() {
    }

    public ReceptSastojak(Recept recept, Sastojak sastojak, int kolicina, String jedinica) {
        this.recept = recept;
        this.sastojak = sastojak;
        this.kolicina = kolicina;
        this.jedinica = jedinica;
    }

    public Recept getRecept() {
        return recept;
    }

    public Sastojak getSastojak() {
        return sastojak;
    }

    public int getKolicina() {
        return kolicina;
    }

    public String getJedinica() {
        return jedinica;
    }

    public void setRecept(Recept recept) {
        this.recept = recept;
    }

    public void setSastojak(Sastojak sastojak) {
        this.sastojak = sastojak;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public void setJedinica(String jedinica) {
        this.jedinica = jedinica;
    }

    @Override
    public String toString() {
        return this.sastojak.getSastojak() + " - " + this.kolicina + " " + this.jedinica;
    }

    
    
}
