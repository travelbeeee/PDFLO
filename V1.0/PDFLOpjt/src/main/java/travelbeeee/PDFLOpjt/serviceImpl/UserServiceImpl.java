package travelbeeee.PDFLOpjt.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLOpjt.domain.Pdf;
import travelbeeee.PDFLOpjt.domain.Profile;
import travelbeeee.PDFLOpjt.domain.User;
import travelbeeee.PDFLOpjt.domain.UserAccount;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.inputform.JoinForm;
import travelbeeee.PDFLOpjt.inputform.LoginForm;
import travelbeeee.PDFLOpjt.repository.ProfileRepository;
import travelbeeee.PDFLOpjt.utility.FileManager;
import travelbeeee.PDFLOpjt.utility.ImageResizer;
import travelbeeee.PDFLOpjt.utility.ReturnCode;
import travelbeeee.PDFLOpjt.repository.UserAccountRepository;
import travelbeeee.PDFLOpjt.repository.UserRepository;
import travelbeeee.PDFLOpjt.service.UserService;
import travelbeeee.PDFLOpjt.utility.Sha256Util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;
    private final Sha256Util sha256Util;
    private final ProfileRepository profileRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final FileManager fileManager;
    private final ImageResizer imageResizer;

    @Value("${files-location}")
    private String filesLocation;

    @Override
    public ReturnCode join(JoinForm joinForm) throws NoSuchAlgorithmException, PDFLOException {
        User findUser = userRepository.selectByName(joinForm.getUsername());
        if(findUser != null) {
            throw new PDFLOException(ReturnCode.USERNAME_DUPLICATION);
        }

        User newUser = new User();
        newUser.setUsername(joinForm.getUsername());
        newUser.setSalt(sha256Util.makeSalt());
        newUser.setUserpwd(sha256Util.sha256(joinForm.getUserpwd(), newUser.getSalt()));
        newUser.setEmail(joinForm.getEmail());
        newUser.setAuth("UNAUTHORIZATION");
        userRepository.insert(newUser);
        userAccountRepository.insert(new UserAccount(newUser.getUserId(), 0));

        return ReturnCode.SUCCESS;
    }

    @Override
    public User login(LoginForm loginForm) throws NoSuchAlgorithmException, PDFLOException {
        User findUser = userRepository.selectByName(loginForm.getUsername());
        if(findUser == null) throw  new PDFLOException(ReturnCode.LOGIN_INPUT_INVALID); // 아이디가 틀렸다!
        String userpwd = sha256Util.sha256(loginForm.getUserpwd(), findUser.getSalt());
        User user = userRepository.selectByNamePwd(loginForm.getUsername(), userpwd);
        if(user == null) throw new PDFLOException(ReturnCode.LOGIN_INPUT_INVALID); // 비밀번호가 틀렸다!

        return user;
    }


    @Override
    public ReturnCode updatePassword(int userId, String newpwd) throws NoSuchAlgorithmException, PDFLOException {
        User findUser = userRepository.selectById(userId);
        newpwd = sha256Util.sha256(newpwd, findUser.getSalt());
        if(newpwd == findUser.getUserpwd()) throw new PDFLOException(ReturnCode.PASSWORD_UPDATE_SAME_PASSWORD); // 동일 비밀번호
        findUser.setUserpwd(newpwd);
        userRepository.update(findUser);

        return ReturnCode.SUCCESS;
    }

    @Override
    public ReturnCode delete(int userId) {
        userRepository.delete(userId);
        userAccountRepository.delete(userId);

        return ReturnCode.SUCCESS;
    }

    @Override
    public User selectById(int userId) {
        User user = userRepository.selectById(userId);

        return user;
    }

    @Override
    public ReturnCode updateAuth(int userId) {
        userRepository.updateAuth(userId);

        return ReturnCode.SUCCESS;
    }

    @Override
    public ReturnCode uploadProfile(MultipartFile profileFile, int userId) throws PDFLOException, NoSuchAlgorithmException, IOException {
        Profile findProfile = profileRepository.selectByUserId(userId);
        if(findProfile != null) throw new PDFLOException(ReturnCode.USER_ALREADY_REGISTERED_PROFILE);

        Profile profile = new Profile();
        profile.setUserId(userId);
        profile.setOriginFileName(profileFile.getOriginalFilename());
        profile.setSaltedFileName(sha256Util.sha256(profile.getOriginFileName(), sha256Util.makeSalt())
                + profile.getOriginFileName().substring(profile.getOriginFileName().indexOf(".")));
        profile.setLocation("/files/PROFILE");

        fileManager.fileUpload(profileFile.getInputStream(), filesLocation + "/PROFILE", profile.getSaltedFileName());
        imageResizer.resizeImage(filesLocation + "/PROFILE/" + profile.getSaltedFileName(), 100, 100);

        profileRepository.insert(profile);

        return ReturnCode.SUCCESS;
    }

    @Override
    public List<User> selectByEamil(String email) {
        List<User> users = userRepository.selectByEmail(email);

        return users;
    }

    @Override
    public User selectByName(String username) {
        User user = userRepository.selectByName(username);
        return user;
    }
}
