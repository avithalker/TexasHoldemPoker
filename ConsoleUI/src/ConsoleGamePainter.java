import Business.GameStatistics;
import Common.GlobalDefines.GameDefines;
import Common.GlobalDefines.PlayerTypes;
import Common.GlobalDefines.PokerTitle;
import Common.PlayerUtilities.PlayerInfo;
import Common.PlayerUtilities.PokerTableView;
import ConsoleGlobalDefines.ConsoleGlobals;

public class ConsoleGamePainter {

    private static final int SPACES = 35;

    public static void paintGameStatistics(GameStatistics statistics,int smallBlindVal, int bigBlindVal){
        System.out.println();
        System.out.println("Total hands: " + statistics.getToalHandGames());
        System.out.println("Total played hands: " + statistics.getTotalPlayedHands());
        System.out.println("Total tokens in game: " + statistics.getTotalTokens());
        System.out.println("Total time past: " + convertTimeInSec(statistics.getTimePastInSec()));
        PaintPlayersStatus(statistics.getPlayerStatuses(),smallBlindVal, bigBlindVal);
    }

    private static String convertTimeInSec(long totalSec){
        String min = Long.toString((totalSec / 60));
        String sec = Long.toString((totalSec % 60));
        return min + ":" + sec;
    }

    public static void PaintPlayersStatus(PlayerInfo[] playersInfo, int smallBlindVal, int bigBlindVal){
        int i = 0;
        boolean oppositePrint = false;
        for(i = 0;i < playersInfo.length - 1;i += 2){
            if(!oppositePrint)
                paintPlayerStatusCouple(playersInfo[i],playersInfo[i+1], smallBlindVal, bigBlindVal);
            else
                paintPlayerStatusCouple(playersInfo[i+1],playersInfo[i], smallBlindVal, bigBlindVal);
            oppositePrint = !oppositePrint;
        }
        if(i < playersInfo.length){
            paintPlayerStatusSingle(playersInfo[i],smallBlindVal, bigBlindVal);
        }
    }

    public static void PaintGambleRound(PlayerInfo[] playersInfo, PokerTableView tableView){
        int i = 0;
        boolean oppositePrint = false;
        for(i = 0;i < playersInfo.length - 1;i += 2) {
            if (!oppositePrint)
                paintPlayerGameplayCouple(playersInfo[i], playersInfo[i + 1], tableView.getSmallValue(), tableView.getBigValue());
            else
                paintPlayerGameplayCouple(playersInfo[i + 1], playersInfo[i], tableView.getSmallValue(), tableView.getBigValue());
            oppositePrint = !oppositePrint;
        }
        if(i < playersInfo.length){
            paintPlayerGameplaySingle(playersInfo[i], tableView.getSmallValue(),tableView.getBigValue());
        }
        paintTableView(tableView);
    }

    public static void exitGameConfirmation(GameStatistics statistics,int smallBlind, int bigBlind){
        System.out.println();
        if(statistics != null){
            System.out.println("This is the game summery:");
            paintGameStatistics(statistics,smallBlind,bigBlind);
        }
        else {
            System.out.println("Game summery is not available right now.");
        }

        System.out.println("Press any key to exist the game...");
        ConsoleGlobals.getInputScanner().nextLine();
    }

    private static void paintTableView(PokerTableView tableView){
        System.out.println();
        String[] cards = tableView.getTableCards();
        int i = 0;
        for(;i < cards.length; i++ ){
            if(i < GameDefines.MAX_TABLE_CARDS -1)
                System.out.print(cards[i]+" | ");
            else
                System.out.print(cards[i]);
        }
        for(;i<GameDefines.MAX_TABLE_CARDS;i++){
            if(i < GameDefines.MAX_TABLE_CARDS -1)
                System.out.print("?? | ");
            else
                System.out.print("??");
        }

        System.out.println("     Pot:" + tableView.getPot());
    }

