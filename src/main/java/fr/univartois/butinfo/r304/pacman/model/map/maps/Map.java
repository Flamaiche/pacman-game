package fr.univartois.butinfo.r304.pacman.model.map.maps;

import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.butinfo.r304.pacman.model.map.ICarte;
import fr.univartois.butinfo.r304.pacman.model.map.Wall;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

/*
le labyrinth sera genera à partir Carte
Carte est un générateur de Cell pour GameMap
 */
@StrategyDesignPattern(strategy = ICarte.class, participant = StrategyParticipant.IMPLEMENTATION)
public class Map implements ICarte {

    public Map() {}

    public GameMap createMap(int width, int height) {
        /*
        largeur & hauteur en nombre de cellules
         */
        GameMap map = new GameMap(height, width);

        SpriteStore spriteStore = new SpriteStore();
        Wall wall = new Wall(spriteStore.getSprite("wall"));
        Sprite path = spriteStore.getSprite("path");

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height - 1 || j == width - 1 || j == 0) {
                    map.setAt(i, j, new Cell(wall));
                } else {
                    map.setAt(i, j, new Cell(path));
                }
            }
        }
        return map;
    }
}