package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

@StrategyDesignPattern(strategy = IMap.class, participant = StrategyParticipant.INTERFACE)
public interface IMap {
    GameMap createMap(int width, int height);
    default IMap getInstance() {return this;}
}
