package con.library.components.fine.services;

import con.library.components.fine.models.Fine;
import con.library.utils.CSVUtils;

import java.util.ArrayList;
import java.util.List;

public class FineService implements IFineService {
    public final static String PATH = "data/fines.csv";
    private static FineService instance;

    private FineService() {
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
    public void collectFine(long userId, long days) {
        //tim user , vuot qua ngay thi nop phat
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

    @Override
    public void update(Fine newFine) {
        List<Fine> fines = findAll();
        for (Fine fine : fines) {
            if (fine.getId().equals(newFine.getId()) ) {
            }
        }
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Fine findById(long id) {
        return null;
    }


}
