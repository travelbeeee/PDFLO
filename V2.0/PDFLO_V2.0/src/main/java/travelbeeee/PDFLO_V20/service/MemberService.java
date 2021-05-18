package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.dto.LoginDto;
import travelbeeee.PDFLO_V20.dto.SignUpDto;
import travelbeeee.PDFLO_V20.exception.PDFLOException;

import java.security.NoSuchAlgorithmException;

public interface MemberService {
    boolean login(LoginDto loginDto) throws PDFLOException, NoSuchAlgorithmException;
    void signUp(SignUpDto signUpDto) throws PDFLOException, NoSuchAlgorithmException;
    void delete(Long memberId);
    void authorize(Long memberId);
    void updatePassword(Long memberId, String newPassword) throws NoSuchAlgorithmException;
}