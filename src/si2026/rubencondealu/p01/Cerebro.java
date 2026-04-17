package si2026.rubencondealu.p01;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.Vector2d;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Cerebro {

    // bandidos getNPCPositions
    public ArrayList<Vector2d> enemigos;
    // jugador getAvatarPosition
    public Vector2d avatar;
    public Vector2d orientacion;

    public boolean balaPropia;
    // proyectiles getMovablePositions
    public ArrayList<Vector2d> proyectiles;
    // obstaculos getImmovablePositions itype = 3
    public ArrayList<Vector2d> obstaculos;
    // zona de bandidos getInmovablePosition itype = 4
    public ArrayList<Vector2d> zonaBandidos;

    public double blockSize;
    public double limitexmin;
    public double limiteymin;
    public double limitexmax;
    public double limiteymax;

    public int ranngoBala;

    public HashMap<Integer,Vector2d> peligro;

    private StateObservation stateObservation;

    public ArrayList<Integer> direccionPeligro;


    public Cerebro() {
        ranngoBala = 4;
    }

    public void analizarMundo(StateObservation stateObs) {
        stateObservation = stateObs;
        peligro = new HashMap<>();
        direccionPeligro = new ArrayList<>();
        blockSize = stateObs.getBlockSize();
        limitexmin = Double.MAX_VALUE;
        limiteymin = Double.MAX_VALUE;
        limitexmax = Double.MIN_VALUE;
        limiteymax = Double.MIN_VALUE;

        ArrayList<Observation>[] npc = stateObs.getNPCPositions();
        balaPropia = false;
        enemigos = new ArrayList<>();
        for(ArrayList<Observation> obs : npc){
            for(Observation o : obs){

                enemigos.add(new Vector2d(Math.floor(o.position.x/blockSize), Math.floor(o.position.y/blockSize)));

            }
        }


        avatar = new Vector2d(Math.floor(stateObs.getAvatarPosition().x/blockSize), Math.floor(stateObs.getAvatarPosition().y/blockSize));
        orientacion = stateObs.getAvatarOrientation();
        ArrayList<Observation>[] movables = stateObs.getMovablePositions();
        proyectiles = new ArrayList<>();
        if(movables != null) {
            for (ArrayList<Observation> mov : movables) {
                for (Observation obs : mov) {

                    proyectiles.add(new Vector2d(Math.floor(obs.position.x/blockSize), Math.floor(obs.position.y/blockSize)));
                }
            }
        }

        ArrayList<Observation>[] inmovibles = stateObs.getImmovablePositions();
        obstaculos = new ArrayList<>();

        zonaBandidos = new ArrayList<>();

        for(ArrayList<Observation> inmov : inmovibles){
            for(Observation obs : inmov){
                if(obs.itype == 3){
                    obstaculos.add(new Vector2d(Math.floor(obs.position.x/blockSize), Math.floor(obs.position.y/blockSize)));
                } else if (obs.itype == 4) {
                    zonaBandidos.add(new Vector2d(Math.floor(obs.position.x/blockSize), Math.floor(obs.position.y/blockSize)));
                    limitexmax = Math.floor(Math.max(obs.position.x/ blockSize, limitexmax) );
                    limiteymax = Math.floor(Math.max(obs.position.y/ blockSize, limiteymax) );
                    limitexmin = Math.floor(Math.min(obs.position.x/ blockSize, limitexmin) );
                    limiteymin = Math.floor(Math.min(obs.position.y/ blockSize, limiteymin) );
                }
            }
        }

        for(Vector2d p : proyectiles){
            if(p.y == avatar.y && p.x < avatar.x && p.dist(avatar) <= ranngoBala){
                peligro.put(0, p);
                direccionPeligro.add(0);
            }
            if(p.x == avatar.x && p.y < avatar.y && p.dist(avatar) <= ranngoBala){
                peligro.put(1, p);
                direccionPeligro.add(1);
            }if(p.y == avatar.y && p.x > avatar.x && p.dist(avatar) <= ranngoBala){
                peligro.put(2, p);
                direccionPeligro.add(2);
            }if(p.x == avatar.x && p.y > avatar.y && p.dist(avatar) <= ranngoBala){
                peligro.put(3, p);
                direccionPeligro.add(3);
            }
        }




    }


    public boolean mataSiDispara(){
        StateObservation futuro = getStateObservation();
        double ScoreAct = stateObservation.getGameScore();


        futuro.advance(Types.ACTIONS.ACTION_USE);
        for(int i = 0; i < 10; i++){
            futuro.advance(Types.ACTIONS.ACTION_NIL);
        }
        return futuro.getGameScore() > ScoreAct;
    }

    public boolean siMeMuevoMuero(Types.ACTIONS action){
        StateObservation futuro = getStateObservation();
        futuro.advance(action);

        for(int i = 0; i < 5; i++){
            futuro.advance(Types.ACTIONS.ACTION_NIL);
        }
        return futuro.isGameOver();
    }


    public boolean puedoMoverme(Types.ACTIONS accion){
        Vector2d posicion = new Vector2d(avatar.x, avatar.y);
        switch (accion){
            case ACTION_UP: posicion.y -= 1; break;
            case ACTION_DOWN: posicion.y += 1; break;
            case ACTION_LEFT: posicion.x -= 1; break;
            case ACTION_RIGHT: posicion.x += 1; break;
            default: return true;
        }

        for(Vector2d obst : obstaculos){
            if(posicion.dist(obst) <= 0.1){
                return false;
            }
        }

        if(posicion.x <= limitexmin || posicion.x >= limitexmax || posicion.y <= limiteymin || posicion.y >= limiteymax){
            return false;
        }
        return true;
    }

    public StateObservation getStateObservation(){
        return stateObservation.copy();
    }

}
