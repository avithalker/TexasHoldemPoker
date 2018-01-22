package Common;

public class ActionResult {

    private boolean isSucceed;
    private String msgError;

    public ActionResult(){

    }

    public ActionResult(boolean isSucceed, String msgError){
        this.isSucceed = isSucceed;
        this.msgError = msgError;
    }

    public boolean isSucceed() {
        return this.isSucceed;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setSucceed(boolean isSucceed){
        this.isSucceed = isSucceed;
    }

    public void setMsgError(String msgError){
        this.msgError = msgError;
    }
}
