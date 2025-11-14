package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.model.map.maps.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChooseRandomMap {

    private final List<ICarte> cartes;
    private final Random random;

    public ChooseRandomMap() {
        this.random = new Random();
        this.cartes = new ArrayList<>();

        this.cartes.add(new Map());
        this.cartes.add(new Map_2());
        this.cartes.add(new Map_4());
        this.cartes.add(new Map_5());


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
        ChooseRandomMap choix = new ChooseRandomMap();
        ICarte carte = choix.choisirMap();
    }
}
