package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

public class ScoreMultiplierEffect implements BonusEffectStrategy {

    private final int multiplier;

    public ScoreMultiplierEffect(int multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public void apply(PacMan pacMan) {
        if (pacMan.getGame() != null) {
            pacMan.getGame().setScoreMultiplier(multiplier);
        }
    }
}
