package Tools;

import com.rundef.poker.Card;
import com.rundef.poker.CardRank;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CardsDeck {

    private final int CARDS_IN_DECK = 52;
    private final Card.Suit[] cardSuits = {Card.Suit.CLUB,Card.Suit.SPADE,Card.Suit.HEART,Card.Suit.DIAMOND};

    private Set<String> usedCards;

    public CardsDeck(){
        this.usedCards = new HashSet<String>();
    }


    public Card[] getPlayerHandCards() {
        Card [] playerCards = new Card[2];
        playerCards[0] = getCard();
        playerCards[1] = getCard();
        return playerCards;
    }

    public Card[] getFlopCards(){
        Card [] flopCards = new Card[3];
        flopCards[0] = getCard();
        flopCards[1] = getCard();
        flopCards[2] = getCard();
        return flopCards;
    }

    public void initNewHandRound(){
        this.usedCards.clear();
    }

    public Card getCard(){

        String cardKey;
        CardRank cardRank;
        Card.Suit cardSuit;
        if (usedCards.size() >= CARDS_IN_DECK){
            return null;
        }
        do {
            cardRank = getRandomRank();
            cardSuit = getRandomSuit();
            cardKey = cardRank.toString() + cardSuit;
        }while (usedCards.contains(cardKey));

        usedCards.add(cardKey);
        return new Card(cardSuit,cardRank);
    }

    private CardRank getRandomRank() {
        int rank = new Random().nextInt(13) + 2;
        CardRank cardRank = new CardRank(rank);
        return cardRank;
    }

    private Card.Suit getRandomSuit(){
        int suitIndex = new Random().nextInt(4);
        return cardSuits[suitIndex];
    }

    private String cardSuitToString(Card.Suit cardSuit){
        switch (cardSuit){
            case CLUB: return "C";
            case HEART: return "H";
            case SPADE: return "S";
            case DIAMOND: return "D";
            default: return null;
        }
    }
}
