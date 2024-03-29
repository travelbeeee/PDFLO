package travelbeeee.PDFLO.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.model.entity.*;
import travelbeeee.PDFLO.domain.model.enumType.PointType;
import travelbeeee.PDFLO.web.form.LoginForm;
import travelbeeee.PDFLO.web.form.ProfileForm;
import travelbeeee.PDFLO.web.form.SignUpForm;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface MemberService {
    Optional<Member> login(LoginForm loginDto) throws PDFLOException, NoSuchAlgorithmException; // 로그인
    void signUp(SignUpForm signUpDto) throws PDFLOException, NoSuchAlgorithmException; // 회원가입
    ReturnCode delete(Long memberId); // 회원탈퇴
    void checkPassword(Long memberId, String password) throws PDFLOException, NoSuchAlgorithmException;
    void updatePassword(Long memberId, String newPassword) throws NoSuchAlgorithmException, PDFLOException; // 비밀번호변경
    void usePoint(Long memberId, Integer amount, PointType pointType) throws PDFLOException; // 포인트(사용,충전,획득)
    Page<PointHistory> findPointHistoryBymember(Long memberId, Pageable pageable); // 포인트 내역 조회
    Page<Order> findOrderByMember(Long memberId, Pageable pageable);
    void uploadProfile(Long memberId, ProfileForm profileForm) throws PDFLOException, NoSuchAlgorithmException,  IOException; // 프로필 등록
    ReturnCode deleteProfile(Long memberId) throws PDFLOException; // 프로필 삭제
    Optional<Profile> findProfileByMember(Long memberId); // 프로필 불러오기
    Member findMember(Long memberId) throws PDFLOException; // 회원 찾기
    Optional<Member> findMemberByUsername(String username);
    Page<OrderItem> findMemberSellHistory(Long memberId, Long itemId, Pageable pageable) throws PDFLOException;
}