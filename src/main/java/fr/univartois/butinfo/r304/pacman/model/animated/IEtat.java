package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

import java.util.Timer;
import java.util.TimerTask;

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

    String[] getCurrentSprites();

    void setSprite(Sprite sprite);

    default String getDirection() {
        if (getVerticalSpeed() != 0) {
            if (getVerticalSpeed() < 0) return "up/";
            else return "down/";
        } else if (getHorizontalSpeed() != 0) {
            if (getHorizontalSpeed() < 0) return "left/";
            else return "right/";
        }
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


    default void timerSetEtat(Etat etat, int delay) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                setEtat(etat);
            }
        }, delay);
    }

    default void setEtatTemp(Etat etat) {
        Etat ancienEtat = getEtat();
        if (ancienEtat == etat) return; // si etat == ancienEtat == INVULNERABLE

        timerSetEtat(ancienEtat, Etat.getDureeEtat());
        setEtat(etat);
    }
}
