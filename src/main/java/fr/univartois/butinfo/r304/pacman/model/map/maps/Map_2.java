package fr.univartois.butinfo.r304.pacman.model.map.maps;

import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.butinfo.r304.pacman.model.map.IMap;
import fr.univartois.butinfo.r304.pacman.model.map.Wall;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

@StrategyDesignPattern(strategy = IMap.class, participant = StrategyParticipant.IMPLEMENTATION)
public class Map_2 extends Map {

    public Map_2() {}

    @Override
    public GameMap createMap(int width, int height) {
        GameMap map = super.createMap(width, height);
        addInternWall(map, width, height);
        return map;
    }

    private void addInternWall(GameMap map, int largeur, int hauteur) {
        SpriteStore spriteStore = new SpriteStore();
        Wall wall = new Wall(spriteStore.getSprite("wall"));
        Sprite path = spriteStore.getSprite("path");

        int offset = 2;
        int layout = 0;

        while (offset < Math.min(largeur, hauteur) / 2 - 2) {

            for (int x = offset; x < largeur - offset; x++) {
                if (x != offset + 1 || layout % 2 == 0)
                    map.setAt(offset, x, new Cell(wall));
            }

            for (int y = offset; y < hauteur - offset; y++) {
                if (y != offset + 1 || layout % 2 == 1) // ouverture alternée
                    map.setAt(y, largeur - offset - 1, new Cell(wall));
            }

            for (int x = largeur - offset - 1; x >= offset; x--) {
                if (x != largeur - offset - 2 || layout % 2 == 0)
                    map.setAt(hauteur - offset - 1, x, new Cell(wall));
            }

            for (int y = hauteur - offset - 1; y >= offset; y--) {
                if (y != hauteur - offset - 2 || layout % 2 == 1)
                    map.setAt(y, offset, new Cell(wall));
            }

            offset += 3;
            layout++;
        }

        int centreX = largeur / 2;
        int centreY = hauteur / 2;
        for (int y = centreY - 2; y <= centreY + 2; y++) {
            for (int x = centreX - 2; x <= centreX + 2; x++) {
                map.setAt(y, x, new Cell(path));
            }
        }
    }
}
