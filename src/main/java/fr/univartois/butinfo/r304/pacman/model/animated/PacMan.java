package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import javafx.beans.property.IntegerProperty;
import javafx.scene.paint.LinearGradient;

public class PacMan extends AbstractAnimated {

    private final IntegerProperty pointsDeVie;
    private final IntegerProperty score;

    private long animationTimer = 0;
    private int animationFrame = 0;
    private static final String[] SPRITES = {
            "closed",
            "half-open",
            "open",
            "open",
            "half-open"
    };

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

    public void animate(long delta) {
        animationTimer += delta;
        if (animationTimer >= 120) {
            animationTimer=0;
            animationFrame = (animationFrame + 1) % SPRITES.length;
            setSprite(getGame().getSpriteStore().getSprite("pacman/"+getDirection()+"/"+SPRITES[animationFrame]));
        }
    }

    private String getDirection() {
        if (getVerticalSpeed() != 0) {
            if (getVerticalSpeed() < 0) return "up";
            else return "down";
        } else if (getHorizontalSpeed() != 0) {
            if (getHorizontalSpeed() < 0) return "left";
            else return "right";
        }
        return "right"; // droite par défaut
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
            game.stopMoving();
        }
    }

    @Override
    public void onCollisionWith(PacGomme pacGomme) {
        setScore(getScore() + 1);
    }
}
