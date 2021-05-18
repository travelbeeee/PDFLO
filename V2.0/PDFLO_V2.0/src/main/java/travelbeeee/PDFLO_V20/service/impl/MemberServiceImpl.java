package travelbeeee.PDFLO_V20.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.dto.JoinDto;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.exception.ReturnCode;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.service.MemberService;
import travelbeeee.PDFLO_V20.utility.Sha256Encryption;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final Sha256Encryption sha256Encryption;

    @Override
    public void join(JoinDto joinDto) throws PDFLOException {
        List<Member> findMembers = memberRepository.findByUsername(joinDto.getUsername());
        if(!findMembers.isEmpty()){ // 이미 존재하는 회원아이디
            throw new PDFLOException(ReturnCode.USERNAME_DUPLICATION);
        }
        
    }
}
