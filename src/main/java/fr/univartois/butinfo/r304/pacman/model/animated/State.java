package fr.univartois.butinfo.r304.pacman.model.animated;

public enum State {
    VULNERABLE(true, false),
    INVULNERABLE(false, false),
    ALMOST_INVULNERABLE(true, false),
    DIE(false, true);

    private final boolean isVulnerable;

    private final boolean isDie;

    private static final int STATE_DURATION = 3_000; // en ms
    private static final int PREVENTIVE_DURATION = 2_000; // en ms

    private static final int DIE_DURATION = 12_000; // en ms

    State(boolean isVulnerable, boolean isDie) {
        this.isVulnerable = isVulnerable;
        this.isDie = isDie;
    }

    public boolean estVulnerable() {
        return isVulnerable;
    }

    public boolean isDie() {
        return isDie;
    }

    public static int getStateDuration() {
        return STATE_DURATION;
    }

    public static int getPreventiveDuration() {
        return PREVENTIVE_DURATION;
    }

    public static int getDieDuration() {
        return DIE_DURATION;
    }
}
