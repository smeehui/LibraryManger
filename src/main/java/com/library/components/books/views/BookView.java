package com.library.components.books.views;

import com.library.components.books.services.BookItemService;
import com.library.components.books.services.BookService;
import com.library.components.books.services.IBookItemService;
import com.library.components.books.services.IBookService;
import com.library.views.InputOption;
import com.library.views.ListView;
import com.library.views.ShowErrorMessage;
import com.library.views.View;
import com.library.components.books.models.Book;
import com.library.components.books.models.BookFormat;
import com.library.components.books.models.BookItem;
import com.library.utils.InstantUtils;
import com.library.utils.ValidateUtils;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.List;


public class BookView extends View implements ListView<Book> {

    private final IBookService bookService; //Dependency Inversion Principle (SOLID)

    public BookView() {
        bookService = BookService.getInstance();
        caption = "DANH SÁCH SÁCH";
        tHeadings = new String[]{"#", "Id", "ISBN", "Tên sách", "Tác giả", "Chủ đề", "Ngôn ngữ", "Ngày tạo", "Ngày cập nhật"};
    }

    IBookItemService bookItemService = BookItemService.getInstance();

    public void add() {
        do {
            System.out.println("Nhập sách cùng tên (N để thêm mới):");
            String title = tryInput.tryString("Tiêu đề");
            if (title.equals("N")) {
                String isbn = inputISBN();
                if (tryInput.isReturn(isbn)) break;
                System.out.println("Nhập tên sách");
                title = tryInput.tryString("Tiêu đề");
                if (tryInput.isReturn(title)) break;
                String author = inputAuthor(InputOption.ADD);
                if (tryInput.isReturn(author)) break;
                String subject = inputSubject(InputOption.ADD);
                if (tryInput.isReturn(subject)) break;
                String language = inputLanguage(InputOption.ADD);
                if (tryInput.isReturn(language)) break;
                Book newBook = new Book(title, isbn, author, subject, language);
                preAddNewBook(newBook);


                //ADD BOOKITEM

                String publisher = inputPublisher();
                if (tryInput.isReturn(String.valueOf(publisher))) break;
                int publicationAt = inputPublicationAt();
                if (tryInput.isReturn(String.valueOf(publicationAt))) break;
                int numberOfPages = inputNumberOfPages();
                if (tryInput.isReturn(String.valueOf(numberOfPages))) break;
                double price = inputPrice();
                if (tryInput.isReturn(String.valueOf(price))) break;
                BookFormat format = inputBookFormat();
                if (tryInput.isReturn(String.valueOf(format))) break;
                BookItem newBookItem = new BookItem(
                        publisher, numberOfPages, price, format,
                        publicationAt, newBook);
                preAddNewBookItem(newBookItem);
            } else if (tryInput.isReturn(title)) break;
            else {
                showList(InputOption.UPDATE, bookService.findAll());
                System.out.println("Bạn muốn thêm thông tin cho sách (cùng tên) nào? Nhập Id:");
                long bookId = tryInput.tryLong("ID");
                if (tryInput.isReturn(String.valueOf(bookId))) break;
                String publisher = inputPublisher();
                int publicationAt = inputPublicationAt();
                int numberOfPages = inputNumberOfPages();
                double price = inputPrice();
                BookFormat format = inputBookFormat();

                BookItem newBookItem = new BookItem(publisher,
                        numberOfPages, price, format, publicationAt, bookId);
                preAddNewBookItem(newBookItem);
            }
        } while (true);
    }

    private void preAddNewBook(Book newBook) {
        System.out.println(newBook.getPreview());
        System.out.println("Nhấn enter để thêm sách, # để huỷ bỏ.");
        boolean confirmed = !tryInput.isReturn(sc.nextLine());
        if (confirmed) {
            bookService.add(newBook);
            System.out.println("Bạn đã thêm sách thành công");
        } else {
            System.out.println("Huỷ bỏ, enter để tiếp tục");
            sc.nextLine();
        }
    }

    private void preAddNewBookItem(BookItem newBookItem) {
        System.out.println(newBookItem.getPreview());
        System.out.println("Nhấn enter để thêm sách, # để huỷ bỏ.");
        boolean confirmed = !tryInput.isReturn(sc.nextLine());
        if (confirmed) {
            bookItemService.add(newBookItem);
            System.out.println("Bạn đã thêm sách thành công\n Enter để tiếp tục.");
            sc.nextLine();
        } else {
            System.out.println("Huỷ bỏ, enter để tiếp tục");
            sc.nextLine();
        }
    }

