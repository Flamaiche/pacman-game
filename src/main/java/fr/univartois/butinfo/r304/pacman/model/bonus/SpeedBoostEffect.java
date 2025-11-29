package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

public class SpeedBoostEffect implements BonusEffectStrategy {

    @Override
    public void apply(PacMan pacMan) {
        pacMan.setHorizontalSpeed(pacMan.getHorizontalSpeed() * 1.5);
        pacMan.setVerticalSpeed(pacMan.getVerticalSpeed() * 1.5);
    }
}
