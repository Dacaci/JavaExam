package model;

import java.time.LocalDateTime;

public class Reservation {
    private int id;
    private Utilisateur utilisateur;
    private BorneRecharge borne;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private StatutReservation statut;

    public Reservation(int id, Utilisateur utilisateur, BorneRecharge borne, LocalDateTime dateDebut, LocalDateTime dateFin, StatutReservation statut) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.borne = borne;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    public int getId() { return id; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public BorneRecharge getBorne() { return borne; }
    public LocalDateTime getDateDebut() { return dateDebut; }
    public LocalDateTime getDateFin() { return dateFin; }
    public StatutReservation getStatut() { return statut; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }
} 