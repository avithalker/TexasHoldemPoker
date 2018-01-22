package ConsoleMenus;

import ConsoleGlobalDefines.ConsoleGlobals;
import ConsoleMenus.MenuChoices.MainMenuChoices;


import java.util.Scanner;

public class MainMenu implements IMenu<MainMenuChoices> {

    @Override
    public void printMenu() {
        System.out.println();
        System.out.println("=====================================");
        System.out.println();
        System.out.println("1. Load game settings");
        System.out.println("2. Start new game");
        System.out.println("3. Show game status");
        System.out.println("4. Start hand round");
        System.out.println("5. Show game statistics");
        System.out.println("6. Buy tokens");
        System.out.println("7. Exit game");
        System.out.println("\n=====================================\n");
    }

    @Override
    public MainMenuChoices getMenuChoice() {
        int userChoiceNative;
        MainMenuChoices userChoice;
        Scanner scanner = ConsoleGlobals.getInputScanner();
        System.out.print("Please enter your choice: ");
        do {
            try {
                userChoiceNative = scanner.nextInt();
                ConsoleGlobals.clearInputBuffer();
                userChoice = convertIntToChoice(userChoiceNative);
            } catch (Exception e) {
                userChoice = MainMenuChoices.InvalidChoice;
            }
            if(userChoice == MainMenuChoices.InvalidChoice){
                System.out.print("Invalid input received. Please reenter your choice: ");
                ConsoleGlobals.clearInputBuffer();
            }

        }while (userChoice == MainMenuChoices.InvalidChoice);

        return userChoice;
    }

    private MainMenuChoices convertIntToChoice(int choice){
        switch (choice){
            case 1: return MainMenuChoices.LoadSettings;
            case 2: return MainMenuChoices.StartGame;
            case 3: return MainMenuChoices.ShowGameStatus;
            case 4: return MainMenuChoices.PlayHand;
            case 5: return MainMenuChoices.GetStatistics;
            case 6: return MainMenuChoices.Buy;
            case 7: return MainMenuChoices.Exit;
            default: return MainMenuChoices.InvalidChoice;
        }
    }
}
