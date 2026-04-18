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

        Condicion miraIzquierda = new ListaCondiciones.MiroIzquierda();
        Condicion miraArriba = new ListaCondiciones.MiroArriba();
        Condicion miraAbajo = new ListaCondiciones.MiroAbajo();
        Condicion miraDerecha = new ListaCondiciones.MiroDerecha();

        Condicion peligroIzquierda = new ListaCondiciones.PeligroIzquierda();
        Condicion peligroArriba = new ListaCondiciones.PeligroArriba();
        Condicion peligroAbajo = new ListaCondiciones.PeligroAbajo();
        Condicion peligroDerecha = new ListaCondiciones.PeligroDerecha();

        Condicion siDisparaMata = new ListaCondiciones.SiDisparaMata();
        Condicion siArribaMata = new ListaCondiciones.SiDisparaArrMata();
        Condicion siIzquierdaMata = new ListaCondiciones.SiDisparaIzqMata();
        Condicion siDerechaMata = new ListaCondiciones.SiDisparaDerMata();
        Condicion siAbajoMata = new ListaCondiciones.SiDisparaAbjMata();

        Condicion siIzquierdaMuero = new ListaCondiciones.SiMeMuevoIzqMuero();
        Condicion siArribaMuero = new ListaCondiciones.SiMeMuevoArrMuero();
        Condicion siDerechaMuero = new ListaCondiciones.SiMeMuevoDerMuero();
        Condicion siAbajoMuero = new ListaCondiciones.SiMeMuevoAbjMuero();

        Condicion siIzquierdaVivo = new ListaCondiciones.SiMeMuevoIzqVivo();
        Condicion siArribaVivo = new ListaCondiciones.SiMeMuevoArrVivo();
        Condicion siDerechaVivo = new ListaCondiciones.SiMeMuevoDerVivo();
        Condicion siAbajoVivo = new ListaCondiciones.SiMeMuevoAbjVivo();

        Condicion sigMovIzquierda = new ListaCondiciones.AstarIzquierda();
        Condicion sigMovArriba = new ListaCondiciones.AstarArriba();
        Condicion sigMovDerecha = new ListaCondiciones.AstarDerecha();
        Condicion sigMovAbajo = new ListaCondiciones.AstarAbajo();
        Condicion sigMovUse = new ListaCondiciones.AstarUse();

        List<Condicion> condiciones = new ArrayList<>();
        condiciones.add(peligroIzquierda);
        condiciones.add(siArribaVivo);
        Regla r1 = new Regla(condiciones, ACTIONS.ACTION_UP);

        condiciones = new ArrayList<>();
        condiciones.add(peligroIzquierda);
        condiciones.add(siAbajoVivo);
        Regla r2 = new Regla(condiciones, ACTIONS.ACTION_DOWN);

        condiciones = new ArrayList<>();
        condiciones.add(peligroIzquierda);
        Regla r3 = new Regla(condiciones, ACTIONS.ACTION_RIGHT);

        condiciones = new ArrayList<>();
        condiciones.add(peligroArriba);
        condiciones.add(siDerechaMuero);



    }

    @Override
    public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        // TODO Esbozo de método generado automáticamente

        cerebro.analizarMundo(stateObs);
        System.out.println(cerebro.orientacion);
        return ACTIONS.ACTION_LEFT;

    }




}