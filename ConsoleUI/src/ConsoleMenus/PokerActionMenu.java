package ConsoleMenus;

import Common.GlobalDefines.PokerAction;
import ConsoleGlobalDefines.ConsoleGlobals;

import java.util.Scanner;

public class PokerActionMenu implements IMenu<PokerAction> {

    @Override
    public void printMenu() {
        System.out.println();
        System.out.print("1. Fold   ");
        System.out.print("2. Bet   ");
        System.out.print("3. Call   ");
        System.out.print("4. Check   ");
        System.out.println("5. Raise");
    }

    @Override
    public PokerAction getMenuChoice()
    {
        int userChoiceNative;
        PokerAction userChoice;
        Scanner scanner = ConsoleGlobals.getInputScanner();
        System.out.print("Please enter your choice: ");
        do {
            try {
                userChoiceNative = scanner.nextInt();
                ConsoleGlobals.clearInputBuffer();
                userChoice = convertIntToChoice(userChoiceNative);
            } catch (Exception e) {
                userChoice = PokerAction.NONE;
                ConsoleGlobals.clearInputBuffer();
            }
            if(userChoice == PokerAction.NONE)
                System.out.print("Invalid input received. Please reenter your choice: ");
        }while (userChoice == PokerAction.NONE);

        return userChoice;
    }

    private PokerAction convertIntToChoice(int choice){
        switch (choice){
            case 1: return PokerAction.FOLD;
            case 2: return PokerAction.BET;
            case 3: return PokerAction.CALL;
            case 4: return PokerAction.CHECK;
            case 5: return PokerAction.RAISE;
            default: return PokerAction.NONE;
        }
    }
}
