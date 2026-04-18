package si2026.rubencondealu.p01;

import ontology.Types;
import tools.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class MotorReglas {
    private List<Regla> reglas;
    private Cerebro cerebro;

    public MotorReglas(List<Regla> r, Cerebro c) {
        this.reglas = r;
        this.cerebro = c;
    }

    public Regla disparo() {

        for (Regla r : reglas) {
            if (r.seCumple(cerebro)) {
                return r;
            }

        }

        return null;

    }


}
