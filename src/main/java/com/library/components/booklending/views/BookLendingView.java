package com.library.components.booklending.views;

import com.library.components.booklending.services.BookLendingService;
import com.library.components.booklending.services.IBookLendingService;
import com.library.components.books.services.BookItemService;
import com.library.components.books.services.BookService;
import com.library.components.books.services.IBookItemService;
import com.library.components.books.services.IBookService;
import com.library.components.books.views.BookItemView;
import com.library.components.fine.services.FineService;
import com.library.components.fine.services.IFineService;
import com.library.components.fine.views.FineView;
import com.library.components.user.models.Role;
import com.library.components.user.services.IUserService;
import com.library.components.user.services.UserService;
import com.library.components.user.views.UserView;
import com.library.views.View;
import com.library.components.books.models.BookItem;
import com.library.components.booklending.model.BookLending;
import com.library.components.books.models.BookStatus;
import com.library.components.booklending.model.LendingStatus;
import com.library.components.user.models.User;
import com.library.services.Constants;
import com.library.utils.CurrencyUtils;
import com.library.utils.InputRetry;
import com.library.utils.InstantUtils;
import com.library.views.InputOption;
import com.library.views.ListView;
import com.library.views.ShowErrorMessage;
import de.vandermeer.asciitable.*;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BookLendingView extends View implements ListView<BookLending> {
    private final InputRetry tryInput = InputRetry.getInstance();
    private final IBookLendingService bookLendingService;
    private final IUserService userService;
    private final IBookItemService bookItemService;
    private final IBookService bookService;
    private static BookLendingView instance;
    private final BookItemView bookItemView;
    private final UserView userView;
    private final FineView fineView;
    private final IFineService fineService;


    private BookLendingView() {
        bookLendingService = BookLendingService.getInstance();
        userService = UserService.getInstance();
        bookItemService = BookItemService.getInstance();
        bookService = BookService.getInstance();
        userView = UserView.getInstance();
        bookItemView = new BookItemView();
        fineView = FineView.getInstance();
        fineService = FineService.getInstance();
        caption = "DANH SÁCH MƯỢN/TRẢ SÁCH";
        tHeadings = new String[]{"Id", "Id BookItem", "Id người dùng", "Tên đầy đủ", "Trạng thái mượn",
                "Ngày Mượn Sách", "Ngày Đến Hạn", "Ngày Trả Sách"};
    }

    public static BookLendingView getInstance() {
        if (instance == null) instance = new BookLendingView();
        return instance;
    }

    public void update() {
        do {
            boolean status = showBooksLending(InputOption.UPDATE);
            if (!status) break;
            long id = inputLendingId(InputOption.UPDATE);
            if (tryInput.isReturn(String.valueOf(id))) break;
            System.out.println(tbConverter.convertSingleCol("CHỈNH SỬA TRẠNG THÁI"));
            BookLending newBookLending = new BookLending();
            newBookLending.setId(id);
            LendingStatus lendingStatus = inputLendingStatus(InputOption.ADD);
            if (tryInput.isReturn(String.valueOf(lendingStatus))) break;
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
            case CHECKOUT -> System.out.println("Nhập mã sách để mượn");
            case RETURN -> System.out.println("Nhập mã sách để trả");
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
        do {
            id = tryInput.tryLong("id của BookLending");
            if (tryInput.isReturn(String.valueOf(id))) return -1;
            boolean exist = bookLendingService.existsById(id);
            switch (option) {
                case ADD -> {
                    if (exist) {
                        System.out.println("Id này đã tồn tại. Vui lòng nhập Id khác!");
                        continue;
                    }
                    return id;
                }
                case UPDATE -> {
                    if (!exist) {
                        System.out.println("Không tìm thấy Id! Vui lòng nhập lại");
                        continue;
                    }
                    if (bookLendingService.findById(id).getStatus().equals(LendingStatus.RETURN)) {
                        System.out.println("Sách đã được trả, không thể sửa.");
                        continue;
                    }
                    return id;
                }
            }
        } while (true);
    }

    public void checkoutBook(long id) {
        User currentUser = userService.findById(id);
        long bookItemId;
        BookItem bookItem;
        do {
            bookItemView.showList(InputOption.CHECKOUT, bookItemService.findAll());
            bookItemId = inputBookItemId(InputOption.CHECKOUT);
            if (tryInput.isReturn(String.valueOf(bookItemId))) return;
            bookItem = bookItemService.findById(bookItemId);
            if (bookItem == null) System.out.println("Không tìm thấy Book Item");
        } while (bookItem == null);

        bookItemView.viewBookItemDetails(bookItem);
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
        if (currentUser.getRole().equals(Role.LIBRARIAN)) {
            do {
                userId = inputUserId();
                if (tryInput.isReturn(String.valueOf(userId))) return;
                user = userService.findById(userId);
            } while (user == null);
            userView.viewUserDetails(user);
        } else user = currentUser;

        if (bookLendingService.isBookIssuedQuotaExceeded(user.getId())) {
            System.out.println("Người dùng đã mượn quá số lượng sách cho phép, nhấn enter.");
            sc.nextLine();
            return;
        }
        bookLendingService.lendBook(user.getId(), bookItem.getBookItemID());
        System.out.println("Bạn đã mượn sách thành công!");
        sc.nextLine();
    }


    public void returnBook(long id) {
        User currentUser = userService.findById(id);
        long userId;
        do {
            if (currentUser.getRole().equals(Role.LIBRARIAN)) {
                userId = inputUserId();
                if (tryInput.isReturn(String.valueOf(userId))) break;
                boolean hasUserNotLent = viewBookLendingByUserId(userId);
                if (!hasUserNotLent) {
                    System.out.println("Người dùng chưa mượn sách");
                    sc.nextLine();
                    return;
                }
            } else userId = currentUser.getId();
            long bookItemId = 0;
            BookItem bookItem = null;
            do {
                List<BookLending> availableBooklendings = bookLendingService.findBookLendingByUserIdAndStatus(userId, LendingStatus.LENDING);
                if (availableBooklendings.isEmpty()) {
                    System.out.println("Bạn chưa mượn sách nào.");
                    sc.nextLine();
                    return;
                }
                showList(InputOption.UPDATE, availableBooklendings);
                bookItemId = inputBookItemId(InputOption.RETURN);
                if (tryInput.isReturn(String.valueOf(bookItemId))) return;
                bookItem = bookItemService.findById(bookItemId);
                if (bookItem == null) {
                    System.out.println("Nhập sai ID sách mượn.");
                    sc.nextLine();
                }
            } while (bookItem == null);
            BookLending bookLending = bookLendingService.findByBookItemIdAndUserIdAndStatus(bookItem.getBookItemID(), userId, LendingStatus.LENDING);
            if (bookLending == null) {
                System.out.println("Người dùng này chưa mượn sách ở thư viện!");
            } else do {
                showBooksLendingBill(bookLending);
                bookLendingService.returnBook(bookLending.getId());
                System.out.println("Bạn đã trả sách thành công \uD83C\uDF8A! Nhấn enter để tiếp tục, # để trở lại.");
            } while (tryInput.isReturn(sc.nextLine()));
        } while (true);
    }

    private void showBooksLendingBill(BookLending bookLending) {
        AsciiTable tb = new AsciiTable();
        tb.addRule();
        User currentUser = userService.findById(bookLending.getUserId());
        AT_Row caption = tb.addRow(null, null, null, null, null, null, "Đọc giả: " + currentUser.getFullName());
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
        Instant returnAt = Instant.now();
        long numberOfExceedDays = InstantUtils.countGapTime(returnAt, bookLending.getDueAt());
        AT_Row row = tb.addRow(title, lendingStatus, createdAt, dueAtStr, InstantUtils.instantToString(returnAt),
                numberOfExceedDays < 0 ? "" : String.valueOf(numberOfExceedDays),
                numberOfExceedDays < 0 ? "" : CurrencyUtils.doubleToVND(numberOfExceedDays * Constants.FINE_AMOUNT));
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
        List<BookLending> bookLendingList = bookLendingService.findBookLendingByUserId(userId);
        List<BookLending> result = new ArrayList<>();
        for (BookLending bookLending : bookLendingList) {
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
        switch (inputOption) {
            case CHECKOUT:
                if (items.isEmpty()) System.out.println("Không có sách nào đang mượn.");
                sc.nextLine();
                return;
        }
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
        if (inputOption.equals(InputOption.SHOW)) {
            System.out.println("Nhấn enter để trở lại");
            sc.nextLine();
            return;
        }
        if (inputOption.equals(InputOption.FINE_SHOW)) {
            System.out.println("Nhấn enter để trở lại");
            sc.nextLine();
        }
    }

    public boolean showBooksLending(InputOption inputOption) {
        List<BookLending> allBookLendins = bookLendingService.findAll();
        if (allBookLendins.isEmpty()) {
            System.out.println("Chưa có thông tin mượn sách.");
            sc.nextLine();
            return false;
        }
        showList(inputOption, allBookLendins);
        return true;
    }
}
