package con.library.components.user.views;

import con.library.components.books.views.BookView;
import con.library.components.user.services.IUserService;
import con.library.components.user.services.UserService;
import con.library.components.user.models.Role;
import con.library.components.user.models.User;
import con.library.utils.AppUtils;
import con.library.utils.InstantUtils;
import con.library.utils.ValidateUtils;
import con.library.views.InputOption;
import con.library.views.MenuView;
import con.library.views.ShowErrorMessage;
import con.library.views.View;

import java.util.List;


public class UserView extends View {//Single Responsibility Principle (SOLID)

    protected final IUserService userService;//Dependency Inversion Principle (SOLID)

    public UserView() {
        userService = UserService.getInstance();
    }

    public void addUser() {
        do {
            try {
                long id = System.currentTimeMillis() / 1000;
                String username = inputUsername();
                String password = inputPassword();
                String fullName = inputFullName(InputOption.ADD);
                String phone = inputPhone(InputOption.ADD);
                String address = inputAddress(InputOption.ADD);
                String email = inputEmail();
                User user = new User(id, username, password, fullName, phone, email, address, Role.MEMBER);
                setRole(user);
                userService.add(user);
                System.out.println("Đã thêm thành công!\uD83C\uDF8A");
            } catch (Exception e) {
                System.out.println("Nhập sai. vui lòng nhập lại!");
            }
        } while (AppUtils.isRetry(InputOption.ADD));
    }

    public void setRole(User user) {
        System.out.println(tbConverter.convertMtplCol("CHỌN VAI TRÒ","MEMBER","LIBRARIAN"));
        System.out.println("Chọn Role: ");
        System.out.print(" ⭆ ");
        int option = sc.nextInt();
        sc.nextLine();
        switch (option) {
            case 1:
                user.setRole(Role.MEMBER);
                break;
            case 2:
                user.setRole(Role.LIBRARIAN);
                break;
            default:
                System.out.println("Nhập không đúng! Vui lòng nhập lại");
                setRole(user);
        }
    }

    public void showUsers(InputOption inputOption) {
        System.out.println("------------------------------------------------------- DANH SÁCH NGƯỜI DÙNG --------------------------------------------------------- ");
        System.out.printf("%-15s %-15s %-15s %-22s %-13s %-13s %-20s %-20s\n", "Id", "Tên", "Số điện thoại", "Email", "Địa chỉ", "Người dùng", "Ngày tạo", "Ngày cập nhật");
        List<User> users = userService.findAll();
        for (User user : users) {
            System.out.printf("%-15s %-15s %-15s %-22s %-13s %-13s %-20s %-20s\n",
                    user.getId(),
                    user.getFullName(),
                    user.getMobile(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getRole(),
                    InstantUtils.instantToString(user.getCreatedAt()),
                    user.getUpdatedAt() == null ? "" : InstantUtils.instantToString(user.getUpdatedAt())
            );
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------  ");
        if (inputOption == InputOption.SHOW) AppUtils.isRetry(InputOption.SHOW);
    }

    public void updateUser() {
        boolean isRetry = false;
        do {
            try {
                showUsers(InputOption.UPDATE);
                //Nếu Id khong Ton tại sẽ không thoát ra khỏi vong lặp trong ham inputId
                int id = inputId(InputOption.UPDATE);
                System.out.println(tbConverter.convertMtplCol("SỬA","Đổi tên","Sửa số điện thoại","Sửa địa chỉ"));

                int option = AppUtils.retryChoose(1, 4);
                User newUser = new User();
                newUser.setId(id);
                switch (option) {
                    case 1:
                        String name = inputFullName(InputOption.UPDATE);
                        newUser.setFullName(name);
                        userService.update(newUser);
                        System.out.println("Bạn đã đổi tên thành công!\uD83C\uDF89");
                        break;
                    case 2:
                        String phone = inputPhone(InputOption.UPDATE);
                        newUser.setMobile(phone);
                        userService.update(newUser);
                        System.out.println("Bạn đã đổi số điện thoại thành công\uD83C\uDF89");
                        break;
                    case 3:
                        String address = inputAddress(InputOption.UPDATE);
                        newUser.setAddress(address);
                        userService.update(newUser);
                        System.out.println("Bạn đã đổi thành công\uD83C\uDF89");
                        break;
                }
                isRetry = option != 4 && AppUtils.isRetry(InputOption.UPDATE);

            } catch (Exception e) {
                System.out.println("Nhập sai! vui lòng nhập lại");
            }
        } while (isRetry);
    }

    private int inputId(InputOption option) {
        int id;
        switch (option) {
            case ADD:
                System.out.println("Nhập Id");
                break;
            case UPDATE:
                System.out.println("Nhập id bạn muốn sửa");
                break;
        }
        boolean isRetry = false;
        do {
            id = AppUtils.retryParseInt();
            boolean exist = userService.existById(id);
            isRetry = BookView.isRetry(option, isRetry, exist);
        } while (isRetry);
        return id;
    }

    public String inputUsername() {
        System.out.println("Nhập Username (không bao gồm dấu cách, kí tự đặc biệt)");
        System.out.print(" ⭆ ");
        String username;
        do {
            if (!ValidateUtils.isUsernameValid(username = AppUtils.retryString("Username"))) {
                System.out.println(username + " của bạn không đúng định dạng! Vui lòng kiểm tra và nhập lại ");
                System.out.print(" ⭆ ");
                continue;
            }
            if (userService.existsByUsername(username)) {
                System.out.println("Username này đã tồn tại. Vui lòng nhập lại");
                System.out.print(" ⭆ ");
                continue;
            }
            break;
        } while (true);
        return username;
    }

    String inputFullName(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập họ và tên (vd: Ho Ten) ");
                break;
            case UPDATE:
                System.out.println("Nhập tên mà bạn muốn sửa đổi");
                break;
        }

        System.out.print(" ⭆ ");
        String fullName;
        while (!ValidateUtils.isNameValid(fullName = sc.nextLine())) {
            System.out.println("Tên " + fullName + "không đúng định dạng." + " Vui lòng nhập lại!" + " (Tên phải viết hoa chữ cái đầu và không dấu)");
            System.out.println("Nhập tên (vd: Ho Ten) ");
            System.out.print(" ⭆ ");
        }
        return fullName;
    }

    public String inputPhone(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập số điện thoại (vd: 0345129876): ");
                break;
            case UPDATE:
                System.out.println("Nhập số điện thoại mà bạn muốn đổi");
                break;
        }
        System.out.print(" ⭆ ");
        String phone;
        do {
            phone = sc.nextLine();
            if (!ValidateUtils.isPhoneValid(phone)) {
                System.out.println("Số " + phone + " của bạn không đúng. Vui lòng nhập lại! " + "(Số điện thoại bao gồm 10 số và bắt đầu là số 0)");
                System.out.println("Nhập số điện thoại (vd: 0345129876)");
                System.out.print(" ⭆ ");
                continue;
            }
            if (userService.existsByPhone(phone)) {
                System.out.println("Số này đã tồn tại! Mời bạn nhập lại");
                System.out.print(" ⭆ ");
                continue;
            }
            break;
        } while (true);

        return phone;
    }

