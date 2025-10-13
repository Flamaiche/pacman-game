package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Fantome;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeplacementFuyard implements IStrategieDeplacement {

    private final Fantome fantome;
    private final PacmanGame game;
    private final IAnimated pacman;
    private final DeplacementVersJoueur versJoueur;
    private static final double DISTANCE_FUITE = 5.0;
    private final Random random = new Random();

    public DeplacementFuyard(Fantome fantome) {
        this.fantome = fantome;
        this.game = fantome.getGame();
        this.pacman = game.getPlayer();
        this.versJoueur = new DeplacementVersJoueur(fantome);
    }

    @Override
    public void mouvement() {
        GameMap carte = game.getGameMap();
        Cell celluleFantome = game.getCellOf(fantome);
        Cell cellulePacman = game.getCellOf(pacman);

        if (celluleFantome == null || cellulePacman == null) return;

        double dx = cellulePacman.getColumn() - celluleFantome.getColumn();
        double dy = cellulePacman.getRow() - celluleFantome.getRow();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double vitesse = PacmanGame.DEFAULT_SPEED;

        if (distance < DISTANCE_FUITE) {
            List<int[]> directionsLibres = new ArrayList<>();
            int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};

            for (int[] dir : directions) {
                int newRow = celluleFantome.getRow() + dir[1];
                int newCol = celluleFantome.getColumn() + dir[0];
                if (carte.isOnMap(newRow, newCol) && carte.getAt(newRow, newCol).isEmpty()) {
                    directionsLibres.add(dir);
                }
            }

            if (directionsLibres.isEmpty()) {
                fantome.setHorizontalSpeed(0);
                fantome.setVerticalSpeed(0);
                return;
            }

            int[] choix = directionsLibres.get(0);
            double maxDistance = distance;
            for (int[] dir : directionsLibres) {
                double newDx = cellulePacman.getColumn() - (celluleFantome.getColumn() + dir[0]);
                double newDy = cellulePacman.getRow() - (celluleFantome.getRow() + dir[1]);
                double newDistance = Math.sqrt(newDx*newDx + newDy*newDy);
                if (newDistance > maxDistance) {
                    maxDistance = newDistance;
                    choix = dir;
                }
            }

            fantome.setHorizontalSpeed(choix[0] * vitesse);
            fantome.setVerticalSpeed(choix[1] * vitesse);

        } else {
            reset();
            versJoueur.mouvement();
        }
    }

    @Override
    public void reset() {
        versJoueur.reset();
        fantome.setHorizontalSpeed(0);
        fantome.setVerticalSpeed(0);
    }
}
