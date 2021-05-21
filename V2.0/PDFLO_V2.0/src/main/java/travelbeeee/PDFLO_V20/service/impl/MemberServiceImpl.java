package travelbeeee.PDFLO_V20.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.entity.PointHistory;
import travelbeeee.PDFLO_V20.domain.enumType.MemberType;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;
import travelbeeee.PDFLO_V20.dto.SignUpDto;
import travelbeeee.PDFLO_V20.dto.LoginDto;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.exception.ErrorCode;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.repository.PointHistoryRepository;
import travelbeeee.PDFLO_V20.service.MemberService;
import travelbeeee.PDFLO_V20.utility.Sha256Encryption;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final Sha256Encryption sha256Encryption;

    @Override
    public boolean login(LoginDto loginDto) throws PDFLOException, NoSuchAlgorithmException {
        List<Member> findMembers = memberRepository.findByUsername(loginDto.getUsername());
        if(findMembers.isEmpty()){ // 존재하지않는 회원아이디
            throw new PDFLOException(ErrorCode.LOGIN_INPUT_INVALID);
        }

        Member findMember = findMembers.get(0);

        if(!findMember.getPassword().equals(sha256Encryption.sha256(loginDto.getPassword(), findMember.getSalt()))){ // 비밀번호가 틀림
            throw new PDFLOException(ErrorCode.LOGIN_INPUT_INVALID);
        }

        return true;
    }

    @Transactional
    @Override
    public void signUp(SignUpDto joinDto) throws PDFLOException, NoSuchAlgorithmException {
        List<Member> findMembers = memberRepository.findByUsername(joinDto.getUsername());
        if(!findMembers.isEmpty()){ // 이미 존재하는 회원아이디
            throw new PDFLOException(ErrorCode.MEMBER_NAME_DUPLICATION);
        }

        String salt = sha256Encryption.makeSalt();
        Member newMember = new Member(joinDto.getUsername(), sha256Encryption.sha256(joinDto.getPassword(), salt), salt, joinDto.getEmail(),
                MemberType.UNAUTHORIZATION, 0);

        memberRepository.save(newMember);
    }

    @Transactional
    @Override
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Transactional
    @Override
    public void authorize(Long memberId) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        Member member = findMember.get();
        member.changeType(MemberType.AUTHORIZATION);
    }

    @Transactional
    @Override
    public void updatePassword(Long memberId, String newPassword) throws NoSuchAlgorithmException, PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        Member member = findMember.get();
        member.changePassword(sha256Encryption.sha256(newPassword, member.getSalt()));
    }

    @Transactional
    @Override
    public void usePoint(Long memberId, Integer amount, PointType pointType) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST); // 존재하지않는회원

        Member member = findMember.get();
        if(pointType == PointType.USE && member.getPoint() < amount){
            throw new PDFLOException(ErrorCode.MEMBER_INSUFFICIENT_BALANCE); // 잔액부족
        }

        if(pointType == PointType.USE) member.losePoint(amount);
        else member.getPoint(amount);

        PointHistory pointHistory = new PointHistory(member, amount, pointType);
        pointHistoryRepository.save(pointHistory);
    }

    @Override
    public List<PointHistory> findMemberPointHistory(Long memberId) {
        return pointHistoryRepository.findMemberPointHistory(memberId);
    }

}
