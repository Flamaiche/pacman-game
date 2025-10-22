package fr.univartois.butinfo.r304.pacman.model.map.carte;

import fr.univartois.butinfo.r304.pacman.model.map.Cell;
import fr.univartois.butinfo.r304.pacman.model.map.GameMap;
import fr.univartois.butinfo.r304.pacman.model.map.ICarte;
import fr.univartois.butinfo.r304.pacman.model.map.Wall;
import fr.univartois.butinfo.r304.pacman.view.Sprite;
import fr.univartois.butinfo.r304.pacman.view.SpriteStore;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyDesignPattern;
import fr.univartois.dpprocessor.designpatterns.strategy.StrategyParticipant;

@StrategyDesignPattern(strategy = ICarte.class, participant = StrategyParticipant.IMPLEMENTATION)
public class Carte_2 extends Carte {

    public Carte_2() {}

    @Override
    public GameMap createMap(int largeur, int hauteur) {
        GameMap map = super.createMap(largeur, hauteur);
        ajoutMursInterieurs(map, largeur, hauteur);
        return map;
    }

    private void ajoutMursInterieurs(GameMap map, int largeur, int hauteur) {
        SpriteStore spriteStore = new SpriteStore();
        Wall wall = new Wall(spriteStore.getSprite("wall"));
        Sprite path = spriteStore.getSprite("path");

        int offset = 2;
        int couche = 0;

        while (offset < Math.min(largeur, hauteur) / 2 - 2) {

            for (int x = offset; x < largeur - offset; x++) {
                if (x != offset + 1 || couche % 2 == 0)
                    map.setAt(offset, x, new Cell(wall));
            }

            for (int y = offset; y < hauteur - offset; y++) {
                if (y != offset + 1 || couche % 2 == 1) // ouverture alternée
                    map.setAt(y, largeur - offset - 1, new Cell(wall));
            }

            for (int x = largeur - offset - 1; x >= offset; x--) {
                if (x != largeur - offset - 2 || couche % 2 == 0)
                    map.setAt(hauteur - offset - 1, x, new Cell(wall));
            }

            for (int y = hauteur - offset - 1; y >= offset; y--) {
                if (y != hauteur - offset - 2 || couche % 2 == 1)
                    map.setAt(y, offset, new Cell(wall));
            }

            offset += 3;
            couche++;
        }

        int centreX = largeur / 2;
        int centreY = hauteur / 2;
        for (int y = centreY - 2; y <= centreY + 2; y++) {
            for (int x = centreX - 2; x <= centreX + 2; x++) {
                map.setAt(y, x, new Cell(path));
            }
        }
    }
}
