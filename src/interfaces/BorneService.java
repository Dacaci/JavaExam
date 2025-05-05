package interfaces;

import model.LieuRecharge;
import java.util.List;

public interface BorneService {
    List<LieuRecharge> getTousLesLieux();
    void ajouterLieu(LieuRecharge lieu);
    void ajouterBorne(LieuRecharge lieu, String type, int puissance);
} 