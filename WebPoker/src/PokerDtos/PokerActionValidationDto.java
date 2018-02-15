package PokerDtos;

public class PokerActionValidationDto {
    private boolean isBetValid;
    private boolean isCallValid;
    private boolean isRaiseValid;
    private boolean isCheckValid;
    private boolean isFoldValid;

    public PokerActionValidationDto(){
        isBetValid =false;
        isCallValid = false;
        isCheckValid = false;
        isFoldValid = false;
        isRaiseValid = false;
    }

    public void setBetValid(boolean betValid) {
        isBetValid = betValid;
    }

    public void setCallValid(boolean callValid) {
        isCallValid = callValid;
    }

    public void setRaiseValid(boolean raiseValid) {
        isRaiseValid = raiseValid;
    }

    public void setCheckValid(boolean checkValid) {
        isCheckValid = checkValid;
    }

    public void setFoldValid(boolean foldValid) {
        isFoldValid = foldValid;
    }

    public boolean isBetValid() {
        return isBetValid;
    }

    public boolean isCallValid() {
        return isCallValid;
    }

    public boolean isRaiseValid() {
        return isRaiseValid;
    }

    public boolean isCheckValid() {
        return isCheckValid;
    }

    public boolean isFoldValid() {
        return isFoldValid;
    }
}
