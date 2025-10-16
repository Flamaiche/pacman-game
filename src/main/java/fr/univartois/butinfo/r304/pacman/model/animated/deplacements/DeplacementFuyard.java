package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Fantome;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;

import java.util.ArrayList;
import java.util.List;

public class DeplacementFuyard implements IStrategieDeplacement {

    private final Fantome fantome;
    private final PacmanGame game;
    private final IAnimated pacman;
    private final DeplacementAleatoire aleatoire;
    private static final double DISTANCE_FUITE = 5.0;

    public DeplacementFuyard(Fantome fantome) {
        this.fantome = fantome;
        this.game = fantome.getGame();
        this.pacman = game.getPlayer();
        this.aleatoire = new DeplacementAleatoire(fantome);
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

        double dirPacX = Math.signum(pacman.getHorizontalSpeed());
        double dirPacY = Math.signum(pacman.getVerticalSpeed());

        double vx = celluleFantome.getColumn() - cellulePacman.getColumn();
        double vy = celluleFantome.getRow() - cellulePacman.getRow();
        double produitScalaire = dirPacX * vx + dirPacY * vy;

        boolean pacmanSeDirigeVersFantome = produitScalaire > 0;

        if (distance < DISTANCE_FUITE && pacmanSeDirigeVersFantome) {
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

            int[] meilleur = directionsLibres.get(0);
            double meilleureDistance = distance;

            for (int[] dir : directionsLibres) {
                int futureRow = celluleFantome.getRow() + dir[1] * 2;
                int futureCol = celluleFantome.getColumn() + dir[0] * 2;

                if (!carte.isOnMap(futureRow, futureCol) || !carte.getAt(futureRow, futureCol).isEmpty())
                    continue;

                double newDx = cellulePacman.getColumn() - futureCol;
                double newDy = cellulePacman.getRow() - futureRow;
                double newDistance = Math.sqrt(newDx * newDx + newDy * newDy);

                if (newDistance > meilleureDistance) {
                    meilleureDistance = newDistance;
                    meilleur = dir;
                }
            }
            fantome.setHorizontalSpeed(meilleur[0] * vitesse * 1.1);
            fantome.setVerticalSpeed(meilleur[1] * vitesse * 1.1);

        } else {
            aleatoire.mouvement();
        }
    }

    @Override
    public void reset() {
        aleatoire.reset();
        fantome.setHorizontalSpeed(0);
        fantome.setVerticalSpeed(0);
    }
}
