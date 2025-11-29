package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

public class SuperBonus implements BonusEffectStrategy {

    private final GhostSlowEffect ghostSlowEffect;
    private final ScoreMultiplierEffect scoreMultiplierEffect;

    public SuperBonus(GhostSlowEffect ghostSlowEffect,
                      ScoreMultiplierEffect scoreMultiplierEffect) {
        this.ghostSlowEffect = ghostSlowEffect;
        this.scoreMultiplierEffect = scoreMultiplierEffect;
    }

    @Override
    public void apply(PacMan pacMan) {
        ghostSlowEffect.apply(pacMan);
        scoreMultiplierEffect.apply(pacMan);
    }
}
