package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Fantome;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

import java.util.*;

@StrategyDesignPattern(strategy = IStrategieDeplacement.class, participant = StrategyParticipant.INTERFACE)
public class DeplacementChasseur implements IStrategieDeplacement {

    private final Fantome fantome;
    private final PacmanGame game;
    private final IAnimated pacman;
    private final double vitesse = PacmanGame.DEFAULT_SPEED*0.85;
    private int anticipation = 0;
    private List<Cell> cheminVersPacman = new ArrayList<>();
    private int indexProchaineCellule = 0;

    public DeplacementChasseur(Fantome fantome) {
        this.fantome = fantome;
        this.game = fantome.getGame();
        this.pacman = game.getPlayer();
    }

    public void setAnticipation(int anticipation) {
        this.anticipation = anticipation;
    }

    @Override
    public void mouvement() {
        GameMap carte = game.getGameMap();
        Cell celluleFantome = game.getCellOf(fantome);
        if (celluleFantome == null) return;

        // Recalcul du chemin si nécessaire
        if (cheminVersPacman.isEmpty() || indexProchaineCellule >= cheminVersPacman.size() ||
                celluleFantome.equals(cheminVersPacman.get(indexProchaineCellule))) {
            Cell cible = determinerCelluleCible();
            if (cible != null) {
                cheminVersPacman = reconstruireCheminBFS(carte, celluleFantome, cible);
                indexProchaineCellule = 0;
            }
        }

        // Avancer vers la prochaine cellule
        if (!cheminVersPacman.isEmpty() && indexProchaineCellule < cheminVersPacman.size()) {
            Cell prochaine = cheminVersPacman.get(indexProchaineCellule);
            if (celluleFantome.equals(prochaine)) {
                indexProchaineCellule++;
                if (indexProchaineCellule >= cheminVersPacman.size()) {
                    fantome.setHorizontalSpeed(0);
                    fantome.setVerticalSpeed(0);
                    return;
                }
                prochaine = cheminVersPacman.get(indexProchaineCellule);
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

    private Cell determinerCelluleCible() {
        Cell cellulePacman = game.getCellOf(pacman);
        if (cellulePacman == null) return null;

        double vitessePacman = Math.sqrt(Math.pow(pacman.getHorizontalSpeed(), 2) + Math.pow(pacman.getVerticalSpeed(), 2));
        int anticipationEffective = anticipation + (int) (vitessePacman * 3);

        int ligne = cellulePacman.getRow();
        int colonne = cellulePacman.getColumn();
        int dx = (int) Math.signum(pacman.getHorizontalSpeed());
        int dy = (int) Math.signum(pacman.getVerticalSpeed());

        GameMap carte = game.getGameMap();
        Cell celluleFantome = game.getCellOf(fantome);

        // Si PacMan est très proche, fonce directement vers lui
        int distanceLignes = Math.abs(celluleFantome.getRow() - ligne);
        int distanceColonnes = Math.abs(celluleFantome.getColumn() - colonne);
        if (distanceLignes <= anticipationEffective && distanceColonnes <= anticipationEffective) {
            return cellulePacman;
        }

        // Sinon, projection classique en fonction de l'anticipation
        for (int i = 0; i < anticipationEffective; i++) {
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

    private List<Cell> reconstruireCheminBFS(GameMap carte, Cell depart, Cell arrivee) {
        if (depart.equals(arrivee)) return new ArrayList<>();

        int hauteur = carte.getHeight();
        int largeur = carte.getWidth();
        boolean[][] visite = new boolean[hauteur][largeur];
        Map<Cell, Cell> precedent = new HashMap<>();
        Queue<Cell> queue = new LinkedList<>();

        queue.add(depart);
        visite[depart.getRow()][depart.getColumn()] = true;

        while (!queue.isEmpty()) {
            Cell actuelle = queue.poll();
            if (actuelle.equals(arrivee)) break;

            for (Cell voisin : obtenirVoisins(carte, actuelle)) {
                if (voisin.isEmpty() && !visite[voisin.getRow()][voisin.getColumn()]) {
                    visite[voisin.getRow()][voisin.getColumn()] = true;
                    precedent.put(voisin, actuelle);
                    queue.add(voisin);
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

    private List<Cell> obtenirVoisins(GameMap carte, Cell cellule) {
        List<Cell> voisins = new ArrayList<>();
        int ligne = cellule.getRow(), colonne = cellule.getColumn();
        if (carte.isOnMap(ligne - 1, colonne)) voisins.add(carte.getAt(ligne - 1, colonne));
        if (carte.isOnMap(ligne + 1, colonne)) voisins.add(carte.getAt(ligne + 1, colonne));
        if (carte.isOnMap(ligne, colonne - 1)) voisins.add(carte.getAt(ligne, colonne - 1));
        if (carte.isOnMap(ligne, colonne + 1)) voisins.add(carte.getAt(ligne, colonne + 1));
        return voisins;
    }

    public List<Cell> getCheminVersPacman() {
        return cheminVersPacman;
    }

    public void reset() {
        indexProchaineCellule = 0;
        cheminVersPacman.clear();
    }
}
