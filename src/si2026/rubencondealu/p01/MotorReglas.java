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
            boolean seCumple = true;
            for (Condicion c : r.antecedentes) {
                if (!c.seCumple(cerebro)) {
                    seCumple = false;
                    break;
                }
            }
            if (seCumple) return r;

        }

        return new Regla(new ArrayList<Condicion>() , Types.ACTIONS.ACTION_USE);

    }


}
