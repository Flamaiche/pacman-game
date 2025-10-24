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

package fr.univartois.butinfo.r304.pacman;

import java.io.IOException;

import fr.univartois.butinfo.r304.pacman.controller.PacmanController;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.map.*;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * La classe PacmanApplication permet de lancer l'implémentation en JavaFX du jeu Pac-Man.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class PacmanApplication extends Application {

    /**
     * La largeur (en pixels) de la fenêtre affichant le jeu.
     */
    private static final int GAME_WIDTH = 1080;

    /**
     * La hauteur (en pixels) de la fenêtre affichant le jeu.
     */
    private static final int GAME_HEIGHT = 720;

    /**
     * Le nombre de fantômes à combattre dans le jeu.
     */
    private static final int NB_GHOSTS = 4;

    /*
     * (non-Javadoc)
     *
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage stage) throws IOException {
        // On commence par charger la vue et son contrôleur.
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/pacman.fxml"));
        Parent viewContent = fxmlLoader.load();
        PacmanController controller = fxmlLoader.getController();
        controller.setStage(stage);


        // Choisir une carte aléatoirement
        ChoisirMapAleatoirement choix = new ChoisirMapAleatoirement();
        ICarte carte = choix.choisirMap();


        // On crée ensuite le jeu, que l'on lie au contrôleur.
        PacmanGame game = new PacmanGame(
                GAME_WIDTH, GAME_HEIGHT, new SpriteStore(), NB_GHOSTS, carte);
        controller.setGame(game);
        game.setController(controller);
        game.prepare();

        // On peut maintenant afficher la scène et la fenêtre.
        Scene scene = new Scene(viewContent, GAME_WIDTH, GAME_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("PacmanFX");
        stage.setResizable(false);
        stage.show();


    }

    /**
     * Exécute l'application JavaFX du jeu Pac-Man.
     *
     * @param args Les arguments de la ligne de commande (dont on ne tient pas compte).
     *
     * @see #launch(String...)
     */
    public static void main(String[] args) {
        launch();
    }

}
