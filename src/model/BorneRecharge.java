package model;

public class BorneRecharge {
    private int id;
    private EtatBorne etat;
    private double tarifHoraire;
    private String type;
    private int puissance;

    public BorneRecharge(int id, EtatBorne etat, double tarifHoraire) {
        this.id = id;
        this.etat = etat;
        this.tarifHoraire = tarifHoraire;
    }

    public BorneRecharge(int id, String type, int puissance) {
        this.id = id;
        this.type = type;
        this.puissance = puissance;
        this.etat = EtatBorne.DISPONIBLE;
        this.tarifHoraire = 0.0;
    }

    public int getId() { return id; }
    public EtatBorne getEtat() { return etat; }
    public void setEtat(EtatBorne etat) { this.etat = etat; }
    public double getTarifHoraire() { return tarifHoraire; }
    public void setTarifHoraire(double tarifHoraire) { this.tarifHoraire = tarifHoraire; }
    public String getType() { return type; }
    public int getPuissance() { return puissance; }
} 