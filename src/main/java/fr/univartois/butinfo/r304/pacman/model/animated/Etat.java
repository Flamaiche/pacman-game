package fr.univartois.butinfo.r304.pacman.model.animated;

public enum Etat {
    VULNERABLE(true, false),
    INVULNERABLE(false, false),
    PRESQUE_INVULNERABLE(false, false),
    MORT(false, true);

    private boolean estVulnerable;

    private boolean estMort;

    private final static int DUREE_ETAT = 3_000; // en ms

    private final static int DUREE_MORT = 5_000; // en ms

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

    static public int getDureeEtat() {
        return DUREE_ETAT;
    }

    public static int getDureeMort() {
        return DUREE_MORT;
    }
}
