package fr.univartois.butinfo.r304.pacman.model.animated;

public enum State {
    VULNERABLE(true, false),
    INVULNERABLE(false, false),
    PRESQUE_INVULNERABLE(true, false),
    MORT(false, true);

    private boolean estVulnerable;

    private boolean estMort;

    private final static int DUREE_ETAT = 3_000; // en ms
    private final static int DUREE_PREVENTIVE = 2_000; // en ms

    private final static int DUREE_MORT = 12_000; // en ms

    State(boolean estVulnerable, boolean estMort) {
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

    public static int getDureePreventive() {
        return DUREE_PREVENTIVE;
    }

    public static int getDureeMort() {
        return DUREE_MORT;
    }
}
