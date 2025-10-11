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
    private List<Fantome> autresFantomes = new ArrayList<>();
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
            if (obj instanceof Fantome f && f != this.fantome) fantomes.add(f);
        }
        return fantomes;
    }

    @Override
    public void mouvement() {
        GameMap carte = game.getGameMap();
        Cell celluleFantome = game.getCellOf(fantome);
        if (celluleFantome == null) return;

        // Recalcul uniquement si on a atteint la prochaine cellule
        if (cheminBarrage.isEmpty() || indexProchaineCellule >= cheminBarrage.size() ||
                celluleFantome.equals(cheminBarrage.get(indexProchaineCellule))) {

            Map<Cell, Integer> couts = new HashMap<>();
            for (Fantome autre : autresFantomes) {
                if (autre.getStrategieDeplacement() instanceof DeplacementVersJoueur strategie) {
                    for (Cell c : strategie.getCheminVersPacman()) {
                        couts.put(c, 100);
                        for (Cell v : obtenirVoisins(carte, c)) couts.put(v, 50);
                    }
                }
            }

            Cell cible = determinerCelluleBarrage(carte);
            if (cible == null) return;

            cheminBarrage = reconstruireCheminDijkstra(carte, celluleFantome, cible, couts);
            // Optimisation locale pour raccourcis
            cheminBarrage = optimiserChemin(cheminBarrage, carte);
            indexProchaineCellule = 0;
        }

        // Avancer vers la prochaine cellule
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

    private Cell determinerCelluleBarrage(GameMap carte) {
        Cell cellulePacman = game.getCellOf(pacman);
        if (cellulePacman == null) return null;

        for (Fantome autre : autresFantomes) {
            if (autre.getStrategieDeplacement() instanceof DeplacementVersJoueur strategie) {
                List<Cell> cheminChasseur = strategie.getCheminVersPacman();
                if (!cheminChasseur.isEmpty()) {
                    Cell pointMilieu = cheminChasseur.get(cheminChasseur.size() / 2);
                    int dx = cellulePacman.getColumn() - pointMilieu.getColumn();
                    int dy = cellulePacman.getRow() - pointMilieu.getRow();
                    int cibleCol = cellulePacman.getColumn() + dx;
                    int cibleRow = cellulePacman.getRow() + dy;
                    if (carte.isOnMap(cibleRow, cibleCol)) {
                        Cell opposite = carte.getAt(cibleRow, cibleCol);
                        if (opposite.isEmpty()) return opposite;
                    }
                }
            }
        }
        return cellulePacman;
    }

    private List<Cell> reconstruireCheminDijkstra(GameMap carte, Cell depart, Cell arrivee, Map<Cell, Integer> couts) {
        Map<Cell, Integer> distances = new HashMap<>();
        Map<Cell, Cell> precedent = new HashMap<>();
        PriorityQueue<Cell> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        distances.put(depart, 0);
        pq.add(depart);

        while (!pq.isEmpty()) {
            Cell actuelle = pq.poll();
            if (actuelle.equals(arrivee)) break;

            for (Cell voisin : obtenirVoisins(carte, actuelle)) {
                if (!voisin.isEmpty()) continue;
                int cout = couts.getOrDefault(voisin, 1);
                int newDist = distances.get(actuelle) + cout;
                if (newDist < distances.getOrDefault(voisin, Integer.MAX_VALUE)) {
                    distances.put(voisin, newDist);
                    precedent.put(voisin, actuelle);
                    pq.add(voisin);
                }
            }
        }

        List<Cell> chemin = new ArrayList<>();
        Cell current = arrivee;
        while (current != null && !current.equals(depart)) {
            chemin.add(0, current);
            current = precedent.get(current);
        }
        return chemin;
    }

    private List<Cell> optimiserChemin(List<Cell> chemin, GameMap carte) {
        if (chemin.size() < 3) return chemin;

        List<Cell> cheminOpti = new ArrayList<>();
        cheminOpti.add(chemin.get(0));

        for (int i = 1; i < chemin.size(); i++) {
            Cell precedent = cheminOpti.get(cheminOpti.size() - 1);
            Cell actuel = chemin.get(i);

            // Cherche une cellule adjacente plus proche de la cible
            for (Cell voisin : obtenirVoisins(carte, precedent)) {
                if (voisin.isEmpty() && !cheminOpti.contains(voisin)) {
                    double distActuel = Math.hypot(actuel.getRow() - chemin.get(chemin.size() - 1).getRow(),
                            actuel.getColumn() - chemin.get(chemin.size() - 1).getColumn());
                    double distVoisin = Math.hypot(voisin.getRow() - chemin.get(chemin.size() - 1).getRow(),
                            voisin.getColumn() - chemin.get(chemin.size() - 1).getColumn());
                    if (distVoisin < distActuel) {
                        actuel = voisin;
                        break;
                    }
                }
            }

            cheminOpti.add(actuel);
        }
        return cheminOpti;
    }

    private List<Cell> obtenirVoisins(GameMap carte, Cell cellule) {
        List<Cell> voisins = new ArrayList<>();
        int ligne = cellule.getRow(), colonne = cellule.getColumn();
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
