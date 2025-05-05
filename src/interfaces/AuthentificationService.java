package interfaces;

import model.Utilisateur;

public interface AuthentificationService {
    Utilisateur inscrire(String email, String motDePasse);
    boolean validerCompte(String email, String codeValidation);
    Utilisateur connecter(String email, String motDePasse);
    void deconnecter(Utilisateur utilisateur);
} 