package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.dpprocessor.designpatterns.state.StateDesignPattern;
import fr.univartois.dpprocessor.designpatterns.state.StateParticipant;
import javafx.beans.property.IntegerProperty;

import java.util.Timer;

@StateDesignPattern(state = IEtat.class, participant = StateParticipant.IMPLEMENTATION)
public class PacMan extends AbstractAnimated implements IEtat {

    private final IntegerProperty pointsDeVie;
    private final IntegerProperty score;
    private int spawnX = 0;
    private int spawnY = 0;
    private State state;
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
        this.state = State.VULNERABLE;
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
    public void onCollisionWith(Ghost ghost) {
        if (ghost.getEtat().estMort()) return;
        if (ghost.getEtat().estVulnerable()) {
            ghost.setEtat(State.MORT);
        } else if (this.state != State.INVULNERABLE) {
            game.respawnFantome();
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
    public void onCollisionWith(PacGum pacGum) {
        setScore(getScore() + 1);
    }

    @Override
    public State getEtat() {
        return state;
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
    public void setEtat(State state) {
        if (state == State.PRESQUE_INVULNERABLE || state == State.MORT) {
            throw new IllegalArgumentException("Etat interdit pour le pacman: " + state);
        }

        if (state == State.INVULNERABLE) setEtatLater(State.VULNERABLE);

        this.state = state;
        applySpeed();
    }

    @Override
    public String[] getCurrentSprites() {
        if (state == State.INVULNERABLE) return SPRITES_BOOST;
        return SPRITES;
    }
}
