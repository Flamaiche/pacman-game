package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

public interface Bonus {
    void appliquer(PacMan pacman, PacmanGame game);
}
