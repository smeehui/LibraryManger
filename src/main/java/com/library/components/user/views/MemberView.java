package com.library.components.user.views;

import com.library.components.booklending.model.BookLending;
import com.library.components.booklending.services.BookLendingService;
import com.library.components.booklending.services.IBookLendingService;
import com.library.components.booklending.views.BookLendingView;
import com.library.components.fine.views.FineView;
import com.library.components.user.models.User;
import com.library.components.user.services.IUserService;
import com.library.components.user.services.UserService;
import com.library.views.InputOption;
import com.library.views.ShowErrorMessage;
import com.library.views.View;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class MemberView extends UserView {

    private final IBookLendingService bookLendingService;
    private final IUserService userService;
    private final BookLendingView bookLendingView;
    private final FineView fineView;

    protected MemberView() {
        bookLendingService = BookLendingService.getInstance();
        userService = UserService.getInstance();
        bookLendingView = BookLendingView.getInstance();
        fineView = FineView.getInstance();
    }

    public static void launch(long id) {
        MemberView memberView = new MemberView();
        memberView.menuOption(id);
    }

    public void menuOption(long id) {
        do {
            showHeader(id);
            showMenu();
            System.out.print("\nChọn chức năng ");
            System.out.print("⭆ ");
            int option = tryInput.tryInt("chức năng");
            switch (option) {
                case 1:
                    bookLendingView.checkoutBook(id);
                    break;
                case 2:
                    bookLendingView.returnBook(id);
                    break;
                case 3:
                    updateInfo(id);
                    break;
                case 4:
                    showBookLendingInfo(id);
                    break;
                case 5:
                    fineView.findByUserId(id);
                    break;
                case -1:
                    return;
                default:
                    ShowErrorMessage.outOfRange("chức năng");
                    break;
            }
        } while (true);
    }

    public void showHeader(long id) {
        User user = userService.findById(id);
        AsciiTable table = new AsciiTable();
        table.addHeavyRule();
        AT_Row tHead = table.addRow(null, null, null, ("Chào mừng " + user.getFullName() + " quay trở lại thư viện").toUpperCase());
        tHead.setTextAlignment(TextAlignment.CENTER);
        table.addHeavyRule();
        AT_Row row = table.addRow("Tên đầy đủ<br>" + user.getFullName(), "Số điện thoại<br>" + user.getMobile(),
                "Email<br>" + user.getEmail(), "Số sách đang mượn<br>" + bookLendingService.countBookItemLendingByUserIdAndStatus(user.getId()));
        row.getCells().get(1).getContext().setTextAlignment(TextAlignment.RIGHT);
        row.getCells().get(3).getContext().setTextAlignment(TextAlignment.RIGHT);
        row.setPaddingRight(1);
        row.setPaddingLeft(1);
        table.addRule();
        table.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        table.getRenderer().setCWC(new CWC_LongestLine());
        System.out.println(table.render(100));
    }

    public void updateInfo(long id) {
        do {
            showUserInfo(id);
            showUpdateMenu();
            int option = tryInput.tryInt("lựa chọn");
            if (tryInput.isReturn(String.valueOf(option))) break;
            User newUser = new User();
            newUser.setId(id);
            switch (option) {
                case 1:
                    String name = inputFullName(InputOption.UPDATE);
                    if (View.tryInput.isReturn(name)) break;
                    newUser.setFullName(name);
                    userService.update(newUser);
                    System.out.println("Bạn đã đổi tên thành công!\uD83C\uDF89");
                    break;
                case 2:
                    String phone = inputPhone(InputOption.UPDATE);
                    if (View.tryInput.isReturn(phone)) break;
                    newUser.setMobile(phone);
                    userService.update(newUser);
                    System.out.println("Bạn đã đổi số điện thoại thành công\uD83C\uDF89");
                    break;
                case 3:
                    String address = inputAddress(InputOption.UPDATE);
                    if (View.tryInput.isReturn(address)) break;
                    newUser.setAddress(address);
                    userService.update(newUser);
                    System.out.println("Bạn đã đổi địa chỉ thành công\uD83C\uDF89");
                    break;
                default:
                    ShowErrorMessage.outOfRange("lựa chọn");
            }
        } while (true);
    }

    private void showUpdateMenu() {
        System.out.println(tbConverter.convertMtplCol("SỬA THÔNG TIN", "Sửa tên", "Sửa số điện thoại", "Sửa địa chỉ"));
    }


    public void showUserInfo(long id) {
        List<User> user = new ArrayList<>();
        user.add(userService.findById(id));
        showList(InputOption.UPDATE, user);
    }


    public void showBookLendingInfo(long id) {
        List<BookLending> bookLendingList = bookLendingService.findBookLendingByUserId(id);
        bookLendingView.showList(InputOption.SHOW, bookLendingList);
    }


    public void showMenu() {
        System.out.println(tbConverter.convertMtplCol("MENU CHÍNH", "Mượn sách", "Trả sách", "Sửa thông tin cá nhân", "Xem thông tin mượn sách","Xem thông tin phạt"));
    }

}
