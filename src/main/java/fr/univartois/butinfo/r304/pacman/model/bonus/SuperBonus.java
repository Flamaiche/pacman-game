package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

/**
 * Bonus qui combine plusieurs effets.
 */
public class SuperBonus implements BonusEffectStrategy {
    private final BonusEffectStrategy[] effects;

    public SuperBonus(BonusEffectStrategy... effects) {
        this.effects = effects;
    }

    @Override
    public void apply(PacMan pacMan) {
        for (BonusEffectStrategy effect : effects) {
            effect.apply(pacMan);
        }
    }
}
