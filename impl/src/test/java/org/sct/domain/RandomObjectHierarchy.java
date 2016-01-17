package org.sct.domain;

public class RandomObjectHierarchy {

    private Hierarchy01 hierarchy;

    public RandomObjectHierarchy(String data) {
        Hierarchy06 h6 = new Hierarchy06(data, null);
        Hierarchy05 h5 = new Hierarchy05(data, h6);
        Hierarchy04 h4 = new Hierarchy04(data, h5);
        Hierarchy03 h3 = new Hierarchy03(data, h4);
        Hierarchy02 h2 = new Hierarchy02(data, h3);
        this.hierarchy = new Hierarchy01(data, h2);
    }

    private static class Hierarchy {
    }

    private static class Hierarchy01 extends Hierarchy {

        private String name = getClass().getName();

        private String data;

        private Hierarchy hierarchy;

        public Hierarchy01(String data, Hierarchy hierarchy) {
            this.data = data;
            this.hierarchy = hierarchy;
        }
    }

    private static class Hierarchy02 extends Hierarchy {

        private String name = getClass().getName();

        private String data;

        private Hierarchy hierarchy;

        public Hierarchy02(String data, Hierarchy hierarchy) {
            this.data = data;
            this.hierarchy = hierarchy;
        }

    }

    private static class Hierarchy03 extends Hierarchy {

        private String name = getClass().getName();

        private String data;

        private Hierarchy hierarchy;

        public Hierarchy03(String data, Hierarchy hierarchy) {
            this.data = data;
            this.hierarchy = hierarchy;
        }

    }

    private static class Hierarchy04 extends Hierarchy {

        private String name = getClass().getName();

        private String data;

        private Hierarchy hierarchy;

        public Hierarchy04(String data, Hierarchy hierarchy) {
            this.data = data;
            this.hierarchy = hierarchy;
        }

    }

    private static class Hierarchy05 extends Hierarchy {

        private String name = getClass().getName();

        private String data;

        private Hierarchy hierarchy;

        public Hierarchy05(String data, Hierarchy hierarchy) {
            this.data = data;
            this.hierarchy = hierarchy;
        }

    }

    private static class Hierarchy06 extends Hierarchy {

        private String name = getClass().getName();

        private String data;

        private Hierarchy hierarchy;

        public Hierarchy06(String data, Hierarchy hierarchy) {
            this.data = data;
            this.hierarchy = hierarchy;
        }

    }

    public Hierarchy01 getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Hierarchy01 hierarchy) {
        this.hierarchy = hierarchy;
    }
}
