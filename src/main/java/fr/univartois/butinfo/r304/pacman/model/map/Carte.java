package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import javafx.beans.binding.IntegerExpression;

import java.util.ArrayList;

/*
le labyrinth sera genera à partir Carte (le labyrinth est une Carte)
 */
public class Carte {
    private int largeur;
    private int hauteur;
    private Cell[][] grid;

    public Carte(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        grid = new Cell[largeur][hauteur];
        initCell();
    }

    public void initCell() {
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                Cell newCell = new Cell(x, y);
                grid[x][y] = newCell;
            }
        }
    }

}