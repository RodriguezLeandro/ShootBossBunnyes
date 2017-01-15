package Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.megamangame.MegamanMainClass;

import Sprites.Fireball;
import Sprites.Lava;
import Sprites.Megaman;
import Sprites.Zero;

/**
 * Created by Leandro on 03/01/2017.
 */

public class WorldContactListener implements ContactListener {


    Fixture fixtureBody;
    Fixture fixtureObject;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int categoryDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (categoryDefinition){

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.COIN_BIT :

                fixtureBody = fixtureA.getUserData().getClass() == Megaman.class ? fixtureA : fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB : fixtureA;

                if (fixtureObject.getUserData() instanceof InteractiveTileObject){
                    ((InteractiveTileObject) fixtureObject.getUserData()).onBodyHit();
                }

                break;

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.LAVA_BIT :

                fixtureBody = fixtureA.getUserData().getClass() == Megaman.class ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;

                if (fixtureObject.getUserData() instanceof  InteractiveTileObject){
                    ((Lava) fixtureObject.getUserData()).onBodyHit();
                }

                break;

            case MegamanMainClass.ZERO_SENSOR_BIT | MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT:

                fixtureBody = fixtureA.getUserData().getClass() == Zero.class ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;

                //Basicamente, si la bola de fuego va hacia la derecha manda true, sino false.
                ((Zero) fixtureBody.getUserData()).onBodyHit();
                //Luego, que la bola no puedo volver a pegarle por unos 3 segundos.
                ((Zero)fixtureBody.getUserData()).setZeroUntouchableDot5Seconds();

                break;

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT:
                fixtureBody = fixtureA.getUserData().getClass() == Megaman.class ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;

                //Basicamente, si la bola de fuego va hacia la derecha manda true, sino false.
                if (((Fireball)fixtureObject.getUserData()).fireToRight) {
                    ((Megaman) fixtureBody.getUserData()).onBodyHit();
                }else{
                    ((Megaman) fixtureBody.getUserData()).onBodyHit();
                }
                break;

            case MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT | MegamanMainClass.ZERO_SENSOR_BIT_2:
                fixtureBody = fixtureA.getUserData().getClass() == Zero.class ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;

                ((Zero)fixtureBody.getUserData()).setZeroShouldJump();

                break;

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.ZERO_SENSOR_BIT:
                fixtureBody = fixtureA.getUserData().getClass() == Megaman.class ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;
                ((Megaman) fixtureBody.getUserData()).onBodyHit();

                break;

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.ENEMY_BIT:

                fixtureBody = fixtureA.getUserData().getClass() == Megaman.class ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;

                //Si megaman es golpeado por la derecha, entonces impulso por la derecha,
                //De lo contrario, impulso por la izquierda.
                if (((Megaman) fixtureBody.getUserData()).body.getPosition().x < ((Enemy) fixtureObject.getUserData()).body.getPosition().x) {
                    //Mandamos true si es impulso a  la izquierda, de lo contrario mandamos false.
                    //Tambien le voy a mandar el body del enemigo que lo choco, para que se separen.
                    ((Megaman) fixtureBody.getUserData()).onBodyHitLower(true);
                }
                else {
                    ((Megaman) fixtureBody.getUserData()).onBodyHitLower(false);
                }
                break;

            case MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT | MegamanMainClass.ENEMY_BIT:

                fixtureBody = fixtureA.getUserData().getClass() == Fireball.class ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;
                ((Enemy) fixtureObject.getUserData()).onBodyHit();
                ((Fireball)fixtureBody.getUserData()).destroy();

                break;

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.WALL_BIT:
                fixtureBody = fixtureA.getUserData().getClass() == Megaman.class ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;
                //Ponemos el damping en 10 cuando megaman esta deslizando(creo que damping seria como rozamiento).
                ((Megaman)fixtureBody.getUserData()).setLinearDamping(10);
                ((Megaman)fixtureBody.getUserData()).setState(Megaman.State.SLIDING);

                //Ahora tenemos que obligar a megaman a deslizar contra la pared, mirando hacia la pared.
                //No podemos permitir lo contrario.
                //Si el cuerpo de megaman esta a la izquierda del cuerpo del objeto, decimos que megaman mire a la derecha.
                if (((Megaman)fixtureBody.getUserData()).body.getPosition().x < ((InteractiveTileObject)fixtureObject.getUserData()).body.getPosition().x){
                    ((Megaman)fixtureBody.getUserData()).setRunningRight(true);
                }else {
                    //De lo contrario, que mire a la izquierda.
                    ((Megaman)fixtureBody.getUserData()).setRunningRight(false);
                }


                break;

            default:

                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int categoryDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch(categoryDefinition){

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.LAVA_BIT :

                //El bug ocurria porque faltaban estas dos lineas de abajo.
                //No preguntabamos quien era la lava y quien era megaman.
                //Siempre suponiamos que el objeto era la lava, sin estar seguros de ello.
                //Por eso, nunca terminaba el contacto entre la lava y el personaje.
                //Este es el primer bug que tuvo el juego, interesante resolucion.

                fixtureBody = fixtureA.getUserData().getClass() == Megaman.class ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;

                if (fixtureObject.getUserData() instanceof  InteractiveTileObject){
                    ((Lava) fixtureObject.getUserData()).onBodyStopHit();
                }
                break;

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.WALL_BIT:
                fixtureBody = fixtureA.getUserData().getClass() == Megaman.class ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;
                //Cuando megaman termina de deslizar, volvemos a poner el damping en 0.
                ((Megaman)fixtureBody.getUserData()).setLinearDamping(0);
                ((Megaman)fixtureBody.getUserData()).setState(Megaman.State.STANDING);

                break;

            default:

                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
