package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.domain.entity.PointHistory;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;
import travelbeeee.PDFLO_V20.dto.LoginDto;
import travelbeeee.PDFLO_V20.dto.SignUpDto;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.repository.PointHistoryRepository;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MemberService {
    boolean login(LoginDto loginDto) throws PDFLOException, NoSuchAlgorithmException;
    void signUp(SignUpDto signUpDto) throws PDFLOException, NoSuchAlgorithmException;
    void delete(Long memberId);
    void authorize(Long memberId) throws PDFLOException;
    void updatePassword(Long memberId, String newPassword) throws NoSuchAlgorithmException, PDFLOException;
    void usePoint(Long memberId, Integer amount, PointType pointType) throws PDFLOException;
    List<PointHistory> findMemberPointHistory(Long memberId);
}