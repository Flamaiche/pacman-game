package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.map.Wall;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

import java.util.Random;

public class Fantome extends AbstractAnimated {

    private CouleurFantome couleurFantome;
    public static final int DELAI = 50;
    public int compteurDeplacement=0;
    private Random random = new Random();

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
       //il ne se passe rien pour les autres objets animés

    }


    @Override
    public void onCollisionWith(PacMan pacMan) {
        //collision entre pacman et fantome gerer par le membre 2
    }

    @Override
    public void onCollisionWith(Fantome fantome) {
        //ne se passe rien pas d'interaction
    }

    @Override
    public void onCollisionWith(PacGomme pacGomme) {
        //Les fantomes traversent les pacGommes donc pas d'effet

    }

    public boolean onStep(long delta){
        compteurDeplacement++;
        if(compteurDeplacement>= DELAI){
            choixRandomDirection();
            compteurDeplacement=0;
        }
        return super.onStep(delta);
    }

    private void choixRandomDirection(){

        double vitesse = 50+random.nextDouble() *100;

        if(random.nextBoolean()){
            setHorizontalSpeed(vitesse*(random.nextBoolean()?1:-1));
            setVerticalSpeed(0);
        }else {
            setVerticalSpeed(vitesse*(random.nextBoolean()?1:-1));
            setHorizontalSpeed(0);
        }

    }

}
