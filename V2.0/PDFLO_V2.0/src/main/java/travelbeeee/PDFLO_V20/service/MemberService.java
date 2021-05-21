package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.domain.entity.PointHistory;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;
import travelbeeee.PDFLO_V20.dto.LoginDto;
import travelbeeee.PDFLO_V20.dto.ProfileDto;
import travelbeeee.PDFLO_V20.dto.SignUpDto;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.repository.PointHistoryRepository;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MemberService {
    boolean login(LoginDto loginDto) throws PDFLOException, NoSuchAlgorithmException; // 로그인
    void signUp(SignUpDto signUpDto) throws PDFLOException, NoSuchAlgorithmException; // 회원가입
    void delete(Long memberId); // 회원탈퇴
    void authorize(Long memberId) throws PDFLOException; // 메일인증
    void updatePassword(Long memberId, String newPassword) throws NoSuchAlgorithmException, PDFLOException; // 비밀번호변경
    void usePoint(Long memberId, Integer amount, PointType pointType) throws PDFLOException; // 포인트(사용,충전,획득)
    List<PointHistory> findMemberPointHistory(Long memberId); // 포인트 내역 조회
    List<Item> findMemberItem(Long memberId); // 회원이 등록한 아이템 조회
    void uploadProfile(Long memberId, ProfileDto profileDto) throws PDFLOException, NoSuchAlgorithmException, IOException;
}