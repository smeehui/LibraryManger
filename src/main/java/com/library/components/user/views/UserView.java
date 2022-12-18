package com.library.components.user.views;

import com.library.components.booklending.services.BookLendingService;
import com.library.components.user.services.IUserService;
import com.library.components.user.services.UserService;
import com.library.components.user.models.Role;
import com.library.components.user.models.User;
import com.library.utils.InstantUtils;
import com.library.utils.ValidateUtils;
import com.library.views.*;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.List;


public class UserView extends View implements ListView<User> {//Single Responsibility Principle (SOLID)

    protected final IUserService userService;//Dependency Inversion Principle (SOLID)
    private final BookLendingService bookLendingService;
    private static UserView instance;
    protected UserView() {
        bookLendingService= BookLendingService.getInstance();
        caption = "DANH SÁCH NGƯỜI DÙNG";
        tHeadings = new String[]{"Id", "Tên", "Số điện thoại", "Email", "Địa chỉ", "Người dùng", "Ngày tạo", "Ngày cập nhật"};
        userService = UserService.getInstance();
    }
    public static UserView getInstance() {
        if (instance == null) {
            instance = new UserView();
        }return instance;
    }
    public void addUser(Role role) {
        do {
            long id = System.currentTimeMillis() / 1000;
            String username = inputUsername();
            if (tryInput.isReturn(String.valueOf(username))) break;
            String password = inputPassword();
            if (tryInput.isReturn(String.valueOf(password))) break;
            String fullName = inputFullName(InputOption.ADD);
            if (tryInput.isReturn(String.valueOf(fullName))) break;
            String phone = inputPhone(InputOption.ADD);
            if (tryInput.isReturn(String.valueOf(phone))) break;
            String address = inputAddress(InputOption.ADD);
            if (tryInput.isReturn(String.valueOf(address))) break;
            String email = inputEmail();
            if (tryInput.isReturn(String.valueOf(email))) break;
            User user = new User(id, username, password, fullName, phone, email, address, Role.MEMBER);
            if (role.equals(Role.LIBRARIAN))setRole(user);
            userService.add(user);
            System.out.println("Đã thêm thành công!\uD83C\uDF8A \n Enter để tiếp tục, # để trở lại");
        } while (tryInput.isReturn(sc.nextLine()));
    }

