package com.library.utils;

import com.library.views.InputOption;
import com.library.views.ShowErrorMessage;

import java.util.Scanner;

public class InputRetry {
    private static InputRetry instance;

    public boolean isRetryId(InputOption option, boolean isRetry, boolean exist) {
        switch (option) {
            case ADD -> {
                if (exist) {
                    System.out.println("Id này đã tồn tại. Vui lòng nhập id khác!");
                }
                isRetry = exist;
            }
            case UPDATE -> {
                if (!exist) {
                    System.out.println("Không tìm thấy id! Vui lòng nhập lại");
                }
                isRetry = !exist;
            }
        }
        return isRetry;
    }

    public double tryDouble(String field) {
        do{
            try {
                String value = sc.nextLine();
                if (value.equals("#")) return -1;
                return Double.parseDouble(value);
            }catch (NumberFormatException e){
                ShowErrorMessage.syntax(field);
            }
        }while (true);

    }

    public long tryLong( String field) {
        do{
            try {
                String value = sc.nextLine();
                if (value.equals("#")) return -1;
                return Long.parseLong(value);
            }catch (NumberFormatException e){
                ShowErrorMessage.syntax(field);
            }
        }while (true);
    }

    enum Type {
        INTEGER("INTEGER"), DOUBLE("DOUBLE"), LONG("LONG");
        String value;


        Type(String value) {
            this.value = value;
        }
        public String parseType(String value) {
            for (Type type : Type.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type.value;
                }
            }
            throw new IllegalArgumentException();
        }

    }
    private InputRetry(){}
    public static InputRetry getInstance(){
        if (instance == null) instance = new InputRetry();
        return instance;
    }
    private static Scanner sc= new Scanner(System.in);
    public int tryInt(String field){
        do{
            try {
                String value = sc.nextLine();
                if (value.equals("#")) return -1;
                return Integer.parseInt(value);
            }catch (NumberFormatException e){
                ShowErrorMessage.syntax(field);
            }
        }while (true);
    }
    public String tryString(String field){
        String result;
        System.out.print(" ⭆ ");
        while ((result = sc.nextLine().trim()).isEmpty()) {
            if (result.equals("#")) return "#";
            System.out.printf("%s không được để trống\n", field);
            System.out.print(" ⭆ ");
        }
        return result;
    }
    public boolean isReturn(String character) {
        if (character.equals("#")||character.equals("-1")||character.equals("null")) return true;
        else return false;
    }
}