    private static  void paintPlayerGameplayCouple(PlayerInfo player1, PlayerInfo player2, int smallBlindVal, int bigBlindVal)
    {
        String firstPlayerText;
        String [] playerCards;
        System.out.println();
        firstPlayerText = "Type: " + player1.getPlayerType();
        System.out.print(firstPlayerText);
        paintSpaces(SPACES - firstPlayerText.length());
        System.out.println("Type: " + player2.getPlayerType());
        firstPlayerText = "State: " + getTitlesFormat(player1.getPlayerTitle(),smallBlindVal,bigBlindVal);
        System.out.print(firstPlayerText);
        paintSpaces(SPACES - firstPlayerText.length());
        System.out.println("State: " + getTitlesFormat(player2.getPlayerTitle(),smallBlindVal,bigBlindVal));
        if(player1.getPlayerType() == PlayerTypes.Human){
            playerCards = player1.getPlayerCards();
            firstPlayerText = "Cards: " + playerCards[0]+" " + playerCards[1];
            System.out.print(firstPlayerText);
        }
        else{
            firstPlayerText = "Cards: ?? ??";
            System.out.print(firstPlayerText);
        }
        paintSpaces(SPACES - firstPlayerText.length());
        if(player2.getPlayerType() == PlayerTypes.Human){
            playerCards = player2.getPlayerCards();
            System.out.println("Cards: " + playerCards[0]+" " + playerCards[1]);
        }
        else{
            System.out.println("Cards: ?? ??");
        }
        firstPlayerText = "Bet: " + player1.getCurrentBet();
        System.out.print(firstPlayerText);
        paintSpaces(SPACES - firstPlayerText.length());
        System.out.println("Bet: " + player2.getCurrentBet());
        firstPlayerText = "Last action: " + player1.getLastAction();
        System.out.print(firstPlayerText);
        paintSpaces(SPACES - firstPlayerText.length());
        System.out.println("Last action: " + player2.getLastAction());
        firstPlayerText = "Chips: " + player1.getTokens();
        System.out.print(firstPlayerText);
        paintSpaces(SPACES - firstPlayerText.length());
        System.out.println("Chips: " + player2.getTokens());
        System.out.println();
    }

    private static  void paintPlayerGameplaySingle(PlayerInfo player, int smallBlindVal, int bigBlindVal)
    {
        String [] playerCards;
        System.out.println();
        System.out.println("Type: " + player.getPlayerType());
        System.out.println("State: " + getTitlesFormat(player.getPlayerTitle(),smallBlindVal,bigBlindVal));
        if(player.getPlayerType() == PlayerTypes.Human){
            playerCards = player.getPlayerCards();
            System.out.println("Cards: " + playerCards[0]+" " + playerCards[1]);
        }
        else{
            System.out.println("Cards: ?? ??    ");
        }
        System.out.println("Bet: " + player.getCurrentBet());
        System.out.println("Last action: " + player.getLastAction());
        System.out.println("Chips: " + player.getTokens());
        System.out.println();
    }

    private static void paintPlayerStatusCouple(PlayerInfo player1, PlayerInfo player2, int smallBlindVal, int bigBlindVal) {
        String firstPlayerText;
        System.out.println();
        firstPlayerText = "Type: " + player1.getPlayerType();
        System.out.print(firstPlayerText);
        paintSpaces(SPACES - firstPlayerText.length());
        System.out.println("Type: " + player2.getPlayerType());
        firstPlayerText = "State: " + getTitlesFormat(player1.getPlayerTitle(), smallBlindVal, bigBlindVal);
        System.out.print(firstPlayerText);
        paintSpaces(SPACES - firstPlayerText.length());
        System.out.println("State: " + getTitlesFormat(player2.getPlayerTitle(), smallBlindVal, bigBlindVal));
        firstPlayerText = "Chips: " + player1.getTokens();
        System.out.print(firstPlayerText);
        paintSpaces(SPACES - firstPlayerText.length());
        System.out.println("Chips: " + player2.getTokens());
        firstPlayerText = "Buys: " + player1.getTotalBuys();
        System.out.print(firstPlayerText);
        paintSpaces(SPACES - firstPlayerText.length());
        System.out.println("Buys: " + player2.getTotalBuys());
        firstPlayerText = "Hands won: " + player1.getTotalWins()+"/"+player1.getTotalHandsPlayed();
        System.out.print(firstPlayerText);
        paintSpaces(SPACES - firstPlayerText.length());
        System.out.println("Hands won: " + player2.getTotalWins()+"/"+player2.getTotalHandsPlayed());
        System.out.println();
    }

    private static void paintPlayerStatusSingle(PlayerInfo player, int smallBlindVal, int bigBlindVal){
        System.out.println();
        System.out.println("Type: " + player.getPlayerType());
        System.out.println("State: " + getTitlesFormat(player.getPlayerTitle(),smallBlindVal,bigBlindVal));
        System.out.println("Chips: " + player.getTokens());
        System.out.println("Buys: " + player.getTotalBuys());
        System.out.println("Hands won: " + player.getTotalWins()+"/"+player.getTotalHandsPlayed());
        System.out.println();
    }

    private static void paintSpaces(int count){
        for(int i=0;i < count; i++)
            System.out.print(" ");
    }

    private static String getTitlesFormat(PokerTitle title, int smallBlindVal, int bigBlindVal){
        switch (title){
            case BIG:{
                return title.toString() + " ("+bigBlindVal+")";
            }
            case SMALL:{
                return title.toString() + " ("+smallBlindVal+")";
            }
            default: return title.toString();
        }
    }
}
