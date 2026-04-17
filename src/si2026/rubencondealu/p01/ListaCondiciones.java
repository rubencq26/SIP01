package si2026.rubencondealu.p01;

import ontology.Types;

public class ListaCondiciones {

    public static class MiroIzquierda implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.x == -1.0;
        }
    }

    public static class MiroArriba implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.y == 1.0;
        }
    }

    public static class MiroDerecha implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.x == 1.0;
        }
    }

    public static class MiroAbajo implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.orientacion.y == +1.0;
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

    public static class SiDisparaMata implements Condicion {
        @Override
        public boolean seCumple(Cerebro cerebro) {
            return cerebro.mataSiDispara();
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
}