    private String inputEmail() {
        System.out.println("Nhập email (vd: hoten@gmail.com)");
        System.out.print(" ⭆ ");
        String email;
        do {
            if (!ValidateUtils.isEmailValid(email = sc.nextLine())) {
                System.out.println("Email " + email + "của bạn không đúng định dạng! Vui lòng kiểm tra và nhập lại ");
                System.out.println("Nhập email (vd: hoten@gmail.com)");
                System.out.print(" ⭆ ");
                continue;
            }
            if (userService.existsByEmail(email)) {
                System.out.println("Email " + email + "của bạn đã tồn tại! vui lòng kiểm tra lại");
                System.out.println("Nhập email (vd: hoten@gmail.com)");
                System.out.print(" ⭆ ");
                continue;
            }
            break;
        } while (true);
        return email;
    }

    private String inputPassword() {
        System.out.println("Nhập mật khẩu( mật khẩu phải > 8 kí tự )");
        System.out.print(" ⭆ ");
        String password;
        while (!ValidateUtils.isPasswordValid(password = sc.nextLine())) {
            System.out.println("Mật khẩu yếu! Vui lòng nhập lại ");
            System.out.print(" ⭆ ");
        }
        return password;
    }

    String inputAddress(InputOption option) {
        switch (option) {
            case ADD:
                System.out.println("Nhập địa chỉ (vd: Hà Nội)");
                break;
            case UPDATE:
                System.out.println("Nhập địa chỉ mà bạn muốn đổi");
                break;
        }
        System.out.print(" ⭆ ");
        return sc.nextLine();
    }

    public void login() {
        boolean isRetry = false;
        do {
            System.out.println(tbConverter.convertSingleCol("Đăng Nhập Hệ Thống"));
            System.out.println("Tên đăng nhập");
            String username = AppUtils.retryString("Tên đăng nhập");
            System.out.println("Mật khẩu");
            String password = AppUtils.retryString("Mật khẩu");
            User user = userService.login(username, password);
            if (user == null) {
                System.out.println("Tài khoản không hợp lệ ");
                isRetry = isRetry();
            } else if (user.getRole() == Role.LIBRARIAN) {
                System.out.println(tbConverter.convertSingleCol("Đăng nhập với tư cách quản trị viên <br>" +
                                                              "CHÀO MỪNG BẠN ĐÃ ĐẾN VỚI THƯ VIỆN SÁCH"));
                LibrarianView.menuOption();

            } else if (user.getRole() == Role.MEMBER) {
                System.out.println(tbConverter.convertSingleCol("Đăng nhập thành công với tư cách thành viên" +
                                                                "<br>CHÀO MỪNG BẠN ĐÃ ĐẾN VỚI THƯ VIỆN SÁCH "));
                MemberView.launch(user.getId());
            }
        } while (isRetry);
    }

    private boolean isRetry() {
        do {
            try {
                System.out.println(tbConverter.convertMtplCol("Chọn:","Nhấn \"y\" đêt đăng nhập lại","Nhấn \"n\" để thoát chương trình"));
                System.out.print(" ⭆ ");
                String option = sc.nextLine();
                switch (option) {
                    case "y":
                        return true;
                    case "n":
                        MenuView.exit();
                        break;
                    default:
                        ShowErrorMessage.outOfRange("lựa chọn");
                        break;
                }

            } catch (Exception ex) {
                System.out.println("Nhập sai! vui lòng nhập lại");
               // ex.printStackTrace();
            }
        } while (true);
    }
}

