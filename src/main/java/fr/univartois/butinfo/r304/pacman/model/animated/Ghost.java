package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.RandomMovement;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.FuyartMovement;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.IMovementStrategy;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.dpprocessor.designpatterns.state.StateDesignPattern;
import fr.univartois.dpprocessor.designpatterns.state.StateParticipant;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

import java.util.Timer;

@StateDesignPattern(state = IState.class, participant = StateParticipant.IMPLEMENTATION)
@StrategyDesignPattern(strategy = IMovementStrategy.class, participant = StrategyParticipant.CONTEXT)
public class Ghost extends AbstractAnimated implements IState {

    private GhostColor ghostColor;
    private IMovementStrategy currentMovement;
    private int spawnX = 0;
    private int spawnY = 0;

    private State state;
    private long animationTimer;
    private int animationFrame;
    private Timer etatTimer;
    private static final String[] SPRITES = {
            "1",
            "2"
    };
    private final static String[] SPRITES_PRESQUE_INVULNERABLE = {
            "../../default/afraid/1",
            "2"
    };

    private final IMovementStrategy defaultMovement;
    private final IMovementStrategy fuyartMovement = new FuyartMovement(this);
    private final IMovementStrategy randomMovement = new RandomMovement(this);

    public Ghost(PacmanGame game, double xPosition, double yPosition, Sprite sprite, GhostColor ghostColor) {
        super(game, xPosition, yPosition, sprite);
        this.ghostColor = ghostColor;
        this.currentMovement = ghostColor.getStrategy(this);
        this.state = State.INVULNERABLE;
        defaultMovement = currentMovement;
    }

    private GhostColor getGhostColor() {
        return ghostColor;
    }

    private void setGhostColor(GhostColor ghostColor) {
        this.ghostColor = ghostColor;
    }

    @Override
    public void onCollisionWith(IAnimated other) {
        // par défaut on laisse l'autre gérer
        other.onCollisionWith(this);
    }

    @Override
    public void onCollisionWith(PacMan pacMan) {
        // on renvoie l'appel à PacMan avec le type dynamique
        pacMan.onCollisionWith(this);
    }

    @Override
    public void onCollisionWith(Ghost ghost) {
        // ne fait rien
    }

    @Override
    public void onCollisionWith(PacGum pacGum) {
        // ne fait rien
    }

    private void setCurrentMovement(IMovementStrategy currentMovement) {
        this.currentMovement = currentMovement;
    }

    private void updateStrategieDeplacement() {
        IMovementStrategy movement = currentMovement;
        switch (state) {
            case MORT -> setCurrentMovement(randomMovement);
            case VULNERABLE -> setCurrentMovement(fuyartMovement);
            default -> setCurrentMovement(defaultMovement);
        }
        if (currentMovement != null && movement != currentMovement) currentMovement.reset();
    }

    @Override
    public boolean onStep(long delta){
        if(currentMovement !=null){
            currentMovement.movement();
        }
        return super.onStep(delta);
    }

    public void respawn() {
        setX(spawnX);
        setY(spawnY);
        currentMovement.reset();
    }

    public void setSpawnPoint(int x, int y) {
        spawnX = x;
        spawnY = y;
    }

    @Override
    public PacmanGame getGame(){
        return game;
    }

    @Override
    public String getFolderSprite() {
        return "ghosts/";
    }

    private String getDirection() {
        if (getVerticalSpeed() != 0) {
            if (getVerticalSpeed() < 0) return "up/";
            else return "down/";
        } else if (getHorizontalSpeed() != 0) {
            if (getHorizontalSpeed() < 0) return "left/";
            else return "right/";
        }
        return "right/"; // droite par défaut
    }

    @Override
    public String getCustomPath() {
        if (state == State.VULNERABLE) {
            return "default/afraid/";
        } else if (state == State.MORT) {
            return "default/hurt/";
        }
        return getDirection() + ghostColor.getFolderName() + "/";
    }

    public IMovementStrategy getCurrentMovement() {
        return currentMovement;
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
    public void setAnimationFrame(int animationFrame) {
        this.animationFrame = animationFrame;
    }

    @Override
    public void setAnimationTimer(long animationTimer) {
        this.animationTimer = animationTimer;
    }

    @Override
    public Timer getStateTimer() {
        return etatTimer;
    }

    @Override
    public void setStateTimer(Timer etatTimer) {
        this.etatTimer = etatTimer;
    }

    @Override
    public void setState(State state) {
        if (this.state == State.MORT && state != State.INVULNERABLE) return;

        switch (state) {
            case VULNERABLE -> setStateLater(State.PRESQUE_INVULNERABLE);
            case PRESQUE_INVULNERABLE -> setStateLater(State.INVULNERABLE, State.getDureePreventive());
            case MORT ->  setStateLater(State.INVULNERABLE, State.getDureeMort());
        }

        this.state = state;
        updateStrategieDeplacement();
    }

    @Override
    public String[] getCurrentSprites() {
        if (state == State.PRESQUE_INVULNERABLE) return SPRITES_PRESQUE_INVULNERABLE;
        else return SPRITES;
    }
}
