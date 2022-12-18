package com.library.components.books.views;

import com.library.components.books.models.Book;
import com.library.components.books.services.BookItemService;
import com.library.components.books.services.BookService;
import com.library.components.books.services.IBookItemService;
import com.library.components.books.models.BookFormat;
import com.library.components.books.models.BookItem;
import com.library.components.books.models.BookStatus;
import com.library.components.books.services.IBookService;
import com.library.utils.CurrencyUtils;
import com.library.utils.InstantUtils;
import com.library.views.InputOption;
import com.library.views.ListView;
import com.library.views.ShowErrorMessage;
import com.library.views.View;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.List;


public class BookItemView extends View implements ListView<BookItem> {
    private final IBookItemService bookItemService;
    private final IBookService bookService;

    public BookItemView() {
        bookItemService = BookItemService.getInstance();
        bookService = BookService.getInstance();
        caption = "DANH SÁCH SÁCH MƯỢN";
        tHeadings = new String[]{"#", "Id", "Tên sách", "Nhà XB", "Năm XB", "Gía sách", "Định dạng ", "Trạng thái ", "Ngày nhập vào TV", "Ngày mượn sách"};
    }

    public void update() {
        do {
            showBooksItem(InputOption.UPDATE);
            long id = inputId(InputOption.UPDATE);
            if (tryInput.isReturn(String.valueOf(id)))break;
            System.out.println(tbConverter.convertMtplCol("Sửa BookItem","Sửa nhà xuất bản","Sửa năm xuất bản",
                   "Sửa giá sách","Sửa định dạng sách","Sửa trạng thái sách"));
            System.out.print("Chọn chức năng: ");
            int option = tryInput.tryInt("chức năng");
            if (tryInput.isReturn(String.valueOf(option))) break;
            BookItem newBookItem = new BookItem();
            newBookItem.setBookItemID(id);
            switch (option) {
                case 1 -> {
                    String publisher = inputPublisher(InputOption.UPDATE);
                    if (tryInput.isReturn(publisher)) break;
                    newBookItem.setPublisher(publisher);
                    bookItemService.update(newBookItem);
                    System.out.println("Nhà xuất bản đã cập nhập thành công");
                }
                case 2 -> {
                    Integer publicationAt = inputPublicationAt(InputOption.UPDATE);
                    if (tryInput.isReturn(String.valueOf(publicationAt))) break ;
                    newBookItem.setPublicationAt(publicationAt);
                    bookItemService.update(newBookItem);
                    System.out.println("Năm xuất bản đã cập nhập thành công");
                }
                case 3 -> {
                    double price = inputPrice(InputOption.UPDATE);
                    if (tryInput.isReturn(String.valueOf(price))) return;
                    newBookItem.setPrice(price);
                    bookItemService.update(newBookItem);
                    System.out.println("Gía sách đã cập nhật thành công");
                }
                case 4 -> {
                    BookFormat bookFormat = inputBookFormat(InputOption.UPDATE);
                    if (tryInput.isReturn(String.valueOf(bookFormat))) return;
                    newBookItem.setFormat(BookFormat.parserBookFormat(bookFormat.getValue()));
                    bookItemService.update(newBookItem);
                    System.out.println("Định dạng sách đã cập nhật thành công");
                }
                case 5 -> {
                    BookStatus status = inputBookStatus(InputOption.UPDATE);
                    if (tryInput.isReturn(String.valueOf(status))) return;
                    newBookItem.setStatus(status);
                    bookItemService.update(newBookItem);
                    System.out.println("Trạng thái của sách đã cập nhập thành công");
                }
                default -> ShowErrorMessage.outOfRange("chức năng");
            }
        } while (true);
    }


    public void showBooksItem(InputOption inputOption) {
        showList(inputOption,bookItemService.findAll());
    }


    private long inputId(InputOption option) {
        long id;
        switch (option) {
            case ADD -> System.out.println("Nhập Id");
            case CHECKOUT -> System.out.println("Nhập Id BookItem để checkout sách");
            case RETURN -> System.out.println("Nhập Id BookItem để trả sách");
            case UPDATE -> System.out.println("Nhập Id bạn muốn sửa");
            case DELETE -> System.out.println("Nhập Id bạn cần xoá: ");
        }
        boolean isRetry = false;
        do {
            id = tryInput.tryLong("ID");
            if (tryInput.isReturn(String.valueOf(id))) return -1;
            boolean exist = bookItemService.existsById(id);
            switch (option) {
                case ADD -> {
                    if (exist) {
                        System.out.println("Id này đã tồn tại. Vui lòng nhập id khác!");
                    }
                    isRetry = exist;
                }
                case CHECKOUT -> {
                    if (!exist) {
                        System.out.println("Không tìm thấy sách ! Vui lòng nhập lại");
                    }
                    isRetry = !exist;
                }
                case UPDATE -> {
                    if (!exist) {
                        System.out.println("Không tìm thấy id! Vui lòng nhập lại");
                    }
                    isRetry = !exist;
                }
            }
        } while (isRetry);
        return id;
    }

