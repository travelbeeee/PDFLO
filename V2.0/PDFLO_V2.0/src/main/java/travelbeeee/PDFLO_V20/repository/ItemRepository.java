package travelbeeee.PDFLO_V20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.domain.entity.Member;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByMember(Member member);
}
