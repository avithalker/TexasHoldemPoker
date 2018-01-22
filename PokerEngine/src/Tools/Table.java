package Tools;

import Common.PlayerUtilities.PokerTableView;
import com.rundef.poker.Card;

import java.util.ArrayList;

public class Table {

    private final int MAX_CARDS = 5;

    private ArrayList<Card> tableCards;
    private int pot;
    private int potLastHandLeftOver;
    private int betSize;

    public Table(){
        this.tableCards = new ArrayList<>(MAX_CARDS);
        this.pot = 0;
        this.betSize = 0;
        this.potLastHandLeftOver = 0;
    }

    public int getPot(){
        return this.pot;
    }

    public PokerTableView getTableView() {
        PokerTableView tableView = new PokerTableView();

        tableView.setPot(this.pot);
        tableView.setBetSize(betSize);
        String[] cardsView = this.tableCards.stream().map(c -> c.toString()).toArray(String[]::new);
        tableView.setTableCards(cardsView);

        return tableView;
    }


    public Card[] getTableCards(){
        Card[] cardsResult = new Card[tableCards.size()];
        cardsResult = this.tableCards.toArray(cardsResult);
        return cardsResult;
    }

    public String [] getTableCardsToString(){
        return tableCards.stream().map(c -> c.toString()).toArray(String[]::new);
    }

    public void initNewHandRound(){
        this.pot = potLastHandLeftOver;
        potLastHandLeftOver = 0;
        this.betSize = 0;
        this.tableCards.clear();
    }

    public void increasePot(int bet){

        this.pot += bet;
    }

    public void updateBetSize(int newBetSize){
        betSize = newBetSize;
    }

    public void exposeNewCard(Card newCard) {
        if (this.tableCards.size() == 5)
            return;
        this.tableCards.add(newCard);
    }

    public int getBetSize() {
        return betSize;
    }

    public void setPotLastHandLeftOver(int potLastHandLeftOver) {
        this.potLastHandLeftOver = potLastHandLeftOver;
    }

    public int getPotLastHandLeftOver() {
        return potLastHandLeftOver ;
    }
}
