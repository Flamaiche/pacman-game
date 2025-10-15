package fr.univartois.butinfo.r304.pacman.model.animated;

import fr.univartois.butinfo.r304.pacman.model.IAnimated;
import fr.univartois.butinfo.r304.pacman.model.PacmanGame;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.DeplacementAleatoire;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.DeplacementFuyard;
import fr.univartois.butinfo.r304.pacman.model.animated.deplacements.IStrategieDeplacement;
import fr.univartois.butinfo.r304.pacman.view.Sprite;

import java.util.Timer;

public class Fantome extends AbstractAnimated implements IEtat {

    private CouleurFantome couleurFantome;
    private IStrategieDeplacement deplacementCurrent;
    private int spawnX = 0;
    private int spawnY = 0;

    private Etat etat;
    private long animationTimer;
    private int animationFrame;
    private Timer etatTimer;
    private final static String[] SPRITES = {
            "1",
            "2"
    };
    private final static String[] SPRITES_PRESQUE_INVULNERABLE = {
            "../afraid/1",
            "2"
    };

    private final IStrategieDeplacement deplacementDefault;
    private final IStrategieDeplacement deplacementFuyard = new DeplacementFuyard(this);
    private final IStrategieDeplacement deplacementAleatoire = new DeplacementAleatoire(this);

    public Fantome(PacmanGame game, double xPosition, double yPosition, Sprite sprite, CouleurFantome couleurFantome) {
        super(game, xPosition, yPosition, sprite);
        this.couleurFantome = couleurFantome;
        this.deplacementCurrent = couleurFantome.getStrategie(this);
        this.etat = Etat.INVULNERABLE;
        deplacementDefault = deplacementCurrent;
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

    private void setDeplacementCurrent(IStrategieDeplacement deplacementCurrent) {
        this.deplacementCurrent = deplacementCurrent;
    }

    private void updateStrategieDeplacement() {
        switch (etat) {
            case MORT -> setDeplacementCurrent(deplacementAleatoire);
            case VULNERABLE -> setDeplacementCurrent(deplacementFuyard);
            default -> setDeplacementCurrent(deplacementDefault);
        }
        if (deplacementCurrent != null) deplacementCurrent.reset();
    }

    public boolean onStep(long delta){
        if(deplacementCurrent !=null){
            deplacementCurrent.mouvement();
        }
        return super.onStep(delta);
    }

    public void respawn() {
        setX(spawnX);
        setY(spawnY);
        deplacementCurrent.reset();
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

    public IStrategieDeplacement getDeplacementCurrent() {
        return deplacementCurrent;
    }

    @Override
    public Etat getEtat() {
        return etat;
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
    public Timer getEtatTimer() {
        return etatTimer;
    }

    @Override
    public void setEtatTimer(Timer etatTimer) {
        this.etatTimer = etatTimer;
    }

    @Override
    public void setEtat(Etat etat) {
        if (this.etat == Etat.MORT && etat != Etat.INVULNERABLE) return;

        switch (etat) {
            case VULNERABLE -> setEtatLater(Etat.PRESQUE_INVULNERABLE);
            case PRESQUE_INVULNERABLE -> setEtatLater(Etat.INVULNERABLE, Etat.getDureePreventive());
            case MORT ->  setEtatLater(Etat.INVULNERABLE, Etat.getDureeMort());
        }

        this.etat = etat;
        updateStrategieDeplacement();
    }

    @Override
    public String[] getCurrentSprites() {
        if (etat == Etat.PRESQUE_INVULNERABLE) return SPRITES_PRESQUE_INVULNERABLE;
        else return SPRITES;
    }
}
