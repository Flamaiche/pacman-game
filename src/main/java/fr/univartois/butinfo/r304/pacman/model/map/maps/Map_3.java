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
public class Map_3 extends Map {

    public Map_3() {}

    @Override
    public GameMap createMap(int width, int height) {
        GameMap map = super.createMap(width, height);
        generateCompleteLabyrinth(map, width, height);
        return map;
    }

    /**
     * Génère le labyrinthe sur toute la map.
     */
    private void generateCompleteLabyrinth(GameMap map, int width, int height) {
        SpriteStore store = new SpriteStore();
        Wall wall = new Wall(store.getSprite("wall"));
        Sprite path = store.getSprite("path");

        int midX = width / 2;
        int midY = height / 2;

        // Bordures
        for (int x = 0; x < width; x++) {
            map.setAt(0, x, new Cell(wall));
            map.setAt(height - 1, x, new Cell(wall));
        }
        for (int y = 0; y < height; y++) {
            map.setAt(y, 0, new Cell(wall));
            map.setAt(y, width - 1, new Cell(wall));
        }

        // Motifs concentriques
        int offset = 2;
        int layout = 0;
        while (offset < Math.min(width, height) / 2 - 2) {
            // haut
            for (int x = offset; x < width - offset; x++) {
                if (x != offset + 1 || layout % 2 == 0) map.setAt(offset, x, new Cell(wall));
            }
            // droite
            for (int y = offset; y < height - offset; y++) {
                if (y != offset + 1 || layout % 2 == 1) map.setAt(y, width - offset - 1, new Cell(wall));
            }
            // bas
            for (int x = width - offset - 1; x >= offset; x--) {
                if (x != width - offset - 2 || layout % 2 == 0) map.setAt(height - offset - 1, x, new Cell(wall));
            }
            // gauche
            for (int y = height - offset - 1; y >= offset; y--) {
                if (y != offset + 1 || layout % 2 == 1) map.setAt(y, offset, new Cell(wall));
            }
            offset += 3;
            layout++;
        }

        // Centre dégagé
        for (int y = midY - 2; y <= midY + 2; y++) {
            for (int x = midX - 3; x <= midX + 3; x++) {
                map.setAt(y, x, new Cell(path));
            }
        }

        // Tunnels latéraux
        map.setAt(midY, 1, new Cell(path));
        map.setAt(midY, width - 2, new Cell(path));

        // Connexions verticales centrales
        for (int y = midY - 5; y <= midY + 5; y++) {
            map.setAt(y, midX - 2, new Cell(path));
            map.setAt(y, midX + 2, new Cell(path));
        }

        // 6️⃣ Coins ouverts pour bonus
        openArea(map, 2, 2, 3, 3, path);
        openArea(map, 2, width - 5, 3, 3, path);
        openArea(map, height - 5, 2, 3, 3, path);
        openArea(map, height - 5, width - 5, 3, 3, path);
    }

    private void openArea(GameMap map, int startY, int startX, int h, int w, Sprite path) {
        for (int y = startY; y < startY + h; y++) {
            for (int x = startX; x < startX + w; x++) {
                map.setAt(y, x, new Cell(path));
            }
        }
    }
}
