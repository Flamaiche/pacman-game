package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

public class ScoreMultiplierEffect implements BonusEffectStrategy {
    private final int factor;

    public ScoreMultiplierEffect(int factor) {
        this.factor = factor;
    }

    @Override
    public void apply(PacMan pacMan) {
        pacMan.setScore(pacMan.getScore() * factor);
    }
}
