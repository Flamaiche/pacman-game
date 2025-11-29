package fr.univartois.butinfo.r304.pacman.model.bonus;

import fr.univartois.butinfo.r304.pacman.model.animated.PacMan;

public class OpenDoorEffect implements BonusEffectStrategy {

    private final int row;
    private final int column;

    public OpenDoorEffect(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public void apply(PacMan pacMan) {
        if (pacMan.getGame() != null) {
            pacMan.getGame().openDoor(row, column);
        }
    }
}
