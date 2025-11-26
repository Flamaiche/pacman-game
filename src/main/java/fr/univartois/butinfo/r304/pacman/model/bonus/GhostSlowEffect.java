package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.Ghost;
import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

public class GhostSlowEffect implements BonusEffectStrategy {
    private final double slowMultiplier;

    public GhostSlowEffect(double slowMultiplier) {
        this.slowMultiplier = slowMultiplier;
    }

    @Override
    public void apply(PacMan pacMan) {
        // On parcourt tous les objets mobiles et on ne modifie que les Ghost
        for (var animated : pacMan.getGame().getMovingObjects()) {
            if (animated instanceof Ghost ghost) {
                ghost.setHorizontalSpeed(ghost.getHorizontalSpeed() * slowMultiplier);
                ghost.setVerticalSpeed(ghost.getVerticalSpeed() * slowMultiplier);
            }
        }
    }
}
