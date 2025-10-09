package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.animated.Fantome;

public class DeplacementVersJoueurA3 implements IStrategieDeplacement {
    private final Fantome fantome;

    public DeplacementVersJoueurA3(Fantome fantome) {
        this.fantome = fantome;
    }

    @Override
    public void mouvement() {
        DeplacementVersJoueur dvj = new DeplacementVersJoueur(fantome);
        dvj.mouvement(3);
    }

}
