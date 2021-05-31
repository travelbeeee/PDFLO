package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.domain.entity.*;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;
import travelbeeee.PDFLO_V20.domain.form.LoginForm;
import travelbeeee.PDFLO_V20.domain.form.ProfileForm;
import travelbeeee.PDFLO_V20.domain.form.SignUpForm;
import travelbeeee.PDFLO_V20.exception.PDFLOException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member login(LoginForm loginDto) throws PDFLOException, NoSuchAlgorithmException; // 로그인
    void signUp(SignUpForm signUpDto) throws PDFLOException, NoSuchAlgorithmException; // 회원가입
    void delete(Long memberId); // 회원탈퇴
    void authorize(Long memberId) throws PDFLOException; // 메일인증
    void checkPassword(Long memberId, String password) throws PDFLOException, NoSuchAlgorithmException;
    void updatePassword(Long memberId, String newPassword) throws NoSuchAlgorithmException, PDFLOException; // 비밀번호변경
    void usePoint(Long memberId, Integer amount, PointType pointType) throws PDFLOException; // 포인트(사용,충전,획득)
    List<PointHistory> findMemberPointHistory(Long memberId); // 포인트 내역 조회
    List<Item> findMemberItem(Long memberId); // 회원이 등록한 아이템 조회
    void uploadProfile(Long memberId, ProfileForm profileForm) throws PDFLOException, NoSuchAlgorithmException, IOException;
    void deleteProfile(Long memberId) throws PDFLOException;
    Optional<Profile> findProfileByMember(Long memberId);
    Member findMember(Long memberId) throws PDFLOException;
    List<Order> findOrder(Long memberId);
    List<Item> findSellingItem(Long memberId);
    List<OrderItem> findSellingHistory(Long itemId);
}