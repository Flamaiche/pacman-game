package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

public class GhostSlowEffect implements BonusEffectStrategy {

    private final double factor;

    public GhostSlowEffect(double factor) {
        this.factor = factor;
    }

    @Override
    public void apply(PacMan pacMan) {
        if (pacMan.getGame() != null) {
            pacMan.getGame().slowDownGhosts(factor);
        }
    }
}
