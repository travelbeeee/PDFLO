package travelbeeee.PDFLOpjt.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLOpjt.domain.UserAccount;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserAccountRepositoryTest {
    @Autowired
    UserAccountRepository userAccountRepository;

    @AfterEach
    void afterEach() {userAccountRepository.deleteAll();}

    @Test
    public void 계좌insert테스트() throws Exception{
        //given
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1);
        userAccount.setBalance(0);
        //when
        int res = userAccountRepository.insert(userAccount);
        //then
        assertThat(res).isEqualTo(1);
    }

    @Test
    public void 계좌update테스트() throws Exception{
        //given
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1);
        userAccount.setBalance(0);
        //when
        userAccountRepository.insert(userAccount);
        userAccount.setBalance(500);
        int res = userAccountRepository.update(userAccount);
        UserAccount findUserAccount = userAccountRepository.selectByUser(userAccount.getUserId());
        //then
        assertThat(res).isEqualTo(1);
        assertThat(findUserAccount.getBalance()).isEqualTo(500);
    }
}