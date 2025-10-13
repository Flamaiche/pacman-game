package fr.univartois.butinfo.r304.pacman.model.animated;

public enum Etat {
    VULNERABLE(true, false),
    INVULNERABLE(false, false),
    PRESQUE_INVULNERABLE(false, false),
    MORT(false, true);

    private boolean estVulnerable;

    private boolean estMort;

    private final static int DUREE_ETAT = 200; // en nombre de frame

    Etat(boolean estVulnerable,  boolean estMort) {
        this.estVulnerable = estVulnerable;
        this.estMort = estMort;
    }

    public boolean estVulnerable() {
        return estVulnerable;
    }

    public boolean estMort() {
        return estMort;
    }
}
