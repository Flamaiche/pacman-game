/**
 * Ce logiciel est distribué à des fins éducatives.
 *
 * Il est fourni "tel quel", sans garantie d'aucune sorte, explicite
 * ou implicite, notamment sans garantie de qualité marchande, d'adéquation
 * à un usage particulier et d'absence de contrefaçon.
 * En aucun cas, les auteurs ou titulaires du droit d'auteur ne seront
 * responsables de tout dommage, réclamation ou autre responsabilité, que ce
 * soit dans le cadre d'un contrat, d'un délit ou autre, en provenance de,
 * consécutif à ou en relation avec le logiciel ou son utilisation, ou avec
 * d'autres éléments du logiciel.
 *
 * (c) 2022-2025 Romain Wallon - Université d'Artois.
 * Tous droits réservés.
 */

package fr.univartois.butinfo.r304.pacman.model;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.univartois.butinfo.r304.pacman.model.map.Carte;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.butinfo.r304.pacman.view.ISpriteStore;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import javafx.animation.AnimationTimer;

/**
 * La classe {@link PacmanGame} gère une partie du jeu Pac-Man.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class PacmanGame {

    /**
     * Le génarateur de nombres aléatoires utilisé dans le jeu.
     */
    public static final Random RANDOM = new Random();

    /**
     * La vitesse de déplacement du joueur (en pixels/s).
     */
    public static final int DEFAULT_SPEED = 150;

    /**
     * La largeur de la carte du jeu (en pixels).
     */
    private final int width;

    /**
     * La hauteur de la carte du jeu (en pixels).
     */
    private final int height;

    /**
     * L'instance de {@link ISpriteStore} permettant de créer les {@link Sprite} du jeu.
     */
    private final ISpriteStore spriteStore;

    /**
     * La carte du jeu.
     */
    private GameMap gameMap;

    /**
     * Le personnage du joueur.
     */
    // TODO Adaptez le type de cet attribut pour correspondre à votre implémentation.
    private IAnimated player;

    /**
     * Le nombre de fantômes initialement dans le jeu.
     */
    private int nbGhosts;

    /**
     * Le nombre de pac-gommes initialement dans le jeu.
     */
    private int nbGums;

    /**
     * La liste des objets mobiles du jeu.
     */
    private final List<IAnimated> movingObjects = new CopyOnWriteArrayList<>();

    /**
     * La liste des objets animés du jeu.
     */
    private final List<IAnimated> animatedObjects = new CopyOnWriteArrayList<>();

    /**
     * L'animation du jeu, qui s'assure que les différents objets évoluent.
     */
    private final AnimationTimer animation = new GameAnimation(movingObjects, animatedObjects);

    /**
     * Le contrôleur du jeu.
     */
    private IPacmanController controller;

    /**
     * Crée une nouvelle instance de PacmanGame.
     *
     * @param gameWidth La largeur de la carte du jeu.
     * @param gameHeight La hauteur de la carte du jeu.
     * @param spriteStore L'instance de {@link ISpriteStore} permettant de créer les
     *        {@link Sprite} du jeu.
     * @param nbGhosts Le nombre de fantômes dans le jeu.
     */
    public PacmanGame(int gameWidth, int gameHeight, ISpriteStore spriteStore, int nbGhosts) {
        this.width = gameWidth;
        this.height = gameHeight;
        this.spriteStore = spriteStore;
        this.nbGhosts = nbGhosts;
    }

    /**
     * Modifie le contrôleur avec lequel interagir pour mettre à jour l'affichage.
     *
     * @param controller Le contrôleur avec lequel interagir.
     */
    public void setController(IPacmanController controller) {
        this.controller = controller;
    }

    /**
     * Donne l'instance de {@link ISpriteStore} permettant de créer les {@link Sprite} du
     * jeu.
     *
     * @return L'instance de {@link ISpriteStore} permettant de créer les {@link Sprite}
     *         du jeu.
     */
    public ISpriteStore getSpriteStore() {
        return spriteStore;
    }

    /**
     * Donne la largeur de la carte du jeu (en pixels).
     *
     * @return La largeur de la carte du jeu.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Donne la hauteur de la carte du jeu (en pixels).
     *
     * @return La hauteur de la carte du jeu.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Prépare une partie de Pac-Man avant qu'elle ne démarre.
     */
    public void prepare() {
        gameMap = createMap();
        controller.prepare(gameMap);
    }

    /**
     * Crée la carte du jeu, en respectant les dimensions de la fenêtre.
     *
     * @return La carte du jeu ayant été créée.
     */
    private GameMap createMap() {
        int nbCellLargeur = width / ISpriteStore.DEFAULT_SPRITE_SIZE;
        int nbCellHauteur = width / ISpriteStore.DEFAULT_SPRITE_SIZE;
        return Carte.createMap(nbCellLargeur, nbCellHauteur);
    }

    /**
     * Démarre la partie de Pac-Man.
     */
    public void start() {
        prepare();
        createAnimated();
        initStatistics();
        animation.start();
    }

    /**
     * Crée les différents objets animés présents au début de la partie.
     */
    private void createAnimated() {
        // On commence par enlever tous les éléments mobiles encore présents.
        clearAnimated();

        // TODO On crée le joueur sur la carte.
        player = null;
        animatedObjects.add(player);
        spawnAnimated(player);

        // On crée ensuite les fantômes sur la carte.
        for (int i = 0; i < nbGhosts; i++) {
            // TODO Créez un fantôme en utilisant votre implémentation.
            IAnimated ghost = null;
            ghost.setHorizontalSpeed(DEFAULT_SPEED * 0.8);
            animatedObjects.add(ghost);
            spawnAnimated(ghost);
        }
    }

    /**
     * Initialise les statistiques de cette partie.
     */
    private void initStatistics() {
        // TODO Lier les propriétés du joueur avec celles du contrôleur.
        controller.bindLife(null);
        controller.bindScore(null);
    }

    /**
     * Fait apparaître un objet animé sur la carte du jeu.
     *
     * @param animated L'objet à faire apparaître.
     */
    private void spawnAnimated(IAnimated animated) {
        List<Cell> spawnableCells = gameMap.getEmptyCells();
        if (!spawnableCells.isEmpty()) {
            Cell cell = spawnableCells.get(RANDOM.nextInt(spawnableCells.size()));
            animated.setX(cell.getColumn() * spriteStore.getSpriteSize());
            animated.setY(cell.getRow() * spriteStore.getSpriteSize());
            addMoving(animated);
        }
    }

    /**
     * Déplace le personnage du joueur vers le haut.
     */
    public void moveUp() {
        stopMoving();
        player.setVerticalSpeed(-DEFAULT_SPEED);
    }

    /**
     * Déplace le personnage du joueur vers la droite.
     */
    public void moveRight() {
        stopMoving();
        player.setHorizontalSpeed(DEFAULT_SPEED);
    }

    /**
     * Déplace le personnage du joueur vers le bas.
     */
    public void moveDown() {
        stopMoving();
        player.setVerticalSpeed(DEFAULT_SPEED);
    }

    /**
     * Déplace le personnage du joueur vers la gauche.
     */
    public void moveLeft() {
        stopMoving();
        player.setHorizontalSpeed(-DEFAULT_SPEED);
    }

    /**
     * Arrête le déplacement du joueur.
     */
    public void stopMoving() {
        player.setVerticalSpeed(0);
        player.setHorizontalSpeed(0);
    }

    /**
     * Récupére la cellule correspondant à la position d'un objet mobile.
     * Il s'agit de la cellule sur laquelle l'objet en question occupe le plus de place.
     *
     * @param animated L'objet mobile dont la cellule doit être récupérée.
     *
     * @return La cellule occupée par l'objet mobile.
     */
    private Cell getCellOf(IAnimated animated) {
        // On commence par récupérer la position du centre de l'objet.
        int midX = animated.getX() + (animated.getWidth() / 2);
        int midY = animated.getY() + (animated.getHeight() / 2);
        return getCellAt(midX, midY);
    }

    /**
     * Donne la cellule à la position donnée sur la carte.
     *
     * @param x La position en x de la cellule.
     * @param y La position en y de la cellule.
     *
     * @return La cellule à la position donnée.
     */
    public Cell getCellAt(int x, int y) {
        // On traduit cette position en position dans la carte.
        int row = y / spriteStore.getSpriteSize();
        int column = x / spriteStore.getSpriteSize();

        // On récupère enfin la cellule à cette position dans la carte.
        return gameMap.getAt(row, column);
    }

    /**
     * Ajoute un objet mobile dans le jeu.
     *
     * @param object L'objet à ajouter.
     */
    public void addMoving(IAnimated object) {
        movingObjects.add(object);
        addAnimated(object);
    }

    /**
     * Ajoute un objet animé dans le jeu.
     *
     * @param object L'objet à ajouter.
     */
    public void addAnimated(IAnimated object) {
        animatedObjects.add(object);
        controller.addAnimated(object);
        object.onSpawn();
    }

    /**
     * Supprime un objet animé dans le jeu.
     *
     * @param object L'objet à supprimer.
     */
    public void removeAnimated(IAnimated object) {
        animatedObjects.remove(object);
        object.onDespawn();
        object.onDestruction();
    }

    /**
     * Supprime tous les objets animé dans le jeu.
     */
    private void clearAnimated() {
        for (IAnimated animated : animatedObjects) {
            animated.onDespawn();
            animated.onDestruction();
        }
        animatedObjects.clear();
    }

    /**
     * Indique que le joueur a mangé une pac-gomme.
     *
     * @param gum La pac-gomme qui a été mangée.
     */
    public void pacGumEaten(IAnimated gum) {
        nbGums--;
        removeAnimated(gum);

        if (nbGums <= 0) {
            gameOver("YOU WIN!");
        }
    }

    /**
     * Termine la partie lorsque le joueur est tué.
     */
    public void playerIsDead() {
        gameOver("YOU HAVE BEEN KILLED!");
    }

    /**
     * Termine la partie en cours.
     *
     * @param message Le message indiquant le résultat de la partie.
     */
    private void gameOver(String message) {
        animation.stop();
        controller.gameOver(message);
    }

}
