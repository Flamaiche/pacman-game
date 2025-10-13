package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Fantome;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;

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

        if (celluleFantome == null || cellulePacman == null)
            return;

        double dx = cellulePacman.getColumn() - celluleFantome.getColumn();
        double dy = cellulePacman.getRow() - celluleFantome.getRow();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double vitesse = PacmanGame.DEFAULT_SPEED;

        if (distance < DISTANCE_FUITE) {
            int dirX = 0;
            int dirY = 0;

            if (Math.abs(dx) > Math.abs(dy)) {
                dirX = dx > 0 ? -1 : 1;
            } else {
                dirY = dy > 0 ? -1 : 1;
            }

            int nextRow = celluleFantome.getRow() + dirY;
            int nextCol = celluleFantome.getColumn() + dirX;

            if (!carte.isOnMap(nextRow, nextCol) || !carte.getAt(nextRow, nextCol).isEmpty()) {
                if (dirX != 0) {
                    dirX = 0;
                    dirY = random.nextBoolean() ? 1 : -1;
                } else {
                    dirY = 0;
                    dirX = random.nextBoolean() ? 1 : -1;
                }
            }
            fantome.setHorizontalSpeed(dirX * vitesse);
            fantome.setVerticalSpeed(dirY * vitesse);
        } else {
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
