package PokerDtos;

public class MsgResultDto {
    private boolean result;
    private String msg;

    public MsgResultDto(boolean result, String msg){

        this.result = result;
        this.msg = msg;
    }

    public void setResult(boolean result) {

        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
