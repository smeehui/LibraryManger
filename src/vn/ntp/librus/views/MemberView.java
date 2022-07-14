package vn.ntp.librus.views;

import vn.ntp.librus.model.BookLending;
import vn.ntp.librus.model.User;
import vn.ntp.librus.services.*;
import vn.ntp.librus.utils.AppUtils;
import vn.ntp.librus.utils.InstantUtils;

import java.util.Scanner;

public class MemberView extends UserView {

    private final IBookItemService bookItemService;
    private final IBookLendingService bookLendingService;
    private final IUserService userService;

    public MemberView() {
        bookItemService = BookItemService.getInstance();
        bookLendingService = BookLendingService.getInstance();
        userService = UserService.getInstance();
    }

    public static void launch(long id) {
        MemberView memberView = new MemberView();
        memberView.menuOption(id);
    }

    public void menuOption(long id) {
        do {
            showHeader(id);
            showMenu();
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nChọn chức năng ");
            System.out.print("⭆ ");
            int option = AppUtils.retryParseInt();
            switch (option) {
                case 1:
                    updateInfo(id);
                    break;
                case 2:
                    showBookLendingInfo(id);
                    break;
                case 3:
                    MenuView.exit();
                    break;
                default:
                    System.out.println("Chọn chức năng không đúng! Vui lòng chọn lại");
                    break;
            }
        } while (true);
    }

    public void showHeader(long id) {
        System.out.println("\t✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬  LIBRUS  ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬");
        System.out.println();
        System.out.printf("\t %-5s %-15s %-5s %-15s %-5s %-15s %-5s %-15s \n",
                "✬|✬"
                , "Tên Người Dùng    "
                , "✬|✬"
                , "Số điện thoại  ",
                "✬|✬    ",
                "Email ",
                "✬|✬",
                "Số sách đang mượn");

        User user = userService.findById(id);
        System.out.printf("\t %-7s %-15s %-7s %-12s %-7s %-23s %-7s %-7s \n",
                "✬-✬-✬-✬",
                user.getUsername(),
                "✬-✬-✬-✬",
                user.getMobile(),
                "✬-✬-✬-✬",
                user.getEmail(),
                "✬-✬-✬-✬",
                bookLendingService.countBookItemLendingByUserIdAndStatus(user.getId()));

        System.out.println();
        System.out.println("\t✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ");
    }

    public void updateInfo(long id){
        boolean isRetry = false;
        do {
            try {
                showUserInfo(id);
                System.out.println("┌ - - - - - SỬA - - - - - ┐");
                System.out.println("︲  1. Đổi tên            ︲");
                System.out.println("︲  2. Sửa số điện thoại  ︲");
                System.out.println("︲  3. Sửa địa chỉ        ︲");
                System.out.println("︲  4. Quay lại           ︲");
                System.out.println("└ - - - - - - - - - - - - ┘");

                int option = AppUtils.retryChoose(1, 4);
                User newUser = new User();
                newUser.setId(id);
                switch (option) {
                    case 1:
                        String name = inputFullName(InputOption.UPDATE);
                        newUser.setFullName(name);
                        userService.update(newUser);
                        System.out.println("Bạn đã đổi tên thành công!\uD83C\uDF89");
                        break;
                    case 2:
                        String phone = inputPhone(InputOption.UPDATE);
                        newUser.setMobile(phone);
                        userService.update(newUser);
                        System.out.println("Bạn đã đổi số điện thoại thành công\uD83C\uDF89");
                        break;
                    case 3:
                        String address = inputAddress(InputOption.UPDATE);
                        newUser.setAddress(address);
                        userService.update(newUser);
                        System.out.println("Bạn đã đổi thành công\uD83C\uDF89");
                        break;
                }
                isRetry = option != 4 && AppUtils.isRetry(InputOption.UPDATE);

            } catch (Exception e) {
                System.out.println("Nhập sai! vui lòng nhập lại");
            }
        } while (isRetry);
    }


    public void showUserInfo(long id) {
        System.out.println("✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬   THÔNG TIN CỦA BẠN ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ");
        System.out.printf("%-15s %-15s %-15s %-15s %-20s %-15s \n",
                "Id", "Tên", "Người dùng","Số điện thoại", "Email", "Địa chỉ" );

        User user = userService.findById(id);
        System.out.printf("%-15s %-15s %-15s %-15s %-20s %-15s \n",
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getMobile(),
                user.getEmail(),
                user.getAddress()
        );
        System.out.println("✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬  ");
    }


    public void showBookLendingInfo(long id){

            System.out.println("--------------------------------------------------------------- BOOK LENDING --------------------------------------------------------------------");
            System.out.printf(" %-15s %-15s %-15s %-15s %-18s %-18s %-18s \n",
                    "Id BookLending",
                    "Id BookItem",
                    "Id User",
                    "Lending Status",
                    "Ngày Mượn Sách",
                    "Ngày Đến Hạn",
                    "Ngày Trả Sách"

            );

            BookLending bookLending = bookLendingService.findByUserId(id);

                System.out.printf(" %-15s %-15s %-15s %-15s %-18s %-18s %-18s \n",
                        bookLending.getId(),
                        bookLending.getBookItemId(),
                        bookLending.getUserId(),
                        bookLending.getStatus(),
                        bookLending.getCreatedAt() == null ? "" : InstantUtils.instantToString(bookLending.getCreatedAt()),
                        bookLending.getDueAt() == null ? "" : InstantUtils.instantToString(bookLending.getDueAt()),
                        bookLending.getReturnAt() == null ? "" : InstantUtils.instantToString(bookLending.getReturnAt())
                );

            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------\n");
        }


    public void showMenu() {
        System.out.println("\t ✬ ✬ ✬ ✬ ✬ ✬ ✬ MENU MEMBER  ✬ ✬ ✬ ✬ ✬ ✬ ✬");
        System.out.println("\t ✬                                    ✬");
        System.out.println("\t ✬     1. Sửa thông tin cá nhân       ✬");
        System.out.println("\t ✬     2. Xem thông tin mượn sách     ✬");
        System.out.println("\t ✬     3. Exit                        ✬");
        System.out.println("\t ✬                                    ✬");
        System.out.println("\t ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬ ✬");
    }

}
