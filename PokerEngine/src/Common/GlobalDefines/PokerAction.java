package Common.GlobalDefines;

public enum PokerAction {
    NONE{
        @Override
        public String toString() {
            return "None";
        }
    },
    FOLD{
        @Override
        public String toString() {
            return "Fold";
        }
    },
    BET{
        @Override
        public String toString() {
            return "Bet";
        }
    },
    CALL{
        @Override
        public String toString() {
            return "Call";
        }
    },
    CHECK{
        @Override
        public String toString() {
            return "Check";
        }
    },
    RAISE{
        @Override
        public String toString() {
            return "Raise";
        }
    }
}
