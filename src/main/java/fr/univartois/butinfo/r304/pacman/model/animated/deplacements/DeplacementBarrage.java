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
    private List<Fantome> autresFantomes;
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
        for (IAnimated obj : game.getAnimatedObjects()) {
            if (obj instanceof Fantome && obj != this.fantome) {
                fantomes.add((Fantome)obj);
            }
        }
        return fantomes;
    }

    @Override
    public void mouvement() {
        GameMap carte = game.getGameMap();
        Cell celluleFantome = game.getCellOf(fantome);
        if (celluleFantome == null) return;

        if (cheminBarrage.isEmpty() || indexProchaineCellule >= cheminBarrage.size()
                || celluleFantome.equals(cheminBarrage.get(indexProchaineCellule))) {

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
        int anticipation = Math.min(8, Math.max(2, (int)(distFantome/3)));

        int dxPac = (int)Math.signum(pacman.getHorizontalSpeed());
        int dyPac = (int)Math.signum(pacman.getVerticalSpeed());

        int projRow = cellulePacman.getRow() + dyPac * anticipation;
        int projCol = cellulePacman.getColumn() + dxPac * anticipation;

        if (!carte.isOnMap(projRow, projCol)) {
            projRow = cellulePacman.getRow();
            projCol = cellulePacman.getColumn();
        }

        Fantome chasseur = null;
        List<Cell> cheminChasseur = new ArrayList<>();

        // recherche du fantome le plus proche (rose)
        double bestDist = Double.MAX_VALUE;
        for (Fantome f : autresFantomes) {
            Cell c = game.getCellOf(f);
            if (c == null) continue;
            double d = distance(c, cellulePacman);
            if (d < bestDist) {
                bestDist = d;
                chasseur = f;
            }
        }

        // cas agressif si proche
        if (distFantome < 5) return cellulePacman;

        // positions candidates latérales
        List<Cell> candidats = new ArrayList<>();
        int perpX = dyPac;
        int perpY = -dxPac;
        for (int i=1; i<=4; i++) {
            int r1 = projRow + perpY*i;
            int c1 = projCol + perpX*i;
            int r2 = projRow - perpY*i;
            int c2 = projCol - perpX*i;
            if (carte.isOnMap(r1,c1)) candidats.add(carte.getAt(r1,c1));
            if (carte.isOnMap(r2,c2)) candidats.add(carte.getAt(r2,c2));
        }

        Cell meilleur = null;
        double meilleurScore = Double.NEGATIVE_INFINITY;
        for (Cell cand : candidats) {
            if (cand == null || !cand.isEmpty()) continue;
            double score = distance(cand, cellulePacman); // plus proche → mieux
            // pénaliser si proche du chasseur
            if (chasseur != null) {
                Cell cCh = game.getCellOf(chasseur);
                if (cCh != null && distance(cand,cCh)<2) score -= 1000;
            }
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleur = cand;
            }
        }

        if (meilleur != null) return meilleur;
        return cellulePacman;
    }

    private List<Cell> reconstruireCheminBFS(GameMap carte, Cell depart, Cell arrivee) {
        if (depart.equals(arrivee)) return new ArrayList<>();
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> precedent = new HashMap<>();
        Set<Cell> visite = new HashSet<>();
        queue.add(depart);
        visite.add(depart);
        boolean trouve = false;
        while (!queue.isEmpty()) {
            Cell actuelle = queue.poll();
            if (actuelle.equals(arrivee)) {
                trouve = true;
                break;
            }
            for (Cell voisin : obtenirVoisins(carte, actuelle)) {
                if (!voisin.isEmpty() || visite.contains(voisin)) continue;
                visite.add(voisin);
                precedent.put(voisin, actuelle);
                queue.add(voisin);
            }
        }
        List<Cell> chemin = new ArrayList<>();
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
        List<Cell> optim = new ArrayList<>();
        optim.add(chemin.get(0));
        for (int i=1;i<chemin.size()-1;i++) {
            Cell a = optim.get(optim.size()-1);
            Cell b = chemin.get(i+1);
            if (peutAllerDirect(a,b,carte)) continue;
            optim.add(chemin.get(i));
        }
        optim.add(chemin.get(chemin.size()-1));
        return optim;
    }

    private boolean peutAllerDirect(Cell a, Cell b, GameMap carte) {
        int dr = Integer.signum(b.getRow()-a.getRow());
        int dc = Integer.signum(b.getColumn()-a.getColumn());
        int r=a.getRow(), c=a.getColumn();
        while (r!=b.getRow() || c!=b.getColumn()) {
            if (!carte.isOnMap(r,c)) return false;
            if (!carte.getAt(r,c).isEmpty()) return false;
            r+=dr; c+=dc;
        }
        return true;
    }

    private double distance(Cell a, Cell b) {
        int dx = a.getColumn()-b.getColumn();
        int dy = a.getRow()-b.getRow();
        return Math.sqrt(dx*dx+dy*dy);
    }

    private List<Cell> obtenirVoisins(GameMap carte, Cell cellule) {
        List<Cell> voisins = new ArrayList<>();
        int l=cellule.getRow(), c=cellule.getColumn();
        if (carte.isOnMap(l-1,c)) voisins.add(carte.getAt(l-1,c));
        if (carte.isOnMap(l+1,c)) voisins.add(carte.getAt(l+1,c));
        if (carte.isOnMap(l,c-1)) voisins.add(carte.getAt(l,c-1));
        if (carte.isOnMap(l,c+1)) voisins.add(carte.getAt(l,c+1));
        return voisins;
    }

    public List<Cell> getCheminBarrage() { return cheminBarrage; }
}
