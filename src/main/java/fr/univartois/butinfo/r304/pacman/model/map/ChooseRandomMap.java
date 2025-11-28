package fr.univartois.butinfo.r304.pacman.model.map;

import fr.univartois.butinfo.r304.pacman.model.map.maps.*;
import fr.univartois.dpprocessor.designpatterns.singleton.Instance;
import fr.univartois.dpprocessor.designpatterns.singleton.SingletonDesignPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SingletonDesignPattern()
public final class ChooseRandomMap {

    private final List<ILevelFactory> factoriesMaps;
    private int score = 6; // commencer au niveau medium
    private final int SCORE_PASSAGE_LEVEL = 2; // gagner 2 fois par niveau pour être au suivant
    private final int SCORE_WIN_MAP = 2;
    private IMap actualMap;

    @Instance
    private final static ChooseRandomMap instance = new ChooseRandomMap();

    @Instance
    public static ChooseRandomMap getInstance() {
        return instance;
    }

    private ChooseRandomMap() {
        this.factoriesMaps = new ArrayList<>();

        factoriesMaps.add(new LevelEasyFactory());
        factoriesMaps.add(new LevelMediumFactory());
        factoriesMaps.add(new LevelHardFactory());

        System.out.println("Niveau de difficulté disponible :");
        for (ILevelFactory factory : factoriesMaps) {
            System.out.println("- " + factory.getLevelName());
            for (IMap map : factory.getAllMap()) {
                System.out.println("   * " + map.getClass().getSimpleName());
            }
        }
    }

    public IMap chooseMap() {
        int levelIndex = Math.min(score / (SCORE_PASSAGE_LEVEL * SCORE_WIN_MAP), factoriesMaps.size() - 1);
        ILevelFactory factory = factoriesMaps.get(levelIndex);
        System.out.println("Niveau de difficulté choisi : " + factory.getLevelName());
        actualMap = factory.chooseMap();
        return actualMap;
    }

    public void incrementScore() {
        score+=SCORE_WIN_MAP;
        if (score > SCORE_PASSAGE_LEVEL * SCORE_WIN_MAP * factoriesMaps.size()) {
            score = SCORE_PASSAGE_LEVEL * SCORE_WIN_MAP * factoriesMaps.size();
        }
    }

    public void decrementScore() {
        score-=SCORE_WIN_MAP;
        if (score < 0) {
            score = 0;
        }
    }

    public IMap getActualMap() {
        return actualMap;
    }
}
