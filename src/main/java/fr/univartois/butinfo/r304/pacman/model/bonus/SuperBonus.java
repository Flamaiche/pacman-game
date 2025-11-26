package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

import java.util.List;

public class SuperBonus implements IBonus {

    private final List<BonusEffectStrategy> effects;
    private final long duration;

    public SuperBonus(List<BonusEffectStrategy> effects, long duration) {
        this.effects = effects;
        this.duration = duration;
    }

    @Override
    public void applyTo(PacMan pacMan) {
        effects.forEach(e -> e.apply(pacMan));
        // retour à l'état normal après duration
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                pacMan.setEtat(Etat.VULNERABLE);
            }
        }, duration);
    }

    @Override
    public long getDuration() {
        return duration;
    }
}
