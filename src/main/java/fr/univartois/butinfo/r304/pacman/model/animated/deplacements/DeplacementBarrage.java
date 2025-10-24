package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.Fantome;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

import java.util.*;

@StrategyDesignPattern(strategy = IStrategieDeplacement.class, participant = StrategyParticipant.IMPLEMENTATION)
public class DeplacementBarrage implements IStrategieDeplacement {

    private final Fantome fantome;
    private final PacmanGame game;
    private final IAnimated pacman;
    private final double vitesse = PacmanGame.DEFAULT_SPEED*0.85;

    private List<Fantome> autresFantomes;
    private List<Cell> cheminBarrage = new ArrayList<>();
    private int indexProchaineCellule = 0;

    private double lastDxPacman = 0;
    private double lastDyPacman = 0;

    public DeplacementBarrage(Fantome fantome) {
        this.fantome = fantome;
        this.game = fantome.getGame();
        this.pacman = game.getPlayer();
        this.autresFantomes = rechercherAutresFantomes();
    }

    private List<Fantome> rechercherAutresFantomes() {
        List<Fantome> fantomes = new ArrayList<>();
        for (IAnimated obj : game.getMovingObjects()) {
            if (obj instanceof Fantome f && f != this.fantome) fantomes.add(f);
        }
        return fantomes;
    }

    @Override
    public void mouvement() {
        GameMap carte = game.getGameMap();
        Cell celluleFantome = game.getCellOf(fantome);
        if (celluleFantome == null) return;

        if (directionPacmanChangee()) {
            cheminBarrage.clear();
            indexProchaineCellule = 0;
        }

        if (cheminBarrage.isEmpty() || indexProchaineCellule >= cheminBarrage.size()
                || celluleFantome.equals(cheminBarrage.get(indexProchaineCellule))) {

            Cell cible = determinerCelluleBarrage(carte, celluleFantome);
            if (cible == null) return;

            cheminBarrage = reconstruireCheminBFS(carte, celluleFantome, cible);
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

    private boolean directionPacmanChangee() {
        double dx = Math.signum(pacman.getHorizontalSpeed());
        double dy = Math.signum(pacman.getVerticalSpeed());
        if (dx != lastDxPacman || dy != lastDyPacman) {
            lastDxPacman = dx;
            lastDyPacman = dy;
            return true;
        }
        return false;
    }

    private Cell determinerCelluleBarrage(GameMap carte, Cell celluleFantome) {
        Cell cellulePacman = game.getCellOf(pacman);
        if (cellulePacman == null) return null;

        double dist = distance(celluleFantome, cellulePacman);
        // Plus il est loin, plus il anticipe
        int anticipation = Math.min(15, Math.max(4, (int) (dist / 2)));

        int dxPac = (int) Math.signum(pacman.getHorizontalSpeed());
        int dyPac = (int) Math.signum(pacman.getVerticalSpeed());

        Fantome chasseur = trouverChasseur();
        List<Cell> cheminChasseur = chasseur != null ? getCheminChasseur(chasseur) : new ArrayList<>();

        Cell embuscadeGauche = projectionEmbuscade(carte, cellulePacman, dxPac, dyPac, anticipation, true);
        Cell embuscadeDroite = projectionEmbuscade(carte, cellulePacman, dxPac, dyPac, anticipation, false);

        Cell cible = choisirEmbuscade(carte, celluleFantome, chasseur, cheminChasseur, embuscadeGauche, embuscadeDroite);

        return (cible != null) ? cible : cellulePacman;
    }

    private Cell projectionEmbuscade(GameMap carte, Cell pacman, int dx, int dy, int anticipation, boolean gauche) {
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

    private Fantome trouverChasseur() {
        Fantome chasseur = null;
        double best = Double.MAX_VALUE;
        for (Fantome f : autresFantomes) {
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

    private List<Cell> getCheminChasseur(Fantome chasseur) {
        try {
            if (chasseur.getDeplacementCurrent() instanceof DeplacementChasseur strat) {
                return strat.getCheminVersPacman();
            } else if (chasseur.getDeplacementCurrent() instanceof DeplacementBarrage strat) {
                return strat.getCheminBarrage();
            }
        } catch (Exception ignored) {}
        return new ArrayList<>();
    }

    private Cell choisirEmbuscade(GameMap carte, Cell celluleFantome, Fantome chasseur,
                                  List<Cell> cheminChasseur, Cell gauche, Cell droite) {

        Cell cChasseur = (chasseur != null) ? game.getCellOf(chasseur) : null;
        double distG = (gauche != null && cChasseur != null) ? distance(gauche, cChasseur) : 0;
        double distD = (droite != null && cChasseur != null) ? distance(droite, cChasseur) : 0;

        Cell prioritaire = (distG > distD) ? gauche : droite;
        Cell secondaire = (distG > distD) ? droite : gauche;

        List<Cell> chemin1 = reconstruireCheminBFS(carte, celluleFantome, prioritaire);
        if (!chemin1.isEmpty() && !cheminChevaucheChasseur(chemin1, cheminChasseur)) return prioritaire;

        List<Cell> chemin2 = reconstruireCheminBFS(carte, celluleFantome, secondaire);
        if (!chemin2.isEmpty() && !cheminChevaucheChasseur(chemin2, cheminChasseur)) return secondaire;

        return null;
    }

    private boolean cheminChevaucheChasseur(List<Cell> chemin, List<Cell> cheminChasseur) {
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

    private List<Cell> reconstruireCheminBFS(GameMap carte, Cell depart, Cell arrivee) {
        if (depart == null || arrivee == null || depart.equals(arrivee)) return new ArrayList<>();

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

    private List<Cell> obtenirVoisins(GameMap carte, Cell cellule) {
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

    public List<Cell> getCheminBarrage() {
        return cheminBarrage;
    }

    public void reset() {
        indexProchaineCellule = 0;
        lastDxPacman = 0;
        lastDyPacman = 0;
        cheminBarrage.clear();
    }
}
