package model;

public class Utilisateur {
    private int id;
    private String email;
    private String motDePasse;
    private String codeValidation;
    private boolean estValide;

    public Utilisateur(int id, String email, String motDePasse, String codeValidation, boolean estValide) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
        this.codeValidation = codeValidation;
        this.estValide = estValide;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getCodeValidation() { return codeValidation; }
    public boolean isEstValide() { return estValide; }
    public void setEstValide(boolean estValide) { this.estValide = estValide; }
} 