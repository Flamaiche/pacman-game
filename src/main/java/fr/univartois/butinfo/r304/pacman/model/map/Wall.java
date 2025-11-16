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

package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.view.Sprite;

/**
 * La classe {@link Wall} représente un mur du labyrinthe dans lequel évolue Pac-Man.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class Wall {

    /**
     * Le sprite représentant ce mur sur la carte.
     */
    private final Sprite sprite;

    /**
     * Crée une nouvelle instance de Wall.
     *
     * @param sprite Le sprite représentant le mur sur la carte.
     */
    public Wall(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * Donne le sprite représentant ce mur sur la carte.
     *
     * @return Le sprite représentant ce mur sur la carte.
     */
    public Sprite getSprite() {
        return sprite;
    }

}
