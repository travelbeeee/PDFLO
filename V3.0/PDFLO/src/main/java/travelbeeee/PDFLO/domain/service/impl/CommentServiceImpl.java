package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO.domain.exception.Code;
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
    public Code uploadComment(Long memberId, Long itemId, CommentForm commentForm) throws PDFLOException {
        // 1)
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) return Code.MEMBER_NO_EXIST;

        // 2)
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty()) return Code.ITEM_NO_EXIST;

        // 3)
        Member member = findMember.get();
        Item item = findItem.get();
        Optional<OrderItem> findOrderItem = orderItemRepository.findByMemberAndItem(member, item);
        if(findOrderItem.isEmpty()) return Code.COMMENT_NO_PERMISSION_BUYING;

        // 4)
        Optional<Comment> findComment = commentRepository.findByMemberIdAndItemId(memberId, itemId);
        if(!findComment.isEmpty()) return Code.COMMENT_ALREADY_WRITTEN;

        Comment comment = new Comment(member, item, commentForm.getComment(), commentForm.getScore());
        commentRepository.save(comment);

        return Code.SUCCESS;
    }

    /**
     * 1) 해당 회원이 존재하는지 확인
     * 2) 해당 후기가 존재하는지 확인
     * 3) 해당 회원이 해당 후기를 작성했는지 확인
     * 4) 해당 후기를 꺼내와서 수정
     */
    @Transactional
    @Override
    public void modifyComment(Long memberId, Long commentId, CommentForm commentForm) throws PDFLOException {
        // 1)
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(Code.MEMBER_NO_EXIST);

        // 2)
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()) throw new PDFLOException(Code.COMMENT_NO_EXIST);

        // 3)
        Comment comment = findComment.get();
        if(comment.getMember().getId() != memberId) throw new PDFLOException(Code.COMMENT_NO_PERMISSION_WRITER);

        // 4)
        comment.modifyComment(commentForm);
    }

    /**
     * 1) 해당 회원이 존재하는지 확인
     * 2) 해당 후기가 존재하는지 확인
     * 3) 해당 회원이 해당 후기를 작성했는지 확인
     */
    @Transactional
    @Override
    public void deleteComment(Long memberId, Long commentId) throws PDFLOException {
        // 1)
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(Code.MEMBER_NO_EXIST);

        // 2)
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()) throw new PDFLOException(Code.COMMENT_NO_EXIST);

        // 3)
        Comment comment = findComment.get();
        if(comment.getMember().getId() != memberId) throw new PDFLOException(Code.COMMENT_NO_PERMISSION_WRITER);

        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> findAllByItem(Long itemId) {
        return commentRepository.findAllByItem(itemId);
    }

    @Override
    public Comment findById(Long commentId) throws PDFLOException {
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if (findComment.isEmpty()) {
            throw new PDFLOException(Code.COMMENT_NO_EXIST);
        }
        return findComment.get();
    }

    @Override
    public Comment findByIdAndMember(Long commentId, Long memberId) throws PDFLOException {
        Optional<Comment> findComment = commentRepository.findWithMemberByCommentAndMember(commentId, memberId);
        if (findComment.isEmpty()) {
            throw new PDFLOException(Code.COMMENT_NO_EXIST);
        }
        return findComment.get();
    }

    @Override
    public List<Comment> findAllWithItemByMember(Long memberId) {
        return commentRepository.findAllWithItemByMember(memberId);
    }
}
