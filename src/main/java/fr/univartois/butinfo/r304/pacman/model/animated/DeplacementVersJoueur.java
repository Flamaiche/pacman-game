package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;

import java.util.*;

public class DeplacementVersJoueur implements IStrategieDeplacement {

    private final Fantome fantome;
    private final PacmanGame game;
    private final IAnimated pacman;
    private final Random random = new Random();

    private int compteurDeplacement = 0;
    private static final int DELAI = 50;

    public DeplacementVersJoueur(Fantome fantome) {
        this.fantome = fantome;
        this.game = fantome.getGame();
        this.pacman = game.getPlayer();
    }

    @Override
    public void mouvement() {
        mouvement(0);
    }

    public void mouvement(int anticipation) {
        compteurDeplacement++;
        if (compteurDeplacement >= DELAI) {
            choisirDirection(anticipation);
            compteurDeplacement = 0;
        }
    }

    private void choisirDirection(int anticipation) {
        GameMap carte = game.getGameMap();
        Cell celluleFantome = game.getCellOf(fantome);
        if (celluleFantome == null) return;

        Cell celluleCible = determinerCelluleCible(anticipation);
        Cell prochaineCellule = trouverProchaineCelluleSurChemin(carte, celluleFantome, celluleCible);

        if (prochaineCellule == null) {
            celluleCible = determinerCelluleCible(0);
            prochaineCellule = trouverProchaineCelluleSurChemin(carte, celluleFantome, celluleCible);
            if (prochaineCellule == null) return;
        }

        int deltaX = prochaineCellule.getColumn() - celluleFantome.getColumn();
        int deltaY = prochaineCellule.getRow() - celluleFantome.getRow();
        double vitesse = 50 + random.nextDouble() * 100;

        if (deltaX != 0) {
            fantome.setHorizontalSpeed(deltaX > 0 ? vitesse : -vitesse);
            fantome.setVerticalSpeed(0);
        } else if (deltaY != 0) {
            fantome.setVerticalSpeed(deltaY > 0 ? vitesse : -vitesse);
            fantome.setHorizontalSpeed(0);
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

    private Cell trouverProchaineCelluleSurChemin(GameMap carte, Cell depart, Cell arrivee) {
        int hauteur = carte.getHeight();
        int largeur = carte.getWidth();
        boolean[][] visite = new boolean[hauteur][largeur];
        Map<Cell, Cell> parent = new HashMap<>();
        List<Cell> file = new ArrayList<>();

        file.add(depart);
        visite[depart.getRow()][depart.getColumn()] = true;

        for (int i = 0; i < file.size(); i++) {
            Cell celluleActuelle = file.get(i);
            if (celluleActuelle.equals(arrivee))
                return reconstruirePremiereEtape(depart, arrivee, parent);

            for (Cell voisin : obtenirVoisins(carte, celluleActuelle)) {
                if (!visite[voisin.getRow()][voisin.getColumn()] && voisin.isEmpty()) {
                    visite[voisin.getRow()][voisin.getColumn()] = true;
                    parent.put(voisin, celluleActuelle);
                    file.add(voisin);
                }
            }
        }
        return null;
    }

    private Cell reconstruirePremiereEtape(Cell depart, Cell arrivee, Map<Cell, Cell> parent) {
        Cell actuelle = arrivee, precedente = null;
        while (actuelle != null && !actuelle.equals(depart)) {
            precedente = actuelle;
            actuelle = parent.get(actuelle);
        }
        return precedente;
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
}