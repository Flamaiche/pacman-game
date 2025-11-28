package fr.univartois.butinfo.r304.pacman.model.map.factoryMap;

import fr.univartois.butinfo.r304.pacman.model.map.ILevelFactory;
import fr.univartois.butinfo.r304.pacman.model.map.IMap;
import fr.univartois.butinfo.r304.pacman.model.map.maps.*;

import java.util.Random;

public class LevelMediumFactory implements ILevelFactory {

    private IMap[] map;
    private Random random = new Random();

    public LevelMediumFactory() {
        this.map = new IMap[]{
                new Map_2().getInstance(),
                new Map_3().getInstance(),
                new Map_4().getInstance()
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
        return "LEVEL MEDIUM";
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
