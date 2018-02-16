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
    };

    public static PokerAction parseFromInt(int action){
        switch (action){
            case 1: return BET;
            case 2: return CHECK;
            case 3: return RAISE;
            case 4: return FOLD;
            case 5: return CALL;
            default: return NONE;
        }

    }
}
