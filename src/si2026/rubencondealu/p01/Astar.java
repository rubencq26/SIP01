package si2026.rubencondealu.p01;

import ontology.Types;
import tools.Vector2d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.HashSet;

public class Astar {

    private Types.ACTIONS accion = Types.ACTIONS.ACTION_NIL;

    public Types.ACTIONS getAccion() {
        return accion;
    }

    public Cerebro cerebro;

    public Astar(Cerebro cerebro) {
        int nIter = 0;
        int nIterMax = 100;
        this.cerebro = cerebro;

        if (cerebro.enemigoCercano == null) {
            return;
        }

        Vector2d meta = cerebro.enemigoCercano;

        Vector2d posicion = cerebro.avatar;
        Nodo hijo;

        Nodo inicio = new Nodo(posicion, 0, meta, null);
        PriorityQueue<Nodo> cola = new PriorityQueue<>(Comparator.comparingDouble(nodo -> nodo.heuristica));
        cola.add(inicio);
        boolean encontrado = false;

        HashSet<Nodo> visitados = new HashSet<>();
        Nodo actual = new Nodo(posicion, 0, meta, null);

        while (!cola.isEmpty() && !encontrado && nIter++ < nIterMax) {
            actual = cola.poll();
            visitados.add(actual);

            if (actual.posicion.dist(meta) < 1) {
                encontrado = true;
            }

            hijo = new Nodo(new Vector2d(actual.posicion.x - 1, actual.posicion.y), actual.costo + 1, meta, actual);
            if (!visitados.contains(hijo) && hijo.posicion.x > cerebro.limitexmin && hijo.posicion.x < cerebro.limitexmax && hijo.posicion.y > cerebro.limiteymin && hijo.posicion.y < cerebro.limiteymax) {
                cola.add(hijo);
            }

            hijo = new Nodo(new Vector2d(actual.posicion.x, actual.posicion.y - 1), actual.costo + 1, meta, actual);
            if (!visitados.contains(hijo) && hijo.posicion.x > cerebro.limitexmin && hijo.posicion.x < cerebro.limitexmax && hijo.posicion.y > cerebro.limiteymin && hijo.posicion.y < cerebro.limiteymax) {
                cola.add(hijo);
            }

            hijo = new Nodo(new Vector2d(actual.posicion.x + 1, actual.posicion.y), actual.costo + 1, meta, actual);
            if (!visitados.contains(hijo) && hijo.posicion.x > cerebro.limitexmin && hijo.posicion.x < cerebro.limitexmax && hijo.posicion.y > cerebro.limiteymin && hijo.posicion.y < cerebro.limiteymax) {
                cola.add(hijo);
            }

            hijo = new Nodo(new Vector2d(actual.posicion.x, actual.posicion.y + 1), actual.costo + 1, meta, actual);
            if (!visitados.contains(hijo) && hijo.posicion.x > cerebro.limitexmin && hijo.posicion.x < cerebro.limitexmax && hijo.posicion.y > cerebro.limiteymin && hijo.posicion.y < cerebro.limiteymax) {
                cola.add(hijo);
            }

        }

        if (encontrado) {
            Nodo buscar = actual;
            if (buscar.padre == null) {
                return;
            }
            while (buscar.padre.padre != null) {
                buscar = buscar.padre;
            }

            if (inicio.posicion.x > buscar.posicion.x) {
                accion = Types.ACTIONS.ACTION_LEFT;
            } else if (inicio.posicion.x < buscar.posicion.x) {
                accion = Types.ACTIONS.ACTION_RIGHT;
            } else if (inicio.posicion.y > buscar.posicion.y) {
                accion = Types.ACTIONS.ACTION_UP;
            } else if (inicio.posicion.y < buscar.posicion.y) {
                accion = Types.ACTIONS.ACTION_DOWN;
            }

            if (cerebro.matrizObstaculos[(int)buscar.posicion.x][(int)buscar.posicion.y]) {
                switch (accion) {
                    case ACTION_LEFT:
                        if (cerebro.orientacion.x == -1.0) {
                            accion = Types.ACTIONS.ACTION_USE;
                        }
                        break;
                    case ACTION_UP:
                        if (cerebro.orientacion.y == -1.0) {
                            accion = Types.ACTIONS.ACTION_USE;
                        }
                        break;
                    case ACTION_RIGHT:
                        if (cerebro.orientacion.x == 1.0) {
                            accion = Types.ACTIONS.ACTION_USE;
                        }
                        break;
                    case ACTION_DOWN:
                        if (cerebro.orientacion.y == 1.0) {
                            accion = Types.ACTIONS.ACTION_USE;
                        }
                        break;

                }
            }

        }


    }

}