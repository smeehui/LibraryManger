package con.library.components.fine.views;

import con.library.components.fine.services.FineService;
import con.library.components.fine.services.IFineService;

import java.util.Scanner;

public class FineView {

    private IFineService fineService;
    private final Scanner scanner = new Scanner(System.in);

    public FineView() {
        fineService = FineService.getInstance();
    }

    public void add() {

    }

    public void collectFine(long userId, long days) {
        System.out.println("200k");
    }
}
