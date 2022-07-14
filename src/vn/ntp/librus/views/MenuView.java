package vn.ntp.librus.views;

import java.util.Scanner;

public class MenuView {

    public static Scanner input = new Scanner(System.in);

    public static void exit() {
        System.out.println(" END PROGRAM | HẸN GẶP LẠI \uD83C\uDF8A ");
        System.exit(0);
    }

    public static void showMainMenu() {
        System.out.println("\t ✬ ✬ ✬ ✬ ✬ ✬  MAIN MENU  ✬ ✬ ✬ ✬ ✬ ✬");
        System.out.println("\t ✬                                 ✬");
        System.out.println("\t ✬     1. Menu Librarian           ✬");
        System.out.println("\t ✬     2. Menu User                ✬");
        System.out.println("\t ✬     3. Thoát chương trình       ✬");
        System.out.println("\t ✬                                 ✬");
        System.out.println("\t ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬");

    }

    public static void showSelection() {
        int choice;
        while (true) {
            showMainMenu();
            try {
                choice = input.nextInt();
                switch (choice) {
                    case 1:
                        LibrarianView.menuOption();
                        break;
                    case 2:
                        UserMenuView.launch();
                        break;
                    case 0:
                        MenuView.exit();
                        break;
                    default:
                        System.out.println("Chọn chức năng không đúng. Vui lòng nhập lại");
                }
            } catch (Exception e) {
                System.out.println("Chọn chức năng không đúng. Vui lòng nhập lại");
                showMainMenu();
            }
        }
    }
}