    private String inputPublisher(InputOption option) {
        switch (option) {
            case ADD -> System.out.println("Nhập nhà xuất bản: ");
            case UPDATE -> System.out.println("Nhập nhà xuất bản muốn sửa: ");
        }
        return tryInput.tryString("Chủ đề");
    }

    private int inputPublicationAt(InputOption option) {
        switch (option) {
            case ADD -> System.out.println("Nhập năm phát hành sách: ");
            case UPDATE -> System.out.println("Nhập năm phát hành sách mà bạn muốn sửa:");
        }
        int publicationAt;
        do {
            publicationAt = tryInput.tryInt("năm xuất bản");
            if (publicationAt==0) return -1;
            if (publicationAt <= 0)
                System.out.println("Năm xuất bản phải lớn hơn 0 ");
        } while (publicationAt <= 0);
        return publicationAt;
    }


    private double inputPrice(InputOption option) {
        switch (option) {
            case ADD -> System.out.println("Nhập giá sách: ");
            case UPDATE -> System.out.println("Nhập giá sách bạn muốn sửa: ");
        }
        double price;
        do {
            price = tryInput.tryDouble("giá");
            if (price==-1) return -1;
            if (price <= 0)
                System.out.println("Giá sách phải lớn hơn 0 (giá > 0)");
        } while (price <= 0);
        return price;
    }

    private BookFormat inputBookFormat(InputOption option) {
       while (true){
           switch (option) {
               case ADD ->
                       System.out.println("Nhập kiểu định dạng của sách: PAPERBACK|HARDCOVER|NEWSPAPER|MAGAZINE|EBOOK ");
               case UPDATE ->
                       System.out.println("Nhập kiểu định dạng của sách muốn sửa: PAPERBACK|HARDCOVER|NEWSPAPER|MAGAZINE|EBOOK ");
           }
           try {
               String input = tryInput.tryString("định dạng");
               if (input.equals("#")) return null;
               return BookFormat.parserBookFormat(input);
           }catch (IllegalArgumentException e) {
               ShowErrorMessage.syntax("định dạng sách");
           }
       }
    }

    private BookStatus inputBookStatus(InputOption option) {
       while (true) {
           switch (option) {
               case ADD -> System.out.println("Nhập trạng thái của sách: AVAILABLE|RESERVED|LOANED|LOST");
               case UPDATE -> System.out.println("Nhập trạng thái sách muốn sửa: AVAILABLE|RESERVED|LOANED|LOST ");
           }
           try {
               String input = tryInput.tryString("trạng thái");
               if (input.equals("#")) return null;
               return BookStatus.parseBookStatus(input);
           }catch (IllegalArgumentException e) {
               ShowErrorMessage.syntax("trạng thái");
           }
       }
    }

    @Override
    public void showList( InputOption inputOption, List<BookItem> bookItems) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_LongestLine());
        at.addRule();
        AT_Row tCaption = at.addRow( null, null, null, null, null, null, null, null, null, caption);
        tCaption.getCells().get(9).getContext().setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        AT_Row tHead = at.addRow(tHeadings);
        tHead.setPaddingLeft(1);
        tHead.setPaddingRight(1);
        at.addRule();
        int count = 0;
        for (BookItem bookItem : bookItems) {
            AT_Row row = at.addRow(++count, bookItem.getBookItemID(), bookItem.getBook().getTitle(), bookItem.getPublisher(), bookItem.getPublicationAt(),
                    CurrencyUtils.doubleToVND(bookItem.getPrice()), bookItem.getFormat(), bookItem.getStatus(),
                    bookItem.getDateOfPurchase() == null ? "" : InstantUtils.instantToString(bookItem.getDateOfPurchase()),
                    bookItem.getBorrowedAt() == null ? "" : InstantUtils.instantToString(bookItem.getBorrowedAt()));
            row.getCells().get(1).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.getCells().get(4).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.getCells().get(5).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.setPaddingRight(1);
            row.setPaddingLeft(1);
            at.addRule();
        }
        at.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        System.out.println(at.render());
        if (inputOption == InputOption.SHOW) {
            System.out.println("Nhấn enter để trở lại");
            sc.nextLine();
        }
    }

    public void viewBookItemDetails(BookItem bookItem) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_LongestLine());
        AT_Row tHead = at.addRow("ID", "Tiêu đề", "Nhà XB", "Số trang", "Ngày mượn sách", "Hạn trả sách", "Giá sách",
                "Định dạng", "Trạng thái sách", "Ngày mua", "Năm XB", "Ngày cập nhật");
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
}
