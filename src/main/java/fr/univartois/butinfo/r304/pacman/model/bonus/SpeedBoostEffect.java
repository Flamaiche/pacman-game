package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

public class SpeedBoostEffect implements BonusEffectStrategy {
    private final double multiplier;

    public SpeedBoostEffect(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public void apply(PacMan pacMan) {
        pacMan.setSpeed(pacMan.getSpeed() * multiplier);
    }
}
