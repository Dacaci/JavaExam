package interfaces;

import model.Reservation;
import model.BorneRecharge;
import model.Utilisateur;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<BorneRecharge> chercherBornesDisponibles(LocalDateTime debut, LocalDateTime fin);
    Reservation creerReservation(Utilisateur utilisateur, BorneRecharge borne, LocalDateTime debut, LocalDateTime fin);
    boolean accepterReservation(int reservationId);
    boolean refuserReservation(int reservationId);
    List<Reservation> getReservationsUtilisateur(Utilisateur utilisateur);
    List<Reservation> getToutesReservations();
    Reservation getReservationById(int id);
} 