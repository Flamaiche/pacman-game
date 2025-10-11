package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Fantome;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;

import java.util.*;

public class DeplacementBarrage implements IStrategieDeplacement {

    private final Fantome fantome;
    private final PacmanGame game;
    private final IAnimated pacman;
    private final double vitesse = PacmanGame.DEFAULT_SPEED;
    private final List<Fantome> autresFantomes;
    private List<Cell> cheminBarrage = new ArrayList<>();
    private int indexProchaineCellule = 0;

    public DeplacementBarrage(Fantome fantome) {
        this.fantome = fantome;
        this.game = fantome.getGame();
        this.pacman = game.getPlayer();
        this.autresFantomes = rechercherAutresFantomes();
    }

    private List<Fantome> rechercherAutresFantomes() {
        List<Fantome> fantomes = new ArrayList<>();
        List<IAnimated> objs = game.getAnimatedObjects();
        for (int i = 0; i < objs.size(); i++) {
            IAnimated obj = objs.get(i);
            if (obj instanceof Fantome) {
                Fantome f = (Fantome) obj;
                if (f != this.fantome) fantomes.add(f);
            }
        }
        return fantomes;
    }

    @Override
    public void mouvement() {
        GameMap carte = game.getGameMap();
        Cell celluleFantome = game.getCellOf(fantome);
        if (celluleFantome == null) return;

        if (cheminBarrage.isEmpty() || indexProchaineCellule >= cheminBarrage.size() ||
                celluleFantome.equals(cheminBarrage.get(indexProchaineCellule))) {

            Cell cible = determinerCelluleBarrage(carte, celluleFantome);
            if (cible == null) return;

            cheminBarrage = reconstruireCheminBFS(carte, celluleFantome, cible);
            cheminBarrage = optimiserChemin(cheminBarrage, carte);
            indexProchaineCellule = 0;
        }

        if (!cheminBarrage.isEmpty() && indexProchaineCellule < cheminBarrage.size()) {
            Cell prochaine = cheminBarrage.get(indexProchaineCellule);
            if (celluleFantome.equals(prochaine)) {
                indexProchaineCellule++;
                if (indexProchaineCellule >= cheminBarrage.size()) {
                    fantome.setHorizontalSpeed(0);
                    fantome.setVerticalSpeed(0);
                    return;
                }
                prochaine = cheminBarrage.get(indexProchaineCellule);
            }

            int dx = prochaine.getColumn() - celluleFantome.getColumn();
            int dy = prochaine.getRow() - celluleFantome.getRow();
            if (Math.abs(dx) > 0) {
                fantome.setHorizontalSpeed(dx > 0 ? vitesse : -vitesse);
                fantome.setVerticalSpeed(0);
            } else if (Math.abs(dy) > 0) {
                fantome.setVerticalSpeed(dy > 0 ? vitesse : -vitesse);
                fantome.setHorizontalSpeed(0);
            }
        }
    }

    private Cell determinerCelluleBarrage(GameMap carte, Cell celluleFantome) {
        Cell cellulePacman = game.getCellOf(pacman);
        if (cellulePacman == null) return null;

        double distFantome = distance(celluleFantome, cellulePacman);
        double bestDistAutre = Double.MAX_VALUE;
        Fantome chasseur = null;
        for (int i = 0; i < autresFantomes.size(); i++) {
            Fantome f = autresFantomes.get(i);
            Cell c = game.getCellOf(f);
            if (c == null) continue;
            double d = distance(c, cellulePacman);
            if (d < bestDistAutre) {
                bestDistAutre = d;
                chasseur = f;
            }
        }

        if (distFantome <= bestDistAutre + 1.0) return cellulePacman;

        int dxPac = (int) Math.signum(pacman.getHorizontalSpeed());
        int dyPac = (int) Math.signum(pacman.getVerticalSpeed());

        int projRow = cellulePacman.getRow() + dyPac * 2;
        int projCol = cellulePacman.getColumn() + dxPac * 2;

        if (chasseur != null) {
            Cell celluleChasseur = game.getCellOf(chasseur);
            if (celluleChasseur != null) {
                int relRow = projRow - celluleChasseur.getRow();
                int relCol = projCol - celluleChasseur.getColumn();
                int cibleRow = celluleChasseur.getRow() + relRow * 2;
                int cibleCol = celluleChasseur.getColumn() + relCol * 2;
                if (carte.isOnMap(cibleRow, cibleCol)) {
                    Cell cible = carte.getAt(cibleRow, cibleCol);
                    if (cible.isEmpty()) return cible;
                }
            }
        }

        if (carte.isOnMap(projRow, projCol)) {
            Cell prevision = carte.getAt(projRow, projCol);
            if (prevision.isEmpty()) return prevision;
        }

        int[][] offsets = {{2,0},{-2,0},{0,2},{0,-2},{1,0},{-1,0},{0,1},{0,-1}};
        for (int i = 0; i < offsets.length; i++) {
            int nc = cellulePacman.getColumn() + offsets[i][0];
            int nr = cellulePacman.getRow() + offsets[i][1];
            if (!carte.isOnMap(nr, nc)) continue;
            Cell c = carte.getAt(nr, nc);
            if (c.isEmpty()) return c;
        }

        return cellulePacman;
    }

