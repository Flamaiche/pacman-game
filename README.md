# *Pac-Man* en JavaFX

### Chef de projet : Babahamou Malik

## Description

Ce projet fournit une implémentation de base du jeu *Pac-Man* en *JavaFX*.
Pour pouvoir développer votre propre implémentation de ce projet, vous devez
en créer une **divergence** en cliquant sur le bouton `Fork` en haut à droite
de cette page.

Lorsque ce sera fait, vous pourrez inviter les membres de votre groupe en tant
que *Developer* pour vous permettre de travailler ensemble sur ce projet.

## Consignes

Vous pouvez retrouver ci-dessous les liens vers les sujets de TP vous guidant
dans le développement de votre projet :

- [Lancement du projet](https://gitlab.univ-artois.fr/enseignements-rwa/modules/but-2/r3-04/tp/-/tree/main/TP03)

## Diagramme de classes

```plantuml
hide empty members

' --------------------------------------- '
' Gestion des images du jeu (les sprites) '
' --------------------------------------- '

abstract class Sprite {
    - imageProperty : ObjectBinding<Image>

    # Sprite(imageProperty : ObjectBinding<Image>)
    + getWidth() : int
    + getHeight() : int
    + imageProperty() : ObjectBinding<Image>
    + {abstract} destroy() : void
}

class StaticSprite extends Sprite {
    - StaticSprite(imageProperty : ObjectBinding<Image>)
    + {static} newInstance(image : Image) : Sprite
    + destroy() : void
}

class AnimatedSprite extends Sprite {
    - timeline : Timeline

    - AnimatedSprite(imageProperty : ObjectBinding<Image>, timeline : Timeline)
    + {static} newInstance(images : ObservableList<Image>, frameRate : int) : Sprite
    + destroy() : void
}

interface ISpriteStore {
    + {static} DEFAULT_SPRITE_SIZE : int
    + {static} DEFAULT_FRAME_RATE : int

    + {abstract} getSprite(identifier : String) : Sprite
    + getSprite(identifiers : String[]) : Sprite
    + getSprite(frameRate : int, identifiers : String[]) : Sprite
    + getSprite(identifiers : List<String>) : Sprite
    + {abstract} getSprite(frameRate : int, identifiers : List<String>) : Sprite
    + getSpriteSize() : int
}

class SpriteStore implements ISpriteStore {
    - spriteCache : Map<String, Sprite>

    + getSprite(identifier : String) : Sprite
    + getSprite(frameRate : int, identifiers : List<String>) : Sprite
    - loadImage(name : String) : Image
}

ISpriteStore --> Sprite : << crée >>

' -------------------------- '
' Gestion de la carte du jeu '
' -------------------------- '

class GameMap {
    - height : int
    - width : int
    - cells : Cell[][]

    + GameMap(height : int, width : int)
    - init() : void
    + getHeight() : int
    + getWidth() : int
    + isOnMap(row : int, column : int) : boolean
    + getAt(row : int, column : int) : Cell
    + setAt(row : int, column : int, cell : Cell) : void
    + getEmptyCells() : List<Cell>
}

class Cell {
    - row : int
    - column : int
    - spriteProperty : ObjectProperty<Sprite>
    - wallProperty : ObjectProperty<Wall>
    - imageProperty : ObjectProperty<Image>

    # Cell(row : int, column : int)
    + Cell(sprite : Sprite)
    + Cell(wall : Wall)
    + getRow() : int
    + getColumn() : int
    + getWidth() : int
    + getHeight() : int
    + isEmpty() : boolean
    + getSprite() : Sprite
    + spriteProperty() : ObjectProperty<Sprite>
    + getWall() : Wall
    + wallProperty() : ObjectProperty<Wall>
    + imageProperty() : ObjectProperty<Image>
    + replaceBy(cell : Cell) : void
}

class Wall {
    - sprite : Sprite

    + Wall(sprite : Sprite)
    + getSprite() : Sprite
}

GameMap *-- "*" Cell

Cell o-- "1" Wall
Cell o-- "1" Sprite

Wall o-- "1" Sprite

' -------------------- '
' Gestion de la partie '
' -------------------- '

class PacmanGame {
    + {static} RANDOM : Random
    + {static} DEFAULT_SPEED : int
    - width : int
    - height : int
    - spriteStore : ISpriteStore
    - gameMap : GameMap
    - player : IAnimated
    - nbGhosts : int
    - nbGums : int
    - movingObjects : List<IAnimated>
    - animatedObjects : List<IAnimated>
    - animation : AnimationTimer
    - controller : IPacmanController

    + PacmanGame(gameWidth : int, gameHeight : int, spriteStore : ISpriteStore, nbGhosts : int)
    + setController(controller : IPacmanController) : void
    + getSpriteStore() : ISpriteStore
    + getWidth() : int
    + getHeight() : int
    + prepare() : void
    + start() : void
    - createMap() : GameMap
    - createAnimated() : void
    - initStatistics() : void
    - spawnAnimated(animated : IAnimated): void
    + moveUp() : void
    + moveRight() : void
    + moveDown() : void
    + moveLeft() : void
    + stopMoving() : void
    - getCellOf(animated : IAnimated) : Cell
    + getCellAt(x : int, y : int) : Cell
    + addMoving(object : IAnimated) : void
    + addAnimated(object : IAnimated) : void
    + removeAnimated(object : IAnimated) : void
    - clearAnimated() : void
    + pacGumEaten(gum : IAnimated) : void
    + playerIsDead() : void
    - gameOver(message : String) : void
}

interface IAnimated {
    + {abstract} getWidth() : int
    + {abstract} getHeight() : int
    + {abstract} setX(xPosition : double) : void
    + {abstract} getX() : int
    + {abstract} xProperty() : DoubleProperty
    + {abstract} setY(yPosition : double) : void
    + {abstract} getY() : int
    + {abstract} yProperty() : DoubleProperty
    + {abstract} setHorizontalSpeed(speed : double) : void
    + {abstract} getHorizontalSpeed() : double
    + {abstract} setVerticalSpeed(speed : double) : void
    + {abstract} getVerticalSpeed() : double
    + {abstract} setSprite(sprite : Sprite) : void
    + {abstract} getSprite() : Sprite
    + {abstract} spriteProperty() : ObjectProperty<Sprite>
    + {abstract} imageProperty() : ObjectProperty<Image>
    + {abstract} setDestroyed(destroyed : boolean) : void
    + {abstract} isDestroyed() : boolean
    + {abstract} destroyedProperty() : BooleanProperty
    + {abstract} onCreation() : void
    + {abstract} onSpawn() : void
    + {abstract} onStep(timeDelta : long) : boolean
    + {abstract} isCollidingWith(other : IAnimated) : boolean
    + {abstract} onCollisionWith(other : IAnimated) : void
    + {abstract} onDespawn() : void
    + {abstract} onDestruction() : void
    + {abstract} self() : IAnimated
}

interface IPacmanController {
    + {abstract} setGame(game : PacmanGame) : void
    + {abstract} prepare(map : GameMap) : void
    + {abstract} bindScore(scoreProperty : IntegerExpression) : void
    + {abstract} bindLife(lifeProperty : IntegerExpression) : void
    + {abstract} addAnimated(movable : IAnimated) : void
    + {abstract} gameOver(endMessage : String) : void
    + {abstract} reset() : void
}

class GameAnimation {
    - previousTimestamp : long
    - movingObjects : List<IAnimated>
    - animatedObjects : List<IAnimated>

    + GameAnimation(movingObjects : List<IAnimated>, animatedObjects : List<IAnimated>)
    + start(): void
    + handle(now : long) : void
    - updateObjects(delta : long) : void
    - checkCollisions() : void
}

PacmanGame o-- "1" ISpriteStore
PacmanGame o-- "1" GameMap
PacmanGame o-- "*" IAnimated
PacmanGame o-- "1" GameAnimation
PacmanGame o-- "1" IPacmanController

GameAnimation o-- "*" IAnimated

' --------------------------------- '
' Gestion de base des objets animés '
' --------------------------------- '

abstract class AbstractAnimated implements IAnimated {
    - {static} MARGIN : int
    # game : PacmanGame
    # xPosition : DoubleProperty
    # yPosition : DoubleProperty
    # horizontalSpeed : double
    # verticalSpeed : double
    # destroyed : BooleanProperty
    # sprite : ObjectProperty<Sprite>
    # image : ObjectProperty<Image>

    # AbstractAnimated(game : PacmanGame, xPosition : double, yPosition : double, sprite : Sprite)
    + getWidth() : int
    + getHeight() : int
    + setX(xPosition : double) : void
    + getX() : int
    + xProperty() : DoubleProperty
    + setY(yPosition : double) : void
    + getY() : int
    + yProperty() : DoubleProperty
    + setHorizontalSpeed(speed : double) : void
    + getHorizontalSpeed() : double
    + setVerticalSpeed(speed : double) : void
    + getVerticalSpeed() : double
    + setSprite(sprite : Sprite) : void
    + getSprite() : Sprite
    + spriteProperty() : ObjectProperty<Sprite>
    + imageProperty() : ObjectProperty<Image>
    + setDestroyed(destroyed : boolean) : void
    + isDestroyed() : boolean
    + destroyedProperty() : BooleanProperty
    + onCreation() : void
    + onSpawn() : void
    + onStep(delta : long) : boolean
    - isOnWall(x : int, y : int) : boolean
    + isCollidingWith(other : IAnimated) : boolean
    + onDespawn() : void
    + onDestruction() : void
    + self() : IAnimated
    + hashCode() : int
    + equals(obj : Object) : boolean
}

AbstractAnimated o-- "1" PacmanGame
AbstractAnimated o-- "1" Sprite

' ----------------- '
' Contrôleur JavaFX '
' ----------------- '

class PacmanController implements IPacmanController {
    - game : PacmanGame
    - stage : Stage
    - backgroundPane : GridPane
    - animatedPane : Pane
    - message : Label
    - score : Label
    - life : Label
    - started : boolean

    + setGame(game : PacmanGame) : void
    + setStage(stage : Stage) : void
    - addKeyListeners() : void
    + prepare(map : GameMap) : void
    - createBackground(map : GameMap) : void
    + bindScore(scoreProperty : IntegerExpression) : void
    + bindLife(lifeProperty : IntegerExpression) : void
    + addAnimated(animated : IAnimated) : void
    + gameOver(endMessage : String) : void
    + reset() : void
}

PacmanController o-- "1" PacmanGame
```

## Tâches réalisées

### Jalon n°1 - TP n°3

| Fonctionnalité                             | Terminée ? | Auteur(s)                                     |
| ------------------------------------------ | ---------- | --------------------------------------------- |
| Gestion des collisions spécifiques         |            |                                               |
| Représentation des pac-gommes              |            |                                               |
| Représentation de Pac-Man                  |            |                                               |
| Intégration de Pac-Man dans la partie      |            |                                               |
| Représentation des fantômes                |            |                                               |
| Intégration des fantômes dans la partie    |            |                                               |
| Création de la carte du jeu                |            |                                               |
| Ajout des pac-gommes sur la carte          |            |                                               |

### Jalon n°2 - TP n°4

| Fonctionnalité                             | Patron de Conception ? | Terminée ? | Auteur(s)                                     |
| ------------------------------------------ | ---------------------- | ---------- | --------------------------------------------- |
| Variantes de génération de labyrinthe      |                        |            |                                               |
| Complétion d'un labyrinthe existant        |                        |            |                                               |
| Variantes de déplacement pour les fantômes |                        |            |                                               |

### Jalon n°3 - TP n°5

| Fonctionnalité                             | Patron de Conception ? | Terminée ? | Auteur(s)                                     |
| ------------------------------------------ | ---------------------- | ---------- | --------------------------------------------- |
| Pac-Man vulnérable                         |                        |            |                                               |
| Pac-Man invulnérable                       |                        |            |                                               |
| Fantômes vulnérables                       |                        |            |                                               |
| Fantômes fuyants                           |                        |            |                                               |
| Fantômes presque invulnérables             |                        |            |                                               |
| Fantômes invulnérables                     |                        |            |                                               |
| Fantômes invulnérables                     |                        |            |                                               |
| Réutilisation des fantômes existants       |                        |            |                                               |
| Ajout des méga-gommes                      |                        |            |                                               |

### Jalon n°4 - TP n°6

*À venir...*
