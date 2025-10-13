package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.model.map.carte.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChoisirMapAleatoirement {

    private final List<ICarte> cartes;
    private final Random random;

    public ChoisirMapAleatoirement() {
        this.random = new Random();
        this.cartes = new ArrayList<>();

        this.cartes.add(new Carte());
        this.cartes.add(new Carte_2());
        this.cartes.add(new Carte_3());

        System.out.println("Cartes disponibles :");
        for (ICarte c : cartes) {
            System.out.println("- " + c.getClass().getSimpleName());
        }
    }


    public ICarte choisirMap() {
        int index = random.nextInt(cartes.size());
        ICarte choisie = cartes.get(index);
        System.out.println("Carte choisie : " + choisie.getClass().getSimpleName());
        return choisie;
    }

    public static void main(String[] args) {
        ChoisirMapAleatoirement choix = new ChoisirMapAleatoirement();
        ICarte carte = choix.choisirMap();
    }
}
