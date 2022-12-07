package con.library.views;

import java.util.List;

public interface ListView<T> {
    void showList(InputOption inputOption, List<T> items);
}
