package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Ghost;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

import java.util.Random;

@StrategyDesignPattern(strategy = IMovementStrategy.class, participant = StrategyParticipant.IMPLEMENTATION)
public class RandomMovement implements IMovementStrategy {
    private final Ghost ghost;
    private final Random random = new Random();
    private int compteurDeplacement =0;
    private static final int DELAI = 30;
    private final double vitesse = PacmanGame.DEFAULT_SPEED*0.85;


    public RandomMovement(Ghost ghost) {
            this.ghost = ghost;
        }

        @Override
        public void movement() {
            compteurDeplacement++;
            if(compteurDeplacement >= DELAI){
                choixDirection();
                compteurDeplacement = 0;
            }

        }

    private void choixDirection() {

        if(random.nextBoolean()){
            ghost.setHorizontalSpeed(vitesse * (random.nextBoolean() ? 1 : -1));
            ghost.setVerticalSpeed(0);
        } else {
            ghost.setVerticalSpeed(vitesse * (random.nextBoolean() ? 1 : -1));
            ghost.setHorizontalSpeed(0);
    }
}

    public void reset() {
                compteurDeplacement = 0;
    }

}
