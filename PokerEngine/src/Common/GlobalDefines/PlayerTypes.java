package Common.GlobalDefines;

public enum PlayerTypes {
    Computer{
        @Override
        public String toString() {
            return "Computer";
        }
    },
    Human{
        @Override
        public String toString() {
            return "Human";
        }
    }
}
