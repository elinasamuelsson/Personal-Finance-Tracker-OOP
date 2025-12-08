package examinationsprojekt.utils;

import java.util.Scanner;

public class UserTerminalInputReader implements IUserInputReader {
    @Override
    public String stringInput() {
        System.out.print(" >");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public double doubleInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(" >");
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    @Override
    public int intInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(" >");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
