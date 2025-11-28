package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

@StrategyDesignPattern(strategy = ILevelFactory.class, participant = StrategyParticipant.INTERFACE)
public interface ILevelFactory {

    IMap chooseMap();
    String getLevelName();
    IMap[] getAllMap();
}
