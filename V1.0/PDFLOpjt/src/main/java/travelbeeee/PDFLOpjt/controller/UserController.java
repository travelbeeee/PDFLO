package travelbeeee.PDFLOpjt.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLOpjt.domain.*;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.inputform.JoinForm;
import travelbeeee.PDFLOpjt.service.*;
import travelbeeee.PDFLOpjt.utility.PermissionChecker;
import travelbeeee.PDFLOpjt.utility.ReturnCode;
import travelbeeee.PDFLOpjt.inputform.LoginForm;
import travelbeeee.PDFLOpjt.utility.MailSender;
import travelbeeee.PDFLOpjt.view.OrderView;
import travelbeeee.PDFLOpjt.view.SellView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Controller @RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserAccountService userAccountService;
    private final ProfileService profileService;
    private final ContentService contentService;
    private final OrderService orderService;
    private final MailSender mailService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/signUp")
    public String signUpForm(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkNoPermission(httpSession); // 이미 로그인 상태로 회원가입 진행
        return "/user/signUp";
    }

    @PostMapping("/user/signUp")
    public String signUp(@Valid JoinForm joinForm, BindingResult bindingResult) throws NoSuchAlgorithmException, PDFLOException {
        if(bindingResult.hasErrors()) throw new PDFLOException(ReturnCode.SIGNUP_INPUT_INVALID);

        userService.join(joinForm);

        return "redirect:/";
    }

    @GetMapping("/user/login")
    public String loginForm(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkNoPermission(httpSession); // 이미 로그인 상태로 또 로그인 접근
        return "/user/login";
    }

    @PostMapping("/user/login")
    public String login(@Valid LoginForm loginForm, BindingResult bindingResult, HttpSession httpSession) throws PDFLOException, NoSuchAlgorithmException {
        if(bindingResult.hasErrors()) {
            logger.warn("로그인 아이디 / 비번 입력이 양식에 해당하지 않습니다.");
            throw new PDFLOException(ReturnCode.LOGIN_INPUT_INVALID);
        }

        User user = userService.login(loginForm);

        httpSession.setAttribute("userId", user.getUserId());
       if(user.getAuth().equals("UNAUTHORIZATION")) {
            return "redirect:/user/sending";
        }

        Profile profile = profileService.selectByUserId(user.getUserId());
        if(profile != null)
            httpSession.setAttribute("profile", profile.getLocation() + "/t-" + profile.getSaltedFileName());

        return "redirect:/";
    }

    @GetMapping("/user/sending")
    public String mailSendingForm(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        return "/user/mailSending";
    }

    @PostMapping("/user/sending")
    public String mailSending(HttpSession httpSession) throws PDFLOException, MessagingException {
        PermissionChecker.checkPermission(httpSession);
        User user = userService.selectById((Integer)httpSession.getAttribute("userId"));
        int code = mailService.mailSending(user.getEmail());
        httpSession.setAttribute("authCode", code);

        return "/user/mailAuth";
    }

    @PostMapping("/user/auth")
    public String auth(HttpSession httpSession, int code) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        int authCode = (int)httpSession.getAttribute("authCode");
        if(authCode != code) {
            httpSession.invalidate();
            throw new PDFLOException(ReturnCode.MAIL_AUTHCODE_INCORRECT);
        }
        userService.updateAuth((Integer)httpSession.getAttribute("userId"));

        return "redirect:/";
    }

    @GetMapping("/user/mypage")
    public String myPage(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        int userId = (Integer) httpSession.getAttribute("userId");
        User user = userService.selectById(userId);
        UserAccount userAccount = userAccountService.selectById(userId);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("balance", userAccount.getBalance());

        return "/user/mypage";
    }

    @GetMapping("/user/modify")
    public String userModifyForm(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        return "/user/modify";
    }

    @PostMapping("/user/modify")
    public String userModify(String newpwd, HttpSession httpSession) throws PDFLOException, NoSuchAlgorithmException {
        PermissionChecker.checkPermission(httpSession);
        userService.updatePassword((Integer) httpSession.getAttribute("userId"), newpwd);

        return "/user/mypage";
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        httpSession.invalidate();

        return "redirect:/";
    }

    @PostMapping("/user/delete")
    public String userDelete(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        userService.delete((Integer)httpSession.getAttribute("userId"));
        httpSession.invalidate();

        return "redirect:/";
    }

    @GetMapping("/user/order")
    public String myOrder(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        List<Order> orders = orderService.selectOrderByUser((Integer) httpSession.getAttribute("userId"));

        List<OrderView> orderViews = new ArrayList<>();

        for(Order order: orders){
            Content content = contentService.selectById(order.getContentId());

            OrderView orderView = new OrderView();
            orderView.setContentId(content.getContentId());
            orderView.setTitle(content.getTitle());
            orderView.setLocaldate(order.getLocaldate());
            orderViews.add(orderView);
        }
        model.addAttribute("orders", orderViews);
        return "/user/myorder";
    }

    @GetMapping("/user/sell")
    public String mySelling(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        List<Order> orders = orderService.selectSelling((Integer) httpSession.getAttribute("userId"));
        List<SellView> sellViews = new ArrayList<>();
        int totalPrice = 0;
        for(Order order : orders){
            SellView sellView = new SellView();
            sellView.setTitle(contentService.selectById(order.getContentId()).getTitle());
            sellView.setLocaldate(order.getLocaldate());
            sellView.setUsername(userService.selectById(order.getUserId()).getUsername());
            totalPrice += contentService.selectById(order.getContentId()).getPrice();
            sellViews.add(sellView);
        }
        model.addAttribute("sells", sellViews);
        model.addAttribute("totalPrice", totalPrice);
        return "/user/mysell";
    }

    @GetMapping("/user/deposit")
    public String depositFrom(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        return "/user/deposit";
    }

    @PostMapping("/user/deposit")
    public String deposit(int price, HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        userAccountService.deposit((Integer) httpSession.getAttribute("userId"), price);

        return "redirect:/user/mypage";
    }

    @GetMapping("/user/profile")
    public String profileForm(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        return "/user/profile";
    }

    @PostMapping("/user/profile")
    public String profile(HttpSession httpSession, MultipartFile profileFile) throws PDFLOException, IOException, NoSuchAlgorithmException {
        PermissionChecker.checkPermission(httpSession);

        userService.uploadProfile(profileFile, (int) httpSession.getAttribute("userId"));

        return "redirect:/user/mypage";
    }

    @GetMapping("/user/findId")
    public String findIdForm(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkNoPermission(httpSession);

        return "/user/findIdForm";
    }

    @PostMapping("/user/findId")
    public String findId(HttpSession httpSession,
                         @RequestParam String email) throws PDFLOException, MessagingException {
        PermissionChecker.checkNoPermission(httpSession);

        List<User> users = userService.selectByEamil(email);
        if(users.isEmpty()) throw new PDFLOException(ReturnCode.EMAIL_NO_EXIST);

        int code = mailService.mailSending(email);

        httpSession.setAttribute("email", email);
        httpSession.setAttribute("code", code);

        return "/user/findIdAuth";
    }

    @PostMapping("/user/findIdAuth")
    public String findIdAuth(HttpSession httpSession, Model model,
                         @RequestParam int inputCode) throws PDFLOException, MessagingException {
        PermissionChecker.checkNoPermission(httpSession);

        String email = (String)httpSession.getAttribute("email");
        int code = (int)httpSession.getAttribute("code");
        httpSession.removeAttribute("email");
        httpSession.removeAttribute("code");

        if(inputCode != code) throw new PDFLOException(ReturnCode.MAIL_AUTHCODE_INCORRECT);

        List<User> users = userService.selectByEamil((String)httpSession.getAttribute("email"));
        model.addAttribute("users", users);

        return "/user/findIdResult";
    }

    @GetMapping("/user/findPassword")
    public String findPasswordForm(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkNoPermission(httpSession);

        return "/user/findPasswordForm";
    }

    @PostMapping("/user/findPassword")
    public String findPassword(HttpSession httpSession, @RequestParam String username,
                               @RequestParam String email) throws PDFLOException, MessagingException {
        PermissionChecker.checkNoPermission(httpSession);

        List<User> users = userService.selectByEamil(email);
        if(users.isEmpty()) throw new PDFLOException(ReturnCode.EMAIL_NO_EXIST);

        int code = mailService.mailSending(email);
        httpSession.setAttribute("code", code);
        httpSession.setAttribute("username", username);
        httpSession.setAttribute("email", email);

        return "/user/findPasswordAuth";
    }

    @PostMapping("/user/findPasswordAuth")
    public String findPasswordAuth(HttpSession httpSession, Model model,
                             @RequestParam int inputCode) throws PDFLOException, MessagingException {
        PermissionChecker.checkNoPermission(httpSession);

        String username = (String) httpSession.getAttribute("username");
        String email = (String)httpSession.getAttribute("email");
        int code = (int)httpSession.getAttribute("code");

        httpSession.removeAttribute("username");
        httpSession.removeAttribute("code");
        httpSession.removeAttribute("email");

        if(inputCode != code) throw new PDFLOException(ReturnCode.MAIL_AUTHCODE_INCORRECT);

        User user = userService.selectByName(username);

        if (user == null) throw new PDFLOException(ReturnCode.USER_NO_EXIST);
        if(!user.getEmail().equals(email)) throw new PDFLOException(ReturnCode.EMAIL_NO_EXIST);

        httpSession.setAttribute("userId", user.getUserId());

        return "/user/findPasswordReset";
    }

    @PostMapping("/user/findPasswordModify")
    public String findPasswordModify(HttpSession httpSession, @RequestParam String newpwd) throws PDFLOException, NoSuchAlgorithmException {
        PermissionChecker.checkPermission(httpSession);

        int userId = (int)httpSession.getAttribute("userId");
        httpSession.removeAttribute("userId");

        userService.updatePassword(userId, newpwd);

        return "redirect:/";
    }
}
