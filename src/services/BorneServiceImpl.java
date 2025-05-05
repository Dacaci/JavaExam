package services;

import interfaces.BorneService;
import model.LieuRecharge;
import model.BorneRecharge;
import model.EtatBorne;
import java.util.ArrayList;
import java.util.List;

public class BorneServiceImpl implements BorneService {
    private List<LieuRecharge> lieux = new ArrayList<>();
    private int nextLieuId = 1;
    private int nextBorneId = 1;

    public BorneServiceImpl() {
        
        LieuRecharge centreVille = new LieuRecharge(nextLieuId++, "Centre Ville", "1 Place de la République");
        LieuRecharge gare = new LieuRecharge(nextLieuId++, "Gare SNCF", "2 Avenue de la Gare");
        
       
        ajouterBorne(centreVille, "AC", 22);
        ajouterBorne(centreVille, "DC", 50);
        ajouterBorne(gare, "AC", 22);
        ajouterBorne(gare, "DC", 50);
        
        lieux.add(centreVille);
        lieux.add(gare);
    }

    @Override
    public List<LieuRecharge> getTousLesLieux() {
        return new ArrayList<>(lieux);
    }

    @Override
    public void ajouterLieu(LieuRecharge lieu) {
        lieu.setId(nextLieuId++);
        lieux.add(lieu);
    }

    @Override
    public void ajouterBorne(LieuRecharge lieu, String type, int puissance) {
        BorneRecharge borne = new BorneRecharge(nextBorneId++, type, puissance);
        borne.setEtat(EtatBorne.DISPONIBLE);
        borne.setTarifHoraire(0.50);
        lieu.getBornes().add(borne);
    }
} 