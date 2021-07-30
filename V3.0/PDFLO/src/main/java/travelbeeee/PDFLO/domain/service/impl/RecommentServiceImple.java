package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.model.entity.Comment;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.entity.Member;
import travelbeeee.PDFLO.domain.model.entity.Recomment;
import travelbeeee.PDFLO.domain.repository.CommentRepository;
import travelbeeee.PDFLO.domain.repository.ItemRepository;
import travelbeeee.PDFLO.domain.repository.MemberRepository;
import travelbeeee.PDFLO.domain.repository.RecommentRepository;
import travelbeeee.PDFLO.domain.service.RecommentService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommentServiceImple implements RecommentService {

    private final RecommentRepository recommentRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;

    /**
     * 답글 업로드
     * 1) 회원 확인
     * 2) 후기 확인
     * 3) 회원이 아이템 판매자인지 확인
     * 4) 회원 아이템의 해당 후기에 답글을 단 적이 없는지 확인
     */
    @Override
    public ReturnCode uploadRecomment(Long memberId, Long commentId, String commentStr) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) return ReturnCode.MEMBER_NO_EXIST;

        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()) return ReturnCode.COMMENT_NO_EXIST;

        Member member = findMember.get();
        Comment comment = findComment.get();
        Item item = comment.getItem();

        if (item.getMember().getId() != member.getId()) {
            return ReturnCode.MEMBER_NOT_SELLER;
        }

        if(comment.getRecomment() != null){
            return ReturnCode.RECOMMENT_ALREADY_WRITTEN;
        }

        Recomment recomment = new Recomment(member, commentStr);
        comment.updateRecomment(recomment);
        recommentRepository.save(recomment);

        return ReturnCode.SUCCESS;
    }

    @Override
    public ReturnCode deleteRecomment(Long memberId, Long recommentId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) return ReturnCode.MEMBER_NO_EXIST;

        Optional<Recomment> findRecomment = recommentRepository.findById(recommentId);
        if(findRecomment.isEmpty()) return ReturnCode.RECOMMENT_NO_EXIST;

        Recomment recomment = findRecomment.get();
        if(recomment.getMember().getId() != memberId) return ReturnCode.RECOMMENT_NO_PERMISSION;

        Optional<Comment> findComment = commentRepository.findByRecommentId(recommentId);
        Comment comment = findComment.get();
        comment.updateRecomment(null);
        recommentRepository.delete(recomment);
        return ReturnCode.SUCCESS;
    }
}
