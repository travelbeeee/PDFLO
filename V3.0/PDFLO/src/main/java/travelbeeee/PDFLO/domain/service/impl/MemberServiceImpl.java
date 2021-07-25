package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.FileInformation;
import travelbeeee.PDFLO.domain.model.entity.*;
import travelbeeee.PDFLO.domain.model.enumType.FileType;
import travelbeeee.PDFLO.domain.model.enumType.PointType;
import travelbeeee.PDFLO.domain.repository.*;
import travelbeeee.PDFLO.domain.service.MemberService;
import travelbeeee.PDFLO.domain.utility.FileManager;
import travelbeeee.PDFLO.domain.utility.Sha256Encryption;
import travelbeeee.PDFLO.web.form.LoginForm;
import travelbeeee.PDFLO.web.form.ProfileForm;
import travelbeeee.PDFLO.web.form.SignUpForm;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ItemRepository itemRepository;
    private final ProfileRepository profileRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final FileManager fileManager;
    private final Sha256Encryption sha256Encryption;

    @Override
    public Optional<Member> login(LoginForm loginDto) throws PDFLOException, NoSuchAlgorithmException {
        Optional<Member> findMember = memberRepository.findByUsername(loginDto.getUsername());
        if (findMember.isEmpty()) { // 아이디가 틀림
            return Optional.empty();
        }

        Member member = findMember.get();

        if(!member.getPassword().equals(sha256Encryption.sha256(loginDto.getPassword(), member.getSalt()))){ // 비밀번호가 틀림
            return Optional.empty();
        }
        return Optional.of(member);
    }

    @Transactional
    @Override
    public void signUp(SignUpForm form) throws PDFLOException, NoSuchAlgorithmException {
        Optional<Member> findMember = memberRepository.findByUsername(form.getUsername());
        if(!findMember.isEmpty()){ // 이미 존재하는 회원아이디
            throw new PDFLOException(ReturnCode.MEMBER_NAME_DUPLICATION);
        }

        String salt = sha256Encryption.makeSalt();
        Member newMember = new Member(form.getUsername(), sha256Encryption.sha256(form.getPassword(), salt), salt, form.getEmail(), 0);

        memberRepository.save(newMember);
    }

    @Transactional
    @Override
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Transactional
    @Override
    public void authorize(Long memberId) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ReturnCode.MEMBER_NO_EXIST);

        Member member = findMember.get();
    }

    @Override
    public void checkPassword(Long memberId, String password) throws PDFLOException, NoSuchAlgorithmException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ReturnCode.MEMBER_NO_EXIST);

        Member member = findMember.get();
        if (!member.getPassword().equals(sha256Encryption.sha256(password, member.getSalt()))) {
            throw new PDFLOException(ReturnCode.PASSWORD_INPUT_INVALID);
        }
    }

    @Transactional
    @Override
    public void updatePassword(Long memberId, String newPassword) throws NoSuchAlgorithmException, PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ReturnCode.MEMBER_NO_EXIST);

        Member member = findMember.get();
        member.changePassword(sha256Encryption.sha256(newPassword, member.getSalt()));
    }

    @Transactional
    @Override
    public void usePoint(Long memberId, Integer amount, PointType pointType) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ReturnCode.MEMBER_NO_EXIST); // 존재하지않는회원

        Member member = findMember.get();
        if(pointType == PointType.USE && member.getPoint() < amount){
            throw new PDFLOException(ReturnCode.MEMBER_INSUFFICIENT_BALANCE); // 잔액부족
        }

        if(pointType == PointType.USE) member.losePoint(amount);
        else member.getPoint(amount);

        PointHistory pointHistory = new PointHistory(member, amount, pointType);
        pointHistoryRepository.save(pointHistory);
    }

    @Override
    public List<PointHistory> findMemberPointHistory(Long memberId) {
        return pointHistoryRepository.findMemberPointHistory(memberId);
    }

    @Transactional
    @Override
    public void uploadProfile(Long memberId, ProfileForm profileForm) throws PDFLOException, NoSuchAlgorithmException, IOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ReturnCode.MEMBER_NO_EXIST);

        Optional<Profile> findProfile = profileRepository.findProfileByMember(memberId);
        if(!findProfile.isEmpty()){ // 기존 프로필 지우기
            Profile deleteProfile = findProfile.get();
            fileManager.fileDelete(deleteProfile.getFileInfo().getLocation(), deleteProfile.getFileInfo().getSaltedFileName());
            profileRepository.delete(deleteProfile);
        }

        MultipartFile profile = profileForm.getProfile();
        FileInformation fileInformation = fileManager.fileSave(profile, FileType.PROFILE);
        Member member = findMember.get();

        Profile newProfile = new Profile(member, fileInformation);
        profileRepository.save(newProfile);
    }

    @Transactional
    @Override
    public void deleteProfile(Long memberId) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ReturnCode.MEMBER_NO_EXIST);

        Optional<Profile> findProfile = profileRepository.findProfileByMember(memberId);
        if(findProfile.isEmpty()) throw new PDFLOException(ReturnCode.PROFILE_NO_EXIST);

        Profile profile = findProfile.get();
        fileManager.fileDelete(profile.getFileInfo().getLocation(), profile.getFileInfo().getSaltedFileName());

        profileRepository.delete(profile);
    }

    @Override
    public Optional<Profile> findProfileByMember(Long memberId) {
        return profileRepository.findProfileByMember(memberId);
    }

    @Override
    public Member findMember(Long memberId) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ReturnCode.MEMBER_NO_EXIST);
        return findMember.get();
    }

    @Override
    public Optional<Member> findMemberByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public List<Order> findOrderWithItemByMember(Long memberId) {
        return orderRepository.findAllByMemberWithItem(memberId);
    }

    @Override
    public List<Item> findSellItem(Long memberId) {
        return itemRepository.findWithThumbnailByMember(memberId);
    }

    @Override
    public List<OrderItem> findSellHistory(Long itemId) {
        return orderItemRepository.findAllWithMemberAndItemByItem(itemId);
    }
}
