package ui;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import interfaces.AuthentificationService;
import interfaces.BorneService;
import interfaces.ReservationService;
import interfaces.DocumentService;
import services.AuthentificationServiceImpl;
import services.BorneServiceImpl;
import services.ReservationServiceImpl;
import services.DocumentServiceImpl;
import model.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static AuthentificationService authService = new AuthentificationServiceImpl();
    private static BorneService borneService = new BorneServiceImpl();
    private static ReservationService reservationService = new ReservationServiceImpl(borneService);
    private static DocumentService documentService = new DocumentServiceImpl();
    private static Utilisateur utilisateurConnecte = null;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        boolean continuer = true;
        
        while (continuer) {
            afficherMenu();
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    inscrire();
                    break;
                case 2:
                    validerInscription();
                    break;
                case 3:
                    connecter();
                    break;
                case 4:
                    if (utilisateurConnecte == null) {
                        System.out.println("Vous devez être connecté pour effectuer cette action.");
                    } else {
                        rechercherEtReserver();
                    }
                    break;
                case 5:
                    if (utilisateurConnecte == null) {
                        System.out.println("Vous devez être connecté pour effectuer cette action.");
                    } else {
                        gererReservations();
                    }
                    break;
                case 6:
                    gererReservationsOperateur();
                    break;
                case 0:
                    continuer = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
        scanner.close();
    }

    private static void afficherMenu() {
        System.out.println("\n=== Electricity Business ===");
        System.out.println("1. S'inscrire");
        System.out.println("2. Valider l'inscription");
        System.out.println("3. Se connecter");
        System.out.println("4. Rechercher & réserver une borne");
        System.out.println("5. Gérer mes réservations");
        System.out.println("6. Mode opérateur (gérer les réservations)");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    private static int lireChoix() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void inscrire() {
        System.out.println("\n=== INSCRIPTION ===");
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        Utilisateur utilisateur = authService.inscrire(email, motDePasse);
        if (utilisateur != null) {
            System.out.println("Inscription réussie ! Un code de validation a été généré.");
            System.out.println("Code de validation : " + utilisateur.getCodeValidation());
        } else {
            System.out.println("Erreur : cet email est déjà utilisé.");
        }
    }

    private static void validerInscription() {
        System.out.println("\n=== VALIDATION D'INSCRIPTION ===");
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Code de validation : ");
        String code = scanner.nextLine();

        if (authService.validerCompte(email, code)) {
            System.out.println("Compte validé avec succès !");
        } else {
            System.out.println("Erreur : code de validation invalide ou compte inexistant.");
        }
    }

    private static void connecter() {
        System.out.println("\n=== CONNEXION ===");
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        utilisateurConnecte = authService.connecter(email, motDePasse);
        if (utilisateurConnecte != null) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Erreur : identifiants invalides ou compte non validé.");
        }
    }

    private static void rechercherEtReserver() {
        if (utilisateurConnecte == null) {
            System.out.println("Vous devez être connecté pour effectuer cette action.");
            return;
        }

        System.out.println("\n=== RECHERCHE ET RÉSERVATION ===");
        System.out.println("Entrez la date et l'heure de début (format dd/MM/yyyy HH:mm) : ");
        LocalDateTime debut = lireDate();
        System.out.println("Entrez la date et l'heure de fin (format dd/MM/yyyy HH:mm) : ");
        LocalDateTime fin = lireDate();

        if (debut != null && fin != null) {
            System.out.println("\nBornes disponibles :");
            List<BorneRecharge> bornesDisponibles = reservationService.chercherBornesDisponibles(debut, fin);
            for (BorneRecharge borne : bornesDisponibles) {
                System.out.println("Borne " + borne.getId() + " - Tarif horaire : " + borne.getTarifHoraire() + "€");
            }

            System.out.print("\nEntrez l'ID de la borne à réserver (0 pour annuler) : ");
            try {
                int borneId = Integer.parseInt(scanner.nextLine());
                if (borneId > 0) {
                    BorneRecharge borneSelectionnee = bornesDisponibles.stream()
                        .filter(b -> b.getId() == borneId)
                        .findFirst()
                        .orElse(null);
                    
                    if (borneSelectionnee != null) {
                        Reservation reservation = reservationService.creerReservation(utilisateurConnecte, borneSelectionnee, debut, fin);
                        documentService.genererRecu(reservation);
                        System.out.println("Réservation créée avec succès ! Un reçu a été généré.");
                    } else {
                        System.out.println("Borne non trouvée.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("ID de borne invalide.");
            }
        }
    }

    private static void gererReservations() {
        if (utilisateurConnecte == null) {
            System.out.println("Vous devez être connecté pour effectuer cette action.");
            return;
        }

        System.out.println("\n=== GESTION DES RÉSERVATIONS ===");
        System.out.println("Vos réservations :");
        for (Reservation reservation : reservationService.getReservationsUtilisateur(utilisateurConnecte)) {
            System.out.println("ID : " + reservation.getId());
            System.out.println("Borne : " + reservation.getBorne().getId());
            System.out.println("Début : " + reservation.getDateDebut().format(formatter));
            System.out.println("Fin : " + reservation.getDateFin().format(formatter));
            System.out.println("Statut : " + reservation.getStatut());
            System.out.println("-------------------");
        }
    }

    private static void gererReservationsOperateur() {
        System.out.println("\n=== GESTION DES RÉSERVATIONS (MODE OPÉRATEUR) ===");
        List<Reservation> reservations = reservationService.getToutesReservations();
        
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation en attente.");
            return;
        }

        System.out.println("Réservations en attente :");
        for (Reservation reservation : reservations) {
            if (reservation.getStatut() == StatutReservation.EN_ATTENTE) {
                System.out.println("ID : " + reservation.getId());
                System.out.println("Utilisateur : " + reservation.getUtilisateur().getEmail());
                System.out.println("Borne : " + reservation.getBorne().getId());
                System.out.println("Début : " + reservation.getDateDebut().format(formatter));
                System.out.println("Fin : " + reservation.getDateFin().format(formatter));
                System.out.println("-------------------");
            }
        }

        System.out.print("\nEntrez l'ID de la réservation à traiter (0 pour annuler) : ");
        try {
            int reservationId = Integer.parseInt(scanner.nextLine());
            if (reservationId > 0) {
                System.out.println("1. Accepter");
                System.out.println("2. Refuser");
                System.out.print("Votre choix : ");
                int choix = lireChoix();
                
                if (choix == 1) {
                    if (reservationService.accepterReservation(reservationId)) {
                        System.out.println("Réservation acceptée !");
                        documentService.genererRecu(reservationService.getReservationById(reservationId));
                    } else {
                        System.out.println("Erreur : réservation non trouvée ou déjà traitée.");
                    }
                } else if (choix == 2) {
                    if (reservationService.refuserReservation(reservationId)) {
                        System.out.println("Réservation refusée !");
                    } else {
                        System.out.println("Erreur : réservation non trouvée ou déjà traitée.");
                    }
                } else {
                    System.out.println("Choix invalide.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ID de réservation invalide.");
        }
    }

    private static LocalDateTime lireDate() {
        try {
            String dateStr = scanner.nextLine();
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Format de date invalide. Utilisez le format dd/MM/yyyy HH:mm");
            return null;
        }
    }
} 