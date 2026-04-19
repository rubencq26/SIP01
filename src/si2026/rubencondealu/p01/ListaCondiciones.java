package si2026.rubencondealu.p01;

import ontology.Types;
import tools.Vector2d;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Queue;

public class ListaCondiciones {

    public static class MiroIzquierda implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.x == -1.0;
        }

    }

    public static class NoMiroIzquierda implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.x != -1.0;
        }
    }


    public static class MiroArriba implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.y == 1.0;
        }
    }

    public static class NoMiroArriba implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.y != 1.0;
        }
    }

    public static class MiroDerecha implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.x == 1.0;
        }
    }

    public static class NoMiroDerecha implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.x != 1.0;
        }
    }



    public static class MiroAbajo implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.y == 1.0;
        }
    }

    public static class NoMiroAbajo implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.y != 1.0;
        }
    }

    public static class PeligroIzquierda implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.direccionPeligro.contains(0);
        }
    }

    public static class PeligroArriba implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.direccionPeligro.contains(1);
        }
    }

    public static class PeligroDerecha implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.direccionPeligro.contains(2);
        }
    }

    public static class PeligroAbajo implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.direccionPeligro.contains(3);
        }
    }

    public static class RompeMuroMata implements Condicion{

        @Override
        public boolean seCumple(Cerebro cerebro) { return cerebro.mataSiDispara(Types.ACTIONS.ACTION_USE);}
    }

    public static class RompeMuroArribaMata implements Condicion{
        @Override
        public boolean seCumple(Cerebro cerebro) {
            ArrayList< Types.ACTIONS> acciones = new ArrayList<>();
            acciones.add(Types.ACTIONS.ACTION_UP);
            acciones.add(Types.ACTIONS.ACTION_USE);
            return cerebro.mataSiDispara(acciones);
        }
    }

    public static class RompeMuroIzquierdaMata implements Condicion{
        @Override
        public boolean seCumple(Cerebro cerebro) {
            ArrayList< Types.ACTIONS> acciones = new ArrayList<>();
            acciones.add(Types.ACTIONS.ACTION_LEFT);
            acciones.add(Types.ACTIONS.ACTION_USE);
            return cerebro.mataSiDispara(acciones);
        }
    }

    public static class RompeMuroDerechaMata implements Condicion{
        @Override
        public boolean seCumple(Cerebro cerebro) {
            ArrayList< Types.ACTIONS> acciones = new ArrayList<>();
            acciones.add(Types.ACTIONS.ACTION_RIGHT);
            acciones.add(Types.ACTIONS.ACTION_USE);
            return cerebro.mataSiDispara(acciones);
        }
    }


    public static class RompeMuroAbajoMata implements Condicion{
        @Override
        public boolean seCumple(Cerebro cerebro) {
            ArrayList< Types.ACTIONS> acciones = new ArrayList<>();
            acciones.add(Types.ACTIONS.ACTION_DOWN);
            acciones.add(Types.ACTIONS.ACTION_USE);
            return cerebro.mataSiDispara(acciones);
        }
    }


    public static class ArmaRecargada implements Condicion{
        @Override
        public boolean seCumple(Cerebro cerebro) {return cerebro.cooldown < 1;}
    }


    public static class SiDisparaMata implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.mataSiDispara();
        }
    }

    public static class SiDisparaIzqMata implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.mataSiDispara(Types.ACTIONS.ACTION_LEFT);
        }
    }

    public static class SiDisparaDerMata implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {

            ArrayList<ArrayList<Vector2d>> bichi = new ArrayList<>();


            return cerebro.mataSiDispara(Types.ACTIONS.ACTION_RIGHT);

        }
    }


    public static class SiDisparaArrMata implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.mataSiDispara(Types.ACTIONS.ACTION_UP);
        }
    }

    public static class SiDisparaAbjMata implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.mataSiDispara(Types.ACTIONS.ACTION_DOWN);
        }
    }


    public static class SiMeMuevoIzqMuero implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.siMeMuevoMuero(Types.ACTIONS.ACTION_LEFT);
        }
    }

    public static class SiMeMuevoDerMuero implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.siMeMuevoMuero(Types.ACTIONS.ACTION_RIGHT);
        }
    }

    public static class SiMeMuevoArrMuero implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.siMeMuevoMuero(Types.ACTIONS.ACTION_UP);
        }
    }

    public static class SiMeMuevoAbjMuero implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.siMeMuevoMuero(Types.ACTIONS.ACTION_DOWN);
        }
    }

    public static class SiMeMuevoIzqVivo implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return !cerebro.siMeMuevoMuero(Types.ACTIONS.ACTION_LEFT);
        }
    }

    public static class SiMeMuevoDerVivo implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return !cerebro.siMeMuevoMuero(Types.ACTIONS.ACTION_RIGHT);
        }
    }

    public static class SiMeMuevoArrVivo implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return !cerebro.siMeMuevoMuero(Types.ACTIONS.ACTION_UP);
        }
    }

    public static class SiMeMuevoAbjVivo implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return !cerebro.siMeMuevoMuero(Types.ACTIONS.ACTION_DOWN);
        }
    }

    public static class AstarIzquierda implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.action == Types.ACTIONS.ACTION_LEFT;
        }
    }

    public static class AstarArriba implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.action == Types.ACTIONS.ACTION_UP;
        }
    }

    public static class AstarDerecha implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.action == Types.ACTIONS.ACTION_RIGHT;
        }
    }

    public static class AstarAbajo implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.action == Types.ACTIONS.ACTION_DOWN;
        }
    }

    public static class AstarUse implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.action == Types.ACTIONS.ACTION_USE ;
        }
    }

    public static class ConTiempo implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.timer.remainingTimeMillis() > 6;
        }
    }



}
