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
@StateDesignPattern(state = IEtat.class, participant = StateParticipant.INTERFACE)
public interface IEtat {

    PacmanGame getGame();

    double getVerticalSpeed();
    double getHorizontalSpeed();

    String getFolderSprite();

    default String getCustomPath() {
        return "";
    }

    Etat getEtat();
    void setEtat(Etat etat);

    long getAnimationTimer();
    void setAnimationTimer(long animationTimer);

    int getAnimationFrame();
    void setAnimationFrame(int animationFrame);

    Timer getEtatTimer();
    void setEtatTimer(Timer timer);

    String[] getCurrentSprites();

    void setSprite(Sprite sprite);

    default String getDirection() {
//        if (getVerticalSpeed() != 0) {
//            if (getVerticalSpeed() < 0) return "up/";
//            else return "down/";
//        } else if (getHorizontalSpeed() != 0) {
//            if (getHorizontalSpeed() < 0) return "left/";
//            else return "right/";
//        }
        return "right/"; // droite par défaut
    }

    default String getPath() {
        return getFolderSprite() + getDirection() + getCustomPath();
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

    default void cancelEtatTimer() {
        if (getEtatTimer() != null) {
            getEtatTimer().cancel();
            setEtatTimer(null);
        }
    }

    default void setEtatLater(Etat etat, int delay) {
        cancelEtatTimer();

        Timer timer = new Timer();
        setEtatTimer(timer);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setEtat(etat);
            }
        }, delay);
    }
    default void setEtatLater(Etat etat) {
        setEtatLater(etat, Etat.getDureeEtat());
    }
    default IEtat nextState() {
        return this;
    }
}
