package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.*;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

import java.util.Timer;
import java.util.TimerTask;

public class Bonus extends AbstractAnimated implements IBonus {

    private final BonusEffectStrategy effect;
    private final long duration;
    private final PacmanGame game;

    public Bonus(PacmanGame game, int x, int y, Sprite sprite, BonusEffectStrategy effect, long duration) {
        super(game, x, y, sprite);
        this.game = game;  // On stocke le jeu localement
        this.effect = effect;
        this.duration = duration;
    }

    // Puis remplacer getGame() par game
    public void onCollisionWith(PacMan pacMan) {
        applyTo(pacMan);
        game.removeAnimated(this); // plus besoin de getGame()
    }


    @Override
    public void onCollisionWith(Ghost ghost) {

    }

    @Override
    public void onCollisionWith(PacGum pacGum) {

    }

    @Override
    public void onCollisionWith(Fantome fantome) {
        // pas d'effet sur les fantômes
    }

    @Override
    public void onCollisionWith(PacGomme pacGomme) {
        // pas d'effet sur les pacgommes
    }

    @Override
    public void onCollisionWith(IAnimated other) {
        other.onCollisionWith(this);
    }

    @Override
    public void applyTo(PacMan pacMan) {
        effect.apply(pacMan);

        // retour à l'état normal après la durée
        new Timer().schedule(new TimerTask() {
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
