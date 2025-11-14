package fr.univartois.butinfo.r304.pacman.model.map.maps;

import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.butinfo.r304.pacman.model.map.ICarte;
import fr.univartois.butinfo.r304.pacman.model.map.Wall;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

import java.util.*;

@StrategyDesignPattern(strategy = ICarte.class, participant = StrategyParticipant.IMPLEMENTATION)
public class Map_5 extends Map {
    private final Random random = new Random();

    public Map_5() {}

    @Override
    public GameMap createMap(int width, int height) {
        GameMap map = super.createMap(width, height);
        ajoutMursInterieurs(map, width, height);
        return map;
    }

    private void ajoutMursInterieurs(GameMap map, int largeur, int hauteur) {
        if (largeur % 2 == 0) largeur--;
        if (hauteur % 2 == 0) hauteur--;

        SpriteStore store = new SpriteStore();
        Wall wall = new Wall(store.getSprite("wall"));
        Sprite path = store.getSprite("path");

        // Remplissage initial de la carte avec des murs sauf la dernière ligne
        for (int y = 0; y < hauteur - 1; y++) {
            for (int x = 0; x < largeur; x++) {
                map.setAt(y, x, new Cell(wall));
            }
        }

        boolean[][] visite = new boolean[hauteur][largeur];
        Stack<int[]> pile = new Stack<>();
        pile.push(new int[]{1, 1});
        map.setAt(1, 1, new Cell(path));
        visite[1][1] = true;

        // Algorithme DFS pour creuser le labyrinthe
        while (!pile.isEmpty()) {
            int[] current = pile.peek();
            int x = current[0];
            int y = current[1];

            List<int[]> voisins = new ArrayList<>();

            if (y > 2 && !visite[y - 2][x]) voisins.add(new int[]{x, y - 2});
            if (y < hauteur - 3 && !visite[y + 2][x]) voisins.add(new int[]{x, y + 2});
            if (x > 2 && !visite[y][x - 2]) voisins.add(new int[]{x - 2, y});
            if (x < largeur - 3 && !visite[y][x + 2]) voisins.add(new int[]{x + 2, y});

            if (!voisins.isEmpty()) {
                int[] next = voisins.get(random.nextInt(voisins.size()));
                int nx = next[0], ny = next[1];

                // Creuse un passage entre la cellule actuelle et la suivante
                map.setAt((y + ny) / 2, (x + nx) / 2, new Cell(path));
                map.setAt(ny, nx, new Cell(path));

                visite[ny][nx] = true;
                pile.push(new int[]{nx, ny});
            } else {
                pile.pop();
            }
        }

        // Nettoyage des culs-de-sac
        supprimerCulsDeSac(map, wall, path, largeur, hauteur);

        // Entrée et sortie du labyrinthe
        map.setAt(1, 0, new Cell(path));
        map.setAt(hauteur - 2, largeur - 1, new Cell(path));

        // Renforcement des bords gauche, droit et haut
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (i == 0 || j == 0 || j == largeur - 1) {
                    map.setAt(i, j, new Cell(wall));
                }
            }
        }

        // Génération de la dernière ligne comme prolongement naturel de la ligne précédente
        for (int x = 0; x < largeur; x++) {
            Cell above = map.getAt(hauteur - 2, x);
            if (above.getWall() != null) {
                map.setAt(hauteur - 1, x, new Cell(above.getWall()));
            } else {
                map.setAt(hauteur - 1, x, new Cell(path));
            }
        }
    }

    private void supprimerCulsDeSac(GameMap map, Wall wall, Sprite path, int largeur, int hauteur) {
        boolean changement = true;
        while (changement) {
            changement = false;
            for (int y = 1; y < hauteur - 1; y++) {
                for (int x = 1; x < largeur - 1; x++) {
                    if (map.getAt(y, x).getSprite() == path) {
                        int count = 0;
                        if (map.getAt(y - 1, x).getSprite() == path) count++;
                        if (map.getAt(y + 1, x).getSprite() == path) count++;
                        if (map.getAt(y, x - 1).getSprite() == path) count++;
                        if (map.getAt(y, x + 1).getSprite() == path) count++;

                        if (count == 1) {
                            List<int[]> murs = new ArrayList<>();
                            if (map.getAt(y - 1, x).getSprite() != path) murs.add(new int[]{x, y - 1});
                            if (map.getAt(y + 1, x).getSprite() != path) murs.add(new int[]{x, y + 1});
                            if (map.getAt(y, x - 1).getSprite() != path) murs.add(new int[]{x - 1, y});
                            if (map.getAt(y, x + 1).getSprite() != path) murs.add(new int[]{x + 1, y});
                            if (!murs.isEmpty()) {
                                int[] mur = murs.get(random.nextInt(murs.size()));
                                map.setAt(mur[1], mur[0], new Cell(path));
                                changement = true;
                            }
                        }
                    }
                }
            }
        }
    }
}
