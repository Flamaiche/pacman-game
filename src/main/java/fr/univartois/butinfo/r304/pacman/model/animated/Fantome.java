package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.IStrategieDeplacement;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

public class Fantome extends AbstractAnimated implements IEtat {

    private CouleurFantome couleurFantome;
    private IStrategieDeplacement strategieDeplacement;
    private int spawnX = 0;
    private int spawnY = 0;

    private Etat etat;
    private long animationTimer;
    private int animationFrame;
    private final static String[] SPRITES = {
            "1",
            "2"
    };
    private final static String[] SPRITES_PRESQUE_INVULNERABLE = {
            "../afraid/1",
            "2"
    };

    public Fantome(PacmanGame game, double xPosition, double yPosition, Sprite sprite, CouleurFantome couleurFantome) {
        super(game, xPosition, yPosition, sprite);
        this.couleurFantome = couleurFantome;
        this.strategieDeplacement = couleurFantome.getStrategie(this);
        this.etat = Etat.INVULNERABLE;
    }

    private CouleurFantome getCouleurFantome() {
        return couleurFantome;
    }

    private void setCouleurFantome(CouleurFantome couleurFantome) {
        this.couleurFantome = couleurFantome;
    }

    @Override
    public void onCollisionWith(IAnimated other) {
        // par défaut on laisse l'autre gérer
        other.onCollisionWith(this);
    }

    @Override
    public void onCollisionWith(PacMan pacMan) {
        // on renvoie l'appel à PacMan avec le type dynamique
        pacMan.onCollisionWith(this);
    }

    @Override
    public void onCollisionWith(Fantome fantome) {
        // ne fait rien
    }

    @Override
    public void onCollisionWith(PacGomme pacGomme) {
        // ne fait rien
    }

    public void setStrategieDeplacement(IStrategieDeplacement strategieDeplacement) {
        this.strategieDeplacement = strategieDeplacement;
    }

    public boolean onStep(long delta){
        if(strategieDeplacement!=null){
            strategieDeplacement.mouvement();
        }
        return super.onStep(delta);
    }

    public void respawn() {
        setX(spawnX);
        setY(spawnY);
        strategieDeplacement.reset();
    }

    public void setSpawnPoint(int x, int y) {
        spawnX = x;
        spawnY = y;
    }

    @Override
    public PacmanGame getGame(){
        return game;
    }

    @Override
    public String getFolderSprite() {
        return "ghosts/";
    }

    @Override
    public String getCustomPath() {
        if (etat == Etat.VULNERABLE) {
            return "afraid/";
        } else if (etat == Etat.MORT) {
            return "hurt/";
        }
        return couleurFantome.getFolderName() + "/";
    }

    public IStrategieDeplacement getStrategieDeplacement() {
        return strategieDeplacement;
    }

    @Override
    public Etat getEtat() {
        return etat;
    }

    @Override
    public void setEtat(Etat etat) {
        if (this.etat == Etat.MORT)
        System.out.println("Fantome"+ getCouleurFantome().getFolderName() +".setEtat()" + etat.name());
        this.etat = etat;
    }

    @Override
    public String[] getCurrentSprites() {
        if (etat == Etat.PRESQUE_INVULNERABLE) return SPRITES_PRESQUE_INVULNERABLE;
        else return SPRITES;
    }

    @Override
    public int getAnimationFrame() {
        return animationFrame;
    }

    @Override
    public long getAnimationTimer() {
        return animationTimer;
    }

    @Override
    public void setAnimationFrame(int animationFrame) {
        this.animationFrame = animationFrame;
    }

    @Override
    public void setAnimationTimer(long animationTimer) {
        this.animationTimer = animationTimer;
    }

    @Override
    public void setEtatTemp(Etat etat) {
        System.out.println("Fantome.setEtatTemp : " + etat);
        Etat ancienEtat = getEtat();
        if (ancienEtat == etat) return; // si etat == ancienEtat == INVULNERABLE

        // TODO: Configurer ici, pour éviter les conflits de timer

        if (etat == Etat.VULNERABLE) {
            timerSetEtat(Etat.PRESQUE_INVULNERABLE, Etat.getDureeEtat());
            timerSetEtat(ancienEtat, Etat.getDureeMort());
        } else {
            timerSetEtat(ancienEtat, Etat.getDureeEtat());
        }

        setEtat(etat);
    }
}
