package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.map.Wall;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

import java.util.Random;

public class Fantome extends AbstractAnimated {

    private CouleurFantome couleurFantome;
    private Random random = new Random();
    private IStrategieDeplacement strategieDeplacement;


    public Fantome(PacmanGame game, double xPosition, double yPosition, Sprite sprite, CouleurFantome couleurFantome) {
        super(game, xPosition, yPosition, sprite);
        this.couleurFantome = couleurFantome;
    }

    private CouleurFantome getCouleurFantome() {
        return couleurFantome;
    }

    private void setCouleurFantome(CouleurFantome couleurFantome) {
        this.couleurFantome = couleurFantome;
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
    public void onCollisionWith(Fantome fantome) {
        // ne fait rien
    }

    @Override
    public void onCollisionWith(PacGomme pacGomme) {
        // ne fait rien
    }

    public void setStrategieDeplacement(IStrategieDeplacement strategieDeplacement) {
        this.strategieDeplacement = strategieDeplacement;
    }

    public boolean onStep(long delta){
        if(strategieDeplacement!=null){
            strategieDeplacement.mouvement();
        }
        return super.onStep(delta);
    }

}
