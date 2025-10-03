package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import javafx.beans.property.IntegerProperty;
import javafx.scene.paint.LinearGradient;

public class PacMan extends AbstractAnimated {

    private final IntegerProperty pointsDeVie;
    private final IntegerProperty score;


    public PacMan(PacmanGame game, int xPosition, int yPosition, Sprite sprite, IntegerProperty pointsDeVie, IntegerProperty score) {
        super(game, xPosition, yPosition, sprite);
        this.pointsDeVie = pointsDeVie;
        this.score = score;
    }

    public PacmanGame getGame() {
        return game;
    }

    public IntegerProperty pointsDeVieProperty() {
        return pointsDeVie;
    }

    public int getPointsDeVie() {
        return pointsDeVie.get();
    }

    public void setPointsDeVie(int pointsDeVie) {
        this.pointsDeVie.set(pointsDeVie);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }


    //Methodes pout toutes les collisisons
    @Override
    public void onCollisionWith(IAnimated other) {
        if (other instanceof PacGomme) {
            onCollisionWith((PacGomme) other);

        } else if (other instanceof Fantome) {
            onCollisionWith((Fantome) other);
        } else if (other instanceof PacMan) {
            onCollisionWith((PacMan) other);

        }

    }

    @Override
    public void onCollisionWith(PacMan pacMan) {

    }

    @Override
    public void onCollisionWith(Fantome fantome) {

    }

    @Override
    public void onCollisionWith(PacGomme pacGomme) {


    }
}
