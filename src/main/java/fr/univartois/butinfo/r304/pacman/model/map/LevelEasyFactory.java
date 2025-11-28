package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.model.map.maps.EasyMap;
import fr.univartois.butinfo.r304.pacman.model.map.maps.Map;

import java.util.Random;

public class LevelEasyFactory implements ILevelFactory {

    private IMap [] map;
    private Random random = new Random();

    public LevelEasyFactory() {
        this.map = new IMap[]{
                new EasyMap().getInstance(),
                new Map().getInstance()
        };
    }

    @Override
    public IMap chooseMap() {
        int index = random.nextInt(map.length);
        IMap choose = map[index];
        System.out.println("Carte choose : " + choose.getClass().getSimpleName());
        return choose;
    }

    @Override
    public String getLevelName() {
        return "LEVEL EASY";
    }

    @Override
    public IMap[] getAllMap() {
        return map;
    }

    @Override
    public String toString() {
        return getLevelName();
    }

}
