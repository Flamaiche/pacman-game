package fr.univartois.butinfo.r304.pacman.model.animated;

public enum CouleurFantome {
    RED("red"),
    PINK("pink"),
    BLUE("blue"),
    ORANGE("orange");

    private final String folderName;

    CouleurFantome(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }
}