    public void update() {
        int option;
        do {
            showList(InputOption.UPDATE, bookService.findAll());
            long id = inputId(InputOption.UPDATE);
            if (tryInput.isReturn(String.valueOf(id))) break;
            do {
                System.out.println(tbConverter.convertMtplCol("SỬA", "Sửa tên sách", "Sửa tác giả", "Sửa chủ đề", "Sửa ngôn ngữ"));
                option = tryInput.tryInt("Lựa chọn");
                if (tryInput.isReturn(String.valueOf(option))) break;
                Book newBook = new Book();
                newBook.setId(id);
                switch (option) {
                    case 1 -> {
                        String title = inputTitle();
                        if (tryInput.isReturn(title)) break;
                        newBook.setTitle(title);
                        bookService.update(newBook);
                        System.out.println("Tên sách đã cập nhật thành công, nhấn enter");
                    }
                    case 2 -> {
                        String author = inputAuthor(InputOption.UPDATE);
                        if (tryInput.isReturn(author)) break;
                        newBook.setAuthor(author);
                        bookService.update(newBook);
                        System.out.println("Tác giả đã cập nhật thành công");
                    }
                    case 3 -> {
                        String subject = inputSubject(InputOption.UPDATE);
                        if (tryInput.isReturn(subject)) break;
                        newBook.setSubject(subject);
                        bookService.update(newBook);
                        System.out.println("Tác giả đã cập nhật thành công");
                    }
                    case 4 -> {
                        String language = inputLanguage(InputOption.UPDATE);
                        if (tryInput.isReturn(language)) break;
                        newBook.setLanguage(language);
                        bookService.update(newBook);
                        System.out.println("Tác giả đã cập nhật thành công");
                    }
                    default -> ShowErrorMessage.outOfRange("Lựa chọn");
                }
                System.out.println("Enter để tiếp tục sửa");
                if (tryInput.isReturn(sc.nextLine())) break;
            } while (true);
        } while (tryInput.isReturn(String.valueOf(option)));
    }


    public void remove() {
        long id;
        while (true) {
            showList(InputOption.DELETE, bookService.findAll());
            id = inputId(InputOption.DELETE);
            if (id == -1) return;
            if (bookService.existsById(id)) break;
            if (tryInput.isReturn(String.valueOf(id))) break;
            System.out.println(tbConverter.convertMtplCol("Không tìm thấy sản phẩm", "Enter để tiếp tục"));
            sc.nextLine();
        }
        System.out.println(tbConverter.convertMtplCol("Bạn có chắc muốn xoá", "Nhấn 1 để xoá"));
        int option = tryInput.tryInt("lựa chọn");
        if (tryInput.isReturn(String.valueOf(option))) return;
        if (option == 1) {
            bookService.deleteById(id);
            System.out.println("Đã xoá sản phẩm thành công! \uD83C\uDF8A, nhấn enter");
            sc.nextLine();
        }
    }

    public void findBooksByName() {
        System.out.println("Nhập tên sách mà bạn muốn tìm kiếm : ");
        String title = tryInput.tryString("Tiêu đề").toLowerCase();
        if (tryInput.isReturn(title)) return;
        showList(InputOption.SHOW, bookService.findAllByTitle(title));
    }

    private long inputId(InputOption option) {
        long id;
        switch (option) {
            case ADD -> System.out.println("Nhập Id");
            case UPDATE -> System.out.println("Nhập ID của sách bạn muốn sửa");
            case DELETE -> System.out.println("Nhập id bạn cần xoá: ");
        }
        boolean isRetry = false;
        do {
            id = tryInput.tryLong("ID");
            if (id == -1) return -1;
            boolean exist = bookService.existsById(id);
            isRetry = tryInput.isRetryId(option, isRetry, exist);
        } while (isRetry);
        return id;
    }

    private String inputTitle() {
        System.out.println("Nhập tên sách muốn sửa: ");
        return tryInput.tryString("Tên sách");
    }

    private String inputAuthor(InputOption option) {
        switch (option) {
            case ADD -> System.out.println("Nhập tên tác giả ");
            case UPDATE -> System.out.println("Nhập tên tác giả muốn sửa: ");
        }
        return tryInput.tryString("Tên tác giả");
    }