    public void setRole(User user) {
        System.out.println(tbConverter.convertMtplCol("CHỌN VAI TRÒ", "MEMBER", "LIBRARIAN"));
        System.out.println("Chọn vai trò: ");
        System.out.print(" ⭆ ");
        int option = sc.nextInt();
        sc.nextLine();
        switch (option) {
            case 1 -> user.setRole(Role.MEMBER);
            case 2 -> user.setRole(Role.LIBRARIAN);
            default -> {
                System.out.println("Nhập không đúng! Vui lòng nhập lại");
                setRole(user);
            }
        }
    }
    public void viewUserDetails(User user) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_LongestLine());
        AT_Row tHead = at.addRow("ID", "Tên đăng nhập", "Họ và tên", "Số điện thoại", "Email", "Địa chỉ", "Số sách đã mượn");
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

    public void updateUserView() {
        do {
            showUsers(InputOption.UPDATE);
            //Nếu Id khong Ton tại sẽ không thoát ra khỏi vong lặp trong ham inputId
            int id = inputId(InputOption.UPDATE);
            if (tryInput.isReturn(String.valueOf(id))) break;
            User newUser = new User();
            newUser.setId(id);
            updateUser(newUser);
        } while (true);
    }

    private void updateUser(User newUser) {
        do {
            System.out.println(tbConverter.convertMtplCol("SỬA", "Đổi tên", "Sửa số điện thoại", "Sửa địa chỉ"));
            int option = tryInput.tryInt("lựa chọn");
            switch (option) {
                case 1 -> {
                    String name = inputFullName(InputOption.UPDATE);
                    if (tryInput.isReturn(String.valueOf(name))) break;
                    newUser.setFullName(name);
                    userService.update(newUser);
                    System.out.println("Bạn đã đổi tên thành công!\uD83C\uDF89");
                }
                case 2 -> {
                    String phone = inputPhone(InputOption.UPDATE);
                    if (tryInput.isReturn(String.valueOf(phone))) break;
                    newUser.setMobile(phone);
                    userService.update(newUser);
                    System.out.println("Bạn đã đổi số điện thoại thành công\uD83C\uDF89");
                }
                case 3 -> {
                    String address = inputAddress(InputOption.UPDATE);
                    if (tryInput.isReturn(String.valueOf(address))) break;
                    newUser.setAddress(address);
                    userService.update(newUser);
                    System.out.println("Bạn đã đổi thành công\uD83C\uDF89");
                }
                case -1 -> {
                    return;
                }
                default -> ShowErrorMessage.outOfRange("Lựa chọn");
            }
        }while (true);
    }

    private int inputId(InputOption option) {
        int id;
        switch (option) {
            case ADD -> System.out.println("Nhập Id");
            case UPDATE -> System.out.println("Nhập id bạn muốn sửa");
        }
        boolean isRetry = false;
        do {
            id = tryInput.tryInt("ID");
            if (tryInput.isReturn(String.valueOf(id))) return -1;
            boolean exist = userService.existById(id);
            isRetry = tryInput.isRetryId(option, isRetry, exist);
        } while (isRetry);
        return id;
    }

    public String inputUsername() {
        System.out.println("Nhập Username (không bao gồm dấu cách, kí tự đặc biệt)");
        String username;
        do {
            username = tryInput.tryString("Username");
            if (tryInput.isReturn(username)) return null;
            if (!ValidateUtils.isUsernameValid(username)) {
                System.out.println(username + " của bạn không đúng định dạng! Vui lòng kiểm tra và nhập lại ");
                continue;
            }
            if (userService.existsByUsername(username)) {
                System.out.println("Username này đã tồn tại. Vui lòng nhập lại");
                continue;
            }
            break;
        } while (true);
        return username;
    }

    String inputFullName(InputOption option) {
        switch (option) {
            case ADD -> System.out.println("Nhập họ và tên (vd: Ho Ten) ");
            case UPDATE -> System.out.println("Nhập tên mà bạn muốn sửa đổi");
        }

        System.out.print(" ⭆ ");
        while (true) {
            String fullName = sc.nextLine();
            if (tryInput.isReturn(fullName)) return null;
            if (ValidateUtils.isFirstCaseValid(fullName)) return fullName;
            System.out.println("Tên " + fullName + " không đúng định dạng." + " Vui lòng nhập lại!" + " (Tên phải viết hoa chữ cái đầu.)");
            System.out.println("Nhập tên (vd: Ho Ten) ");
            System.out.print(" ⭆ ");
        }
    }

    public String inputPhone(InputOption option) {
        switch (option) {
            case ADD -> System.out.println("Nhập số điện thoại (vd: 0345129876): ");
            case UPDATE -> System.out.println("Nhập số điện thoại mà bạn muốn đổi");
        }
        System.out.print(" ⭆ ");
        String phone;
        do {
            phone = sc.nextLine();
            if (tryInput.isReturn(phone)) return null;
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
            email = sc.nextLine();
            if (tryInput.isReturn(email)) return null;
            if (!ValidateUtils.isEmailValid(email)) {
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
        do {
            System.out.println("Mật khẩu yếu! Vui lòng nhập lại ");
            System.out.print("⭆ ");
            password = sc.nextLine();
            if (tryInput.isReturn(password)) return null;
        }
        while (!ValidateUtils.isPasswordValid(password));
        return password;
    }

    String inputAddress(InputOption option) {
        switch (option) {
            case ADD -> System.out.println("Nhập địa chỉ (vd: Hà Nội)");
            case UPDATE -> System.out.println("Nhập địa chỉ mà bạn muốn đổi");
        }
        System.out.print(" ⭆ ");
        String address;
        while (true) {
            address = sc.nextLine();
            if (tryInput.isReturn(address)) return null;
            if (ValidateUtils.isFirstCaseValid(address)) return address;
            System.out.println("Địa chỉ " + address + "không đúng định dạng." + " Vui lòng nhập lại!" + " (Địa chỉ phải viết hoa chữ cái đầu.)");
            System.out.println("Nhập địa chỉ (vd: Huế) ");
            System.out.print(" ⭆ ");
        }
    }

    public void login() {

        boolean isRetry = false;
        do {
            System.out.println(tbConverter.convertSingleCol("Đăng Nhập Hệ Thống"));
            System.out.println("Tên đăng nhập");
            String username = tryInput.tryString("Tên đăng nhập");
            System.out.println("Mật khẩu");
            String password = tryInput.tryString("Mật khẩu");
            User user = userService.login(username, password);
            if (user == null) {
                System.out.println("Tài khoản không hợp lệ ");
                isRetry = isRetry();
            } else if (user.getRole() == Role.LIBRARIAN) {
                System.out.println(tbConverter.convertSingleCol("Đăng nhập với tư cách thủ thư <br>" +
                                                                "CHÀO MỪNG BẠN ĐÃ ĐẾN VỚI THƯ VIỆN SÁCH"));
                LibrarianView.menuOption(user.getId());

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
                System.out.println(tbConverter.convertMtplCol("Chọn:", "Nhấn \"y\" đêt đăng nhập lại", "Nhấn \"n\" để thoát chương trình"));
                System.out.print(" ⭆ ");
                String option = sc.nextLine();
                switch (option) {
                    case "y" -> {
                        return true;
                    }
                    case "n" -> MenuView.exit();
                    default -> ShowErrorMessage.outOfRange("lựa chọn");
                }

            } catch (Exception ex) {
                System.out.println("Nhập sai! vui lòng nhập lại");
                // ex.printStackTrace();
            }
        } while (true);
    }

    public void showUsers(InputOption inputOption) {
        List<User> users = userService.findAll();
        showList(inputOption, users);
    }

    @Override
    public void showList(InputOption inputOption, List<User> users) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_LongestLine());
        at.addHeavyRule();
        AT_Row tCaption = at.addRow(null, null, null, null, null, null, null, caption);
        tCaption.setTextAlignment(TextAlignment.CENTER);
        at.addHeavyRule();
        AT_Row tHead = at.addRow(tHeadings);
        tHead.getCells().get(0).getContext().setTextAlignment(TextAlignment.RIGHT);
        tHead.getCells().get(2).getContext().setTextAlignment(TextAlignment.RIGHT);
        tHead.setPaddingLeft(1);
        tHead.setPaddingRight(1);
        at.setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        for (User user : users) {
            AT_Row row = at.addRow(user.getId(),
                    user.getFullName(),
                    user.getMobile(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getRole(),
                    InstantUtils.instantToString(user.getCreatedAt()),
                    user.getUpdatedAt() == null ? "" : InstantUtils.instantToString(user.getUpdatedAt()));
            row.getCells().get(0).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.getCells().get(2).getContext().setTextAlignment(TextAlignment.RIGHT);
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

