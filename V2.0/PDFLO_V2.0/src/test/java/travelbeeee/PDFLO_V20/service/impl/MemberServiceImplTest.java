package travelbeeee.PDFLO_V20.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;
import travelbeeee.PDFLO_V20.dto.LoginDto;
import travelbeeee.PDFLO_V20.dto.ProfileDto;
import travelbeeee.PDFLO_V20.dto.SignUpDto;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.repository.PointHistoryRepository;
import travelbeeee.PDFLO_V20.service.MemberService;
import travelbeeee.PDFLO_V20.utility.Sha256Encryption;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberServiceImplTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 회원가입_정상() throws PDFLOException, NoSuchAlgorithmException {
        SignUpDto signUpDto = new SignUpDto("member1", "password1", "email1");

        memberService.signUp(signUpDto);

        List<Member> members = memberRepository.findByUsername("member1");
        Member findMember = members.get(0);

        Assertions.assertThat(members.size()).isEqualTo(1);
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
        Assertions.assertThat(findMember.getPassword()).isNotEqualTo("password1");
        Assertions.assertThat(findMember.getPoint()).isEqualTo(0);
    }

    @Test
    public void 회원가입_이름중복_에러() throws PDFLOException, NoSuchAlgorithmException {
        SignUpDto signUpDto = new SignUpDto("member1", "password1", "email1");
        SignUpDto signUpDto2 = new SignUpDto("member1", "password2", "email2");

        memberService.signUp(signUpDto);

        assertThrows(PDFLOException.class, () ->
                memberService.signUp(signUpDto2));
    }

    @Test
    public void 로그인_정상() throws PDFLOException, NoSuchAlgorithmException {
        SignUpDto signUpDto = new SignUpDto("member1", "password1", "email1");
        memberService.signUp(signUpDto);

        LoginDto loginDto = new LoginDto("member1", "password1");
        boolean res = memberService.login(loginDto);

        Assertions.assertThat(res).isEqualTo(true);
    }

    @Test
    public void 로그인_아이디_에러() throws PDFLOException, NoSuchAlgorithmException {
        SignUpDto signUpDto = new SignUpDto("member1", "password1", "email1");
        memberService.signUp(signUpDto);

        LoginDto loginDto = new LoginDto("member2", "password1");

        assertThrows(PDFLOException.class, () ->
                memberService.login(loginDto));
    }

    @Test
    public void 로그인_비밀번호_에러() throws PDFLOException, NoSuchAlgorithmException {
        SignUpDto signUpDto = new SignUpDto("member1", "password1", "email1");
        memberService.signUp(signUpDto);

        LoginDto loginDto = new LoginDto("member1", "password2");

        assertThrows(PDFLOException.class, () ->
                memberService.login(loginDto));
    }

    @Test
    public void 회원삭제() throws Exception {
        // given
        Member member = new Member("member1", "password1", null, null, null, null);
        memberRepository.save(member);

        // when
        memberService.delete(member.getId());

        em.flush();
        em.clear();

        Optional<Member> findMember = memberRepository.findById(member.getId());
        // then
        Assertions.assertThat(findMember.isEmpty()).isTrue();
    }

    @Test
    public void 비밀번호_수정() throws Exception {
        // given
        SignUpDto signUpDto = new SignUpDto("member1", "password1", "email1");
        memberService.signUp(signUpDto);

        Member member = memberRepository.findByUsername("member1").get(0);

        // when
        memberService.updatePassword(member.getId(), "newPassword");

        // then
        boolean res = memberService.login(new LoginDto("member1", "newPassword"));

        Assertions.assertThat(res).isTrue();
    }

    @Test
    public void 멤버_포인트_충전_테스트() throws Exception {
        // given
        SignUpDto signUpDto = new SignUpDto("member1", "password1", "email1");
        memberService.signUp(signUpDto);

        Member member = memberRepository.findByUsername("member1").get(0);
        // when
        memberService.usePoint(member.getId(), 5000, PointType.CHARGE);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findByUsername("member1").get(0);

        // then
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
        Assertions.assertThat(findMember.getPoint()).isEqualTo(5000);
    }

    @Test
    public void 멤버_포인트_사용_예외_잔액부족() throws Exception {
        // given
        SignUpDto signUpDto = new SignUpDto("member1", "password1", "email1");
        memberService.signUp(signUpDto);

        Member member = memberRepository.findByUsername("member1").get(0);
        // when

        assertThrows(PDFLOException.class, () ->
                memberService.usePoint(member.getId(), 5000, PointType.USE));
    }

    @Test
    public void 프로필_업로드_테스트() throws Exception {
        // given
        FileInputStream profileFileInputStream = new FileInputStream(new File("C:/Users/sochu/바탕 화면/study/github/PDFLO/V2.0/TESTFILES/profile1.JPG"));
        MockMultipartFile profile = new MockMultipartFile("testFile1", "testFile1.JPG", "JPG", profileFileInputStream);

        ProfileDto profileDto = new ProfileDto(profile);

        Member member = new Member("member1", "password1", "salt1", null, null, null);
        memberRepository.save(member);

        // when
        memberService.uploadProfile(member.getId(), profileDto);

        // then
    }

    @Test
    public void 프로필_삭제_테스트() throws Exception{
        // given
        FileInputStream profileFileInputStream = new FileInputStream(new File("C:/Users/sochu/바탕 화면/study/github/PDFLO/V2.0/TESTFILES/profile1.JPG"));
        MockMultipartFile profile = new MockMultipartFile("testFile1", "testFile1.JPG", "JPG", profileFileInputStream);

        ProfileDto profileDto = new ProfileDto(profile);

        Member member = new Member("member1", "password1", "salt1", null, null, null);
        memberRepository.save(member);
        memberService.uploadProfile(member.getId(), profileDto);

        // when
        memberService.deleteProfile(member.getId());

        // then
    }
}