    private String inputSubject(InputOption option) {
        switch (option) {
            case ADD -> System.out.println("Nhập chủ đề sách: ");
            case UPDATE -> System.out.println("Nhập chủ đề sách muốn sửa: ");
        }
        return tryInput.tryString("Chủ đề");
    }

    private String inputPublisher() {
        System.out.println("Nhập nhà xuất bản: ");
        return tryInput.tryString("Chủ đề");
    }

    private String inputLanguage(InputOption option) {
        switch (option) {
            case ADD -> System.out.println("Nhập ngôn ngữ của sách: ");
            case UPDATE -> System.out.println("Nhập ngôn ngữ của sách muốn sửa: ");
        }
        return tryInput.tryString("Ngôn ngữ sách");
    }

    private String inputISBN() {
        String isbn;
        do {
            System.out.println("Nhập ISBN - ISBN phải 10 chữ số và theo đúng định dạng như ví dụ sau: 0-596-52068-9 hoặc 0596520689 ");
            isbn = sc.nextLine();
            System.out.print(" ⭆ ");
            if (isbn.equals("#")) return "#";
            if (!ValidateUtils.isIsbnValid(isbn)) {
                System.out.println("Isbn " + isbn + "của bạn không đúng định dạng! Vui lòng kiểm tra và nhập lại ");
                continue;
            }
            if (bookService.existByISBN(isbn)) {
                System.out.println("Isbn " + isbn + "của bạn đã tồn tại! vui lòng kiểm tra lại");
                continue;
            }
            break;
        } while (true);
        return isbn;
    }


    private int inputNumberOfPages() {
        System.out.println("Nhập số trang sách: ");
        int numberOfPages;
        do {
            numberOfPages = tryInput.tryInt("Số trang");
            if (numberOfPages == -1) return -1;
            if (numberOfPages < -1 || numberOfPages == 0)
                System.out.println("Số trang phải lớn hơn 0 (số trang > 0)");
        } while (numberOfPages <= 0);
        return numberOfPages;
    }


    private double inputPrice() {
        System.out.println("Nhập giá sách: ");
        double price;
        do {
            price = tryInput.tryDouble("giá");
            if (price == -1) return -1;
            if (price <= 0)
                System.out.println("Giá sách phải lớn hơn 0 (giá > 0)");
        } while (price <= 0);
        return price;
    }


    private int inputPublicationAt() {
        System.out.println("Nhập năm phát hành sách: ");
        int publicationAt;
        do {
            publicationAt = tryInput.tryInt("Năm phát hành");
            if (publicationAt == -1) return -1;
            if (publicationAt < -1 || publicationAt == 0)
                System.out.println("Năm xuất bản phải lớn hơn 0 ");
        } while (publicationAt <= 0);
        return publicationAt;
    }

    private BookFormat inputBookFormat() {
        while (true) {
            System.out.println("Nhập kiểu định dạng của sách: PAPERBACK|HARDCOVER|NEWSPAPER|MAGAZINE|EBOOK ");
            try {
                String input = tryInput.tryString("định dạng");
                if (input.equals("#")) return null;
                return BookFormat.parserBookFormat(input);
            } catch (IllegalArgumentException e) {
                ShowErrorMessage.syntax("Định dạng sách");
            }
        }
    }

    public void showAllBooks() {
        showList(InputOption.SHOW, bookService.findAll());
    }

    @Override
    public void showList(InputOption inputOption, List<Book> books) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_LongestLine());
        at.addHeavyRule();
        at.addRow(null, null, null, null, null, null, null, null, caption);
        at.addHeavyRule();
        AT_Row tHead = at.addRow(tHeadings);
        tHead.getCells().get(1).getContext().setTextAlignment(TextAlignment.RIGHT);
        tHead.getCells().get(2).getContext().setTextAlignment(TextAlignment.RIGHT);
        tHead.getCells().get(3).getContext().setTextAlignment(TextAlignment.RIGHT);
        tHead.setPaddingLeft(1);
        tHead.setPaddingRight(1);
        at.setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        int count = 0;
        for (Book book : books) {
            AT_Row row = at.addRow(++count, book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getSubject(), book.getLanguage(),
                    InstantUtils.instantToString(book.getCreatedAt()), InstantUtils.instantToString(book.getUpdatedAt()));
            row.getCells().get(0).getContext().setTextAlignment(TextAlignment.CENTER);
            row.getCells().get(1).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.getCells().get(3).getContext().setTextAlignment(TextAlignment.LEFT);
            row.getCells().get(4).getContext().setTextAlignment(TextAlignment.LEFT);
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
}
