package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

@StrategyDesignPattern(strategy = IStrategieDeplacement.class, participant = StrategyParticipant.INTERFACE)
public interface IStrategieDeplacement {
        void mouvement();
        void reset();
}
