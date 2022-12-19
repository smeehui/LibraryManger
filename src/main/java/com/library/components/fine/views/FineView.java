package com.library.components.fine.views;

import com.library.components.books.services.BookItemService;
import com.library.components.books.services.IBookItemService;
import com.library.components.fine.models.Fine;
import com.library.components.fine.models.FineStatus;
import com.library.components.fine.services.FineService;
import com.library.components.fine.services.IFineService;
import com.library.components.user.models.Role;
import com.library.components.user.models.User;
import com.library.components.user.services.IUserService;
import com.library.components.user.services.UserService;
import com.library.components.user.views.UserView;
import com.library.services.Constants;
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

import java.util.ArrayList;
import java.util.List;

public class FineView extends View implements ListView<Fine> {

    private IFineService fineService;
    private IUserService userService;
    private IBookItemService bookItemService;
    private UserView userView;
    private static FineView instance;

    private FineView() {
        userView = UserView.getInstance();
        fineService = FineService.getInstance();
        userService = UserService.getInstance();
        bookItemService = BookItemService.getInstance();
        //1670919901,1657613272,1657613920,1150000.0,2022-12-13T08:25:01.169088Z
        tHeadings = new String[]{"ID", "Tiêu đề sách", "Thành viên", "Số tiền phạt", "Ngày tạo", "Trạng thái", "Ngày nộp phạt"};
        caption = "DANH SÁCH TIỀN PHẠT";
    }

    public static FineView getInstance() {
        if (instance == null) {
            instance = new FineView();
        }
        return instance;
    }

    public Fine add(long numberOfExceedDays, Long bookItemId, long userId) {
        double fineAmount = numberOfExceedDays * Constants.FINE_AMOUNT;
        Fine fine = new Fine(bookItemId, userId, fineAmount);
        fineService.add(fine);
        return fine;
    }

    public void viewUnpaidFines(long userId) {
        List<Fine> unPaidFines = fineService.findUnpaidFines();
        if (unPaidFines.isEmpty()) {
            System.out.println("Không có người dùng chưa nộp phạt");
            return;
        }
        do {
            showList(InputOption.FINE_EDIT, unPaidFines);
            unpaidFinesView();
            int option = tryInput.tryInt("lựa chọn");
            if (tryInput.isReturn(String.valueOf(option))) break;
            switch (option) {
                case 1 -> payByFineIdView(userId);
                case 2 -> payByUserID();
                default -> ShowErrorMessage.outOfRange("lựa chọn");
            }
        } while (true);
    }

    private void unpaidFinesView() {
        System.out.println(tbConverter.convertMtplCol("NỘP TIỀN PHẠT", "Theo ID phạt", "Theo ID người dùng"));
        ;
    }

    private void payByUserID() {
        do {
            long userId = inputId("người dùng");
            if (tryInput.isReturn(String.valueOf(userId))) break;
            List<Fine> fines = fineService.findByUserIdAndStatus(userId, FineStatus.UNPAID);
            if (fines.isEmpty()) {
                System.out.println("Không tìm thấy người dùng, hoặc người dùng này không bị phạt. Enter để tiếp tục,# để trở lại.");
                if (tryInput.isReturn(sc.nextLine())) break;
                continue;
            }
            showList(InputOption.FINE_EDIT, fines);
            long fineId = inputId("khoản phạt");
            if (tryInput.isReturn(String.valueOf(fineId))) break;
            for (Fine fine : fines) {
                if (fine.getId() == fineId) {
                    fine.setStatus(FineStatus.PAID);
                    fine.setPaidAt();
                    fineService.update(fine);
                    System.out.println("Nộp phạt thành công. Nhấn enter.");
                    sc.nextLine();
                    break;
                }
            }
        } while (true);
    }

    private void payByFineIdView(long userId) {
        List<Fine> fineList = fineService.findByUserId(userId);
        if (fineList.isEmpty()) {
            System.out.println("Người dùng chưa bị phạt");
            sc.nextLine();
            return;
        }
        for (Fine unpaidFine : fineList) {
            showList(InputOption.FINE_EDIT, fineList);
            if (unpaidFine.getStatus().equals(FineStatus.UNPAID)) {
                long fineId = inputId("khoản phạt");
                if (tryInput.isReturn(String.valueOf(fineId))) return;
                Fine fine = fineService.findById(fineId);
                if (fine == null) {
                    System.out.println("Không tìm thấy khoản phạt.");
                    if (tryInput.isReturn(sc.nextLine())) break;
                    break;
                }
                paidFine(fine);
            }
        }
    }

