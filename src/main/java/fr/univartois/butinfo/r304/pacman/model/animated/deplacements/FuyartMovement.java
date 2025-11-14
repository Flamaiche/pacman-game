package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Ghost;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

import java.util.ArrayList;
import java.util.List;

@StrategyDesignPattern(strategy = IMovementStrategy.class, participant = StrategyParticipant.IMPLEMENTATION)
public class FuyartMovement implements IMovementStrategy {

    private final Ghost ghost;
    private final PacmanGame game;
    private final IAnimated pacman;
    private final RandomMovement randomMovement;
    private static final double LEAK_DISTANCE = 5.0;

    public FuyartMovement(Ghost ghost) {
        this.ghost = ghost;
        this.game = ghost.getGame();
        this.pacman = game.getPlayer();
        this.randomMovement = new RandomMovement(ghost);
    }

    @Override
    public void movement() {
        GameMap carte = game.getGameMap();
        Cell ghostCell = game.getCellOf(ghost);
        Cell pacManCell = game.getCellOf(pacman);

        if (ghostCell == null || pacManCell == null) return;

        double dx = pacManCell.getColumn() - ghostCell.getColumn();
        double dy = pacManCell.getRow() - ghostCell.getRow();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double speed = PacmanGame.DEFAULT_SPEED;

        double dirPacX = Math.signum(pacman.getHorizontalSpeed());
        double dirPacY = Math.signum(pacman.getVerticalSpeed());

        double vx = ghostCell.getColumn() - pacManCell.getColumn();
        double vy = ghostCell.getRow() - pacManCell.getRow();
        double productScalar = dirPacX * vx + dirPacY * vy;

        boolean pacmanHeadsToGhost = productScalar > 0;

        if (distance < LEAK_DISTANCE && pacmanHeadsToGhost) {
            List<int[]> freeDirections = new ArrayList<>();
            int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};

            for (int[] dir : directions) {
                int newRow = ghostCell.getRow() + dir[1];
                int newCol = ghostCell.getColumn() + dir[0];
                if (carte.isOnMap(newRow, newCol) && carte.getAt(newRow, newCol).isEmpty()) {
                    freeDirections.add(dir);
                }
            }

            if (freeDirections.isEmpty()) {
                ghost.setHorizontalSpeed(0);
                ghost.setVerticalSpeed(0);
                return;
            }

            int[] better = freeDirections.get(0);
            double betterDistance = distance;

            for (int[] dir : freeDirections) {
                int futureRow = ghostCell.getRow() + dir[1] * 2;
                int futureCol = ghostCell.getColumn() + dir[0] * 2;

                if (!carte.isOnMap(futureRow, futureCol) || !carte.getAt(futureRow, futureCol).isEmpty())
                    continue;

                double newDx = pacManCell.getColumn() - futureCol;
                double newDy = pacManCell.getRow() - futureRow;
                double newDistance = Math.sqrt(newDx * newDx + newDy * newDy);

                if (newDistance > betterDistance) {
                    betterDistance = newDistance;
                    better = dir;
                }
            }
            ghost.setHorizontalSpeed(better[0] * speed * 1.1);
            ghost.setVerticalSpeed(better[1] * speed * 1.1);

        } else {
            randomMovement.movement();
        }
    }

    @Override
    public void reset() {
        randomMovement.reset();
        ghost.setHorizontalSpeed(0);
        ghost.setVerticalSpeed(0);
    }
}
