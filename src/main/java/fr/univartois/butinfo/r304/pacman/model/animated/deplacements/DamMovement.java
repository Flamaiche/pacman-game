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
public class DamMovement implements IMovementStrategy {

    private static final double SPEED = PacmanGame.DEFAULT_SPEED*0.85;

    private final Ghost ghost;
    private final PacmanGame game;
    private final IAnimated pacman;

    private List<Ghost> otherGhosts;
    private List<Cell> damPath = new ArrayList<>();
    private int indexNextCell = 0;

    private double lastDxPacman = 0;
    private double lastDyPacman = 0;

    public DamMovement(Ghost ghost) {
        this.ghost = ghost;
        this.game = ghost.getGame();
        this.pacman = game.getPlayer();
        this.otherGhosts = rechercherAutresFantomes();
    }

    private List<Ghost> rechercherAutresFantomes() {
        List<Ghost> ghosts = new ArrayList<>();
        for (IAnimated obj : game.getMovingObjects()) {
            if (obj instanceof Ghost f && f != this.ghost) ghosts.add(f);
        }
        return ghosts;
    }

    @Override
    public void movement() {
        GameMap carte = game.getGameMap();
        Cell celluleFantome = game.getCellOf(ghost);
        if (celluleFantome == null) return;

        if (pacManHasRotate()) {
            damPath.clear();
            indexNextCell = 0;
        }

        if (damPath.isEmpty() || indexNextCell >= damPath.size()
                || celluleFantome.equals(damPath.get(indexNextCell))) {

            Cell cible = getDamCell(carte, celluleFantome);
            if (cible == null) return;

            damPath = rebuildPathBFS(carte, celluleFantome, cible);
            indexNextCell = 0;
        }

        if (!damPath.isEmpty() && indexNextCell < damPath.size()) {
            Cell prochaine = damPath.get(indexNextCell);
            if (celluleFantome.equals(prochaine)) {
                indexNextCell++;
                if (indexNextCell >= damPath.size()) {
                    ghost.setHorizontalSpeed(0);
                    ghost.setVerticalSpeed(0);
                    return;
                }
                prochaine = damPath.get(indexNextCell);
            }

            int dx = prochaine.getColumn() - celluleFantome.getColumn();
            int dy = prochaine.getRow() - celluleFantome.getRow();
            if (Math.abs(dx) > 0) {
                ghost.setHorizontalSpeed(dx > 0 ? SPEED : -SPEED);
                ghost.setVerticalSpeed(0);
            } else if (Math.abs(dy) > 0) {
                ghost.setVerticalSpeed(dy > 0 ? SPEED : -SPEED);
                ghost.setHorizontalSpeed(0);
            }
        }
    }

    private boolean pacManHasRotate() {
        double dx = Math.signum(pacman.getHorizontalSpeed());
        double dy = Math.signum(pacman.getVerticalSpeed());
        if (dx != lastDxPacman || dy != lastDyPacman) {
            lastDxPacman = dx;
            lastDyPacman = dy;
            return true;
        }
        return false;
    }

    private Cell getDamCell(GameMap carte, Cell celluleFantome) {
        Cell cellulePacman = game.getCellOf(pacman);
        if (cellulePacman == null) return null;

        double dist = distance(celluleFantome, cellulePacman);
        // Plus il est loin, plus il anticipe
        int anticipation = Math.min(15, Math.max(4, (int) (dist / 2)));

        int dxPac = (int) Math.signum(pacman.getHorizontalSpeed());
        int dyPac = (int) Math.signum(pacman.getVerticalSpeed());

        Ghost chasseur = findHunter();
        List<Cell> cheminChasseur = chasseur != null ? getHunterPath(chasseur) : new ArrayList<>();

        Cell embuscadeGauche = futureDam(carte, cellulePacman, dxPac, dyPac, anticipation, true);
        Cell embuscadeDroite = futureDam(carte, cellulePacman, dxPac, dyPac, anticipation, false);

        Cell cible = chooseAmbush(carte, celluleFantome, chasseur, cheminChasseur, embuscadeGauche, embuscadeDroite);

        return (cible != null) ? cible : cellulePacman;
    }

    private Cell futureDam(GameMap carte, Cell pacman, int dx, int dy, int anticipation, boolean gauche) {
        if (dx == 0 && dy == 0) dx = 1;

        int perpX = dy;
        int perpY = -dx;
        if (!gauche) {
            perpX *= -1;
            perpY *= -1;
        }

        int projRow = pacman.getRow() + dy * anticipation + perpY * 2;
        int projCol = pacman.getColumn() + dx * anticipation + perpX * 2;

        if (carte.isOnMap(projRow, projCol)) {
            Cell c = carte.getAt(projRow, projCol);
            if (c.isEmpty()) return c;
        }
        return pacman;
    }

