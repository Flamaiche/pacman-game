package fr.univartois.butinfo.r304.pacman.model.map.maps;

import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.butinfo.r304.pacman.model.map.ICarte;
import fr.univartois.butinfo.r304.pacman.model.map.Wall;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

@StrategyDesignPattern(strategy = ICarte.class, participant = StrategyParticipant.IMPLEMENTATION)
public class Map_4 extends Map {

    public Map_4() {}

    @Override
    public GameMap createMap(int width, int height) {


        GameMap map = super.createMap(width, height);
        ajoutMursInterieurs(map, width, height);
        return map;
    }

    private void ajoutMursInterieurs(GameMap map, int largeur, int hauteur) {
        SpriteStore spriteStore = new SpriteStore();
        Wall wall = new Wall(spriteStore.getSprite("wall"));
        Sprite path = spriteStore.getSprite("path");

        double centreX = (largeur-1) / 2.0;
        double centreY = (hauteur-1) / 2.0;

        for (int x = 1; x < largeur-1; x++) {
            for (int y = 1; y < hauteur-1; y++) {
                if (x%5 != 0) {
                    if (y%5 < 4) {
                        map.setAt(y, x, new Cell(wall));
                    }
                }
                if (x > 4 && x < largeur-5 && y > 4 && y < hauteur-5) {
                    if (x%11 == 0 || y%11 == 0) {
                        map.setAt(y, x, new Cell(path));
                    }
                } else if (x == (int)centreX || y == (int)centreY) {
                    map.setAt(y, x, new Cell(path));
                }
                if (y == 1 || y == hauteur - 2 || x == largeur - 2 || x == 1) {
                        map.setAt(y, x, new Cell(path));
                }
            }
        }
    }
}
