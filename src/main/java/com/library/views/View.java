package com.library.views;

import com.library.utils.InputRetry;
import com.library.utils.MenuTableConverter;

import java.util.Scanner;

public abstract class View{
    public static Scanner sc = new Scanner(System.in);
    public static MenuTableConverter tbConverter = MenuTableConverter.getInstance();
    public static InputRetry tryInput = InputRetry.getInstance();
    public String caption;
    public String[] tHeadings;
}