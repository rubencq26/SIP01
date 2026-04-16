package si2026.rubencondealu.p01;

import ontology.Types;
import tools.Vector2d;

import java.util.ArrayList;

public class CondicionPeligro implements Condicion{

    public Vector2d peligro;

    CondicionPeligro(){

    }

    @Override
    public boolean seCumple(Cerebro cerebro) {
        double mejorDistancia = Double.MAX_VALUE;
        boolean pel = false;
        Vector2d avatar = cerebro.avatar;

        for(Vector2d misil : cerebro.proyectiles){
            if((int)misil.x == (int)avatar.x || (int)misil.y == (int)avatar.y){
                double distancia = misil.dist(avatar);
                if(distancia <= 4 && distancia < mejorDistancia){
                    mejorDistancia = distancia;
                    pel = true;
                    this.peligro = new Vector2d(misil.x, misil.y);
                }
            }
        }
        return pel;
    }

    public Types.ACTIONS decidirDireccion(Cerebro cerebro){
        Vector2d av = cerebro.avatar;

        if(peligro.x == av.x){
            if(cerebro.puedoMoverme(Types.ACTIONS.ACTION_RIGHT)) return Types.ACTIONS.ACTION_RIGHT;
            if(cerebro.puedoMoverme(Types.ACTIONS.ACTION_LEFT)) return Types.ACTIONS.ACTION_LEFT;
        }

        if(peligro.y == av.y){
            if(cerebro.puedoMoverme(Types.ACTIONS.ACTION_UP)) return Types.ACTIONS.ACTION_UP;
            if(cerebro.puedoMoverme(Types.ACTIONS.ACTION_DOWN)) return Types.ACTIONS.ACTION_DOWN;
        }
        return Types.ACTIONS.ACTION_USE;
    }
}
