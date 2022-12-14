package com.library.components.books.views;

import com.library.views.View;

public class BookMenuView extends View {

    public static void launch() {
        int choice;
        do {
            BookView bookView = new BookView();
            menuBook();
            System.out.println("Chọn chức năng :");
            System.out.print(" ⭆ ");
            choice = tryInput.tryInt("lựa chọn");
            if (choice == -1) break;
            switch (choice) {
                case 1:
                    bookView.add();
                    break;
                case 2:
                    bookView.update();
                    break;
                case 3:
                    bookView.remove();
                    break;
                case 4:
                    bookView.showAllBooks();
                    break;
                case 5:
                    bookView.findBooksByName();
                    break;
                default:
                    System.out.println("Chọn chức năng không đúng. Vui lòng chọn lại");
            }
        } while (true);
    }

    public static void menuBook() {
        System.out.println(tbConverter.convertMtplCol("MENU BOOK", "Thêm sách", "Chỉnh sửa sách", "Xóa sách", "Hiển thị danh sách sách", "Tìm kiếm sách theo tên"));
    }
}
