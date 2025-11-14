package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Ghost;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

import java.util.*;

@StrategyDesignPattern(strategy = IMovementStrategy.class, participant = StrategyParticipant.IMPLEMENTATION)
public class HuntMovement implements IMovementStrategy {

    private static final double SPEED = PacmanGame.DEFAULT_SPEED*0.85;

    private final Ghost ghost;
    private final PacmanGame game;
    private final IAnimated pacman;
    private int anticipation = 0;
    private List<Cell> pathToPacMan = new ArrayList<>();
    private int indexNextCell = 0;

    public HuntMovement(Ghost ghost) {
        this.ghost = ghost;
        this.game = ghost.getGame();
        this.pacman = game.getPlayer();
    }

    public void setAnticipation(int anticipation) {
        this.anticipation = anticipation;
    }

    @Override
    public void movement() {
        GameMap carte = game.getGameMap();
        Cell ghostCell = game.getCellOf(ghost);
        if (ghostCell == null) return;

        // Recalcul du chemin si nécessaire
        if (pathToPacMan.isEmpty() || indexNextCell >= pathToPacMan.size() ||
                ghostCell.equals(pathToPacMan.get(indexNextCell))) {
            Cell targetCell = determineTargetCell();
            if (targetCell != null) {
                pathToPacMan = rebuildPathBFS(carte, ghostCell, targetCell);
                indexNextCell = 0;
            }
        }

        // Move to the next cell
        if (!pathToPacMan.isEmpty() && indexNextCell < pathToPacMan.size()) {
            Cell nextCell = pathToPacMan.get(indexNextCell);
            if (ghostCell.equals(nextCell)) {
                indexNextCell++;
                if (indexNextCell >= pathToPacMan.size()) {
                    ghost.setHorizontalSpeed(0);
                    ghost.setVerticalSpeed(0);
                    return;
                }
                nextCell = pathToPacMan.get(indexNextCell);
            }

            int dx = nextCell.getColumn() - ghostCell.getColumn();
            int dy = nextCell.getRow() - ghostCell.getRow();

            if (Math.abs(dx) > 0) {
                ghost.setHorizontalSpeed(dx > 0 ? SPEED : -SPEED);
                ghost.setVerticalSpeed(0);
            } else if (Math.abs(dy) > 0) {
                ghost.setVerticalSpeed(dy > 0 ? SPEED : -SPEED);
                ghost.setHorizontalSpeed(0);
            }
        }
    }

    private Cell determineTargetCell() {
        Cell pacManCell = game.getCellOf(pacman);
        if (pacManCell == null) return null;

        double pacManSpeed = Math.sqrt(Math.pow(pacman.getHorizontalSpeed(), 2) + Math.pow(pacman.getVerticalSpeed(), 2));
        int anticipationEffective = anticipation + (int) (pacManSpeed * 3);

        int line = pacManCell.getRow();
        int column = pacManCell.getColumn();
        int dx = (int) Math.signum(pacman.getHorizontalSpeed());
        int dy = (int) Math.signum(pacman.getVerticalSpeed());

        GameMap carte = game.getGameMap();
        Cell ghostCell = game.getCellOf(ghost);

        // if pacman is near go in front of him
        int lineDistance = Math.abs(ghostCell.getRow() - line);
        int columnDistance = Math.abs(ghostCell.getColumn() - column);
        if (lineDistance <= anticipationEffective && columnDistance <= anticipationEffective) {
            return pacManCell;
        }

        // else classic anticipation
        for (int i = 0; i < anticipationEffective; i++) {
            int newLine = line + dy;
            int newColumn = column + dx;
            if (!carte.isOnMap(newLine, newColumn)) break;
            Cell next = carte.getAt(newLine, newColumn);
            if (!next.isEmpty()) break;
            line = newLine;
            column = newColumn;
        }
        return carte.getAt(line, column);
    }

    private List<Cell> rebuildPathBFS(GameMap map, Cell begin, Cell end) {
        if (begin.equals(end)) return new ArrayList<>();

        int height = map.getHeight();
        int width = map.getWidth();
        boolean[][] visite = new boolean[height][width];
        Map<Cell, Cell> previous = new HashMap<>();
        Queue<Cell> tail = new LinkedList<>();

        tail.add(begin);
        visite[begin.getRow()][begin.getColumn()] = true;

        while (!tail.isEmpty()) {
            Cell current = tail.poll();
            if (current.equals(end)) break;

            for (Cell neighbor : getNeighbor(map, current)) {
                if (neighbor.isEmpty() && !visite[neighbor.getRow()][neighbor.getColumn()]) {
                    visite[neighbor.getRow()][neighbor.getColumn()] = true;
                    previous.put(neighbor, current);
                    tail.add(neighbor);
                }
            }
        }

        List<Cell> path = new ArrayList<>();
        Cell current = end;
        while (current != null && !current.equals(begin)) {
            path.add(0, current);
            current = previous.get(current);
        }
        return path;
    }

    private List<Cell> getNeighbor(GameMap map, Cell cell) {
        List<Cell> voisins = new ArrayList<>();
        int line = cell.getRow();
        int column = cell.getColumn();
        if (map.isOnMap(line - 1, column)) voisins.add(map.getAt(line - 1, column));
        if (map.isOnMap(line + 1, column)) voisins.add(map.getAt(line + 1, column));
        if (map.isOnMap(line, column - 1)) voisins.add(map.getAt(line, column - 1));
        if (map.isOnMap(line, column + 1)) voisins.add(map.getAt(line, column + 1));
        return voisins;
    }

    public List<Cell> getPathToPacMan() {
        return pathToPacMan;
    }

    public void reset() {
        indexNextCell = 0;
        pathToPacMan.clear();
    }
}
