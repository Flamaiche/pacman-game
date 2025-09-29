package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;
import javafx.beans.binding.IntegerExpression;

import java.util.ArrayList;
import java.util.Random;

/*
le labyrinth sera genera à partir Carte
Carte est un générateur de Cell pour GameMap
 */
public class Carte {
    private static final Random rand = new Random();

    public static GameMap createMap(int largeur, int hauteur) {
        GameMap map = new GameMap(hauteur, largeur);

        SpriteStore spriteStore = new SpriteStore();
        Sprite wall = spriteStore.getSprite("wall");
        Sprite path = spriteStore.getSprite("path");

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (i == 0 && (j == 0 || j == largeur - 1) || i == hauteur - 1 && (j == 0 || j == largeur - 1)) {
                    map.setAt(i, j, new Cell(new Wall(wall)));
                } else map.setAt(i, j, new Cell(path));
            }
        }
        return map;
    }


}