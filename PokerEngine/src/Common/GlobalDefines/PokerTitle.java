package Common.GlobalDefines;

public enum  PokerTitle {
    REGULAR{
        @Override
        public String toString() {
            return " ";
        }
    },
    DEALER{
        @Override
        public String toString() {
            return "Dealer";
        }
    },
    SMALL{
        @Override
        public String toString() {
            return "Small";
        }
    },
    BIG{
        @Override
        public String toString() {
            return "Big";
        }
    }
}
