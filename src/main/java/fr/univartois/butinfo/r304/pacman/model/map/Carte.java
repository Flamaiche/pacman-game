package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;

/*
le labyrinth sera genera à partir Carte
Carte est un générateur de Cell pour GameMap
 */
public class Carte {

    public static GameMap createMap(int largeur, int hauteur) {
        /*
        largeur & hauteur en nombre de cellules
         */
        GameMap map = new GameMap(hauteur, largeur);

        SpriteStore spriteStore = new SpriteStore();
        Wall wall = new Wall(spriteStore.getSprite("wall"));
        Sprite path = spriteStore.getSprite("path");

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (i == 0 || i == hauteur - 1 || j == largeur - 1 || j == 0) {
                    map.setAt(i, j, new Cell(wall));
                } else {
                    map.setAt(i, j, new Cell(path));
                }
            }
        }
        return map;
    }
}