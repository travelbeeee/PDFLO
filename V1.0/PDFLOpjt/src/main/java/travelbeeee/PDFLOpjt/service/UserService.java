package travelbeeee.PDFLOpjt.service;

import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLOpjt.domain.User;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.inputform.JoinForm;
import travelbeeee.PDFLOpjt.inputform.LoginForm;
import travelbeeee.PDFLOpjt.utility.ReturnCode;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    ReturnCode join(JoinForm joinForm) throws NoSuchAlgorithmException, PDFLOException; // 아이디 비밀번호 이메일 입력받고 회원DB에 등록
    User login(LoginForm loginForm) throws NoSuchAlgorithmException, PDFLOException; // 아이디 비밀번호 입력받고 회원 DB 조회 후 로그인 진행
    ReturnCode updatePassword(int userId, String newpwd) throws NoSuchAlgorithmException, PDFLOException; // 새로운 비밀번호 입력받고 회원 DB 갱신
    ReturnCode delete(int userId); // 회원DB에서 해당 회원 삭제 --> userAccount까지 같이 삭제해야된다.
    User selectById(int userId);
    ReturnCode updateAuth(int userId);
    ReturnCode uploadProfile(MultipartFile profileFile, int userId) throws PDFLOException, NoSuchAlgorithmException, IOException;
    List<User> selectByEamil(String email);
    User selectByName(String username);
}
