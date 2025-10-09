package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.DeplacementAleatoire;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.DeplacementVersJoueur;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.DeplacementVersJoueurA3;
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
        return switch (this) {
            case PINK -> new DeplacementVersJoueur(fantome);
            case BLUE -> new DeplacementVersJoueurA3(fantome);
            default -> new DeplacementAleatoire(fantome);
        };
    }
}

