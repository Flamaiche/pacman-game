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

    @Override
    public void onCollisionWith(IAnimated other) {
        // toutes les collisions arrivent ici ( voir GameAnimation.checkCollision() )
        other.onCollisionWith(this);
    }

    @Override
    public void onCollisionWith(PacMan pacMan) {
        // Collision avec lui meme impossible
    }

    @Override
    public void onCollisionWith(Fantome fantome) {
        setPointsDeVie(getPointsDeVie() - 1 );
        if (pointsDeVie.get() <= 0) {
            game.playerIsDead();
        } else {
            setX(game.getHeight() / 2); // todo : utiliser spawn() de la classe Game
            setY(game.getHeight() / 2);
            setHorizontalSpeed(0);
            setVerticalSpeed(0);
        }
    }

    @Override
    public void onCollisionWith(PacGomme pacGomme) {
        setScore(getScore() + 1);
    }
}
