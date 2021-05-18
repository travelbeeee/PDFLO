package travelbeeee.PDFLOpjt.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLOpjt.domain.User;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void afterEach(){
        userRepository.deleteAll();
    }

    User getMember(){
        User user = new User();
        user.setUsername("member1");
        user.setUserpwd("password1");
        user.setEmail("email1");
        user.setSalt("salt1");
        user.setAuth("UNAUTH");
        return user;
    }

    @Test
    void 유저insertTest(){
        //given Member를 하나 만들어서 DB에 Insert해보자.
        User member = getMember();
        //when
        int res = userRepository.insert(member);
        //then insert는 성공하면 1을 반환
        assertThat(res).isEqualTo(1);
    }

    @Test
    public void 유저selectByIdTest() throws Exception{
        //given
        User member = getMember();
        //when
        userRepository.insert(member);
        User findMember = userRepository.selectById(member.getUserId());
        //then
        assertThat(member.getUserId()).isEqualTo(findMember.getUserId());
    }

    @Test
    public void 유저selectByNameTest() throws Exception{
        //given
        User member = getMember();
        //when
        userRepository.insert(member);
        User findMember = userRepository.selectByName(member.getUsername());
        //then
        assertThat(member.getUserId()).isEqualTo(findMember.getUserId());
    }

    @Test
    public void 유저비밀번호수정Test() throws Exception{
        //given
        User member = getMember();
        //when
        userRepository.insert(member);
        member.setUserpwd("newPassword");
        userRepository.update(member);
        User findMember = userRepository.selectById(member.getUserId());
        //then
        assertThat(findMember.getUserpwd()).isEqualTo("newPassword");
    }
    @Test
    void 유저deleteTest(){
        //given Member를 하나 만들어서 DB에 Insert해보자.
        User member = getMember();
        //when
        userRepository.insert(member);
        userRepository.delete(member.getUserId());
        User findMember = userRepository.selectById(member.getUserId());
        //then
        assertThat(findMember).isNull();
    }
}