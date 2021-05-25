package travelbeeee.PDFLO_V20;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.service.MemberService;

import javax.persistence.EntityManager;

@SpringBootTest
public class PersistenceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    /**
     * 컨트롤러와 동일하게 @Transactional 을 없애기 위해 미리 DB에
     * ID=1, USERNAME="username1", PASSWORD="password1" 을 셋팅해놓음.
     */
    @Test
    public void 영속성_관리_테스트() throws Exception {
        // given
        Member member = memberService.findMember(1L);
        member.changePassword("password1");
    }
}
