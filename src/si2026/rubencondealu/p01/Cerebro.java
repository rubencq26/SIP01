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


        normalizarEnemigoCercano();



        Astar astar = new Astar(this);
        action = astar.getAccion();
    }


    public boolean mataSiDispara() {
        boolean cumple = false;



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
                if (orientacion.y != -1.0) {
                    futuro.advance(Types.ACTIONS.ACTION_UP);
                }
                break;
            case ACTION_DOWN:
                if (orientacion.y != 1.0) {
                    futuro.advance(Types.ACTIONS.ACTION_DOWN);
                }
                break;
            case ACTION_LEFT:
                if (orientacion.x != -1.0) {
                    futuro.advance(Types.ACTIONS.ACTION_LEFT);
                }
                break;
            case ACTION_RIGHT:
                if (orientacion.x != 1.0) {
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

    public void normalizarEnemigoCercano() {
        if (enemigoCercano != null) {
            // Usamos variables temporales para calcular la meta sin alterar al enemigo a mitad del proceso
            double metaX = enemigoCercano.x;
            double metaY = enemigoCercano.y;

            // 1. DETERMINAR DIRECCIÓN DEL ENEMIGO (Movimiento Antihorario en el perímetro)
            // Usamos un margen de 2 casillas para saber por qué pared se está moviendo.
            boolean movIzquierda = (enemigoCercano.y <= limiteymin + 2);
            boolean movAbajo     = (enemigoCercano.x <= limitexmin + 2);
            boolean movDerecha   = (enemigoCercano.y >= limiteymax - 2);
            boolean movArriba    = (enemigoCercano.x >= limitexmax - 2);

            // 2. ¿SENTIDO HORARIO (Intercepción) O ANTIHORARIO (Persecución)?
            boolean intercepcion = false;
            if (movIzquierda && avatar.x < enemigoCercano.x) intercepcion = true;
            if (movAbajo && avatar.y > enemigoCercano.y) intercepcion = true;
            if (movDerecha && avatar.x > enemigoCercano.x) intercepcion = true;
            if (movArriba && avatar.y < enemigoCercano.y) intercepcion = true;

            // 3. APLICAR LÓGICA DE MOVIMIENTO
            if (!intercepcion) {
                // PERSECUCIÓN: Adelantar la meta 2 posiciones en la dirección que lleva
                if (movIzquierda) metaX = enemigoCercano.x - 2;
                if (movAbajo)     metaY = enemigoCercano.y + 2;
                if (movDerecha)   metaX = enemigoCercano.x + 2;
                if (movArriba)    metaY = enemigoCercano.y - 2;
            } else {
                // INTERCEPCIÓN: Justo a la derecha si está a la izquierda, abajo si está arriba...
                if (enemigoCercano.x < avatar.x) metaX = enemigoCercano.x + 1;
                else if (enemigoCercano.x > avatar.x) metaX = enemigoCercano.x - 1;

                if (enemigoCercano.y < avatar.y) metaY = enemigoCercano.y + 1;
                else if (enemigoCercano.y > avatar.y) metaY = enemigoCercano.y - 1;
            }

            // 4. EVITAR ENCERRONAS EN LAS ESQUINAS
            // Verificamos si la meta calculada cae en un vértice (o a 1 casilla de distancia)
            boolean esquinaSupIzq = (metaX <= limitexmin + 1 && metaY <= limiteymin + 1);
            boolean esquinaSupDer = (metaX >= limitexmax - 1 && metaY <= limiteymin + 1);
            boolean esquinaInfIzq = (metaX <= limitexmin + 1 && metaY >= limiteymax - 1);
            boolean esquinaInfDer = (metaX >= limitexmax - 1 && metaY >= limiteymax - 1);

            if (esquinaSupIzq || esquinaSupDer || esquinaInfIzq || esquinaInfDer) {
                // Empujamos la meta "hacia adentro" para que vaya a la siguiente de la esquina
                if (esquinaSupIzq) { metaX = limitexmin + 2; metaY = limiteymin + 2; }
                if (esquinaSupDer) { metaX = limitexmax - 2; metaY = limiteymin + 2; }
                if (esquinaInfIzq) { metaX = limitexmin + 2; metaY = limiteymax - 2; }
                if (esquinaInfDer) { metaX = limitexmax - 2; metaY = limiteymax - 2; }
            }

            // 5. ASEGURAR QUE NO SE SALGA DEL MAPA (Safety Check)
            // Esto sustituye la necesidad de poner "if (meta < limite)" en cada bloque
            if (metaX < limitexmin) metaX = limitexmin;
            if (metaX > limitexmax) metaX = limitexmax;
            if (metaY < limiteymin) metaY = limiteymin;
            if (metaY > limiteymax) metaY = limiteymax;

            // 6. ASIGNAR RESULTADO FINAL AL ENEMIGO
            enemigoCercano.x = metaX;
            enemigoCercano.y = metaY;
        }
    }





    public StateObservation getStateObservation() {
        return stateObservation.copy();
    }

}
