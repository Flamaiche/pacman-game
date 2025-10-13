package fr.univartois.butinfo.r304.pacman.model.animated;

import java.util.Timer;
import java.util.TimerTask;

public interface Mortel {

    Etat getEtat();
    void setEtat(Etat etat);

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
        timerSetEtat(ancienEtat);

        setEtat(etat);
    }
}
