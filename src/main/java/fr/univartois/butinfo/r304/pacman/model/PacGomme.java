package fr.univartois.butinfo.r304.pacman.model;

import fr.univartois.butinfo.r304.pacman.model.animated.AbstractAnimated;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

public class PacGomme extends AbstractAnimated {


    /**
     * Crée une nouvelle instance de AbstractAnimated.
     *
     * @param game      Le jeu dans lequel l'objet animé évolue.
     * @param xPosition La position en x initiale de l'objet animé.
     * @param yPosition La position en y initiale de l'objet animé.
     * @param sprite    L'instance de {@link Sprite} représentant l'objet animé.
     */
    protected PacGomme(PacmanGame game, double xPosition, double yPosition, Sprite sprite) {
        super(game, xPosition, yPosition, sprite);
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
