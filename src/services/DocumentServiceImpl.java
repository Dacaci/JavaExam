package services;

import interfaces.DocumentService;
import model.Reservation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DocumentServiceImpl implements DocumentService {
    @Override
    public void genererRecu(Reservation reservation) {
        try {
            File dir = new File("exports");
            if (!dir.exists()) dir.mkdir();
            File recu = new File(dir, "recu_" + reservation.getId() + ".txt");
            try (FileWriter writer = new FileWriter(recu)) {
                writer.write("Reçu de réservation\n");
                writer.write("ID réservation : " + reservation.getId() + "\n");
                writer.write("Utilisateur : " + reservation.getUtilisateur().getEmail() + "\n");
                writer.write("Borne : " + reservation.getBorne().getId() + "\n");
                writer.write("Début : " + reservation.getDateDebut() + "\n");
                writer.write("Fin : " + reservation.getDateFin() + "\n");
                writer.write("Statut : " + reservation.getStatut() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la génération du reçu : " + e.getMessage());
        }
    }
} 