package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.*;

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
            case PINK, ORANGE:
                DeplacementChasseur dvj = new DeplacementChasseur(fantome);
                dvj.setAnticipation(4);
                mouvement =  dvj;
                break;

            case RED, BLUE :
                mouvement = new DeplacementBarrage(fantome);
                break;

            default :
                mouvement = new DeplacementAleatoire(fantome);
        }
        return mouvement;
    }
}

