package travelbeeee.PDFLO_V20.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.entity.Item;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ItemRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void test() throws Exception {
        // given
        Member member = new Member("username1", "password1", null, null, null, null);
        memberRepository.save(member);

        Item item1 = new Item(member, "title1", "content", 1000, null, null);
        Item item2 = new Item(member, "title2", "content", 1000, null, null);
        Item item3 = new Item(member, "title3", "content", 1000, null, null);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        // when
//        List<Item> items = itemRepository.findByMember(member.getId());

        // then
//        Assertions.assertThat(items.size()).isEqualTo(3);
    }

    @Test
    public void findSelectedItemWithMember_테스트() throws Exception {
        // given
        Member member = new Member("username1", "password1", null, null, null, null);
        memberRepository.save(member);

        Item item1 = new Item(member, "title1", "content", 1000, null, null);
        Item item2 = new Item(member, "title2", "content", 1000, null, null);
        Item item3 = new Item(member, "title3", "content", 1000, null, null);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        // when
        List<Long> itemIds = new ArrayList<>();
        itemIds.add(item1.getId());
        itemIds.add(item2.getId());
        itemIds.add(item3.getId());

        List<Item> items = itemRepository.findSelectedItemWithMember(itemIds);
        for (Item item : items) {
            System.out.println(item);
        }
        // then
    }
}