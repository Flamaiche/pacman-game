package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Ghost;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

import java.util.Random;

@StrategyDesignPattern(strategy = IMovementStrategy.class, participant = StrategyParticipant.IMPLEMENTATION)
public class RandomMovement implements IMovementStrategy {

    private static final double SPEED = PacmanGame.DEFAULT_SPEED*0.85;

    private final Ghost ghost;
    private final Random random = new Random();
    private int movementCounter =0;
    private static final int DELAY = 30;


    public RandomMovement(Ghost ghost) {
            this.ghost = ghost;
        }

        @Override
        public void movement() {
            movementCounter++;
            if(movementCounter >= DELAY){
                chooseDirection();
                movementCounter = 0;
            }

        }

    private void chooseDirection() {

        if(random.nextBoolean()){
            ghost.setHorizontalSpeed(SPEED * (random.nextBoolean() ? 1 : -1));
            ghost.setVerticalSpeed(0);
        } else {
            ghost.setVerticalSpeed(SPEED * (random.nextBoolean() ? 1 : -1));
            ghost.setHorizontalSpeed(0);
    }
}

    public void reset() {
                movementCounter = 0;
    }

}
