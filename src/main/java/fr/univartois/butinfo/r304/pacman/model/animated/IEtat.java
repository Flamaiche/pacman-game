package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

import java.util.Timer;
import java.util.TimerTask;

public interface IEtat {

    PacmanGame getGame();

    Etat getEtat();
    void setEtat(Etat etat);

    long getAnimationTimer();
    void setAnimationTimer(long animationTimer);

    int getAnimationFrame();
    void setAnimationFrame(int animationFrame);

    String[] getCurrentSprites();

    void setSprite(Sprite sprite);

    default void setSprite(String[] currentSprites) {
        setAnimationFrame((getAnimationFrame() + 1) % currentSprites.length);
        setSprite(getGame().getSpriteStore().getSprite(getCurrentSprites()[getAnimationFrame()]));
    }

    default void animate(long delta) {
        setAnimationTimer(getAnimationTimer() + delta);
        if (getAnimationTimer() >= 120) {
            setAnimationTimer(0);
            String[] currentSprites = getCurrentSprites();
            setSprite(currentSprites);
        }
    }


    default void timerSetEtat(Etat etat) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                setEtat(etat);
            }
        }, Etat.getDureeEtat());
    }

    default void setEtatTemp(Etat etat) {
        Etat ancienEtat = getEtat();
        if (ancienEtat == etat) return; // si etat == ancienEtat == INVULNERABLE
        timerSetEtat(ancienEtat);

        setEtat(etat);
    }
}
