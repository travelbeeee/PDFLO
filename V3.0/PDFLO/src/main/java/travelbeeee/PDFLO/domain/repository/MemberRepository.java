package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbeeee.PDFLO.domain.model.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
}
