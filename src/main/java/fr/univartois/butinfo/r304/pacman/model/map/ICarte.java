package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.dpprocessor.designpatterns.decorator.DecoratorDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

@StrategyDesignPattern(strategy = ICarte.class, participant = StrategyParticipant.INTERFACE)
public interface ICarte {
    GameMap createMap(int largeur, int hauteur);
    default ICarte getInstance() {return this;}
}
