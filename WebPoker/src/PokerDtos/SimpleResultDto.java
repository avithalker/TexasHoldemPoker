package PokerDtos;

public class SimpleResultDto {
    private boolean result;

    public SimpleResultDto(boolean result){
        this.result = result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
