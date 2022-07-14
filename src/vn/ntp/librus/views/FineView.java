package vn.ntp.librus.views;

import vn.ntp.librus.model.Role;
import vn.ntp.librus.model.User;
import vn.ntp.librus.services.FineService;
import vn.ntp.librus.services.IFineService;
import vn.ntp.librus.utils.AppUtils;

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
