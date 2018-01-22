import Common.ExternalPlayer.IExternalPlayer;
import Common.GlobalDefines.PokerAction;
import Common.PlayerUtilities.PlayerInfo;
import Common.PlayerUtilities.PokerTableView;
import ConsoleGlobalDefines.ConsoleGlobals;
import ConsoleMenus.IMenu;
import ConsoleMenus.PokerActionMenu;

import java.util.Scanner;

public class HumanPlayer implements IExternalPlayer {

    private IMenu<PokerAction> pokerActionMenu;

    public HumanPlayer(){
        pokerActionMenu = new PokerActionMenu();
    }

    @Override
    public PokerAction GetNextAction(PlayerInfo playerInfo, PokerTableView tableView) {

        pokerActionMenu.printMenu();
        return pokerActionMenu.getMenuChoice();
    }

    @Override
    public void Fold(PlayerInfo playerInfo, PokerTableView tableView) {

    }

    @Override
    public int Bet(PlayerInfo playerInfo, PokerTableView tableView) {
        return askForMoney("Please enter your bet: ");
    }

    @Override
    public void Call(PlayerInfo playerInfo, PokerTableView tableView) {

    }

    @Override
    public void Check(PlayerInfo playerInfo, PokerTableView tableView) {

    }

    @Override
    public int Raise(PlayerInfo playerInfo, PokerTableView tableView) {
        return askForMoney("Please enter your raise: ");
    }

    private int askForMoney(String msg){
        Scanner scanner = ConsoleGlobals.getInputScanner();
        boolean isValid;
        int value = 0;
        do{
            System.out.print(msg);
            try {
                value = scanner.nextInt();
                ConsoleGlobals.clearInputBuffer();
                isValid = true;
            }
            catch (Exception e){
                isValid = false;
                System.out.println("Invalid value. try again.");
                ConsoleGlobals.clearInputBuffer();
            }
        }while (!isValid);

        return value;
    }
}
