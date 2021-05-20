package travelbeeee.PDFLO_V20.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.enumType.MemberType;
import travelbeeee.PDFLO_V20.dto.SignUpDto;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.service.ItemService;
import travelbeeee.PDFLO_V20.service.MemberService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceImplTest {

    @Value("${file.location}")
    private String fileLocation;

    @Autowired
    ItemService itemService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void test() throws Exception{
        //given
        Member member = new Member("member1", "password1", "salt1", "email1", MemberType.UNAUTHORIZATION, 0);
        memberRepository.save(member);

        //when

        //then

    }
}