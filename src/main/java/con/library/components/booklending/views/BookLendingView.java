package con.library.components.booklending.views;

import con.library.components.booklending.services.BookLendingService;
import con.library.components.booklending.services.IBookLendingService;
import con.library.components.books.models.Book;
import con.library.components.books.models.BookItem;
import con.library.components.booklending.model.BookLending;
import con.library.components.books.models.BookStatus;
import con.library.components.booklending.model.LendingStatus;
import con.library.components.books.services.BookService;
import con.library.components.user.models.User;
import con.library.components.user.services.IUserService;
import con.library.components.user.services.UserService;
import con.library.services.Constants;
import con.library.components.books.services.BookItemService;
import con.library.components.books.services.IBookItemService;
import con.library.utils.CurrencyUtils;
import con.library.utils.InputRetry;
import con.library.utils.InstantUtils;
import con.library.views.InputOption;
import con.library.views.ListView;
import con.library.views.ShowErrorMessage;
import con.library.views.View;
import de.vandermeer.asciitable.*;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BookLendingView extends View implements ListView<BookLending> {
    private final IBookLendingService bookLendingService;
    private final IUserService userService;
    private final IBookItemService bookItemService;
    private final InputRetry tryInput = InputRetry.getInstance();
    private final BookService bookService;


    public BookLendingView() {
        bookLendingService = BookLendingService.getInstance();
        userService = UserService.getInstance();
        bookItemService = BookItemService.getInstance();
        bookService = BookService.getInstance();
        caption = "DANH SÁCH MƯỢN/TRẢ SÁCH";
        tHeadings = new String[] {"Id", "Id BookItem", "Id người dùng", "Tên đầy đủ", "Trạng thái mượn",
                "Ngày Mượn Sách", "Ngày Đến Hạn", "Ngày Trả Sách"};
    }

    public void update() {
        do {
            showBooksLending(InputOption.UPDATE);
            long id = inputLendingId(InputOption.UPDATE);
            if(tryInput.isReturn(String.valueOf(id))) break;
            System.out.println(tbConverter.convertSingleCol("CHỈNH SỬA TRẠNG THÁI"));
            BookLending newBookLending = new BookLending();
            newBookLending.setId(id);
            LendingStatus lendingStatus = inputLendingStatus(InputOption.ADD);
            newBookLending.setStatus(lendingStatus);
            bookLendingService.update(newBookLending);
            System.out.println("Cập nhập trạng thái mượn sách thành công! Nhấn enter để tiếp tục,  # để trở lại.");
            if ((tryInput.isReturn(sc.nextLine()))) break;
        } while (true);
    }
    private LendingStatus inputLendingStatus(InputOption option) {
       while (true) {
           switch (option) {
               case ADD -> System.out.println("Nhập Lending status : LENDING | DUE | RETURN");
               case UPDATE -> System.out.println("Nhập Lending status muốn sửa: LENDING | DUE | RETURN ");
           }
           try {
               return LendingStatus.parseLendingStatus(tryInput.tryString("lending status"));
           } catch (IllegalArgumentException e) {
               ShowErrorMessage.syntax("sai định dạng trạng thái sách");
           }
       }
    }

    private long inputBookItemId(InputOption option) {
        long id;
        switch (option) {
            case CHECKOUT -> System.out.println("Nhập Id BookItem để checkout sách");
            case RETURN -> System.out.println("Nhập Id BookItem để trả sách");
        }
        boolean isRetry = false;
        do {
            id = tryInput.tryLong("ID");
            if (tryInput.isReturn(String.valueOf(id))) return -1;
            boolean exist = bookItemService.existsById(id);
            switch (option) {
                case RETURN, CHECKOUT -> {
                    if (!exist) {
                        System.out.println("Không tìm thấy sách ! Vui lòng nhập lại");
                    }
                    isRetry = !exist;
                }
            }
        } while (isRetry);
        return id;
    }

    private long inputLendingId(InputOption option) {
        long id;
        switch (option) {
            case ADD -> System.out.println("Nhập Id BookLending ");
            case UPDATE -> System.out.println("Nhập Id BookLending bạn muốn sửa");
        }
        boolean isRetry = false;
        do {
            id = tryInput.tryLong("id của BookLending");
            if (tryInput.isReturn(String.valueOf(id))) return -1;
            boolean exist = bookLendingService.existsById(id);
            switch (option) {
                case ADD -> {
                    if (exist) {
                        System.out.println("Id này đã tồn tại. Vui lòng nhập Id khác!");
                    }
                    isRetry = exist;
                }
                case UPDATE -> {
                    if (!exist) {
                        System.out.println("Không tìm thấy Id! Vui lòng nhập lại");
                        isRetry = true;
                        continue;
                    }
                    if (bookLendingService.findById(id).getStatus().equals(LendingStatus.RETURN)){
                        System.out.println("Sách đã được trả, không thể sửa.");
                        isRetry = true;
                    }
                }
            }
        } while (isRetry);
        return id;
    }

    public void checkoutBook() {
        long bookItemId;
        BookItem bookItem;
        do {
            bookItemId = inputBookItemId(InputOption.CHECKOUT);
            if (tryInput.isReturn(String.valueOf(bookItemId))) return;
            bookItem = bookItemService.findById(bookItemId);
            if (bookItem == null) System.out.println("Không tìm thấy Book Item");
        } while (bookItem == null);

        viewBookItemDetails(bookItem);
        if (bookItem.getStatus() != BookStatus.AVAILABLE) {
            System.out.println("Sách này tạm thời không có sẵn, nhấn enter");
            sc.nextLine();
            return;
        }

        if (bookItem.isReferenceOnly()) {
            System.out.println("Sách này chỉ được đọc ở Thư Viện, không được phép mượn, nhấn enter.");
            sc.nextLine();
            return;
        }

        User user;
        long userId;
        do {
            userId = inputUserId();
            if (tryInput.isReturn(String.valueOf(userId))) return;
            user = userService.findById(userId);
        } while (user == null);
        viewUserDetails(user);

        if (bookLendingService.isBookIssuedQuotaExceeded(userId)) {
            System.out.println("Người dùng đã mượn quá số lượng sách cho phép, nhấn enter.");
            sc.nextLine();
            return;
        }

        bookLendingService.lendBook(userId, bookItem.getBookItemID());
        System.out.println("Bạn đã mượn sách thành công!");
    }

    private void viewUserDetails(User user) {
        tHeadings = new String[]{"ID", "Tên đăng nhập", "Họ và tên", "Số điện thoại", "Email", "Địa chỉ", "Số sách đã mượn"};
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_LongestLine());
        AT_Row tHead = at.addRow(tHeadings);
        tHead.getCells().get(0).getContext().setTextAlignment(TextAlignment.RIGHT);
        tHead.getCells().get(3).getContext().setTextAlignment(TextAlignment.RIGHT);
        tHead.getCells().get(6).getContext().setTextAlignment(TextAlignment.RIGHT);
        tHead.setPaddingLeft(1);
        tHead.setPaddingRight(1);
        at.setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        AT_Row row = at.addRow(user.getId(), user.getUsername(), user.getFullName(), user.getMobile(), user.getEmail(),
                user.getAddress(), bookLendingService.countBookItemLendingByUserIdAndStatus(user.getId()));
        row.setPaddingLeft(1);
        row.setPaddingRight(1);
        row.getCells().get(0).getContext().setTextAlignment(TextAlignment.RIGHT);
        row.getCells().get(3).getContext().setTextAlignment(TextAlignment.RIGHT);
        row.getCells().get(6).getContext().setTextAlignment(TextAlignment.RIGHT);
        at.addRule();
        at.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        System.out.println(at.render(100));
    }

    private void viewBookItemDetails(BookItem bookItem) {
        tHeadings = new String[]{"ID", "Tiêu đề", "Nhà XB", "Số trang", "Ngày mượn sách", "Hạn trả sách", "Giá sách",
                "Định dạng", "Trạng thái sách", "Ngày mua", "Năm XB", "Ngày cập nhật"};
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_LongestLine());
        AT_Row tHead = at.addRow(tHeadings);
        tHead.getCells().get(0).getContext().setTextAlignment(TextAlignment.RIGHT);
        tHead.getCells().get(2).getContext().setTextAlignment(TextAlignment.RIGHT);
        tHead.setPaddingLeft(1);
        tHead.setPaddingRight(1);
        at.setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        String[] details = rearrangeTitles(bookItem);
        AT_Row row = at.addRow(details);
        row.setPaddingLeft(1);
        row.setPaddingRight(1);
        at.addRule();
        at.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        System.out.println(at.render());
    }

    private String[] rearrangeTitles(BookItem bookItem) {
        String[] splitted = bookItem.toString().split(",");
        Book book = bookService.findById(Long.valueOf(splitted[11]));
        return new String[]{splitted[0], book.getTitle(), splitted[1], splitted[2], splitted[3],
                splitted[4], splitted[5], splitted[6], splitted[7],
                splitted[8], splitted[9], splitted[10]};
    }

    public void returnBook() {
        do{
            long userId = inputUserId();
            if (tryInput.isReturn(String.valueOf(userId))) break;
            boolean hasUserNotLent = viewBookLendingByUserId(userId);
            if (!hasUserNotLent) {
                System.out.println("Người dùng chưa mượn sách");
                sc.nextLine();
                continue;
            }
            long bookItemId;
            BookItem bookItem ;
            do {
                bookItemId = inputBookItemId(InputOption.RETURN);
                if (tryInput.isReturn(String.valueOf(bookItemId))) return;
                bookItem = bookItemService.findById(bookItemId);
                if (bookItem == null) {
                    System.out.println("Nhập sai ID sách mượn.");
                    sc.nextLine();
                }
            } while (bookItem == null);
            BookLending bookLending = bookLendingService.findByBookItemIdAndUserIdAndStatus(bookItem.getBookItemID(), userId, LendingStatus.RETURN);
            if (bookLending == null) {
                System.out.println("Người dùng này chưa mượn sách ở thư viện!");
                continue;
            } else showBooksLendingBill(bookLending);
            bookLendingService.returnBook(bookItemId, bookLending.getUserId());
            System.out.println("Bạn đã trả sách thành công \uD83C\uDF8A! Nhấn enter để tiếp tục, # để trở lại.");
            if (tryInput.isReturn(sc.nextLine())) break;
        } while (true);
    }

    private void showBooksLendingBill(BookLending bookLending) {
        AsciiTable tb = new AsciiTable();
        tb.addRule();
        AT_Row caption = tb.addRow(null, null, null, null, null,null, "Đọc giả: " + userService.findById(bookLending.getUserId()).getFullName());
        caption.setTextAlignment(TextAlignment.CENTER);
        tb.addRule();
        AT_Row thead = tb.addRow("Tiêu đề", "Trạng thái", "Ngày mượn sách", "Hạn trả sách", "Ngày trả sách", "Số ngày quá hạn", "Số tiền phạt");
        thead.setTextAlignment(TextAlignment.CENTER);
        thead.setPaddingRight(1);
        thead.setPaddingLeft(1);
        tb.addRule();
        String title = getBookTitle(bookLending.getBookItemId());
        String lendingStatus = String.valueOf(bookLending.getStatus());
        String createdAt = InstantUtils.instantToString(bookLending.getCreatedAt());
        String dueAtStr = InstantUtils.instantToString(bookLending.getDueAt());
        bookLending.setReturnAt(Instant.now());
        String returnAt = InstantUtils.instantToString(Instant.now());
        long gapInMillis  = System.currentTimeMillis() - bookLending.getDueAt().toEpochMilli();
        long numberOfExceedDays = gapInMillis>0?gapInMillis/86400000: 0;
        double fine = numberOfExceedDays*Constants.FINE_AMOUNT;
        AT_Row row = tb.addRow(title, lendingStatus, createdAt, dueAtStr,returnAt,
                numberOfExceedDays==0?"":String.valueOf(numberOfExceedDays),
                fine==0?"": CurrencyUtils.doubleToVND(fine));
        row.getCells().get(5).getContext().setTextAlignment(TextAlignment.RIGHT);
        row.getCells().get(6).getContext().setTextAlignment(TextAlignment.RIGHT);
        row.setPaddingRight(1);
        row.setPaddingLeft(1);
        tb.addRule();
        tb.getRenderer().setCWC(new CWC_LongestLine());
        System.out.println(tb.render(100));
    }
    private boolean viewBookLendingByUserId(long userId) {
        List<BookLending> result = getNotReturnedBook(userId);
        if (result.isEmpty()) return false;
        AsciiTable tb = new AsciiTable();
        tb.addRule();
        AT_Row caption = tb.addRow(null, null, null, null, null, "Đọc giả: " + userService.findById(userId).getFullName());
        caption.setTextAlignment(TextAlignment.CENTER);
        tb.addRule();
        AT_Row thead = tb.addRow("ID", "ID sách mượn", "Tiêu đề", "Trạng thái", "Ngày mượn sách", "Hạn trả sách");
        thead.setTextAlignment(TextAlignment.CENTER);
        thead.setPaddingRight(1);
        thead.setPaddingLeft(1);
        tb.addRule();
        for (BookLending bookLending : result) {
            if (bookLending.getStatus() != LendingStatus.RETURN) {
                String ID = String.valueOf(bookLending.getId());
                String title = getBookTitle(bookLending.getBookItemId());
                String bookItemID = String.valueOf(bookLending.getBookItemId());
                String lendingStatus = String.valueOf(bookLending.getStatus());
                String createdAt = InstantUtils.instantToString(bookLending.getCreatedAt());
                String dueAt = InstantUtils.instantToString(bookLending.getDueAt());
                AT_Row row = tb.addRow(ID, bookItemID, title, lendingStatus, createdAt, dueAt);
                row.getCells().get(0).getContext().setTextAlignment(TextAlignment.RIGHT);
                row.getCells().get(1).getContext().setTextAlignment(TextAlignment.RIGHT);
                row.getCells().get(3).getContext().setTextAlignment(TextAlignment.RIGHT);
                row.setPaddingRight(1);
                row.setPaddingLeft(1);
            }
        }
        tb.addRule();
        tb.getRenderer().setCWC(new CWC_LongestLine());
        System.out.println(tb.render(100));
        return true;
    }

    private List<BookLending> getNotReturnedBook(long userId) {
        List<BookLending> bookLendingList  = bookLendingService.findBookLendingByUserId(userId);
        List<BookLending> result = new ArrayList<>();
        for (BookLending bookLending : bookLendingList){
            if (bookLending.getStatus() != LendingStatus.RETURN) result.add(bookLending);
        }
        return result;
    }


    private String getBookTitle(Long bookItemId) {
        return bookService.findById(bookItemService.findById(bookItemId).getBookId()).getTitle();
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

    @Override
    public void showList(InputOption inputOption, List<BookLending> items) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_LongestLine());
        at.addHeavyRule();
        at.addRow(null, null, null, null, null, null, null, caption);
        at.addHeavyRule();
        AT_Row tHead = at.addRow(tHeadings);
        tHead.setPaddingLeft(1);
        tHead.setPaddingRight(1);
        at.setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        int count = 0;
        for (BookLending bookLending : items) {
            AT_Row row = at.addRow(bookLending.getId(),
                    bookLending.getBookItemId(),
                    bookLending.getUserId(),
                    userService.findById(bookLending.getUserId()).getFullName(),
                    bookLending.getStatus(),
                    bookLending.getCreatedAt() == null ? "" : InstantUtils.instantToString(bookLending.getCreatedAt()),
                    bookLending.getDueAt() == null ? "" : InstantUtils.instantToString(bookLending.getDueAt()),
                    bookLending.getReturnAt() == null ? "" : InstantUtils.instantToString(bookLending.getReturnAt()));
            row.getCells().get(0).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.getCells().get(1).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.getCells().get(2).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.getCells().get(4).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.setPaddingLeft(1);
            row.setPaddingRight(1);
            at.addRule();
        }
        at.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        System.out.println(at.render(100));
        if (inputOption == InputOption.SHOW) {
            System.out.println("Nhấn enter để trở lại");
            sc.nextLine();
        }
    }
    public void showBooksLending(InputOption inputOption) {
        showList(inputOption,bookLendingService.findAll());
    }
}
