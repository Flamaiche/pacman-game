package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;
import fr.univartois.butinfo.r304.pacman.model.animated.Etat;

public class InvincibilityEffect implements BonusEffectStrategy {

    @Override
    public void apply(PacMan pacMan) {
        pacMan.setEtat(Etat.INVULNERABLE);
    }
}
