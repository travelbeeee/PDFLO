package travelbeeee.PDFLOpjt.serviceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLOpjt.domain.User;
import travelbeeee.PDFLOpjt.inputform.JoinForm;
import travelbeeee.PDFLOpjt.inputform.LoginForm;
import travelbeeee.PDFLOpjt.repository.UserRepository;
import travelbeeee.PDFLOpjt.service.UserService;
import travelbeeee.PDFLOpjt.utility.Sha256Util;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Sha256Util sha256Util;

    @AfterEach
    void afterEach(){
        userRepository.deleteAll();}

    JoinForm getJoinForm(){
        JoinForm joinForm = new JoinForm();
        joinForm.setUsername("member1");
        joinForm.setUserpwd("password1");
        joinForm.setEmail("email1");
        return joinForm;
    }

    @Test
    public void 아이디중복테스트() throws Exception{
        //given
        JoinForm joinForm = getJoinForm();
        userService.join(joinForm);

        JoinForm newJoinForm = getJoinForm();
        //when
//        boolean res = userService.join(newJoinForm);
        //then
//        assertThat(res).isFalse();
    }

    @Test
    public void 로그인테스트() throws NoSuchAlgorithmException {
//        //given
//        JoinForm joinForm = getJoinForm();
//        userService.join(joinForm);
//        LoginForm loginForm = new LoginForm();
//        loginForm.setUsername(joinForm.getUsername());
//        loginForm.setPassword(joinForm.getUserpwd());
//        //when
//        int res = userService.login(loginForm);
//        //then
//        assertThat(res).isNotEqualTo(-1);
    }

    @Test
    public void 비밀번호변경테스트() throws Exception{
        //given
        JoinForm joinForm = getJoinForm();
        userService.join(joinForm);
        User user = userRepository.selectByName(joinForm.getUsername());

        //when
        userService.updatePassword(user.getUserId(), "newpassword");
        User finduser = userRepository.selectById(user.getUserId());
        //then
        assertThat(finduser.getUserpwd()).isEqualTo(sha256Util.sha256("newpassword", user.getSalt()));
    }
}