package com.library.components.fine.views;

import com.library.components.fine.services.FineService;
import com.library.components.fine.services.IFineService;
import com.library.views.ShowErrorMessage;
import com.library.views.View;

public class FineMenuView extends View {
    private static  FineView fineView = FineView.getInstance();
    private static IFineService fineService = FineService.getInstance();
    public static void launch(){
        fineService.collectFine();
        do {
            showMenu();
            int choice = tryInput.tryInt("lựa chọn");
            if (tryInput.isReturn(String.valueOf(choice)))break;
            switch (choice) {
                case 1->fineView.viewUnpaidFines();
                case 2->fineView.findByUserId();
                case 3->fineView.showHistory();
                default -> ShowErrorMessage.outOfRange("lựa chọn");
            }
        }while (true);
    }

    private static void showMenu() {
        System.out.println(tbConverter.convertMtplCol("QUẢN LÝ NỘP PHẠT","Chỉnh sửa trạng thái nộp phạt",
                "Tìm theo ID người dùng","Hiển thị lịch sử nộp phạt"));
    }
}
