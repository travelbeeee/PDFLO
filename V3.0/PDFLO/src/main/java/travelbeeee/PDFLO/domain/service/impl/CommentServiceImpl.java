package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.entity.Comment;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.entity.Member;
import travelbeeee.PDFLO.domain.model.entity.OrderItem;
import travelbeeee.PDFLO.domain.repository.CommentRepository;
import travelbeeee.PDFLO.domain.repository.ItemRepository;
import travelbeeee.PDFLO.domain.repository.MemberRepository;
import travelbeeee.PDFLO.domain.repository.OrderItemRepository;
import travelbeeee.PDFLO.domain.service.CommentService;
import travelbeeee.PDFLO.web.form.CommentForm;

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
    public ReturnCode uploadComment(Long memberId, Long itemId, CommentForm commentForm) throws PDFLOException {
        // 1)
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) return ReturnCode.MEMBER_NO_EXIST;

        // 2)
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty()) return ReturnCode.ITEM_NO_EXIST;

        // 3)
        Member member = findMember.get();
        Item item = findItem.get();
        Optional<OrderItem> findOrderItem = orderItemRepository.findByMemberAndItem(member, item);
        if(findOrderItem.isEmpty()) return ReturnCode.COMMENT_NO_PERMISSION_BUYING;

        // 4)
        Optional<Comment> findComment = commentRepository.findByMemberIdAndItemId(memberId, itemId);
        if(!findComment.isEmpty()) return ReturnCode.COMMENT_ALREADY_WRITTEN;

        Comment comment = new Comment(member, item, commentForm.getComment(), commentForm.getScore());
        commentRepository.save(comment);

        return ReturnCode.SUCCESS;
    }

    /**
     * 1) 해당 회원이 존재하는지 확인
     * 2) 해당 후기가 존재하는지 확인
     * 3) 해당 회원이 해당 후기를 작성했는지 확인
     */
    @Transactional
    @Override
    public ReturnCode deleteComment(Long memberId, Long commentId) throws PDFLOException {
        // 1)
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) return ReturnCode.MEMBER_NO_EXIST;

        // 2)
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()) return ReturnCode.COMMENT_NO_EXIST;

        // 3)
        Comment comment = findComment.get();
        if(comment.getMember().getId() != memberId) return ReturnCode.COMMENT_NO_PERMISSION_WRITER;

        commentRepository.delete(comment);
        return ReturnCode.SUCCESS;
    }

    @Override
    public List<Comment> findAllByItem(Long itemId) {
        return commentRepository.findAllByItem(itemId);
    }

    @Override
    public Comment findById(Long commentId) throws PDFLOException {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.COMMENT_NO_EXIST));
    }

    @Override
    public Comment findByIdAndMember(Long commentId, Long memberId) throws PDFLOException {
        return commentRepository.findWithMemberByCommentAndMember(commentId, memberId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.COMMENT_NO_EXIST));
    }

    @Override
    public List<Comment> findAllWithItemByMember(Long memberId) {
        return commentRepository.findAllWithItemByMember(memberId);
    }
}
