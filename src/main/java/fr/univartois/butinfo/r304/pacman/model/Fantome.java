package fr.univartois.butinfo.r304.pacman.model;

import fr.univartois.butinfo.r304.pacman.model.animated.AbstractAnimated;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

public class Fantome extends AbstractAnimated {

    protected Fantome(PacmanGame game, double xPosition, double yPosition, Sprite sprite) {
        super(game, xPosition, yPosition, sprite);
    }

    @Override
    public void onCollisionWith(IAnimated other) {

    }


}
