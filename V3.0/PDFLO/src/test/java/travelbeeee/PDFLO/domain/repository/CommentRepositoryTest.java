package travelbeeee.PDFLO.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLO.domain.model.entity.Comment;
import travelbeeee.PDFLO.domain.model.entity.Item;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ItemRepository itemRepository;

    @Test
    void findAllWithRecommentByItem_테스트(){
        List<Comment> comments = commentRepository.findAllWithRecommentByItem(5L);
        for (Comment c : comments) {
            System.out.println("comment id : " + c.getId());
            if(c.getRecomment() != null){
                System.out.println("recomment id : " + c.getRecomment().getId());
            }
        }
    }

}