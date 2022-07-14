package vn.ntp.librus.views;

import vn.ntp.librus.model.Book;
import vn.ntp.librus.model.BookFormat;
import vn.ntp.librus.model.BookItem;
import vn.ntp.librus.services.BookItemService;
import vn.ntp.librus.services.BookService;
import vn.ntp.librus.services.IBookService;
import vn.ntp.librus.utils.AppUtils;
import vn.ntp.librus.utils.InstantUtils;
import vn.ntp.librus.utils.ValidateUtils;

import java.util.List;
import java.util.Scanner;


public class BookView {

    private IBookService bookService; //Dependency Inversion Principle (SOLID)
    private final Scanner scanner = new Scanner(System.in);


    public BookView() {
        bookService = BookService.getInstance();
    }

    BookItemService bookItemService = BookItemService.getInstance();

    public void add() {
        do {
            System.out.println("Nhập sách cùng tên (Enter để thêm mới):");
            String title = AppUtils.SCANNER.nextLine();
            if (title.trim().isEmpty()) {
                String isbn = inputISBN();
                System.out.println("Nhập tên sách");
                title = AppUtils.retryString("title");
                String author = inputAuthor(InputOption.ADD);
                String subject = inputSubject(InputOption.ADD);
                String language = inputLanguage(InputOption.ADD);
                Book newBook = new Book(title, isbn, author, subject, language);
                bookService.add(newBook);


                //ADD BOOKITEM

                String publisher =  inputPublisher(InputOption.ADD);
                int publicationAt = inputPublicationAt(InputOption.ADD);
                int numberOfPages = inputNumberOfPages(InputOption.ADD);
                double price = inputPrice(InputOption.ADD);
                BookFormat format = inputBookFormat(InputOption.ADD);

                BookItem newBookItem = new BookItem(publisher,
                        numberOfPages, price, format, publicationAt, newBook.getId());
                bookItemService.add(newBookItem);
                System.out.println("Bạn đã thêm sách thành công\n");

            } else {
                showBooksSearchAdd(title);
                System.out.println("Bạn muốn thêm thông tin cho sách (cùng tên) nào? Nhập Id:");
                long bookId = AppUtils.retryParseLong();

                String publisher = inputPublisher(InputOption.ADD);
                int publicationAt = inputPublicationAt(InputOption.ADD);
                int numberOfPages = inputNumberOfPages(InputOption.ADD);
                double price = inputPrice(InputOption.ADD);
                BookFormat format = inputBookFormat(InputOption.ADD);

                BookItem newBookItem = new BookItem(publisher,
                        numberOfPages, price, format, publicationAt, bookId);
                bookItemService.add(newBookItem);
                System.out.println("Bạn đã thêm sách thành công\n");

            }
        } while (AppUtils.isRetry(InputOption.ADD));
    }

    public void update() {
        boolean isRetry;
        do {
            showBooks(InputOption.UPDATE);
            int id = inputId(InputOption.UPDATE);
            System.out.println("┌ - - - - SỬA  - - - ┐");
            System.out.println("| 1.Sửa tên sách     |");
            System.out.println("| 2.Sửa tác giả      |");
            System.out.println("| 3.Sửa chủ đề       |");
            System.out.println("| 4.Sửa ngôn ngữ     |");
            System.out.println("| 5.Quay lại MENU    |");
            System.out.println("└ - - - - - -  - - - ┘");
            System.out.println("Chọn chức năng: ");
            int option = AppUtils.retryChoose(1, 7);
            Book newBook = new Book();
            newBook.setId(id);
            switch (option) {
                case 1:
                    String title = inputTitle(InputOption.UPDATE);
                    newBook.setTitle(title);
                    bookService.update(newBook);
                    System.out.println("Tên sách đã cập nhật thành công");
                    break;
                case 2:
                    String author = inputAuthor(InputOption.UPDATE);
                    newBook.setAuthor(author);
                    bookService.update(newBook);
                    System.out.println("Tác giả đã cập nhật thành công");
                    break;
                case 3:
                    String subject = inputSubject(InputOption.UPDATE);
                    newBook.setSubject(subject);
                    bookService.update(newBook);
                    System.out.println("Tác giả đã cập nhật thành công");
                    break;
                case 4:
                    String language = inputLanguage(InputOption.UPDATE);
                    newBook.setLanguage(language);
                    bookService.update(newBook);
                    System.out.println("Tác giả đã cập nhật thành công");
                    break;
                case 5:

                    break;
            }
            isRetry = option != 7 && AppUtils.isRetry(InputOption.UPDATE);
        } while (isRetry);
    }

