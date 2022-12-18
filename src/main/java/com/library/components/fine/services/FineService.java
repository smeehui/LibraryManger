package com.library.components.fine.services;

import com.library.components.booklending.model.BookLending;
import com.library.components.booklending.model.LendingStatus;
import com.library.components.booklending.services.BookLendingService;
import com.library.components.booklending.services.IBookLendingService;
import com.library.components.fine.models.Fine;
import com.library.components.fine.models.FineStatus;
import com.library.services.Constants;
import com.library.utils.CSVUtils;
import com.library.utils.InstantUtils;

import java.util.ArrayList;
import java.util.List;

public class FineService implements IFineService {
    public final static String PATH = "data/fines.csv";
    public final static String PAID_PATH = "data/paid-fines.csv";
    private static FineService instance;
    private final IBookLendingService bookLendingService;

    private FineService() {
        bookLendingService = BookLendingService.getInstance();
    }


    public static FineService getInstance() {
        if (instance == null)
            instance = new FineService();
        return instance;
    }

    @Override
    public List<Fine> findAll() {
        List<Fine> fines = new ArrayList<>();
        List<String> records = CSVUtils.read(PATH);
        for (String record : records) {
            fines.add(Fine.parse(record));
        }
        return fines;
    }

    @Override
    public Fine findById(Long id) {
        List<Fine> bookItems = findAll();
        for (Fine fine : bookItems) {
            if (fine.getId().equals(id))
                return fine;
        }
        return null;
    }

    @Override
    public void collectFine() {
        for (BookLending bookLending : bookLendingService.findAll()) {
            if (bookLending.getStatus() == LendingStatus.RETURN) {
                if (bookLending.getDueAt().isBefore(bookLending.getReturnAt())) {
                    Fine existedFine = findByUserIdAndBookItemId(bookLending.getUserId(), bookLending.getBookItemId());
                    //Nếu chưa có trong danh sách nộp phạt
                    if (existedFine == null) {
                        double fineAmount = InstantUtils.countGapTime(bookLending.getReturnAt(), bookLending.getDueAt()) * Constants.FINE_AMOUNT;
                        Fine newFine = new Fine(bookLending.getBookItemId(), bookLending.getUserId(), fineAmount);
                        newFine.setStatus(FineStatus.UNPAID);
                        add(newFine,true);
                    }
                    //Nếu đã có trong danh sách nộp phạt
                    else if (existedFine != null) {
                        Fine newFine = new Fine();
                        newFine.setId(existedFine.getId());
                        newFine.setFineAmount(InstantUtils.countGapTime(bookLending.getReturnAt(), bookLending.getDueAt()) * Constants.FINE_AMOUNT);
                        update(newFine);
                    }
                }
            }
        }
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id) != null;
    }


    @Override
    public void add(Fine newFine) {
        List<Fine> fines = findAll();
        fines.add(newFine);
        CSVUtils.write(PATH, fines);
    }
    public void add(Fine newFine,boolean append) {
        CSVUtils.write(PATH, newFine,append);
    }

    @Override
    public void update(Fine newFine) {
        List<Fine> fines = findAll();
        for (Fine fine : fines) {
            if (fine.getId().equals(newFine.getId())) {
                if (newFine.getFineAmount() != null) {
                    fine.setFineAmount(newFine.getFineAmount());
                }
                if (newFine.getStatus() != null) {
                    fine.setStatus(newFine.getStatus());
                    fine.setPaidAt(newFine.getPaidAt());
                    List<Fine> paidFines = findAllPaidFInes();
                    paidFines.add(fine);
                    CSVUtils.write(PAID_PATH, paidFines);
                }
                CSVUtils.write(PATH, fines);
            }
        }
    }

    private List<Fine> findAllPaidFInes() {
        List<Fine> fines = new ArrayList<>();
        List<String> records = CSVUtils.read(PAID_PATH);
        for (String record : records) {
            fines.add(Fine.parse(record));
        }
        return fines;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Fine findById(long id) {
        List<Fine> fines = findAll();
        for (Fine fine : fines) {
            if (fine.getId().equals(id))
                return fine;
        }
        return null;
    }

    @Override
    public Fine findByUserIdAndBookItemId(long userId, long bookItemId) {
        List<Fine> fines = findAll();
        for (Fine fine : fines) {
            if (fine.getUserId() == userId && fine.getBookItemId() == bookItemId)
                return fine;
        }
        return null;

    }

    @Override
    public List<Fine> findUnpaidFines() {
        List<Fine> unPaidFines = new ArrayList<>();
        for (Fine fine : findAll()) {
            if (fine.getStatus().equals(FineStatus.UNPAID)) {
                unPaidFines.add(fine);
            }
        }
        return unPaidFines;
    }

    @Override
    public List<Fine> findByUserId(long id) {
        List<Fine> results = new ArrayList<>();
        for (Fine fine : findAll()) {
            if (fine.getUserId() == id) {
                results.add(fine);
            }
        }
        return results;
    }

    @Override
    public boolean isExistByUserId(long userId) {
        for (Fine fine : findAll()) {
            if (fine.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Fine> findByUserIdAndStatus(long id, FineStatus status) {
        List<Fine> results = new ArrayList<>();
        for (Fine fine : findAll()) {
            if (fine.getUserId() == id&& fine.getStatus().equals(status)) {
                results.add(fine);
            }
        }
        return results;
    }
}
