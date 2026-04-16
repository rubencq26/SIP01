package si2026.rubencondealu.p01;

import ontology.Types;
import tools.Vector2d;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Astar {

    private Types.ACTIONS accion = Types.ACTIONS.ACTION_NIL;

    public Types.ACTIONS getAccion() { return accion; }

    public Astar(Cerebro cerebro) {
        Vector2d avatarNormalizado = cerebro.avatar;
        if (cerebro.enemigos == null || cerebro.enemigos.isEmpty()) return;

        // 1. Buscar enemigo más cercano
        Vector2d metaNormalizada = null;
        double mejorDist = Double.MAX_VALUE;
        for (Vector2d e : cerebro.enemigos) {
            double d = avatarNormalizado.dist(e);
            if (d < mejorDist) { mejorDist = d; metaNormalizada = e; }
        }
        if (metaNormalizada == null) return;

        // 2. Configurar A* con Heurística Inflada (Greedy)
        // Usamos un comparador que priorice llegar rápido
        PriorityQueue<Nodo> abiertos = new PriorityQueue<>(Comparator.comparingDouble(n -> n.heuristica));
        ArrayList<Nodo> cerrados = new ArrayList<>();

        abiertos.add(new Nodo(avatarNormalizado, 0, metaNormalizada, null));

        Nodo nodoFinal = null;
        int iteraciones = 0;

        while (!abiertos.isEmpty() && iteraciones < 2000) {
            iteraciones++;
            Nodo actual = abiertos.poll();

            // --- MEJORA: META TÁCTICA ---
            // No queremos pisar al enemigo, queremos estar alineados a 2-4 casillas
            double distAMeta = actual.posicion.dist(metaNormalizada);
            boolean alineado = (int)actual.posicion.x == (int)metaNormalizada.x ||
                    (int)actual.posicion.y == (int)metaNormalizada.y;

            if (alineado && distAMeta <= 4 && distAMeta >= 2) {
                nodoFinal = actual;
                break;
            }

            // Guardar el que más se acerque por si acaso
            if (nodoFinal == null || distAMeta < nodoFinal.posicion.dist(metaNormalizada)) {
                nodoFinal = actual;
            }

            cerrados.add(actual);

            Vector2d[] dirs = {new Vector2d(1, 0), new Vector2d(-1, 0), new Vector2d(0, 1), new Vector2d(0, -1)};
            for (Vector2d d : dirs) {
                Vector2d sigPos = new Vector2d(actual.posicion.x + d.x, actual.posicion.y + d.y);
                if (estaEnLista(cerrados, sigPos)) continue;

                // --- MEJORA: COSTE AGRESIVO ---
                // Si el muro cuesta 3, el agente preferirá romperlo antes que dar un rodeo de 4 pasos
                int costePaso = 1;
                if (estaEnListaMuros(cerebro.obstaculos, sigPos)) {
                    costePaso = 3;
                }

                // --- MEJORA: PENALIZACIÓN POR GIRO ---
                // Si cambiamos de dirección respecto al padre, añadimos un pequeño coste
                // Esto obliga al A* a preferir líneas rectas (movimiento más eficiente)
                if (actual.padre != null) {
                    double dxPrev = actual.posicion.x - actual.padre.posicion.x;
                    double dyPrev = actual.posicion.y - actual.padre.posicion.y;
                    if (d.x != dxPrev || d.y != dyPrev) {
                        costePaso += 1;
                    }
                }

                Nodo vecino = new Nodo(sigPos, actual.costo + costePaso, metaNormalizada, actual);
                abiertos.add(vecino);
            }
        }

        // 3. Reconstrucción
        if (nodoFinal != null && nodoFinal.padre != null) {
            Nodo paso = nodoFinal;
            while (paso.padre != null && paso.padre.padre != null) {
                paso = paso.padre;
            }

            if (estaEnListaMuros(cerebro.obstaculos, paso.posicion)) {
                this.accion = Types.ACTIONS.ACTION_USE;
            } else {
                this.accion = calcularDireccion(avatarNormalizado, paso.posicion);
            }
        }
    }

    private boolean estaEnLista(ArrayList<Nodo> lista, Vector2d pos) {
        for (Nodo n : lista) {
            if ((int)n.posicion.x == (int)pos.x && (int)n.posicion.y == (int)pos.y) return true;
        }
        return false;
    }

    private boolean estaEnListaMuros(ArrayList<Vector2d> lista, Vector2d pos) {
        for (Vector2d m : lista) {
            if ((int)m.x == (int)pos.x && (int)m.y == (int)pos.y) return true;
        }
        return false;
    }

    private Types.ACTIONS calcularDireccion(Vector2d origen, Vector2d destino) {
        int dx = (int)destino.x - (int)origen.x;
        int dy = (int)destino.y - (int)origen.y;
        if (dx > 0) return Types.ACTIONS.ACTION_RIGHT;
        if (dx < 0) return Types.ACTIONS.ACTION_LEFT;
        if (dy > 0) return Types.ACTIONS.ACTION_DOWN;
        if (dy < 0) return Types.ACTIONS.ACTION_UP;
        return Types.ACTIONS.ACTION_NIL;
    }
}