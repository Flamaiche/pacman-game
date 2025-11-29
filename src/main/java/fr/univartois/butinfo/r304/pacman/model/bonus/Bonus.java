package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.*;
import fr.univartois.butinfo.r304.pacman.view.Sprite;


public class Bonus extends AbstractAnimated implements IBonus {

    private final BonusEffectStrategy effect;
    private final long duration;
    private final PacmanGame game;

    public Bonus(PacmanGame game, int x, int y, Sprite sprite,
                 BonusEffectStrategy effect, long duration) {
        super(game, x, y, sprite);
        this.game = game;
        this.effect = effect;
        this.duration = duration;
    }

    @Override
    public void onCollisionWith(PacMan pacMan) {
        applyTo(pacMan);
        game.removeAnimated(this);
    }

    @Override
    public void onCollisionWith(Ghost ghost) {
        // Rien à faire : un fantôme qui marche sur le bonus ne le déclenche pas
    }

    @Override
    public void onCollisionWith(PacGum pacGum) {
        // Rien
    }

    @Override
    public void onCollisionWith(IAnimated other) {
        other.onCollisionWith(this);
    }

    @Override
    public void applyTo(PacMan pacMan) {
        effect.apply(pacMan);
    }



}
