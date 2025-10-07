package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;

public class Carte_2 extends Carte {

    public Carte_2() {
        super();
    }

    @Override
    public GameMap createMap(int largeur, int hauteur) {
        largeur = 36;
        hauteur = 24;

        GameMap map = new GameMap(hauteur, largeur);

        SpriteStore spriteStore = new SpriteStore();
        Wall wall = new Wall(spriteStore.getSprite("wall"));
        Sprite path = spriteStore.getSprite("path");

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {

                // Bords fermés
                if (i == 0 || i == hauteur - 1 || j == 0 || j == largeur - 1) {
                    map.setAt(i, j, new Cell(wall));
                }
                // Murs horizontaux pour créer couloirs
                else if ((i == 4 || i == 8 || i == 12 || i == 16 || i == 20) && (j % 3 != 0)) {
                    map.setAt(i, j, new Cell(wall));
                }
                // Murs verticaux pour créer couloirs
                else if ((j == 5 || j == 11 || j == 17 || j == 23 || j == 29) && (i % 4 != 0)) {
                    map.setAt(i, j, new Cell(wall));
                }
                // Chemins libres partout ailleurs
                else {
                    map.setAt(i, j, new Cell(path));
                }
            }
        }

        return map;
    }
}
