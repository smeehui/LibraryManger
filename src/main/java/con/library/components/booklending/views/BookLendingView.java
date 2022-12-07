package con.library.components.booklending.views;

import con.library.components.booklending.services.BookLendingService;
import con.library.components.booklending.services.IBookLendingService;
import con.library.components.books.models.BookItem;
import con.library.components.booklending.model.BookLending;
import con.library.components.books.models.BookStatus;
import con.library.components.booklending.model.LendingStatus;
import con.library.components.user.models.User;
import con.library.components.user.services.IUserService;
import con.library.components.user.services.UserService;
import con.library.services.Constants;
import con.library.components.books.services.BookItemService;
import con.library.components.books.services.IBookItemService;
import con.library.utils.AppUtils;
import con.library.utils.InputRetry;
import con.library.utils.InstantUtils;
import con.library.views.InputOption;
import con.library.views.View;

import java.time.Instant;

public class BookLendingView extends View {
    private IBookLendingService bookLendingService;
    private IUserService userService;
    private IBookItemService bookItemService;
    private InputRetry tryInput = InputRetry.getInstance();


    public BookLendingView() {
        bookLendingService = BookLendingService.getInstance();
        userService = UserService.getInstance();
        bookItemService = BookItemService.getInstance();
    }

    public void update() {
        boolean isRetry;
        do {
            showBooksLending(InputOption.UPDATE);
            long id = inputLendingId(InputOption.UPDATE);
            System.out.println("┌ - - - - - - -  SỬA - - - - - - - ┐");
            System.out.println("|   1. Sửa trạng thái Lending      |");
            System.out.println("|   2. Quay lại Menu               |");
            System.out.println("└ - - - - - - - - - - - - - - - - -┘");
            System.out.println("Chọn chức năng: ");
            int option = AppUtils.retryChoose(1, 2);
            BookLending newBookLending = new BookLending();
            newBookLending.setId(id);

            switch (option) {
                case 1:
                    LendingStatus lendingStatus = inputLendingStatus(InputOption.ADD);
                    newBookLending.setStatus(lendingStatus);
                    bookLendingService.update(newBookLending);
                    System.out.println("Cập nhập trạng thái Lending thành công");
                    break;
                case 2:
                    showBooksLending(InputOption.UPDATE);
                    break;
            }
            isRetry = option != 2 && AppUtils.isRetry(InputOption.UPDATE);
        } while (isRetry);
    }


    public void showBooksLending(InputOption inputOption) {
        System.out.println("--------------------------------------------------------------- BOOK LENDING --------------------------------------------------------------------");
        System.out.printf(" %-15s %-15s %-15s  %-15s %-15s %-20s %-18s %-18s \n",
                "Id BookLending",
                "Id BookItem",
                "Id User",
                "Full Name",
                "Lending Status",
                "Ngày Mượn Sách",
                "Ngày Đến Hạn",
                "Ngày Trả Sách"

        );
        for (BookLending bookLending : bookLendingService.findAll()) {

            System.out.printf(" %-15s %-15s %-15s %-18s %-15s %-18s %-18s %-18s \n",
                    bookLending.getId(),
                    bookLending.getBookItemId(),
                    bookLending.getUserId(),
                    userService.findById(bookLending.getUserId()).getFullName(),
                    bookLending.getStatus(),
                    bookLending.getCreatedAt() == null ? "" : InstantUtils.instantToString(bookLending.getCreatedAt()),
                    bookLending.getDueAt() == null ? "" : InstantUtils.instantToString(bookLending.getDueAt()),
                    bookLending.getReturnAt() == null ? "" : InstantUtils.instantToString(bookLending.getReturnAt())
            );
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------\n");
        if (inputOption == InputOption.SHOW)
            AppUtils.isRetry(InputOption.SHOW);
    }

    private LendingStatus inputLendingStatus(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập Lending status : LENDING | DUE | RETURN");
                break;
            case UPDATE:
                System.out.println("Nhập Lending status muốn sửa: LENDING | DUE | RETURN ");
                break;
        }
        return LendingStatus.parseLendingStatus(AppUtils.retryString("lending status"));
    }

    private long inputBookItemId(InputOption option) {
        long id;
        switch (option) {

            case CHECKOUT:
                System.out.println("Nhập Id BookItem để checkout sách");
                break;
            case RETURN:
                System.out.println("Nhập Id BookItem để trả sách");
                break;
        }
        boolean isRetry = false;
        do {
            id = tryInput.tryLong("ID");
            if (tryInput.isReturn(String.valueOf(id))) return -1;
            boolean exist = bookItemService.existsById(id);
            switch (option) {
                case RETURN:
                case CHECKOUT:
                    if (!exist) {
                        System.out.println("Không tìm thấy sách ! Vui lòng nhập lại");
                    }
                    isRetry = !exist;
                    break;
            }
        } while (isRetry);
        return id;
    }

    private long inputLendingId(InputOption option) {
        long id;
        switch (option) {
            case ADD:
                System.out.println("Nhập Id BookLending ");
                break;
            case UPDATE:
                System.out.println("Nhập Id BookLending bạn muốn sửa");
                break;
        }
        boolean isRetry = false;
        do {
            id = tryInput.tryLong("id của BookLending");
            if (tryInput.isReturn(String.valueOf(id))) return -1;
            boolean exist = bookLendingService.existsById(id);
            switch (option) {
                case ADD:
                    if (exist) {
                        System.out.println("Id này đã tồn tại. Vui lòng nhập Id khác!");
                    }
                    isRetry = exist;
                    break;
                case UPDATE:
                    if (!exist) {
                        System.out.println("Không tìm thấy Id! Vui lòng nhập lại");
                    }
                    isRetry = !exist;
                    break;
            }
        } while (isRetry);
        return id;
    }

