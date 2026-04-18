package si2026.rubencondealu.p01;

import ontology.Types;

import java.util.List;

public class Regla {
    List<Condicion> antecedentes;
    Types.ACTIONS action;

    public Regla(List<Condicion> antecedentes, Types.ACTIONS action) {
        this.antecedentes = antecedentes;
        this.action = action;
    }

    public List<Condicion> getAntecedentes() {
        return antecedentes;
    }
    public void setAntecedentes(List<Condicion> antecedentes) {
        this.antecedentes = antecedentes;
    }

    public Types.ACTIONS getAction() {
        return action;
    }

    public void setAction(Types.ACTIONS action) {
        this.action = action;
    }

    public boolean seCumple(Cerebro cerebro) {
        boolean seCumple = true;
        for (Condicion condicion : antecedentes) {
            if(!condicion.seCumple(cerebro)){
                seCumple = false;
            }
        }

        return seCumple;
    }



}
