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

    public Astar(Cerebro cerebro, Vector2d meta) {
        this.cerebro = cerebro;
        Vector2d avatarNormalizado = cerebro.avatar;
        if (cerebro.enemigos == null || cerebro.enemigos.isEmpty() || cerebro.enemigoCercano == null) return;

        Vector2d metaNormalizada = meta;

        // PriorityQueue sigue siendo eficiente
        PriorityQueue<Nodo> abiertos = new PriorityQueue<>(Comparator.comparingDouble(n -> n.heuristica));

        // CAMBIO 1: Usamos un HashSet de IDs únicos (x * 100 + y) para búsqueda O(1)
        HashSet<Integer> cerradosHash = new HashSet<>();

        abiertos.add(new Nodo(avatarNormalizado, 0, metaNormalizada, null));

        Nodo nodoFinal = null;

        // CAMBIO 2: Límite de iteraciones drásticamente más bajo (100-200 es suficiente)
        int iteraciones = 0;
        int maxIteraciones = 100;

        while (!abiertos.isEmpty() && iteraciones < maxIteraciones) {
            iteraciones++;
            Nodo actual = abiertos.poll();

            int idActual = (int) actual.posicion.x * 100 + (int) actual.posicion.y;
            if (cerradosHash.contains(idActual)) continue;
            cerradosHash.add(idActual);

            double distAMeta = actual.posicion.dist(metaNormalizada);
            boolean alineado = (int) actual.posicion.x == (int) metaNormalizada.x ||
                    (int) actual.posicion.y == (int) metaNormalizada.y;

            // Meta encontrada: alineado y en rango
            if (alineado && distAMeta <= 6 && distAMeta >= 1) {
                nodoFinal = actual;
                break;
            }

            // Guardamos siempre el "mejor hasta ahora" por si agotamos iteraciones
            if (nodoFinal == null || distAMeta < nodoFinal.posicion.dist(metaNormalizada)) {
                nodoFinal = actual;
            }

            Vector2d[] dirs = {new Vector2d(1, 0), new Vector2d(-1, 0), new Vector2d(0, 1), new Vector2d(0, -1)};
            for (Vector2d d : dirs) {
                Vector2d sigPos = new Vector2d(actual.posicion.x + d.x, actual.posicion.y + d.y);

                // Límites del mapa
                if (sigPos.x < cerebro.limitexmin || sigPos.x > cerebro.limitexmax ||
                        sigPos.y < cerebro.limiteymin || sigPos.y > cerebro.limiteymax) continue;

                // Búsqueda instantánea en el hash
                if (cerradosHash.contains((int) sigPos.x * 100 + (int) sigPos.y)) continue;

                int costePaso = 1;
                if (estaEnListaMuros(cerebro.matrizObstaculos, sigPos)) {
                    costePaso = 2; // Coste de romper muro
                }

                // Penalización por giro
                if (actual.padre != null) {
                    double dxPrev = actual.posicion.x - actual.padre.posicion.x;
                    double dyPrev = actual.posicion.y - actual.padre.posicion.y;
                    if (d.x != dxPrev || d.y != dyPrev) costePaso += 0.5;
                }

                abiertos.add(new Nodo(sigPos, actual.costo + costePaso, metaNormalizada, actual));
            }
        }

        // 3. Reconstrucción de la acción (Se mantiene tu lógica de encaramiento)
        if (nodoFinal != null && nodoFinal.padre != null) {
            Nodo paso = nodoFinal;
            while (paso.padre != null && paso.padre.padre != null) {
                paso = paso.padre;
            }

            Types.ACTIONS direccionNecesaria = calcularDireccion(avatarNormalizado, paso.posicion);
            Vector2d oriActual = cerebro.orientacion;

            Vector2d vNec = new Vector2d(0, 0);
            if (direccionNecesaria == Types.ACTIONS.ACTION_UP) vNec.set(0, -1);
            else if (direccionNecesaria == Types.ACTIONS.ACTION_DOWN) vNec.set(0, 1);
            else if (direccionNecesaria == Types.ACTIONS.ACTION_LEFT) vNec.set(-1, 0);
            else if (direccionNecesaria == Types.ACTIONS.ACTION_RIGHT) vNec.set(1, 0);

            // Comparación robusta
            boolean estaEncarado = Math.abs(vNec.x - oriActual.x) < 0.1 &&
                    Math.abs(vNec.y - oriActual.y) < 0.1;

            // Acceso a matriz con casteo seguro
            int px = (int) paso.posicion.x;
            int py = (int) paso.posicion.y;

            if (cerebro.matrizObstaculos[px][py]) {
                // SI hay muro: disparar si estoy encarado, si no, GIRAR
                this.accion = estaEncarado ? Types.ACTIONS.ACTION_USE : direccionNecesaria;
            } else {
                // NO hay muro: simplemente moverse
                this.accion = direccionNecesaria;
            }
        }
    }

    // Nota: Para optimizar más, 'cerebro.obstaculos' debería ser una matriz boolean[][] en el Cerebro
    private boolean estaEnListaMuros(boolean[][] lista, Vector2d pos) {

        if ( lista[(int)pos.x][(int)pos.y]) return true;


        return false;
    }

    private Types.ACTIONS calcularDireccion(Vector2d origen, Vector2d destino) {
        int dx = (int) destino.x - (int) origen.x;
        int dy = (int) destino.y - (int) origen.y;
        if (dx > 0) return Types.ACTIONS.ACTION_RIGHT;
        if (dx < 0) return Types.ACTIONS.ACTION_LEFT;
        if (dy > 0) return Types.ACTIONS.ACTION_DOWN;
        if (dy < 0) return Types.ACTIONS.ACTION_UP;
        return Types.ACTIONS.ACTION_NIL;
    }
}