package services;

import interfaces.AuthentificationService;
import model.Utilisateur;

import java.util.*;

public class AuthentificationServiceImpl implements AuthentificationService {
    private Map<String, Utilisateur> utilisateursParEmail = new HashMap<>();
    private int nextId = 1;

    public AuthentificationServiceImpl() {
       
    }

    @Override
    public Utilisateur inscrire(String email, String motDePasse) {
        if (utilisateursParEmail.containsKey(email)) return null;
        String codeValidation = String.valueOf(new Random().nextInt(100000, 999999));
        Utilisateur utilisateur = new Utilisateur(nextId++, email, motDePasse, codeValidation, false);
        utilisateursParEmail.put(email, utilisateur);
        return utilisateur;
    }

    @Override
    public boolean validerCompte(String email, String codeValidation) {
        Utilisateur utilisateur = utilisateursParEmail.get(email);
        if (utilisateur != null && utilisateur.getCodeValidation().equals(codeValidation)) {
            utilisateur.setEstValide(true);
            return true;
        }
        return false;
    }

    @Override
    public Utilisateur connecter(String email, String motDePasse) {
        Utilisateur utilisateur = utilisateursParEmail.get(email);
        if (utilisateur != null && utilisateur.getMotDePasse().equals(motDePasse) && utilisateur.isEstValide()) {
            return utilisateur;
        }
        return null;
    }

    @Override
    public void deconnecter(Utilisateur utilisateur) {
        
    }
} 