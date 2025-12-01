package fr.univartois.butinfo.r304.pacman.model.map.maps;

import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.butinfo.r304.pacman.model.map.IMap;
import fr.univartois.butinfo.r304.pacman.model.map.Wall;
import fr.univartois.butinfo.r304.pacman.view.ISpriteStore;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

import java.util.*;

@StrategyDesignPattern(strategy = IMap.class, participant = StrategyParticipant.IMPLEMENTATION)
public class Map_5 extends Map {
    private final Random random = new Random();

    public Map_5() {}

    @Override
    public GameMap createMap(int width, int height) {
        GameMap map = super.createMap(width, height);
        addInternWall(map, width, height);
        return map;
    }

    private void addInternWall(GameMap map, int width, int height) {
        if (width % 2 == 0) width--;
        if (height % 2 == 0) height--;

        ISpriteStore store = SpriteStore.getInstance();
        Wall wall = new Wall(store.getSprite("wall"));
        Sprite path = store.getSprite("path");

        // Remplissage initial de la carte avec des murs sauf la dernière ligne
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width; x++) {
                map.setAt(y, x, new Cell(wall));
            }
        }

        boolean[][] visit = new boolean[height][width];
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{1, 1});
        map.setAt(1, 1, new Cell(path));
        visit[1][1] = true;

        // Algorithme DFS pour creuser le labyrinthe
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int x = current[0];
            int y = current[1];

            List<int[]> neightbor = new ArrayList<>();

            if (y > 2 && !visit[y - 2][x]) neightbor.add(new int[]{x, y - 2});
            if (y < height - 3 && !visit[y + 2][x]) neightbor.add(new int[]{x, y + 2});
            if (x > 2 && !visit[y][x - 2]) neightbor.add(new int[]{x - 2, y});
            if (x < width - 3 && !visit[y][x + 2]) neightbor.add(new int[]{x + 2, y});

            if (!neightbor.isEmpty()) {
                int[] next = neightbor.get(random.nextInt(neightbor.size()));
                int nx = next[0], ny = next[1];

                // Creuse un passage entre la cellule actuelle et la suivante
                map.setAt((y + ny) / 2, (x + nx) / 2, new Cell(path));
                map.setAt(ny, nx, new Cell(path));

                visit[ny][nx] = true;
                stack.push(new int[]{nx, ny});
            } else {
                stack.pop();
            }
        }

        // Nettoyage des culs-de-sac
        deleteDeadEnds(map, path, width, height);

        // Entrée et sortie du labyrinthe
        map.setAt(1, 0, new Cell(path));
        map.setAt(height - 2, width - 1, new Cell(path));

        // Renforcement des bords gauche, droit et haut
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || j == 0 || j == width - 1) {
                    map.setAt(i, j, new Cell(wall));
                }
            }
        }

        // Génération de la dernière ligne comme prolongement naturel de la ligne précédente
        for (int x = 0; x < width; x++) {
            Cell above = map.getAt(height - 2, x);
            if (above.getWall() != null) {
                map.setAt(height - 1, x, new Cell(above.getWall()));
            } else {
                map.setAt(height - 1, x, new Cell(path));
            }
        }
    }

    private void deleteDeadEnds(GameMap map, Sprite path, int width, int height) {
        boolean changement = true;
        while (changement) {
            changement = false;
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
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