    private List<Cell> reconstruireCheminBFS(GameMap carte, Cell depart, Cell arrivee) {
        if (depart.equals(arrivee)) return new ArrayList<Cell>();
        Queue<Cell> queue = new LinkedList<Cell>();
        Map<Cell, Cell> precedent = new HashMap<Cell, Cell>();
        Set<Cell> visite = new HashSet<Cell>();
        queue.add(depart);
        visite.add(depart);
        boolean trouve = false;
        while (!queue.isEmpty()) {
            Cell actuelle = queue.poll();
            if (actuelle.equals(arrivee)) {
                trouve = true;
                break;
            }
            List<Cell> voisins = obtenirVoisins(carte, actuelle);
            for (int i = 0; i < voisins.size(); i++) {
                Cell voisin = voisins.get(i);
                if (!voisin.isEmpty() || visite.contains(voisin)) continue;
                visite.add(voisin);
                precedent.put(voisin, actuelle);
                queue.add(voisin);
            }
        }
        List<Cell> chemin = new ArrayList<Cell>();
        if (!trouve) return chemin;
        Cell current = arrivee;
        while (current != null && !current.equals(depart)) {
            chemin.add(0, current);
            current = precedent.get(current);
        }
        return chemin;
    }

    private List<Cell> optimiserChemin(List<Cell> chemin, GameMap carte) {
        if (chemin.size() < 4) return chemin;
        List<Cell> optim = new ArrayList<Cell>();
        optim.add(chemin.get(0));
        for (int i = 1; i < chemin.size() - 1; i++) {
            Cell a = optim.get(optim.size() - 1);
            Cell b = chemin.get(i + 1);
            if (peutAllerDirect(a, b, carte)) continue;
            optim.add(chemin.get(i));
        }
        optim.add(chemin.get(chemin.size() - 1));
        return optim;
    }

    private boolean peutAllerDirect(Cell a, Cell b, GameMap carte) {
        int dr = Integer.signum(b.getRow() - a.getRow());
        int dc = Integer.signum(b.getColumn() - a.getColumn());
        int r = a.getRow();
        int c = a.getColumn();
        while (r != b.getRow() || c != b.getColumn()) {
            if (!carte.isOnMap(r, c)) return false;
            Cell cell = carte.getAt(r, c);
            if (!cell.isEmpty()) return false;
            r += dr;
            c += dc;
        }
        return true;
    }

    private double distance(Cell a, Cell b) {
        int dx = a.getColumn() - b.getColumn();
        int dy = a.getRow() - b.getRow();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private List<Cell> obtenirVoisins(GameMap carte, Cell cellule) {
        List<Cell> voisins = new ArrayList<Cell>();
        int ligne = cellule.getRow();
        int colonne = cellule.getColumn();
        if (carte.isOnMap(ligne - 1, colonne)) voisins.add(carte.getAt(ligne - 1, colonne));
        if (carte.isOnMap(ligne + 1, colonne)) voisins.add(carte.getAt(ligne + 1, colonne));
        if (carte.isOnMap(ligne, colonne - 1)) voisins.add(carte.getAt(ligne, colonne - 1));
        if (carte.isOnMap(ligne, colonne + 1)) voisins.add(carte.getAt(ligne, colonne + 1));
        return voisins;
    }

    public List<Cell> getCheminBarrage() {
        return cheminBarrage;
    }
}
