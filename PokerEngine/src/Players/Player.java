package Players;

import Common.ActionResult;
import Common.GlobalDefines.PlayerTypes;
import Common.GlobalDefines.PokerAction;
import Common.GlobalDefines.PokerTitle;
import Common.PlayerUtilities.PlayerInfo;
import Players.Contracts.IPokerContract;
import com.rundef.poker.Card;
import com.rundef.poker.Hand;

import javax.print.attribute.standard.NumberUp;

public class Player implements IPokerContract {

    private final int MAX_CARDS = 2;
    private final int FIRST_CARD = 0;
    private final int SECOND_CARD = 1;

    private int playerId;
    private String playerName;
    private PokerTitle title;
    private PokerAction lastAction;
    private PlayerStatistics statistics;
    private PlayerTypes playerType;
    private Card [] cards;
    private int tokens;
    private int currentBet;
    private int totalBuys;
    private int totalWins;
    private int totalHandsPlayed;
    private boolean isPlayerFoldFromEntireGame;


    public Player(int playerId, PlayerTypes playerType, int tokens, String playerName){
        this.playerId = playerId;
        this.playerName = playerName;
        this.cards = new Card[MAX_CARDS];
        this.playerType = playerType;
        this.tokens = tokens;
        this.title = PokerTitle.REGULAR;
        this.totalBuys = 1;
        isPlayerFoldFromEntireGame = false;
    }

    public int getPlayerId(){

        return this.playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getTokens(){
        return  this.tokens;
    }

    public int getCurrentBet(){

        return this.currentBet;
    }

    public Card[] getCards() {

        return this.cards;
    }

    public Hand getHand(){
        return new Hand(cards[0],cards[1]);
    }

    public PokerAction getLastAction() {

        return lastAction;
    }

    public PokerTitle getTitle() {
        return title;
    }

    public int getTotalBuys() {
        return totalBuys;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalHandsPlayed() { return totalHandsPlayed; }

    public boolean isPlayerFoldFromEntireGame() {
        return isPlayerFoldFromEntireGame;
    }

    public void setPlayerFoldFromEntireGame(boolean playerFoldFromEntireGame) {
        isPlayerFoldFromEntireGame = playerFoldFromEntireGame;
    }


    public PlayerInfo getPlayerInfo()
    {
        PlayerInfo info = new PlayerInfo();
        info.setPlayerId(this.playerId);
        info.setPlayerName(playerName);
        info.setCurrentBet(this.currentBet);
        info.setPlayerTitle(this.title);
        info.setPlayerType(this.playerType);
        info.setTokens(this.tokens);
        if(cards[FIRST_CARD] == null || cards[SECOND_CARD] == null)
            info.setPlayerCards(new String[0]);
        else
            info.setPlayerCards(new String[]{this.cards[FIRST_CARD].toString().toUpperCase(),this.cards[SECOND_CARD].toString().toUpperCase()});
        info.setTotalHandsPlayed(this.totalHandsPlayed);
        info.setTotalBuys(this.totalBuys);
        info.setTotalWins(this.totalWins);
        info.setLastAction(lastAction);
        info.setPlayerFoldFromEntireGame(isPlayerFoldFromEntireGame);
        return info;
    }

    public void setLastAction(PokerAction action){

        this.lastAction = action;
    }

    public void setTitle(PokerTitle title) {
        this.title = title;
    }

    public void initNewHandRound(Card[] cards){
        this.cards[FIRST_CARD] = cards[FIRST_CARD];
        this.cards[SECOND_CARD] = cards[SECOND_CARD];
        this.currentBet = 0;
        this.lastAction = PokerAction.NONE;
        this.totalHandsPlayed++;
    }

    public void dropCards(){
        cards[FIRST_CARD] = null;
        cards[SECOND_CARD] = null;
        currentBet = 0;
    }

    public void initNewGambleRound(){
        currentBet = 0;
        lastAction = PokerAction.NONE;
    }

    public void blindBet(int blindValue){
        if(this.tokens < blindValue) {
            increaseBet(this.tokens);
        }
        else{
            increaseBet(blindValue);
        }
    }

    private boolean increaseBet(int raise){
        if(this.tokens < raise)
            return false;
        this.tokens -= raise;
        this.currentBet += raise;
        return true;
    }

    public void addBuy(int tokens,int buyAmount) {
        this.tokens += tokens;
        totalBuys += buyAmount;
    }

    public void notifyOnWin(int winningPrice){
        tokens += winningPrice;
        totalWins++;
    }

    @Override
    public void Fold() {

    }

    @Override
    public ActionResult Bet(int bet)
    {
        if(bet <= 0)
            return new ActionResult(false,"Error- bet value must be positive");

        boolean result = increaseBet(bet);
        ActionResult actionResult = new ActionResult();
        actionResult.setSucceed(result);
        if(!result){
            actionResult.setMsgError("Insufficient tokens for bet action!");
        }
        return actionResult;
    }

    @Override
    public ActionResult Call(int currBetSize)
    {
        int difference = currBetSize - currentBet;
        boolean result = increaseBet(difference);
        ActionResult actionResult = new ActionResult();
        actionResult.setSucceed(result);
        if(!result){
            actionResult.setMsgError("Insufficient tokens for call action!");
        }
        return actionResult;
    }

    @Override
    public void Check() {

    }

    @Override
    public ActionResult Raise(int currBetSize, int raise)
    {
        if(raise <= 0)
            return new ActionResult(false,"Error- raise value must be positive");

        int difference = (currBetSize + raise) - currentBet;

        boolean result = increaseBet(difference);
        ActionResult actionResult = new ActionResult();
        actionResult.setSucceed(result);
        if(!result){
            actionResult.setMsgError("Insufficient tokens for raise action!");
        }
        return actionResult;
    }
}
