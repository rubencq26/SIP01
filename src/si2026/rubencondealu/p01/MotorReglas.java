package si2026.rubencondealu.p01;

import ontology.Types;
import tools.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class MotorReglas {
    private List<Condicion> reglas;
    private Cerebro cerebro;

    public MotorReglas(List<Condicion> r, Cerebro c) {
        this.reglas = r;
        this.cerebro = c;
    }

    public Types.ACTIONS disparo() {
        Types.ACTIONS acc = null;
        for (int i = 0; i < reglas.size(); i++) {
            Condicion regla = reglas.get(i);
            if (regla.seCumple(cerebro)) {
                switch (i) {
                    case 0:
                        CondicionPeligro c = (CondicionPeligro) regla;
                        return c.decidirDireccion(cerebro);
                    case 1:
                        CondicionDisparo d = (CondicionDisparo) regla;
                        return d.decidirDireccion(cerebro);

                    case 2:
                        CondicionDisparoObstaculizado d2 = (CondicionDisparoObstaculizado) regla;
                        return d2.decidirDireccionDisparo(cerebro);
                }
            }


        }


        Astar astar = new Astar(cerebro);

        return astar.getAccion();
    }


}
