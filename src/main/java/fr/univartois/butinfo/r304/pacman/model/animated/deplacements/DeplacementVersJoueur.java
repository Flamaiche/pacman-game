package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Fantome;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;

import java.util.*;

public class DeplacementVersJoueur implements IStrategieDeplacement {

    private final Fantome fantome;
    private final PacmanGame game;
    private final IAnimated pacman;
    private final Random random = new Random();
    private int anticipation = 0;
    private double vitesse = 100;


    private int compteurDeplacement = 0;
    private static final int DELAI = 50;

    private int directionX = 0;
    private int directionY = 0;

    public DeplacementVersJoueur(Fantome fantome) {
        this.fantome = fantome;
        this.game = fantome.getGame();
        this.pacman = game.getPlayer();
        vitesse = PacmanGame.DEFAULT_SPEED;
    }

    @Override
    public void mouvement() {
        compteurDeplacement++;
        if (compteurDeplacement >= DELAI) {
            choisirDirection();
            compteurDeplacement = 0;
        }
    }

    private void choisirDirection() {
        GameMap carte = game.getGameMap();
        Cell celluleFantome = game.getCellOf(fantome);
        Cell cellulePacman = determinerCelluleCible(anticipation);
        if (celluleFantome == null || cellulePacman == null) return;

        List<Cell> voisins = obtenirVoisins(carte, celluleFantome);
        Map<Cell, Integer> distances = new HashMap<>();

        for (Cell voisin : voisins) {
            if (!voisin.isEmpty()) continue;
            int dist = bfsDistance(carte, voisin, cellulePacman);
            if (dist >= 0) distances.put(voisin, dist);
        }

        if (distances.isEmpty()) return;

        int minDist = Collections.min(distances.values());
        List<Cell> meilleursVoisins = new ArrayList<>();
        for (Map.Entry<Cell, Integer> entry : distances.entrySet()) {
            if (entry.getValue() == minDist) meilleursVoisins.add(entry.getKey());
        }

        Cell prochaine = null;
        for (Cell c : meilleursVoisins) {
            int dx = c.getColumn() - celluleFantome.getColumn();
            int dy = c.getRow() - celluleFantome.getRow();
            if (dx == directionX && dy == directionY) {
                prochaine = c;
                break;
            }
        }

        if (prochaine == null) {
            prochaine = meilleursVoisins.get(random.nextInt(meilleursVoisins.size()));
        }

        int deltaX = prochaine.getColumn() - celluleFantome.getColumn();
        int deltaY = prochaine.getRow() - celluleFantome.getRow();

        setDirection(deltaX, deltaY, vitesse);
    }

    private void setDirection(int deltaX, int deltaY, double vitesse) {
        if (Math.abs(deltaX) > 0) {
            fantome.setHorizontalSpeed(deltaX > 0 ? vitesse : -vitesse);
            fantome.setVerticalSpeed(0);
            directionX = deltaX > 0 ? 1 : -1;
            directionY = 0;
        } else if (Math.abs(deltaY) > 0) {
            fantome.setVerticalSpeed(deltaY > 0 ? vitesse : -vitesse);
            fantome.setHorizontalSpeed(0);
            directionY = deltaY > 0 ? 1 : -1;
            directionX = 0;
        }
    }

    private Cell determinerCelluleCible(int anticipation) {
        Cell cellulePacman = game.getCellOf(pacman);
        if (cellulePacman == null) return null;

        int ligne = cellulePacman.getRow();
        int colonne = cellulePacman.getColumn();
        int dx = 0, dy = 0;

        if (pacman.getHorizontalSpeed() > 0) dx = 1;
        else if (pacman.getHorizontalSpeed() < 0) dx = -1;
        else if (pacman.getVerticalSpeed() > 0) dy = 1;
        else if (pacman.getVerticalSpeed() < 0) dy = -1;

        GameMap carte = game.getGameMap();
        for (int i = 0; i < anticipation; i++) {
            int nouvelleLigne = ligne + dy;
            int nouvelleColonne = colonne + dx;
            if (!carte.isOnMap(nouvelleLigne, nouvelleColonne)) break;
            Cell prochaine = carte.getAt(nouvelleLigne, nouvelleColonne);
            if (!prochaine.isEmpty()) break;
            ligne = nouvelleLigne;
            colonne = nouvelleColonne;
        }

        return carte.getAt(ligne, colonne);
    }

    private int bfsDistance(GameMap carte, Cell depart, Cell arrivee) {
        if (depart.equals(arrivee)) return 0;

        int hauteur = carte.getHeight();
        int largeur = carte.getWidth();
        boolean[][] visite = new boolean[hauteur][largeur];
        Queue<Cell> file = new LinkedList<>();
        Map<Cell, Integer> distance = new HashMap<>();

        file.add(depart);
        visite[depart.getRow()][depart.getColumn()] = true;
        distance.put(depart, 0);

        while (!file.isEmpty()) {
            Cell actuelle = file.poll();
            int distActuelle = distance.get(actuelle);

            for (Cell voisin : obtenirVoisins(carte, actuelle)) {
                if (voisin.isEmpty() && !visite[voisin.getRow()][voisin.getColumn()]) {
                    if (voisin.equals(arrivee)) return distActuelle + 1;
                    visite[voisin.getRow()][voisin.getColumn()] = true;
                    distance.put(voisin, distActuelle + 1);
                    file.add(voisin);
                }
            }
        }
        return -1;
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

    public void setAnticipation(int anticipation) {
        this.anticipation = anticipation;
    }
}
