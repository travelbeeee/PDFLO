package travelbeeee.PDFLO_V20.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.Comment;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.dto.CommentDto;
import travelbeeee.PDFLO_V20.exception.ErrorCode;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.repository.CommentRepository;
import travelbeeee.PDFLO_V20.repository.ItemRepository;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.repository.OrderItemRepository;
import travelbeeee.PDFLO_V20.service.CommentService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 1) 해당 회원이 존재하는지 확인
     * 2) 해당 아이템이 존재하는지 확인
     * 3) 해당 회원이 해당 아이템을 구매했는지 확인
     * 4) 해당 회원이 해당 아이템에 후기를 이미 남겼는지 확인
     */
    @Transactional
    @Override
    public void uploadComment(Long memberId, Long itemId, CommentDto commentDto) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);
        if(findItem.isEmpty()) throw new PDFLOException(ErrorCode.ITEM_NO_EXIST);

        // 구매자인지 확인해야된다...

        Optional<Comment> findComment = commentRepository.findByMemberIdAndItemId(memberId, itemId);
        if(!findComment.isEmpty()) throw new PDFLOException(ErrorCode.COMMENT_ALREADY_WRITTEN);

        Member member = findMember.get();
        Item item = findItem.get();

        Comment comment = new Comment(member, item, commentDto.getComment(), commentDto.getScore());
        commentRepository.save(comment);
    }

    /**
     *
     * 1) 해당 회원이 존재하는지 확인
     * 2) 해당 후기가 존재하는지 확인
     * 3) 해당 회원이 해당 후기를 작성했는지 확인
     */
    @Transactional
    @Override
    public void deleteComment(Long memberId, Long commentId) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()) throw new PDFLOException(ErrorCode.COMMENT_NO_EXIST);

        Comment comment = findComment.get();
        if(comment.getMember().getId() != memberId) throw new PDFLOException(ErrorCode.COMMENT_NO_PERMISSION_WRITER);

        commentRepository.delete(comment);
    }

}
