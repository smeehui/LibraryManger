package con.library.views;

import con.library.utils.InputRetry;
import con.library.utils.MenuTableConverter;

import java.util.Scanner;

public abstract class View{
    public static Scanner sc = new Scanner(System.in);
    public static MenuTableConverter tbConverter = MenuTableConverter.getInstance();
    public static InputRetry tryInput = InputRetry.getInstance();
    public String caption;
    public String[] tHeadings;

}