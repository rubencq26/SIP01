package si2026.rubencondealu.p01;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.ElapsedCpuTimer;
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

    public HashMap<Integer, Vector2d> peligro;

    private StateObservation stateObservation;

    public ArrayList<Integer> direccionPeligro;

    public Types.ACTIONS action;

    public Vector2d enemigoCercano;

    public int cooldown = 0;

    public boolean[][] matrizObstaculos = new boolean[40][40];

    public ElapsedCpuTimer timer = new ElapsedCpuTimer();

    public int filas;
    public int columnas;




    public Cerebro() {
        ranngoBala = 4;
    }


    public void analizarMundo(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        timer = elapsedTimer;
        stateObservation = stateObs;
        peligro = new HashMap<>();
        direccionPeligro = new ArrayList<>();
        blockSize = stateObs.getBlockSize();
        filas = (int)(stateObs.getWorldDimension().height/blockSize);
        columnas = (int)(stateObs.getWorldDimension().width/blockSize);

        limitexmin = Double.MAX_VALUE;
        limiteymin = Double.MAX_VALUE;
        limitexmax = Double.MIN_VALUE;
        limiteymax = Double.MIN_VALUE;
        matrizObstaculos = new boolean[columnas][filas];
        avatar = new Vector2d(Math.floor(stateObs.getAvatarPosition().x / blockSize), Math.floor(stateObs.getAvatarPosition().y / blockSize));

        if (cooldown > 0) {
            cooldown--;
        }

        ArrayList<Observation>[] npc = stateObs.getNPCPositions();
        balaPropia = false;
        enemigos = new ArrayList<>();
        double distancia = Double.MAX_VALUE;

        for (ArrayList<Observation> obs : npc) {
            for (Observation o : obs) {

                enemigos.add(new Vector2d(Math.floor(o.position.x / blockSize), Math.floor(o.position.y / blockSize)));
                if (enemigos.get(enemigos.size() - 1).dist(avatar) < distancia) {
                    distancia = enemigos.get(enemigos.size() - 1).dist(avatar);
                    enemigoCercano = enemigos.get(enemigos.size() - 1);
                }

            }
        }



        orientacion = stateObs.getAvatarOrientation();
        ArrayList<Observation>[] movables = stateObs.getMovablePositions();
        proyectiles = new ArrayList<>();
        if (movables != null) {
            for (ArrayList<Observation> mov : movables) {
                for (Observation obs : mov) {

                    proyectiles.add(new Vector2d(Math.floor(obs.position.x / blockSize), Math.floor(obs.position.y / blockSize)));
                }
            }
        }

        ArrayList<Observation>[] inmovibles = stateObs.getImmovablePositions();
        obstaculos = new ArrayList<>();

        zonaBandidos = new ArrayList<>();

        for (ArrayList<Observation> inmov : inmovibles) {
            for (Observation obs : inmov) {
                if (obs.itype == 3) {
                    matrizObstaculos[(int)(obs.position.x/blockSize)][(int)(obs.position.y/blockSize)] = true;
                } else if (obs.itype == 4) {
                    zonaBandidos.add(new Vector2d(Math.floor(obs.position.x / blockSize), Math.floor(obs.position.y / blockSize)));
                    limitexmax = Math.floor(Math.max(obs.position.x / blockSize, limitexmax));
                    limiteymax = Math.floor(Math.max(obs.position.y / blockSize, limiteymax));
                    limitexmin = Math.floor(Math.min(obs.position.x / blockSize, limitexmin));
                    limiteymin = Math.floor(Math.min(obs.position.y / blockSize, limiteymin));
                }
            }
        }

        for (Vector2d p : proyectiles) {
            if (p.y == avatar.y && p.x < avatar.x && p.dist(avatar) <= ranngoBala) {
                peligro.put(0, p);
                direccionPeligro.add(0);
            }
            if (p.x == avatar.x && p.y < avatar.y && p.dist(avatar) <= ranngoBala) {
                peligro.put(1, p);
                direccionPeligro.add(1);
            }
            if (p.y == avatar.y && p.x > avatar.x && p.dist(avatar) <= ranngoBala) {
                peligro.put(2, p);
                direccionPeligro.add(2);
            }
            if (p.x == avatar.x && p.y > avatar.y && p.dist(avatar) <= ranngoBala) {
                peligro.put(3, p);
                direccionPeligro.add(3);
            }
        }


        Vector2d metaAstar = null;
        if(enemigoCercano != null){
            metaAstar = new Vector2d(enemigoCercano.x, enemigoCercano.y);


            if (Math.abs(avatar.x - enemigoCercano.x) > Math.abs(avatar.y - enemigoCercano.y)) {
                double offset = (avatar.x > enemigoCercano.x) ? 2 : -2;
                metaAstar.x += offset;
            } else {

                double offset = (avatar.y > enemigoCercano.y) ? 2 : -2;
                metaAstar.y += offset;
            }


            if(metaAstar.x < limitexmin) metaAstar.x = limitexmin + 1;
            if(metaAstar.x > limitexmax) metaAstar.x = limitexmax - 1;
            if(metaAstar.y < limiteymin) metaAstar.y = limiteymin + 1;
            if(metaAstar.y > limiteymax) metaAstar.y = limiteymax - 1;
        }


        Astar astar = new Astar(this, metaAstar);
        action = astar.getAccion();
    }


    public boolean mataSiDispara() {
        boolean cumple = false;

        if(cooldown > 0){
            return false;
        }

        StateObservation futuro = getStateObservation();
        double ScoreAct = stateObservation.getGameScore();
        double distancia = 0;
        if(orientacion.x == -1.0){
            distancia = avatar.dist(new Vector2d(limitexmin, avatar.y));
        }else if(orientacion.x == 1.0){
            distancia = avatar.dist(new Vector2d(limitexmax, avatar.y));
        }else if(orientacion.y == -1.0){
            distancia = avatar.dist(new Vector2d(avatar.x, limiteymin));
        }else if(orientacion.y == 1.0){
            distancia = avatar.dist(new Vector2d(avatar.x, limiteymax));
        }

        distancia = distancia * 1.6;

        int tiempo = (int)Math.floor(distancia);
        tiempo += 1;
        futuro.advance(Types.ACTIONS.ACTION_USE);
        tiempo = Math.min(tiempo, 15);
        for (int i = 0; i < tiempo; i++) {
            futuro.advance(Types.ACTIONS.ACTION_NIL);
        }

        cumple = futuro.getGameScore() > ScoreAct;


        return cumple;
    }

    public boolean mataSiDispara(Types.ACTIONS action) {
        StateObservation futuro = getStateObservation();
        double ScoreAct = stateObservation.getGameScore();


        futuro.advance(action);
        if(cooldown - 1 > 0){
            return false;
        }

        double distancia = 0;
        if(orientacion.x == -1.0){
            distancia = avatar.dist(new Vector2d(limitexmin, avatar.y));
        }else if(orientacion.x == 1.0){
            distancia = avatar.dist(new Vector2d(limitexmax, avatar.y));
        }else if(orientacion.y == -1.0){
            distancia = avatar.dist(new Vector2d(avatar.x, limiteymin));
        }else if(orientacion.y == 1.0){
            distancia = avatar.dist(new Vector2d(avatar.x, limiteymax));
        }

        distancia = distancia * 1.6;

        int tiempo = (int)Math.floor(distancia);
        tiempo += 1;



        futuro.advance(Types.ACTIONS.ACTION_USE);
        tiempo = Math.min(tiempo, 15);
        for (int i = 0; i < tiempo; i++) {
            futuro.advance(Types.ACTIONS.ACTION_NIL);
        }
        return futuro.getGameScore() > ScoreAct;


    }

    public boolean mataSiDispara(ArrayList<Types.ACTIONS> acciones) {
        StateObservation futuro = getStateObservation();
        double ScoreAct = stateObservation.getGameScore();


        futuro.advance(acciones.get(0));
        futuro.advance(acciones.get(1));

        double distancia = 0;
        if(orientacion.x == -1.0){
            distancia = avatar.dist(new Vector2d(limitexmin, avatar.y));
        }else if(orientacion.x == 1.0){
            distancia = avatar.dist(new Vector2d(limitexmax, avatar.y));
        }else if(orientacion.y == -1.0){
            distancia = avatar.dist(new Vector2d(avatar.x, limiteymin));
        }else if(orientacion.y == 1.0){
            distancia = avatar.dist(new Vector2d(avatar.x, limiteymax));
        }

        distancia = distancia * 1.6;

        int tiempo = (int)Math.floor(distancia);
        tiempo += 1;



        futuro.advance(Types.ACTIONS.ACTION_USE);
        tiempo = Math.min(tiempo, 15);
        for (int i = 0; i < tiempo; i++) {
            futuro.advance(Types.ACTIONS.ACTION_NIL);
        }
        return futuro.getGameScore() > ScoreAct;


    }

    public boolean siMeMuevoMuero(Types.ACTIONS action) {
        StateObservation futuro = getStateObservation();

        switch (action) {
            case ACTION_UP:
                if (orientacion.y == 1.0) {
                    futuro.advance(Types.ACTIONS.ACTION_UP);
                }
                break;
            case ACTION_DOWN:
                if (orientacion.y == -1.0) {
                    futuro.advance(Types.ACTIONS.ACTION_DOWN);
                }
                break;
            case ACTION_LEFT:
                if (orientacion.x == 1.0) {
                    futuro.advance(Types.ACTIONS.ACTION_LEFT);
                }
                break;
            case ACTION_RIGHT:
                if (orientacion.x == -1.0) {
                    futuro.advance(Types.ACTIONS.ACTION_RIGHT);
                }
                break;
        }

        futuro.advance(action);

        for (int i = 0; i < 5; i++) {
            futuro.advance(Types.ACTIONS.ACTION_NIL);
        }
        return futuro.isGameOver();
    }




    public boolean puedoMoverme(Types.ACTIONS accion) {
        Vector2d posicion = new Vector2d(avatar.x, avatar.y);
        switch (accion) {
            case ACTION_UP:
                posicion.y -= 1;
                break;
            case ACTION_DOWN:
                posicion.y += 1;
                break;
            case ACTION_LEFT:
                posicion.x -= 1;
                break;
            case ACTION_RIGHT:
                posicion.x += 1;
                break;
            default:
                return true;
        }

        for (Vector2d obst : obstaculos) {
            if (posicion.dist(obst) <= 0.1) {
                return false;
            }
        }

        if (posicion.x <= limitexmin || posicion.x >= limitexmax || posicion.y <= limiteymin || posicion.y >= limiteymax) {
            return false;
        }
        return true;
    }

    public StateObservation getStateObservation() {
        return stateObservation.copy();
    }

}
