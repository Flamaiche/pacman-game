package fr.univartois.butinfo.r304.pacman.model.map;

public interface ILevelFactory {

    IMap chooseMap();
    String getLevelName();
    IMap[] getAllMap();
}
