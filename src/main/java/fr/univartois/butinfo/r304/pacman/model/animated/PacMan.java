package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import javafx.beans.property.IntegerProperty;

import java.util.Timer;

public class PacMan extends AbstractAnimated implements IEtat {

    private final IntegerProperty pointsDeVie;
    private final IntegerProperty score;
    private int spawnX = 0;
    private int spawnY = 0;
    private Etat etat;
    private Timer etatTimer;

    private long animationTimer = 0;
    private int animationFrame = 0;
    private static final String[] SPRITES = {
            "closed",
            "half-open",
            "open",
            "open",
            "half-open"
    };
    private static final String[] SPRITES_BOOST = {
            "open",
            "half-open",
    };

    public PacMan(PacmanGame game, int xPosition, int yPosition, Sprite sprite, IntegerProperty pointsDeVie, IntegerProperty score) {
        super(game, xPosition, yPosition, sprite);
        this.pointsDeVie = pointsDeVie;
        this.score = score;
        this.etat = Etat.VULNERABLE;
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
    public String getFolderSprite() {
        return "pacman/";
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
        if (fantome.getEtat().estMort()) return;
        if (fantome.getEtat().estVulnerable()) {
            fantome.setEtat(Etat.MORT);
        } else if (this.etat != Etat.INVULNERABLE) {
            fantome.respawn();
            setPointsDeVie(getPointsDeVie() - 1 );
            if (pointsDeVie.get() <= 0) {
                game.playerIsDead();
            } else {
                setX(spawnX);
                setY(spawnY);
                game.stopMoving();
            }
        }
    }

    public void setSpawnPoint(int x, int y) {
        spawnX = x;
        spawnY = y;
    }

    @Override
    public void onCollisionWith(PacGomme pacGomme) {
        setScore(getScore() + 1);
    }

    @Override
    public Etat getEtat() {
        return etat;
    }

    @Override
    public int getAnimationFrame() {
        return animationFrame;
    }

    @Override
    public long getAnimationTimer() {
        return animationTimer;
    }

    @Override
    public void setAnimationTimer(long animationTimer) {
        this.animationTimer = animationTimer;
    }

    @Override
    public void setAnimationFrame(int animationFrame) {
        this.animationFrame = animationFrame;
    }

    @Override
    public Timer getEtatTimer() {
        return etatTimer;
    }

    @Override
    public void setEtatTimer(Timer etatTimer) {
        this.etatTimer = etatTimer;
    }

    @Override
    public void setEtat(Etat etat) {
        if (etat == Etat.PRESQUE_INVULNERABLE || etat == Etat.MORT) {
            throw new IllegalArgumentException("Etat interdit pour le pacman: " + etat);
        }

        if (etat == Etat.INVULNERABLE) setEtatLater(Etat.VULNERABLE);

        this.etat = etat;
    }

    @Override
    public String[] getCurrentSprites() {
        if (etat == Etat.INVULNERABLE) return SPRITES_BOOST;
        return SPRITES;
    }
}
