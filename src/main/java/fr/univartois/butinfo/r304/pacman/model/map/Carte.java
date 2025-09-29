package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
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
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {

            }
        }
        return map;
    }


}