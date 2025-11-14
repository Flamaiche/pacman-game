package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.model.map.maps.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChooseRandomMap {

    private final List<IMap> maps;
    private final Random random;

    public ChooseRandomMap() {
        this.random = new Random();
        this.maps = new ArrayList<>();

        this.maps.add(new Map());
        this.maps.add(new Map_2());
        this.maps.add(new Map_4());
        this.maps.add(new Map_5());


        System.out.println("Cartes disponibles :");
        for (IMap map : maps) {
            System.out.println("- " + map.getClass().getSimpleName());
        }
    }


    public IMap chooseMap() {
        int index = random.nextInt(maps.size());
        IMap choose = maps.get(index);
        System.out.println("Carte choose : " + choose.getClass().getSimpleName());
        return choose;
    }

    public static void main(String[] args) {
        ChooseRandomMap choice = new ChooseRandomMap();
        IMap map = choice.chooseMap();
    }
}
