package con.library.utils;

import con.library.views.InputOption;
import con.library.views.MenuView;

import java.text.DecimalFormat;
import java.util.Scanner;

public class AppUtils {
    public final static Scanner SCANNER = new Scanner(System.in);

    public static int retryChoose(int min, int max) {
        int option;
        do {
            System.out.print(" ⭆ ");
            try {
                option = Integer.parseInt(SCANNER.nextLine());
                if (option > max || option < min) {
                    System.out.println("Chọn chức năng không đúng! Vui lòng chọn lại");
                    continue;
                }
                break;
            } catch (Exception ex) {
                System.out.println("Nhập sai! vui lòng nhập lại");
            }
        } while (true);
        return option;
    }

    public static int retryParseInt() {
        int result;
        do {
            System.out.print(" ⭆ ");
            try {
                result = Integer.parseInt(SCANNER.nextLine());
                return result;
            } catch (Exception ex) {
                System.out.println("Nhập sai! vui lòng nhập lại");
            }
        } while (true);
    }


    public static String retryString(String fieldName) {
        String result;
        System.out.print(" ⭆ ");
        while ((result = SCANNER.nextLine().trim()).isEmpty()) {
            System.out.printf("%s không được để trống\n", fieldName);
            System.out.print(" ⭆ ");
        }
        return result;
    }

    public static long retryParseLong() {
        long result;
        do {
            System.out.print("⭆");
            try {
                result = Long.parseLong(SCANNER.nextLine());
                return result;
            } catch (Exception e) {
                System.out.println("Nhập sai.Vui lòng nhập lại.");
            }
        } while (true);
    }

    public static double retryParseDouble() {
        double result;
        do {
            System.out.print(" ⭆ ");
            try {
                result = Double.parseDouble(SCANNER.nextLine());
                return result;
            } catch (Exception ex) {
                System.out.println("Nhập sai! vui lòng nhập lại");
            }
        } while (true);
    }

    public static String doubleToVND(double value) {
        String patternVND = ",###₫";
        DecimalFormat decimalFormat = new DecimalFormat(patternVND);
        return decimalFormat.format(value);
    }

    public static boolean isRetry(InputOption inputOption) {
        do {
            switch (inputOption) {
                case ADD:
                    System.out.println("Nhấn 'y' để thêm tiếp \t|\t 'q' để quay lại \t|\t 't' để thoát chương trình");
                    break;
                case UPDATE:
                    System.out.println("Nhấn 'y' để sửa tiếp \t|\t 'q' để quay lại\t|\t 't' để thoát chương trình");
                    break;
                case DELETE:
                    System.out.println("Nhấn 'q' để quay lại\t|\t 't' để thoát chương trình");
                    break;
                case SHOW:
                    System.out.println("Nhấn 'q' để trở lại \t|\t 't' để thoát chương trình");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + inputOption);
            }

            System.out.print(" ⭆ ");
            String option = SCANNER.nextLine();
            switch (option) {
                case "y":
                    return true;
                case "q":
                    return false;
                case "t":
                    MenuView.exit();
                    break;
                default:
                    System.out.println("Chọn chức năng không đúng! Vui lòng chọn lại");
                    break;
            }
        } while (true);
    }
}
