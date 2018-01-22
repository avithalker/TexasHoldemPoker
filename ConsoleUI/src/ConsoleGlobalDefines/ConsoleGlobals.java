package ConsoleGlobalDefines;

import java.util.Scanner;

public class ConsoleGlobals {

    private static Scanner inputScanner;

    public static Scanner getInputScanner(){
        if(inputScanner == null)
            inputScanner = new Scanner(System.in);

        return inputScanner;
    }

    public static void clearInputBuffer(){
        getInputScanner().nextLine();
    }
}
