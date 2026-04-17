package si2026.rubencondealu.p01;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Agente extends AbstractPlayer {



    // bandidos getNPCPositions
    // jugador getAvatarPosition
    // proyectiles getMovablePositions
    // obstaculos getImmovablePositions itype = 3
    // zona de bandidos getInmovablePosition itype = 4

    Cerebro cerebro;
    MotorReglas motor;
    /**
     *
     */
    public Agente(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        // TODO Esbozo de constructor generado automáticamente
        this.cerebro = new Cerebro();
        Condicion c1 = new CondicionPeligro();
        List<Condicion> condiciones = new ArrayList<>();
        condiciones.add(c1);


        Condicion c2 = new CondicionDisparo();

        condiciones.add(c2);

        Condicion c3 = new CondicionDisparoObstaculizado();

        condiciones.add(c3);






        motor = new MotorReglas(condiciones, cerebro);




    }

    @Override
    public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        // TODO Esbozo de método generado automáticamente

        cerebro.analizarMundo(stateObs);
        System.out.println(cerebro.orientacion);
        return ACTIONS.ACTION_LEFT;

    }




}