    public void showBooksSearchAdd(String title) {

        System.out.println("-------------------------------------------------- DANH SÁCH SÁCH CÙNG TÊN -------------------------------------------------");
        System.out.printf("%-13s %-20s %-20s %-15s %-15s %-20s %-20s\n",
                "Id Book",
                "Tên sách",
                "Tác giả",
                "Chủ đề",
                "Ngôn ngữ",
                "Ngày tạo",
                "Ngày cập nhật");
        List<Book> books = bookService.findAllByTitle(title);
        for (Book book : books) {
            System.out.printf("%-13s %-20s %-20s %-15s %-15s %-20s %-20s\n",
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getSubject(),
                    book.getLanguage(),
                    InstantUtils.instantToString(book.getCreatedAt()),
                    book.getUpdatedAt() == null ? "" : InstantUtils.instantToString(book.getUpdatedAt()));
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------\n");

    }


    public void showBooks(InputOption inputOption) {
        System.out.println("-------------------------------------------------------------- DANH SÁCH SÁCH -------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-20s %-18s %-13s %-13s %-20s %-20s\n",
                "Id",
                "ISBN",
                "Tên sách",
                "Tác giả",
                "Chủ đề",
                "Ngôn ngữ",
                "Ngày tạo",
                "Ngày cập nhật");
        for (Book book : bookService.findAll()) {
            System.out.printf("%-15s %-15s %-20s %-18s %-13s %-13s %-20s %-20s\n",
                    book.getId(),
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getSubject(),
                    book.getLanguage(),
                    InstantUtils.instantToString(book.getCreatedAt()),
                    book.getUpdatedAt() == null ? "" : InstantUtils.instantToString(book.getUpdatedAt()));
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------\n");
        if (inputOption == InputOption.SHOW) AppUtils.isRetry(InputOption.SHOW);
    }

    public void remove() {
        showBooks(InputOption.DELETE);
        long id;
        while (!bookService.existsById(id = inputId(InputOption.DELETE))) {
            System.out.println("Không tìm thấy sản phẩm cần xóa");
            System.out.println("Nhấn 'y' để tiếp tục xóa \t|\t 'q' để quay lại \t|\t 't' để thoát chương trình");
            System.out.print(" ⭆ ");
            String option = scanner.nextLine();
            switch (option) {
                case "y":
                    break;
                case "q":
                    return;
                case "t":
                    MenuView.exit();
                    break;
                default:
                    System.out.println("Chọn chức năng không đúng! Vui lòng chọn lại");
                    break;
            }
        }

        System.out.println("❄ ❄ ❄ ❄ REMOVE COMFIRM ❄ ❄ ❄ ❄");
        System.out.println("❄  1. Nhấn 1 để xoá        ❄");
        System.out.println("❄  2. Nhấn 2 để quay lại   ❄");
        System.out.println("❄ ❄ ❄ ❄ ❄ ❄ ❄  ❄ ❄ ❄ ❄ ❄ ❄ ❄");
        int option = AppUtils.retryChoose(1, 2);
        if (option == 1) {
            bookService.deleteById(id);
            System.out.println("Đã xoá sản phẩm thành công! \uD83C\uDF8A");
            AppUtils.isRetry(InputOption.DELETE);
        }

    }

    public void findBooksByName() {
        System.out.println("Nhập tên sách mà bạn muốn tìm kiếm : ");
        String title = scanner.nextLine().toLowerCase();
        showBooksSearchAdd(title);

    }

    private int inputId(InputOption option) {
        int id;
        switch (option) {
            case ADD:
                System.out.println("Nhập Id");
                break;
            case UPDATE:
                System.out.println("Nhập ID của sách bạn muốn sửa");
                break;
            case DELETE:
                System.out.println("Nhập id bạn cần xoá: ");
                break;
        }
        boolean isRetry = false;
        do {
            id = AppUtils.retryParseInt();
            boolean exist = bookService.existsById(id);
            switch (option) {
                case ADD:
                    if (exist) {
                        System.out.println("Id này đã tồn tại. Vui lòng nhập id khác!");
                    }
                    isRetry = exist;
                    break;
                case UPDATE:
                    if (!exist) {
                        System.out.println("Không tìm thấy id! Vui lòng nhập lại");
                    }
                    isRetry = !exist;
                    break;
            }
        } while (isRetry);
        return id;
    }

    private String inputTitle(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập tên sách ");
                break;
            case UPDATE:
                System.out.println("Nhập tên sách muốn sửa: ");
                break;
        }
        return AppUtils.retryString("Tên sách");
    }

    private String inputAuthor(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập tên tác giả ");
                break;
            case UPDATE:
                System.out.println("Nhập tên tác giả muốn sửa: ");
                break;
        }
        return AppUtils.retryString("Tên tác giả");
    }

