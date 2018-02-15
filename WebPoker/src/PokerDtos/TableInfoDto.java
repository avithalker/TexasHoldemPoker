package PokerDtos;

public class TableInfoDto {
    private final String DEFAULT_CARD = "card";
    private int pot;
    private String[] cards;

    public void setPot(int pot) {
        this.pot = pot;
    }

    public void setCards(String[] cards) {
        this.cards = new String[5];
        int i;
        for(i =0;i < cards.length;i++){
            this.cards[i] = cards[i].toUpperCase();
        }
        for(;i < 5;i++){
            this.cards[i] = DEFAULT_CARD;
        }
    }

    public int getPot() {
        return pot;
    }
}
