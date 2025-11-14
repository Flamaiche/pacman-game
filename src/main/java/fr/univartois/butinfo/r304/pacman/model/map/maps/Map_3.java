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
        genererLabyrintheComplet(map, width, height);
        return map;
    }

    /**
     * Génère le labyrinthe sur toute la map.
     */
    private void genererLabyrintheComplet(GameMap map, int largeur, int hauteur) {
        SpriteStore store = new SpriteStore();
        Wall wall = new Wall(store.getSprite("wall"));
        Sprite path = store.getSprite("path");

        int midX = largeur / 2;
        int midY = hauteur / 2;

        // 1️⃣ Bordures
        for (int x = 0; x < largeur; x++) {
            map.setAt(0, x, new Cell(wall));
            map.setAt(hauteur - 1, x, new Cell(wall));
        }
        for (int y = 0; y < hauteur; y++) {
            map.setAt(y, 0, new Cell(wall));
            map.setAt(y, largeur - 1, new Cell(wall));
        }

        // 2️⃣ Motifs concentriques
        int offset = 2;
        int couche = 0;
        while (offset < Math.min(largeur, hauteur) / 2 - 2) {
            // haut
            for (int x = offset; x < largeur - offset; x++) {
                if (x != offset + 1 || couche % 2 == 0) map.setAt(offset, x, new Cell(wall));
            }
            // droite
            for (int y = offset; y < hauteur - offset; y++) {
                if (y != offset + 1 || couche % 2 == 1) map.setAt(y, largeur - offset - 1, new Cell(wall));
            }
            // bas
            for (int x = largeur - offset - 1; x >= offset; x--) {
                if (x != largeur - offset - 2 || couche % 2 == 0) map.setAt(hauteur - offset - 1, x, new Cell(wall));
            }
            // gauche
            for (int y = hauteur - offset - 1; y >= offset; y--) {
                if (y != offset + 1 || couche % 2 == 1) map.setAt(y, offset, new Cell(wall));
            }
            offset += 3;
            couche++;
        }

        // 3️⃣ Centre dégagé
        for (int y = midY - 2; y <= midY + 2; y++) {
            for (int x = midX - 3; x <= midX + 3; x++) {
                map.setAt(y, x, new Cell(path));
            }
        }

        // 4️⃣ Tunnels latéraux
        map.setAt(midY, 1, new Cell(path));
        map.setAt(midY, largeur - 2, new Cell(path));

        // 5️⃣ Connexions verticales centrales
        for (int y = midY - 5; y <= midY + 5; y++) {
            map.setAt(y, midX - 2, new Cell(path));
            map.setAt(y, midX + 2, new Cell(path));
        }

        // 6️⃣ Coins ouverts pour bonus
        ouvrirZone(map, 2, 2, 3, 3, path);
        ouvrirZone(map, 2, largeur - 5, 3, 3, path);
        ouvrirZone(map, hauteur - 5, 2, 3, 3, path);
        ouvrirZone(map, hauteur - 5, largeur - 5, 3, 3, path);
    }

    private void ouvrirZone(GameMap map, int startY, int startX, int h, int w, Sprite path) {
        for (int y = startY; y < startY + h; y++) {
            for (int x = startX; x < startX + w; x++) {
                map.setAt(y, x, new Cell(path));
            }
        }
    }
}
