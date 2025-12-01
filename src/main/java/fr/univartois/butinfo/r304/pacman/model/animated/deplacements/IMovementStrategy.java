package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

@StrategyDesignPattern(strategy = IMovementStrategy.class, participant = StrategyParticipant.INTERFACE)
public interface IMovementStrategy {
        void movement();
        void reset();
}
