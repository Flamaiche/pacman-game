package fr.univartois.butinfo.r304.pacman.model.animated.deplacements;

import fr.univartois.butinfo.r304.pacman.model.animated.Fantome;

import java.util.Random;

public class DeplacementAleatoire implements IStrategieDeplacement {
        private final Fantome fantome;
        private final Random random = new Random();
        private int compteurDeplacement =0;
        private static final int DELAI = 50;

        public DeplacementAleatoire(Fantome fantome) {
            this.fantome = fantome;
        }

        @Override
        public void mouvement() {
            compteurDeplacement++;
            if(compteurDeplacement >= DELAI){
                choixDirection();
                compteurDeplacement = 0;
            }

        }

        private void choixDirection() {
            double vitesse = 50 + random.nextDouble() * 100;

            if(random.nextBoolean()){
                fantome.setHorizontalSpeed(vitesse * (random.nextBoolean() ? 1 : -1));
                fantome.setVerticalSpeed(0);
            } else {
                fantome.setVerticalSpeed(vitesse * (random.nextBoolean() ? 1 : -1));
                fantome.setHorizontalSpeed(0);
        }
    }

    public void reset() {
            compteurDeplacement = 0;
    }

}
