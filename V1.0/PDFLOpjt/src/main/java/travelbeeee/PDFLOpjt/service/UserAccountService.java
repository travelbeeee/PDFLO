package travelbeeee.PDFLOpjt.service;

import travelbeeee.PDFLOpjt.domain.UserAccount;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.utility.ReturnCode;

public interface UserAccountService {
    ReturnCode deposit(int userId, int price); // 해당 유저 계좌를 price만큼 돈 충전
    ReturnCode withdraw(int userId, int price) throws PDFLOException; // 해당 유저 계좌를 price만큼 돈 감소
    UserAccount selectById(int userId);
}
