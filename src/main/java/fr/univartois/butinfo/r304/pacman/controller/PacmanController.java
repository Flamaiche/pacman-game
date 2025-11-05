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

package fr.univartois.butinfo.r304.pacman.controller;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.IPacmanController;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import javafx.beans.binding.IntegerExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * La classe PacmanController fournit le contrôleur permettant de jouer au jeu
 * Pac-Man dans une interface JavaFX.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class PacmanController implements IPacmanController {

    /**
     * La partie du jeu Pac-Man en cours.
     */
    private PacmanGame game;

    /**
     * La fenêtre dans laquelle se déroule le jeu.
     */
    private Stage stage;

    /**
     * Le conteneur affichant l'arrière-plan du jeu.
     */
    @FXML
    private GridPane backgroundPane;

    /**
     * Le conteneur affichant les objets animés du jeu.
     */
    @FXML
    private Pane animatedPane;

    /**
     * Le label affichant un message à l'utilisateur.
     */
    @FXML
    private Label message;

    /**
     * Le label affichant le score du joueur.
     */
    @FXML
    private Label score;

    /**
     * Le label affichant les vies restantes du joueur.
     */
    @FXML
    private Label life;

    /**
     * Un booléen permettant de savoir si la partie a démarré.
     * Il permet de temporiser le démarrage du jeu, en attendant que l'utilisateur appuie
     * sur une touche de son clavier.
     */
    private boolean started = false;

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.butinfo.r304.pacman.model.IPacmanController#setGame(fr.univartois.
     * butinfo.r304.pacman.model.PacmanGame)
     */
    @Override
    public void setGame(PacmanGame game) {
        this.game = game;
    }

    /**
     * Associe à ce contrôleur la fenêtre affichant le jeu.
     *
     * @param stage La fenêtre affichant le jeu.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        addKeyListeners();
    }

    /**
     * Ajoute les écouteurs de touches pour le jeu.
     */
    private void addKeyListeners() {
        // L'appui (bref) sur une touche permet dans un premier temps de lancer le jeu.
        stage.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            if (!started) {
                // La partie démarre à la première touche appuyée.
                started = true;
                message.setVisible(false);
                game.start();
            }
        });

        // Lorsque l'utilisateur appuie sur une flèche, on déplace son personnage.
        stage.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (started) {
                if (e.getCode() == KeyCode.UP) {
                    game.moveUp();

                } else if (e.getCode() == KeyCode.LEFT) {
                    game.moveLeft();

                } else if (e.getCode() == KeyCode.DOWN) {
                    game.moveDown();

                } else if (e.getCode() == KeyCode.RIGHT) {
                    game.moveRight();
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.butinfo.r304.pacman.model.IPacmanController#prepare(fr.univartois.
     * butinfo.r304.pacman.model.map.GameMap)
     */
    @Override
    public void prepare(GameMap map) {
        createBackground(map);
    }

    /**
     * Crée l'arrière-plan du jeu.
     *
     * @param map La carte du jeu à afficher.
     */
    private void createBackground(GameMap map) {
        backgroundPane.getChildren().clear();
        for (int row = 0; row < map.getHeight(); row++) {
            for (int column = 0; column < map.getWidth(); column++) {
                Cell cell = map.getAt(row, column);
                ImageView view = new ImageView();
                view.imageProperty().bind(cell.imageProperty());
                backgroundPane.add(view, column, row);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.butinfo.r304.pacman.model.IPacmanController#bindScore(javafx.beans.
     * binding.IntegerExpression)
     */
    @Override
    public void bindScore(IntegerExpression scoreProperty) {
        score.textProperty().bind(scoreProperty.asString());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.butinfo.r304.pacman.model.IPacmanController#bindLife(javafx.beans.
     * binding.IntegerExpression)
     */
    @Override
    public void bindLife(IntegerExpression lifeProperty) {
        life.textProperty().bind(lifeProperty.asString());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.butinfo.r304.pacman.model.IPacmanController#addAnimated(fr.univartois
     * .butinfo.r304.pacman.model.IAnimated)
     */
    @Override
    public void addAnimated(IAnimated animated) {
        // On affiche l'objet au bon endroit.
        ImageView view = new ImageView();
        view.xProperty().bind(animated.xProperty());
        view.yProperty().bind(animated.yProperty());
        view.rotateProperty().bind(animated.rotateProperty());
        animatedPane.getChildren().add(view);

        // Lorsque le sprite de l'objet change, son image doit changer également.
        view.imageProperty().bind(animated.imageProperty());

        // Lorsque l'objet est consommé, il n'est plus affiché.
        animated.destroyedProperty().addListener((p, o, n) -> {
            if (n == Boolean.TRUE) {
                animatedPane.getChildren().remove(view);
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IPacmanController#gameOver(java.lang.
     * String)
     */
    @Override
    public void gameOver(String endMessage) {
        started = false;
        message.setVisible(true);
        message.setText(endMessage + "\nPRESS ANY KEY TO RESTART...");
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IPacmanController#reset()
     */
    @Override
    public void reset() {
        animatedPane.getChildren().clear();
    }

}
