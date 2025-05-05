package services;

import interfaces.ReservationService;
import model.*;
import java.time.LocalDateTime;
import java.util.*;
import interfaces.BorneService;

public class ReservationServiceImpl implements ReservationService {
    private Map<Integer, Reservation> reservations = new HashMap<>();
    private int nextReservationId = 1;
    private BorneService borneService;

    public ReservationServiceImpl(BorneService borneService) {
        this.borneService = borneService;
    }

    @Override
    public List<BorneRecharge> chercherBornesDisponibles(LocalDateTime debut, LocalDateTime fin) {
        List<BorneRecharge> bornesDisponibles = new ArrayList<>();
        
    
        for (LieuRecharge lieu : borneService.getTousLesLieux()) {
            for (BorneRecharge borne : lieu.getBornes()) {
     
                if (borne.getEtat() == EtatBorne.DISPONIBLE) {
                    
                    boolean estDisponible = true;
                    for (Reservation reservation : reservations.values()) {
                        if (reservation.getBorne().getId() == borne.getId() && 
                            reservation.getStatut() == StatutReservation.ACCEPTEE &&
                            !(fin.isBefore(reservation.getDateDebut()) || debut.isAfter(reservation.getDateFin()))) {
                            estDisponible = false;
                            break;
                        }
                    }
                    if (estDisponible) {
                        bornesDisponibles.add(borne);
                    }
                }
            }
        }
        return bornesDisponibles;
    }

    @Override
    public Reservation creerReservation(Utilisateur utilisateur, BorneRecharge borne, LocalDateTime debut, LocalDateTime fin) {
        Reservation reservation = new Reservation(nextReservationId++, utilisateur, borne, debut, fin, StatutReservation.EN_ATTENTE);
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public boolean accepterReservation(int reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation != null && reservation.getStatut() == StatutReservation.EN_ATTENTE) {
            reservation.setStatut(StatutReservation.ACCEPTEE);
            return true;
        }
        return false;
    }

    @Override
    public boolean refuserReservation(int reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation != null && reservation.getStatut() == StatutReservation.EN_ATTENTE) {
            reservation.setStatut(StatutReservation.REFUSEE);
            return true;
        }
        return false;
    }

    @Override
    public List<Reservation> getReservationsUtilisateur(Utilisateur utilisateur) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations.values()) {
            if (r.getUtilisateur().equals(utilisateur)) {
                result.add(r);
            }
        }
        return result;
    }


    @Override
    public List<Reservation> getToutesReservations() {
        return new ArrayList<>(reservations.values());
    }

    @Override
    public Reservation getReservationById(int id) {
        return reservations.get(id);
    }
} 