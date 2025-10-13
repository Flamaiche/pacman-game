package fr.univartois.butinfo.r304.pacman.model.map.carte;

import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.butinfo.r304.pacman.model.map.Wall;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;


public class CarteFacile extends Carte {

    public CarteFacile() {}

    @Override
    public GameMap createMap(int largeur, int hauteur) {


        GameMap map = new Carte().createMap(largeur, hauteur);
        ajoutMursInterieurs(map, largeur, hauteur);
        return map;
    }

    private void ajoutMursInterieurs(GameMap map, int largeur, int hauteur) {
        SpriteStore spriteStore = new SpriteStore();
        Wall wall = new Wall(spriteStore.getSprite("wall"));
        Sprite path = spriteStore.getSprite("path");

        double centreX = (largeur-1) / 2.0;
        double centreY = (hauteur-1) / 2.0;

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                // coins
                if ((y == 3 || y == hauteur-4) && ((x > 2 && x <= 5) || (x > largeur-7 && x <= largeur-4))) {
                    map.setAt(y, x, new Cell(wall));
                } else if ((x == 3 || x == largeur-4) && ((y > 2 && y <= 5) || (y > hauteur-7 && y <= hauteur-4))) {
                    map.setAt(y, x, new Cell(wall));
                // centre
                } else if (x == (int)centreX && (x < 3 || x > hauteur-3)) {
                    map.setAt(y, x, new Cell(wall));
                    if (x < centreX) {
                        map.setAt(y, x+1, new Cell(wall));
                    }
                } else if (y == (int)centreY && (x > 3 && x < largeur-4) && x%11 != 0) {
                    map.setAt(y, x, new Cell(wall));
                    if (y < centreY) {
                        map.setAt(y+1, x, new Cell(wall));
                    }
                } else if (x == (int)centreX && (y > 3 && y < hauteur-4)) {
                    map.setAt(y, x, new Cell(wall));
                    if (x < centreX) {
                        map.setAt(y, x + 1, new Cell(wall));
                    }
                }
                // autres
                else if ((y == 6 || y == hauteur-7) && (x > 5 && x < largeur-6) && x%3!=0) {
                    map.setAt(y, x, new Cell(wall));
                } else if ((x == 6 || x == largeur-7) && (y > 5 && y < hauteur-6) && y%3!=0) {
                    map.setAt(y, x, new Cell(wall));
                }
            }
        }

    }
}
