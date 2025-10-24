package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

public class PacGomme extends AbstractAnimated {

    private boolean isMegaGum = false;

    /**
     * Crée une nouvelle instance de AbstractAnimated.
     *
     * @param game      Le jeu dans lequel l'objet animé évolue.
     * @param xPosition La position en x initiale de l'objet animé.
     * @param yPosition La position en y initiale de l'objet animé.
     * @param sprite    L'instance de {@link Sprite} représentant l'objet animé.
     */
    public PacGomme(PacmanGame game, double xPosition, double yPosition, Sprite sprite) {
        super(game, xPosition, yPosition, sprite);
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
        if (isDestroyed()) return;
        this.onDestruction();
        game.pacGumEaten(this);
    }

    @Override
    public void onCollisionWith(Fantome fantome) {
        // ne fait rien
    }

    @Override
    public void onCollisionWith(PacGomme pacGomme) {
        // ne fait rien
    }

    public boolean isMegaGum() {
        return isMegaGum;
    }

    public void setMegaGum(boolean megaGum) {
        isMegaGum = megaGum;
    }
}
