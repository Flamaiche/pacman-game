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

package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * La classe {@link AbstractAnimated} fournit une implémentation de base pour tous les
 * objets animés dans le jeu.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public abstract class AbstractAnimated implements IAnimated {

    /**
     * La marge de sécurité pour les obstacles (en pixels).
     *
     * WARNING (hard) : Les marges sont reliés entre elle.
     */
    private static final int MARGIN = 1;

    /**
     * La marge de sécurité pour les collisions (fantome, pac-gomme)
     *
     * WARNING (light) : Les marges sont reliés entre elle.
     */
    private static final int MARGIN_COLLISION = 5;

    /**
     * La marge de pixels pour considérer aligner un IAnimated sur la grille
     *
     * WARNING (hard) : Les marges sont reliés entre elle.
     */
    private static final int ALIGN_TOLERANCE = 4;

    /**
     * Le jeu dans lequel cet objet animé évolue.
     */
    protected final PacmanGame game;

    /**
     * La position en x de cet objet animé.
     */
    protected final DoubleProperty xPosition;

    /**
     * La position en y de cet objet animé.
     */
    protected final DoubleProperty yPosition;

    /**
     * La vitesse horizontale actuelle de cet objet (en pixels/s).
     */
    protected double horizontalSpeed;

    /**
     * La vitesse verticale actuelle de cet objet (en pixels/s).
     */
    protected double verticalSpeed;

    /**
     * Si cet objet animé a été détruit.
     */
    protected final BooleanProperty destroyed;

    /**
     * L'instance de {@link Sprite} représentant cet objet animé.
     */
    protected final ObjectProperty<Sprite> sprite;

    /**
     *
     */
    protected final ObjectProperty<Image> image;

    protected double requestedHorizontalSpeed = 0;
    protected double requestedVerticalSpeed = 0;

    /**
     * Crée une nouvelle instance de AbstractAnimated.
     *
     * @param game Le jeu dans lequel l'objet animé évolue.
     * @param xPosition La position en x initiale de l'objet animé.
     * @param yPosition La position en y initiale de l'objet animé.
     * @param sprite L'instance de {@link Sprite} représentant l'objet animé.
     */
    protected AbstractAnimated(PacmanGame game, double xPosition,
            double yPosition, Sprite sprite) {
        this.game = game;
        this.xPosition = new SimpleDoubleProperty(xPosition);
        this.yPosition = new SimpleDoubleProperty(yPosition);
        this.destroyed = new SimpleBooleanProperty(false);
        this.sprite = new SimpleObjectProperty<>(sprite);
        this.image = new SimpleObjectProperty<>();
        this.image.bind(this.sprite.get().imageProperty());
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#getWidth()
     */
    @Override
    public int getWidth() {
        return sprite.get().getWidth();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#getHeight()
     */
    @Override
    public int getHeight() {
        return sprite.get().getHeight();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#setX(double)
     */
    @Override
    public void setX(double xPosition) {
        this.xPosition.set(xPosition);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#getX()
     */
    @Override
    public int getX() {
        return xPosition.intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#xProperty()
     */
    @Override
    public DoubleProperty xProperty() {
        return xPosition;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#setY(double)
     */
    @Override
    public void setY(double yPosition) {
        this.yPosition.set(yPosition);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#getY()
     */
    @Override
    public int getY() {
        return yPosition.intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#yProperty()
     */
    @Override
    public DoubleProperty yProperty() {
        return yPosition;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#setHorizontalSpeed(double)
     */
    @Override
    public void setHorizontalSpeed(double speed) {
        requestedHorizontalSpeed = speed * getSpeedBoost();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#getHorizontalSpeed()
     */
    @Override
    public double getHorizontalSpeed() {
        return horizontalSpeed;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#setVerticalSpeed(double)
     */
    @Override
    public void setVerticalSpeed(double speed) {
        requestedVerticalSpeed = speed * getSpeedBoost();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#getVerticalSpeed()
     */
    @Override
    public double getVerticalSpeed() {
        return verticalSpeed;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.butinfo.r304.pacman.model.IAnimated#setSprite(fr.univartois.butinfo.
     * r304.pacman.view.Sprite)
     */
    @Override
    public void setSprite(Sprite sprite) {
        this.sprite.set(sprite);

        this.image.unbind();
        this.image.bind(this.sprite.get().imageProperty());
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#getSprite()
     */
    @Override
    public Sprite getSprite() {
        return sprite.get();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#spriteProperty()
     */
    @Override
    public ObjectProperty<Sprite> spriteProperty() {
        return sprite;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#imageProperty()
     */
    @Override
    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#setDestroyed(boolean)
     */
    @Override
    public void setDestroyed(boolean destroyed) {
        this.destroyed.set(destroyed);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#isDestroyed()
     */
    @Override
    public boolean isDestroyed() {
        return destroyed.get();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#destroyedProperty()
     */
    @Override
    public BooleanProperty destroyedProperty() {
        return destroyed;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#onCreation()
     */
    @Override
    public void onCreation() {
        // Par défaut, cette méthode ne fait rien.
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#onSpawn()
     */
    @Override
    public void onSpawn() {
        // Par défaut, cette méthode ne fait rien.
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#onStep(long)
     */
    @Override
    public boolean onStep(long delta) {

        // Si l'objet est aligné avec la grille, on applique la direction demandée.
        if (isAlignedWithGrid(ALIGN_TOLERANCE)) {
            if ((requestedHorizontalSpeed != horizontalSpeed) || (requestedVerticalSpeed != verticalSpeed)) {
                horizontalSpeed = requestedHorizontalSpeed;
                verticalSpeed = requestedVerticalSpeed;
                alignToGrid();
            }
        }

        // On calcule la nouvelle position sur l'axe X.
        int limitMaxX = game.getWidth() - getWidth();
        double newX = xPosition.get() + (horizontalSpeed * delta) / 1000;
        if ((newX < 0) || (newX > limitMaxX)) {
            // L'objet a atteint la limite sur l'axe X.
            return false;
        }

        // On calcule la nouvelle position sur l'axe Y.
        int limitMaxY = game.getHeight() - getHeight();
        double newY = yPosition.get() + (verticalSpeed * delta) / 1000;
        if ((newY < 0) || (newY > limitMaxY)) {
            // L'objet a atteint la limite sur l'axe Y.
            return false;
        }

        // On vérifie s’il y a un mur à la nouvelle position.
        if (isOnWall((int) newX, (int) newY)) {
            // L'objet a atteint un mur.
            return false;
        }

        // Si tout est bon, on applique la nouvelle position.
        xPosition.set(newX);
        yPosition.set(newY);

        return true;
    }

    private double getSpeedBoost() {
        if (this instanceof PacMan pacman && !pacman.getEtat().estVulnerable()) return 2;
        return 1d;
    }

    public void resetSpeed() {
        setHorizontalSpeed(horizontalSpeed);
        setVerticalSpeed(verticalSpeed);
    }

    public void alignToGrid() {
        int cellWidth = game.getCellAt(0, 0).getWidth();
        int cellHeight = game.getCellAt(0, 0).getHeight();

        int alignedX = (int) Math.round((double) getX() / cellWidth) * cellWidth;
        int alignedY = (int) Math.round((double) getY() / cellHeight) * cellHeight;

        setX(alignedX);
        setY(alignedY);
    }


    /**
     * Vérifie si la nouvelle position de l'objet est sur un mur.
     *
     * @param x La nouvelle position en x de l'objet.
     * @param y La nouvelle position en y de l'objet.
     *
     * @return Si la nouvelle position de l'objet est sur un mur.
     */
    private boolean isOnWall(int x, int y) {
        if (game.getCellAt(x, y).getWall() != null) {
            // Le coin supérieur gauche de l'objet a atteint un mur.
            return true;
        }

        if (game.getCellAt(x, y + getHeight() - MARGIN).getWall() != null) {
            // Le coin inférieur gauche de l'objet a atteint un mur.
            return true;
        }

        if (game.getCellAt(x + getWidth() - MARGIN, y).getWall() != null) {
            // Le coin supérieur droit de l'objet a atteint un mur.
            return true;
        }

        if (game.getCellAt(x + getWidth() - MARGIN, y + getHeight() - MARGIN).getWall() != null) {
            // Le coin inférieur droit de l'objet a atteint un mur.
            return true;
        }

        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.butinfo.r304.pacman.model.IAnimated#isCollidingWith(fr.univartois.
     * butinfo.r304.pacman.model.IAnimated)
     */
    @Override
    public boolean isCollidingWith(IAnimated other) {
        if (isDestroyed() || other.isDestroyed()) return false;

        Rectangle pacmanRect = new Rectangle(
                getX() + MARGIN_COLLISION,
                getY() + MARGIN_COLLISION,
                getWidth() - 2 * MARGIN_COLLISION,
                getHeight() - 2 * MARGIN_COLLISION
        );

        Rectangle otherRect = new Rectangle(other.getX(), other.getY(), other.getWidth(), other.getHeight());
        return pacmanRect.intersects(otherRect.getBoundsInLocal());
    }
    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#onDespawn()
     */
    @Override
    public void onDespawn() {
        // Par défaut, cette méthode ne fait rien.
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#onDestruction()
     */
    @Override
    public void onDestruction() {
        setDestroyed(true);
        sprite.get().destroy();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.r304.pacman.model.IAnimated#self()
     */
    @Override
    public IAnimated self() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            // Les deux objets sont forcément différents.
            return false;
        }

        if (obj == this) {
            // Les deux objets sont strictement identiques.
            return true;
        }

        if (obj instanceof IAnimated other) {
            // On compare les "vrais objets".
            return other.self() == self();
        }

        // L'objet donné n'est pas d'une classe compatible.
        return false;
    }

    public void onCollisionWith(IAnimated other) {
        // ici on ne fait rien du tout car sinon appel recursive
        // l'appel est fait dans les sous classes, ainsi le type dynamique est pris en compte
    }

    public boolean isAlignedWithGrid(int alignTolerance) {
        int cellWidth = game.getCellAt(0, 0).getWidth();
        int cellHeight = game.getCellAt(0, 0).getHeight();

        int modX = getX() % cellWidth;
        int modY = getY() % cellHeight;

        return (modX <= alignTolerance || modX >= cellWidth - alignTolerance)
                && (modY <= alignTolerance || modY >= cellHeight - alignTolerance);
    }


}