    private String inputSubject(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập chủ đề sách: ");
                break;
            case UPDATE:
                System.out.println("Nhập chủ đề sách muốn sửa: ");
                break;
        }
        return AppUtils.retryString("Chủ đề");
    }

    private String inputPublisher(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập nhà xuất bản: ");
                break;
            case UPDATE:
                System.out.println("Nhập nhà xuất bản muốn sửa: ");
                break;
        }
        return AppUtils.retryString("Chủ đề");
    }

    private String inputLanguage(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập ngôn ngữ của sách: ");
                break;
            case UPDATE:
                System.out.println("Nhập ngôn ngữ của sách muốn sửa: ");
                break;
        }
        return AppUtils.retryString("Ngôn ngữ sách");
    }

    private String inputISBN() {
        System.out.println("Nhập ISBN - ISBN phải 10 chữ số và theo đúng định dạng như ví dụ sau: 0-596-52068-9 hoặc 0596520689 ");
        System.out.print(" ⭆ ");
        String isbn;
        do {
            if (!ValidateUtils.isIsbnValid(isbn = scanner.nextLine())) {
                System.out.println("Isbn " + isbn + "của bạn không đúng định dạng! Vui lòng kiểm tra và nhập lại ");
                System.out.println("Nhập ISBN - ISBN phải 10 chữ số và theo đúng định dạng như ví dụ sau: 0-596-52068-9 hoặc 0596520689");
                System.out.print(" ⭆ ");
                continue;
            }
            if (bookService.existByISBN(isbn)) {
                System.out.println("Isbn " + isbn + "của bạn đã tồn tại! vui lòng kiểm tra lại");
                System.out.println("Nhập ISBN - ISBN phải 10 chữ số và theo đúng định dạng như ví dụ sau: 0-596-52068-9 hoặc 0596520689");
                System.out.print(" ⭆ ");
                continue;
            }
            break;
        } while (true);
        return isbn;
    }


    private int inputNumberOfPages(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập số trang sách: ");
                break;
            case UPDATE:
                System.out.println("Nhập số trang sách muốn sửa: ");
                break;
        }
        int numberOfPages;
        do {
            numberOfPages = AppUtils.retryParseInt();
            if (numberOfPages <= 0)
                System.out.println("Số trang phải lớn hơn 0 (số trang > 0)");
        } while (numberOfPages <= 0);
        return numberOfPages;
    }


    private double inputPrice(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập giá sách: ");
                break;
            case UPDATE:
                System.out.println("Nhập giá sách bạn muốn sửa: ");
                break;
        }
        double price;
        do {
            price = AppUtils.retryParseDouble();
            if (price <= 0)
                System.out.println("Giá sách phải lớn hơn 0 (giá > 0)");
        } while (price <= 0);
        return price;
    }


    private int inputPublicationAt(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập năm phát hành sách: ");
                break;
            case UPDATE:
                System.out.println("Nhập năm phát hành sách mà bạn muốn sửa:");
                break;
        }
        int publicationAt;
        do {
            publicationAt = AppUtils.retryParseInt();
            if (publicationAt <= 0)
                System.out.println("Năm xuất bản phải lớn hơn 0 ");
        } while (publicationAt <= 0);
        return publicationAt;
    }

    private BookFormat inputBookFormat(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập kiểu định dạng của sách: PAPERBACK|HARDCOVER|NEWSPAPER|MAGAZINE|EBOOK ");
                break;
            case UPDATE:
                System.out.println("Nhập kiểu định dạng của sách muốn sửa: PAPERBACK|HARDCOVER|NEWSPAPER|MAGAZINE|EBOOK ");
                break;
        }
        return BookFormat.parserBookFormat(AppUtils.retryString(""));
    }
}
