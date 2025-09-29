package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

public class Fantome extends AbstractAnimated {

    private CouleurFantome couleurFantome;

    protected Fantome(PacmanGame game, double xPosition, double yPosition, Sprite sprite, CouleurFantome couleurFantome) {
        super(game, xPosition, yPosition, sprite);
        this.couleurFantome = couleurFantome;
    }

    private CouleurFantome getCouleurFantome() {
        return couleurFantome;
    }

    private void setCouleurFantome(CouleurFantome couleurFantome) {
        this.couleurFantome = couleurFantome;
    }

    @Override
    public void onCollisionWith(IAnimated other) {

    }


    @Override
    public void onCollisionWith(PacMan pacMan) {

    }

    @Override
    public void onCollisionWith(Fantome fantome) {

    }

    @Override
    public void onCollisoinWith(PacGomme pacGomme) {

    }
}
