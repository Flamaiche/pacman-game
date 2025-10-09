package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.DeplacementAleatoire;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.DeplacementVersJoueur;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.IStrategieDeplacement;

public enum CouleurFantome {
    RED("red"),
    PINK("pink"),
    BLUE("blue"),
    ORANGE("orange");

    private final String folderName;

    CouleurFantome(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public IStrategieDeplacement getStrategie(Fantome fantome) {
        IStrategieDeplacement mouvement;
        switch (this) {
            case PINK :
                mouvement = new DeplacementVersJoueur(fantome);
            case BLUE :
                DeplacementVersJoueur dvj = new DeplacementVersJoueur(fantome);
                dvj.setAnticipation(3);
                mouvement =  dvj;
            default :
                mouvement = new DeplacementAleatoire(fantome);
        };
        return mouvement;
    }
}

