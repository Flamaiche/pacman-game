package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;
import fr.univartois.dpprocessor.designpatterns.state.StateDesignPattern;
import fr.univartois.dpprocessor.designpatterns.state.StateParticipant;
import javafx.beans.property.IntegerProperty;

import java.util.Timer;

public class PacMan extends AbstractAnimated implements IState {

    private static final String SPRITE_1 = "closed";
    private static final String SPRITE_2 = "half-open";
    private static final String SPRITE_3 = "open";

    private final IntegerProperty lifePoint;
    private final IntegerProperty score;
    private int spawnX = 0;
    private int spawnY = 0;
    private State state;
    private Timer stateTimer;

    private long animationTimer = 0;
    private int animationFrame = 0;
    private static final String[] SPRITES = {
            SPRITE_1,
            SPRITE_2,
            SPRITE_3,
            SPRITE_3,
            SPRITE_2
    };
    private static final String[] SPRITES_BOOST = {
            SPRITE_3,
            SPRITE_2,
    };

    public PacMan(PacmanGame game, int xPosition, int yPosition, Sprite sprite, IntegerProperty lifePoint, IntegerProperty score) {
        super(game, xPosition, yPosition, sprite);
        this.lifePoint = lifePoint;
        this.score = score;
        this.state = State.VULNERABLE;
    }

    public PacMan(PacmanGame game, int xPosition, int yPosition, IntegerProperty lifePoint, IntegerProperty score) {
        this(game, xPosition, yPosition, game.getSpriteStore().getSprite("pacman/closed"), lifePoint, score);
    }

    public PacmanGame getGame() {
        return game;
    }

    public IntegerProperty lifePointProperty() {
        return lifePoint;
    }

    public int getLifePoint() {
        return lifePoint.get();
    }

    public void setLifePoint(int lifePoint) {
        this.lifePoint.set(lifePoint);
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
        if (ghost.getState().isDie()) return;
        if (ghost.getState().estVulnerable()) {
            ghost.setState(State.DIE);
        } else if (this.state != State.INVULNERABLE) {
            game.respawnFantome();
            setLifePoint(getLifePoint() - 1 );
            if (lifePoint.get() <= 0) {
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
    public State getState() {
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
    public Timer getStateTimer() {
        return stateTimer;
    }

    @Override
    public void setStateTimer(Timer etatTimer) {
        this.stateTimer = etatTimer;
    }

    @Override
    public void setState(State state) {
        if (state == State.ALMOST_INVULNERABLE || state == State.DIE) {
            throw new IllegalArgumentException("Prohibited State for PacMan: " + state);
        }

        if (state == State.INVULNERABLE) setStateLater(State.VULNERABLE);

        this.state = state;
        applySpeed();
    }

    @Override
    public String[] getCurrentSprites() {
        if (state == State.INVULNERABLE) return SPRITES_BOOST;
        return SPRITES;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
