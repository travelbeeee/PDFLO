package travelbeeee.PDFLOpjt.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import travelbeeee.PDFLOpjt.domain.UserAccount;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.repository.UserAccountRepository;
import travelbeeee.PDFLOpjt.service.UserAccountService;
import travelbeeee.PDFLOpjt.utility.ReturnCode;

@Service @RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;

    @Override
    public ReturnCode deposit(int userId, int price) {
        UserAccount userAccount = userAccountRepository.selectByUser(userId);
        userAccount.setBalance(userAccount.getBalance() + price);
        userAccountRepository.update(userAccount);

        return ReturnCode.SUCCESS;
    }

    @Override
    public ReturnCode withdraw(int userId, int price) throws PDFLOException {
        UserAccount userAccount = userAccountRepository.selectByUser(userId);
        if( userAccount.getBalance() < price) throw new PDFLOException(ReturnCode.USER_INSUFFICIENT_BALANCE);
        userAccount.setBalance(userAccount.getBalance() - price);
        userAccountRepository.update(userAccount);
        return ReturnCode.SUCCESS;
    }

    @Override
    public UserAccount selectById(int userId) {
        UserAccount userAccount = userAccountRepository.selectByUser(userId);

        return userAccount;
    }
}
