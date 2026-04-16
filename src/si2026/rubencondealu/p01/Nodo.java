package si2026.rubencondealu.p01;

import tools.Vector2d;

import java.util.Objects;

public class Nodo {
    public Vector2d posicion;
    public int costo;
    public double heuristica;
    public Nodo padre;

    public Nodo(Vector2d posicion, int costo, Vector2d meta, Nodo padre){
        this.posicion = posicion;
        this.costo = costo;
        this.padre = padre;
        calcularHeuristica(meta);
    }

    private void calcularHeuristica(Vector2d meta){
        double dx = Math.abs(meta.x - posicion.x);
        double dy = Math.abs(meta.y - posicion.y);


        this.heuristica = (dx + dy) * 1.5 + costo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Nodo nodo = (Nodo) o;
        return Objects.equals(posicion, nodo.posicion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(posicion);
    }
}