    private Ghost findHunter() {
        Ghost chasseur = null;
        double best = Double.MAX_VALUE;
        for (Ghost f : otherGhosts) {
            Cell c = game.getCellOf(f);
            Cell p = game.getCellOf(pacman);
            if (c == null || p == null) continue;
            double d = distance(c, p);
            if (d < best) {
                best = d;
                chasseur = f;
            }
        }
        return chasseur;
    }

    private List<Cell> getHunterPath(Ghost chasseur) {
        try {
            if (chasseur.getCurrentMovement() instanceof HuntMovement strat) {
                return strat.getPathToPacMan();
            } else if (chasseur.getCurrentMovement() instanceof DamMovement strat) {
                return strat.getDamPath();
            }
        } catch (Exception ignored) {}
        return new ArrayList<>();
    }

    private Cell chooseAmbush(GameMap carte, Cell celluleFantome, Ghost chasseur,
                              List<Cell> cheminChasseur, Cell gauche, Cell droite) {

        Cell cChasseur = (chasseur != null) ? game.getCellOf(chasseur) : null;
        double distG = (gauche != null && cChasseur != null) ? distance(gauche, cChasseur) : 0;
        double distD = (droite != null && cChasseur != null) ? distance(droite, cChasseur) : 0;

        Cell prioritaire = (distG > distD) ? gauche : droite;
        Cell secondaire = (distG > distD) ? droite : gauche;

        List<Cell> chemin1 = rebuildPathBFS(carte, celluleFantome, prioritaire);
        if (!chemin1.isEmpty() && !pathHorseHunter(chemin1, cheminChasseur)) return prioritaire;

        List<Cell> chemin2 = rebuildPathBFS(carte, celluleFantome, secondaire);
        if (!chemin2.isEmpty() && !pathHorseHunter(chemin2, cheminChasseur)) return secondaire;

        return null;
    }

    private boolean pathHorseHunter(List<Cell> chemin, List<Cell> cheminChasseur) {
        for (Cell c : chemin) {
            for (Cell cc : cheminChasseur) {
                if (cc == null) continue;
                int dr = Math.abs(c.getRow() - cc.getRow());
                int dc = Math.abs(c.getColumn() - cc.getColumn());
                if (dr <= 1 && dc <= 1) return true;
            }
        }
        return false;
    }

    private List<Cell> rebuildPathBFS(GameMap carte, Cell begin, Cell end) {
        if (begin == null || end == null || begin.equals(end)) return new ArrayList<>();

        Queue<Cell> tail = new LinkedList<>();
        Map<Cell, Cell> previous = new HashMap<>();
        Set<Cell> visit = new HashSet<>();

        tail.add(begin);
        visit.add(begin);
        boolean find = false;

        while (!tail.isEmpty()) {
            Cell current = tail.poll();
            if (current.equals(end)) {
                find = true;
                break;
            }
            for (Cell neighbor : getNeighbor(carte, current)) {
                if (!neighbor.isEmpty() || visit.contains(neighbor)) continue;
                visit.add(neighbor);
                previous.put(neighbor, current);
                tail.add(neighbor);
            }
        }

        List<Cell> chemin = new ArrayList<>();
        if (!find) return chemin;
        Cell current = end;
        while (current != null && !current.equals(begin)) {
            chemin.add(0, current);
            current = previous.get(current);
        }
        return chemin;
    }

    private List<Cell> getNeighbor(GameMap carte, Cell cellule) {
        List<Cell> voisins = new ArrayList<>();
        int l = cellule.getRow(), c = cellule.getColumn();
        if (carte.isOnMap(l - 1, c)) voisins.add(carte.getAt(l - 1, c));
        if (carte.isOnMap(l + 1, c)) voisins.add(carte.getAt(l + 1, c));
        if (carte.isOnMap(l, c - 1)) voisins.add(carte.getAt(l, c - 1));
        if (carte.isOnMap(l, c + 1)) voisins.add(carte.getAt(l, c + 1));
        return voisins;
    }

    private double distance(Cell a, Cell b) {
        int dx = a.getColumn() - b.getColumn();
        int dy = a.getRow() - b.getRow();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public List<Cell> getDamPath() {
        return damPath;
    }

    public void reset() {
        indexNextCell = 0;
        lastDxPacman = 0;
        lastDyPacman = 0;
        damPath.clear();
    }
}