    private void paidFine(Fine fine) {
        List<Fine> fines = new ArrayList<>();
        fines.add(fine);
        if (fine.getStatus().equals(FineStatus.PAID)) {
            showList(InputOption.FINE_EDIT, fines);
            System.out.println("Khoản phạt đã thanh toán");
        } else {
            showList(InputOption.FINE_EDIT, fines);
            System.out.println("Nhấn enter để thanh toán khoản phạt.");
            if (tryInput.isReturn(sc.nextLine())) ;
            else {
                Fine paidFine = new Fine();
                paidFine.setId(fine.getId());
                paidFine.setStatus(FineStatus.PAID);
                paidFine.setPaidAt();
                fineService.update(paidFine);
                System.out.println("Thanh toán thành công, nhấn enter.");
            }
        }
    }

    private long inputId(String field) {
        System.out.println("Nhập ID " + field);
        long id = tryInput.tryLong("Id khoản phạt");
        return id;
    }

    public void findByUserId(long userId) {
        User user = userService.findById(userId);
        do {
            fineService.collectFine();
            if (user.getRole().equals(Role.LIBRARIAN)) {
                userView.showList(InputOption.UPDATE, userService.findAll());
                userId = inputId("người dùng");
                if (tryInput.isReturn(String.valueOf(userId))) break;
            }
            boolean isExist = fineService.isExistByUserId(userId);

            if (isExist) {
                payByFineIdView(userId);
                break;
            }
            if (user.getRole().equals(Role.LIBRARIAN)) {
                System.out.println("Không tìm thấy người dùng, hoặc người dùng chưa bị phạt.");
            } else System.out.println("Bạn chưa bị phạt tiền.");
            sc.nextLine();
            break;
        } while (true);
    }


    public void showHistory() {
        showList(InputOption.FINE_SHOW, fineService.findAll());
    }

    @Override
    public void showList(InputOption inputOption, List<Fine> fines) {
        //1670919901,1657613272,1657613920,1150000.0,2022-12-13T08:25:01.169088Z,UNPAID
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_LongestLine());
        at.addRule();
        AT_Row tCaption = at.addRow(null, null, null, null, null, null, caption);
        tCaption.setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        AT_Row tHead = at.addRow(tHeadings);
        tHead.setPaddingLeft(1);
        tHead.setPaddingRight(1);
        at.addRule();
        double totalFine = 0;
        double totalUnpaidFine = 0;
        for (Fine fine : fines) {
            if (fine.getStatus().equals(FineStatus.UNPAID)) totalUnpaidFine += fine.getFineAmount();
            totalFine += fine.getFineAmount();
            AT_Row row = at.addRow(fine.getId(),
                    bookItemService.findById(fine.getBookItemId()).getBook().getTitle(),
                    userService.findById(fine.getUserId()).getFullName(),
                    CurrencyUtils.doubleToVND(fine.getFineAmount()),
                    InstantUtils.instantToString(fine.getCreatedAt()),
                    fine.getStatus(),
                    fine.getPaidAt() == null ? "" : InstantUtils.instantToString(fine.getPaidAt()));
            row.setPaddingRight(1);
            row.setPaddingLeft(1);
            at.addRule();
        }
        AT_Row totalUnpaidRow = at.addRow(null, null, null, "Tổng tiền phạt chưa nộp", null, null, CurrencyUtils.doubleToVND(totalUnpaidFine));
        totalUnpaidRow.getCells().get(5).getContext().setTextAlignment(TextAlignment.LEFT);
        totalUnpaidRow.setPaddingLeft(1);
        AT_Row totalRow = at.addRow(null, null, null, "Tổng tiền phạt", null, null, CurrencyUtils.doubleToVND(totalFine));
        totalRow.getCells().get(5).getContext().setTextAlignment(TextAlignment.LEFT);
        totalRow.setPaddingLeft(1);
        at.addRule();
        at.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        System.out.println(at.render());
        if (inputOption == InputOption.SHOW) {
            System.out.println("Nhấn enter để trở lại");
            sc.nextLine();
        }
        if (inputOption == InputOption.FINE_SHOW) {
            System.out.println("Nhấn enter để trở lại");
            sc.nextLine();
        }
    }
}
