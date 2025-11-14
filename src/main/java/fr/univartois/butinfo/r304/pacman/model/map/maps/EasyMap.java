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
public class EasyMap extends Map {

    public EasyMap() {}

    @Override
    public GameMap createMap(int width, int height) {


        GameMap map = super.createMap(width, height);
        addInternWall(map, width, height);
        return map;
    }

    private void addInternWall(GameMap map, int width, int height) {
        SpriteStore spriteStore = new SpriteStore();
        Wall wall = new Wall(spriteStore.getSprite("wall"));

        double centreX = (width-1) / 2.0;
        double centreY = (height-1) / 2.0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // coins
                if ((y == 3 || y == height-4) && ((x > 2 && x <= 5) || (x > width-7 && x <= width-4))) {
                    map.setAt(y, x, new Cell(wall));
                } else if ((x == 3 || x == width-4) && ((y > 2 && y <= 5) || (y > height-7 && y <= height-4))) {
                    map.setAt(y, x, new Cell(wall));
                // centre
                } else if (x == (int)centreX && (x < 3 || x > height-3)) {
                    map.setAt(y, x, new Cell(wall));
                    if (x < centreX) {
                        map.setAt(y, x+1, new Cell(wall));
                    }
                } else if (y == (int)centreY && (x > 3 && x < width-4) && x%11 != 0) {
                    map.setAt(y, x, new Cell(wall));
                    if (y < centreY) {
                        map.setAt(y+1, x, new Cell(wall));
                    }
                } else if (x == (int)centreX && (y > 3 && y < height-4)) {
                    map.setAt(y, x, new Cell(wall));
                    if (x < centreX) {
                        map.setAt(y, x + 1, new Cell(wall));
                    }
                }
                // autres
                else if ((y == 6 || y == height-7) && (x > 5 && x < width-6) && x%3!=0) {
                    map.setAt(y, x, new Cell(wall));
                } else if ((x == 6 || x == width-7) && (y > 5 && y < height-6) && y%3!=0) {
                    map.setAt(y, x, new Cell(wall));
                }
            }
        }

    }
}
