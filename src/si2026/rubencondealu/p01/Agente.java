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
        ArrayList<Regla> reglas = new ArrayList<>();

        Condicion miraIzquierda = new ListaCondiciones.MiroIzquierda();
        Condicion miraArriba = new ListaCondiciones.MiroArriba();
        Condicion miraAbajo = new ListaCondiciones.MiroAbajo();
        Condicion miraDerecha = new ListaCondiciones.MiroDerecha();

        Condicion noMiraIzquierda = new ListaCondiciones.NoMiroIzquierda();
        Condicion noMiraArriba = new ListaCondiciones.NoMiroArriba();
        Condicion noMiraAbajo = new ListaCondiciones.NoMiroAbajo();
        Condicion noMiraDerecha = new ListaCondiciones.NoMiroDerecha();

        Condicion peligroIzquierda = new ListaCondiciones.PeligroIzquierda();
        Condicion peligroArriba = new ListaCondiciones.PeligroArriba();
        Condicion peligroAbajo = new ListaCondiciones.PeligroAbajo();
        Condicion peligroDerecha = new ListaCondiciones.PeligroDerecha();

        Condicion armaRecargada = new ListaCondiciones.ArmaRecargada();

        Condicion siDisparaMata = new ListaCondiciones.SiDisparaMata();
        Condicion siArribaMata = new ListaCondiciones.SiDisparaArrMata();
        Condicion siIzquierdaMata = new ListaCondiciones.SiDisparaIzqMata();
        Condicion siDerechaMata = new ListaCondiciones.SiDisparaDerMata();
        Condicion siAbajoMata = new ListaCondiciones.SiDisparaAbjMata();

        Condicion siRompeMuroMata = new ListaCondiciones.RompeMuroMata();
        Condicion siRompeMuroIzquierdaMata = new ListaCondiciones.RompeMuroIzquierdaMata();
        Condicion siRompeMuroArribaMata = new ListaCondiciones.RompeMuroArribaMata();
        Condicion siRompeMuroAbajoMata = new ListaCondiciones.RompeMuroAbajoMata();
        Condicion siRompeMuroDerechaMata = new ListaCondiciones.RompeMuroDerechaMata();

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

        Condicion conTiempo = new ListaCondiciones.ConTiempo();

        List<Condicion> condiciones = new ArrayList<>();
        condiciones.add(peligroIzquierda);
        condiciones.add(siArribaVivo);
        Regla r1 = new Regla(condiciones, ACTIONS.ACTION_UP);
        reglas.add(r1);


        condiciones = new ArrayList<>();
        condiciones.add(peligroIzquierda);
        condiciones.add(siAbajoVivo);
        Regla r2 = new Regla(condiciones, ACTIONS.ACTION_DOWN);
        reglas.add(r2);

        condiciones = new ArrayList<>();
        condiciones.add(peligroIzquierda);
        Regla r3 = new Regla(condiciones, ACTIONS.ACTION_RIGHT);
        reglas.add(r3);

        condiciones = new ArrayList<>();
        condiciones.add(peligroArriba);
        condiciones.add(siDerechaVivo);
        Regla r4 = new Regla(condiciones, ACTIONS.ACTION_RIGHT);
        reglas.add(r4);

        condiciones = new ArrayList<>();
        condiciones.add(peligroArriba);
        condiciones.add(siIzquierdaVivo);
        Regla r5 = new Regla(condiciones, ACTIONS.ACTION_LEFT);
        reglas.add(r5);

        condiciones = new ArrayList<>();
        condiciones.add(peligroArriba);
        Regla r6 = new Regla(condiciones, ACTIONS.ACTION_DOWN);
        reglas.add(r6);

        condiciones = new ArrayList<>();
        condiciones.add(peligroDerecha);
        condiciones.add(siArribaVivo);
        Regla r7 = new Regla(condiciones, ACTIONS.ACTION_UP);
        reglas.add(r7);

        condiciones = new ArrayList<>();
        condiciones.add(peligroDerecha);
        condiciones.add(siAbajoVivo);
        Regla r8 = new Regla(condiciones, ACTIONS.ACTION_DOWN);
        reglas.add(r8);

        condiciones = new ArrayList<>();
        condiciones.add(peligroDerecha);
        Regla r9 = new Regla(condiciones, ACTIONS.ACTION_LEFT);
        reglas.add(r9);

        condiciones = new ArrayList<>();
        condiciones.add(peligroAbajo);
        condiciones.add(siIzquierdaVivo);
        Regla r10 = new Regla(condiciones, ACTIONS.ACTION_LEFT);
        reglas.add(r10);

        condiciones = new ArrayList<>();
        condiciones.add(peligroAbajo);
        condiciones.add(siDerechaVivo);
        Regla r11 = new Regla(condiciones, ACTIONS.ACTION_RIGHT);
        reglas.add(r11);

        condiciones = new ArrayList<>();
        condiciones.add(peligroAbajo);
        Regla r12 = new Regla(condiciones, ACTIONS.ACTION_UP);
        reglas.add(r12);

        condiciones = new ArrayList<>();
        condiciones.add(armaRecargada);
        condiciones.add(siDisparaMata);
        Regla r13 = new Regla(condiciones, ACTIONS.ACTION_USE);
        reglas.add(r13);

        condiciones = new ArrayList<>();
        condiciones.add(siIzquierdaMata);
        condiciones.add(noMiraIzquierda);
        Regla r14 = new Regla(condiciones, ACTIONS.ACTION_LEFT);
        reglas.add(r14);

        condiciones = new ArrayList<>();
        condiciones.add(siArribaMata);
        condiciones.add(noMiraArriba);
        Regla r15 = new Regla(condiciones, ACTIONS.ACTION_UP);
        reglas.add(r15);


        condiciones = new ArrayList<>();
        condiciones.add(siDerechaMata);
        condiciones.add(noMiraDerecha);
        Regla r16 = new Regla(condiciones, ACTIONS.ACTION_RIGHT);
        reglas.add(r16);

        condiciones = new ArrayList<>();
        condiciones.add(siAbajoMata);
        condiciones.add(noMiraAbajo);
        Regla r17 = new Regla(condiciones, ACTIONS.ACTION_DOWN);
        reglas.add(r17);

        condiciones = new ArrayList<>();
        condiciones.add(conTiempo);
        condiciones.add(armaRecargada);
        condiciones.add(siRompeMuroMata);
        Regla r23 = new Regla(condiciones, ACTIONS.ACTION_USE);
        reglas.add(r23);


        condiciones = new ArrayList<>();
        condiciones.add(conTiempo);
        condiciones.add(armaRecargada);
        condiciones.add(siRompeMuroIzquierdaMata);
        Regla r24 = new Regla(condiciones, ACTIONS.ACTION_LEFT);
        reglas.add(r24);

        condiciones = new ArrayList<>();
        condiciones.add(conTiempo);
        condiciones.add(armaRecargada);
        condiciones.add(siRompeMuroArribaMata);
        Regla r25 = new Regla(condiciones, ACTIONS.ACTION_UP);
        reglas.add(r25);

        condiciones = new ArrayList<>();
        condiciones.add(conTiempo);
        condiciones.add(armaRecargada);
        condiciones.add(siRompeMuroDerechaMata);
        Regla r26 = new Regla(condiciones, ACTIONS.ACTION_RIGHT);
        reglas.add(r26);


        condiciones = new ArrayList<>();
        condiciones.add(conTiempo);
        condiciones.add(siRompeMuroAbajoMata);
        Regla r27 = new Regla(condiciones, ACTIONS.ACTION_DOWN);
        reglas.add(r27);

        condiciones = new ArrayList<>();
        condiciones.add(sigMovUse);
        Regla r18 = new Regla(condiciones, ACTIONS.ACTION_USE);
        reglas.add(r18);

        condiciones = new ArrayList<>();
        condiciones.add(sigMovIzquierda);
        condiciones.add(siIzquierdaVivo);
        Regla r19 = new Regla(condiciones, ACTIONS.ACTION_LEFT);
        reglas.add(r19);

        condiciones = new ArrayList<>();
        condiciones.add(sigMovArriba);
        condiciones.add(siArribaVivo);
        Regla r20 = new Regla(condiciones, ACTIONS.ACTION_UP);
        reglas.add(r20);

        condiciones = new ArrayList<>();
        condiciones.add(sigMovDerecha);
        condiciones.add(siDerechaVivo);
        Regla r21 = new Regla(condiciones, ACTIONS.ACTION_RIGHT);
        reglas.add(r21);

        condiciones = new ArrayList<>();
        condiciones.add(sigMovAbajo);
        condiciones.add(siAbajoVivo);
        Regla r22 = new Regla(condiciones, ACTIONS.ACTION_DOWN);
        reglas.add(r22);



        motor = new MotorReglas(reglas, cerebro);

    }

    @Override
    public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        // TODO Esbozo de método generado automáticamente

        cerebro.analizarMundo(stateObs, elapsedTimer);
        Regla r = motor.disparo();
        //System.out.println(r.toString());
        return r.action;

    }




}