    public void checkoutBook() {
        long bookItemId = inputBookItemId(InputOption.CHECKOUT);
        BookItem bookItem = bookItemService.findById(bookItemId);
        System.out.println(bookItem);
        if (bookItem.getStatus() != BookStatus.AVAILABLE){
            System.out.println("Sách này tạm thời không có sẵn");
        }
        if (bookItem.isReferenceOnly()) {
            System.out.println("Sách này chỉ được đọc ở Thư Viện, không được phép mượn");
            return;
        }
        long userId = inputUserId();
        User user = userService.findById(userId);
        System.out.println(user);
        if (bookLendingService.isBookIssuedQuotaExceeded(userId)) {
            System.out.println("Người dùng đã mượn quá số lượng sách cho phép");
            return;
        }

        bookLendingService.lendBook(userId, bookItem.getBookItemID());
        System.out.println("Bạn đã mượn sách thành công");
    }

    public void returnBook() {

        long userId = inputUserId();
        long bookItemId = inputBookItemId(InputOption.RETURN);
        BookItem bookItem = bookItemService.findById(bookItemId);
        System.out.println("--------------------------------------------------------------- THÔNG TIN BOOKITEM --------------------------------------------------------------------");
        System.out.printf("%-15s %-10s %-8s %-15s %-15s %-8s %-8s %-8s %-15s %-15s %-10s \n",
                "IdBookItem",
                "NXB",
                "Số trang",
                "Ngày mượn",
                "Hạn trả sách",
                "Giá sách",
                "Định dạng",
                "Trạng thái",
                "Ngày nhập",
                "Update",
                "IdBook");
        System.out.printf("%-15s %-10s %-8s %-15s %-15s %-8s %-8s %-8s %-15s %-15s %-10s \n ",
                bookItem.getBookItemID(),
                bookItem.getPublisher(),
                bookItem.getNumberOfPages(),
                bookItem.getBorrowedAt() == null ? "" : InstantUtils.instantToString(bookItem.getBorrowedAt()),
                bookItem.getDueAt() == null ? "" : InstantUtils.instantToString(bookItem.getDueAt()),
                bookItem.getPrice(),
                bookItem.getFormat(),
                bookItem.getStatus(),
                bookItem.getDateOfPurchase() == null ? "" : InstantUtils.instantToString(bookItem.getDateOfPurchase()),
                bookItem.getUpdatedAt() == null ? "" : InstantUtils.instantToString(bookItem.getDateOfPurchase()),
                bookItem.getBookId());
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------\n");

        BookLending bookLending = bookLendingService.findByBookItemIdAndUserIdAndStatus(bookItemId, userId, LendingStatus.RETURN);
        if (bookLending == null) {
            System.out.println("Người dùng này chưa mượn sách ở thư viện ! BookItem này thuộc người dùng khác");
            return;
        } else
            System.out.println("--------------------------------------------------------------- THÔNG TIN BOOKLENDING --------------------------------------------------------------------");
        System.out.printf(" %-15s %-15s %-15s %-10s %-18s %-18s %-18s %-18s %-13s \n",
                "Id BookLending",
                "Tên sách",
                "Full Name",
                "Lending Status",
                "Ngày Mượn Sách",
                "Ngày Đến Hạn",
                "Ngày Trả Sách",
                "Số ngày quá hạn",
                "Số tiền phạt"

        );
        Instant dueAt = bookLending.getDueAt();
        long millis = System.currentTimeMillis() - dueAt.toEpochMilli();
        System.out.printf(" %-15s %-15s %-15s %-10s %-18s %-18s %-18s %-18s %-13s \n",
                bookLending.getId(),
                bookItem.getBook().getTitle(),
                userService.findById(bookLending.getUserId()).getFullName(),
                bookLending.getStatus(),
                bookLending.getCreatedAt() == null ? "" : InstantUtils.instantToString(bookLending.getCreatedAt()),
                bookLending.getDueAt() == null ? "" : InstantUtils.instantToString(bookLending.getDueAt()),
                InstantUtils.instantToString(Instant.now()),
//                bookLending.getReturnAt() == null ? "" : InstantUtils.instantToString(bookLending.getReturnAt()),
                millis > 0 ? millis/86400000 : 0,
                millis > 0 ? millis/86400000*Constants.FINE_AMOUNT : 0

        );
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------\n");


//        Duration duration = Duration.between(bookLending.getCreatedAt(), bookLending.getReturnAt());
//        System.out.println("Số ngày mượn cuốn sách tính tới hiện tại là: " + duration.toDays());
//
//        if (duration.toDays() > Constants.MAX_LENDING_DAYS) {
//            System.out.println("Người dùng vượt quá số ngày cho phép");
//            System.out.println("Người dùng bị phạt 100.000đ");
//            return;
//        }
        bookLendingService.returnBook(bookItemId, bookLending.getUserId());
        System.out.println("Bạn đã trả sách thành công \uD83C\uDF8A");

    }

    private long inputUserId() {
        long userId;
        System.out.println("Nhập UserId :");
        while (!userService.existById(userId = tryInput.tryLong("ID"))) {
            if (tryInput.isReturn(String.valueOf(userId))) return -1;
            System.out.println("Không tìm thấy người dùng ! Vui lòng nhập lại");
        }
        return userId;
    }
}
