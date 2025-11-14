package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.*;

public enum GhostColor {
    RED("red"),
    PINK("pink"),
    BLUE("blue"),
    ORANGE("orange");

    private final String folderName;

    GhostColor(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public IMovementStrategy getStrategie(Ghost ghost) {
        IMovementStrategy mouvement;
        switch (this) {
            case PINK, ORANGE:
                HuntMovement dvj = new HuntMovement(ghost);
                dvj.setAnticipation(4);
                mouvement =  dvj;
                break;

            case RED, BLUE :
                mouvement = new DamMovement(ghost);
                break;

            default :
                mouvement = new RandomMovement(ghost);
        }
        return mouvement;
    }
}

