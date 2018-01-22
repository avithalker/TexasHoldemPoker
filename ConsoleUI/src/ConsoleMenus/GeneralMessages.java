package ConsoleMenus;

import Common.PlayerUtilities.WinnerInfo;
import ConsoleGlobalDefines.ConsoleGlobals;

import java.util.List;

public class GeneralMessages {

    public static void moveNextGambleRoundConfirmation(){
        System.out.print("\nPress any key in order to start the next gamble round...");
        ConsoleGlobals.getInputScanner().nextLine();
    }

    public static void winnerAnnouncementConfirmation(){
        System.out.println("\nHand round is over!\nPress any key to see the winners...");
        ConsoleGlobals.getInputScanner().nextLine();
    }

    public static void announceWinners(List<WinnerInfo> winnersInfo){
        System.out.println("\n***********************");
        System.out.println("The winners of this hand round are:");
        for(WinnerInfo info:winnersInfo){
            System.out.println();
            System.out.println(info.toString());
        }
        System.out.println();
        System.out.println("***********************");
    }

    public static void skipAllGambleRoundsConfirmation(){
        System.out.println("\nNot all players has enough tokens to continue the hand.");
        System.out.println("Press any key to skip all left gamble rounds...");
        ConsoleGlobals.getInputScanner().nextLine();
    }

    public static void announceOnHandKilled(){
        System.out.println("\n*** The hand was stopped because the human player folded ***");
    }

    public static void announceOnTokenBuy(){
        System.out.println("\n*** additional tokens were bought for player");
    }
}
