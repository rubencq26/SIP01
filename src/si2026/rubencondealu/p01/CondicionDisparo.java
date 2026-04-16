package si2026.rubencondealu.p01;

import core.game.Observation;
import ontology.Types;
import tools.Vector2d;

public class CondicionDisparo implements Condicion{

    public Vector2d enemigo;

    @Override
    public boolean seCumple(Cerebro cerebro) {
        Vector2d avatar = cerebro.avatar;
        double mejorDistancia = Double.MAX_VALUE;
        boolean puedeDisparar = false;
        double rangoMaximo =  6;

        for (Vector2d enemig : cerebro.enemigos) {

            double distancia = avatar.dist(enemig);

            if (distancia < rangoMaximo) {
                boolean alineadoX = (int)enemig.x == (int)avatar.x;
                boolean alineadoY = (int)enemig.y == (int)avatar.y;

                if (alineadoX || alineadoY) {
                    boolean caminoBloqueado = false;


                    for (Vector2d obst : cerebro.obstaculos) {
                        if (alineadoX && (int)obst.x == (int)avatar.x) {
                            if (isBetween(avatar.y, enemig.y, obst.y)) {
                                caminoBloqueado = true;
                                break;
                            }
                        } else if (alineadoY && (int)obst.y == (int)avatar.y) {
                            if (isBetween(avatar.x, enemig.x, obst.x)) {
                                caminoBloqueado = true;
                                break;
                            }
                        }
                    }


                    if (!caminoBloqueado) {

                        if (distancia < mejorDistancia) {
                            mejorDistancia = distancia;
                            this.enemigo = new Vector2d(enemig.x, enemig.y);
                            puedeDisparar = true;
                        }
                    }
                }
            }
        }
        return puedeDisparar;
    }

    private boolean isBetween(double a, double b, double c) {
        return c > Math.min(a, b) && c < Math.max(a, b);
    }

    public Types.ACTIONS decidirDireccion(Cerebro cerebro) {
        if(enemigo == null) return Types.ACTIONS.ACTION_NIL;
        Vector2d av = cerebro.avatar;
        Vector2d ori = cerebro.orientacion;
        int dirObjetivo;
        int miOrientacion;


        if ((int)av.x == (int)enemigo.x) {
            dirObjetivo = (enemigo.y > av.y) ? 3 : 1;
        } else {
            dirObjetivo = (enemigo.x > av.x) ? 2 : 0;
        }

        if (ori.x < -0.9) miOrientacion = 0;      // Left
        else if (ori.y < -0.9) miOrientacion = 1; // Up
        else if (ori.x > 0.9) miOrientacion = 2;  // Right
        else miOrientacion = 3;

        if (dirObjetivo == miOrientacion) {
            return Types.ACTIONS.ACTION_USE;
        } else {
            switch (dirObjetivo) {
                case 0:
                    return Types.ACTIONS.ACTION_LEFT;
                case 1:
                    return Types.ACTIONS.ACTION_UP;
                case 2:
                    return Types.ACTIONS.ACTION_RIGHT;
                case 3:
                    return Types.ACTIONS.ACTION_DOWN;
                default:
                    return Types.ACTIONS.ACTION_USE;
            }
        }





    }

}
