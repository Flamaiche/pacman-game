package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.dpprocessor.designpatterns.state.StateDesignPattern;
import fr.univartois.dpprocessor.designpatterns.state.StateParticipant;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Interface pour les objets ayant un Etat et étant animé.
 */
public interface IState {

    PacmanGame getGame();

    String getFolderSprite();

    default String getCustomPath() {
        return "";
    }

    State getState();
    void setState(State state);

    long getAnimationTimer();
    void setAnimationTimer(long animationTimer);

    int getAnimationFrame();
    void setAnimationFrame(int animationFrame);

    Timer getStateTimer();
    void setStateTimer(Timer timer);

    String[] getCurrentSprites();

    void setSprite(Sprite sprite);

    default String getPath() {
        return getFolderSprite() + getCustomPath();
    }

    default void setSprite(String[] currentSprites) {
        setAnimationFrame((getAnimationFrame() + 1) % currentSprites.length);
        setSprite(getGame().getSpriteStore().getSprite(getPath() + getCurrentSprites()[getAnimationFrame()]));
    }

    default void animate(long delta) {
        setAnimationTimer(getAnimationTimer() + delta);
        if (getAnimationTimer() >= 120) {
            setAnimationTimer(0);
            String[] currentSprites = getCurrentSprites();
            setSprite(currentSprites);
        }
    }

    default void cancelStateTimer() {
        if (getStateTimer() != null) {
            getStateTimer().cancel();
            setStateTimer(null);
        }
    }

    default void setStateLater(State state, int delay) {
        cancelStateTimer();

        Timer timer = new Timer();
        setStateTimer(timer);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setState(state);
            }
        }, delay);
    }
    default void setStateLater(State state) {
        setStateLater(state, State.getStateDuration());
    }
}
