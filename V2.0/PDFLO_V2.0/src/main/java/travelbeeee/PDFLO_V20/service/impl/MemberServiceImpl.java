package travelbeeee.PDFLO_V20.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLO_V20.domain.FileInformation;
import travelbeeee.PDFLO_V20.domain.entity.*;
import travelbeeee.PDFLO_V20.domain.enumType.FileType;
import travelbeeee.PDFLO_V20.domain.enumType.MemberType;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;
import travelbeeee.PDFLO_V20.domain.form.ProfileForm;
import travelbeeee.PDFLO_V20.domain.form.SignUpForm;
import travelbeeee.PDFLO_V20.domain.form.LoginForm;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.exception.ErrorCode;
import travelbeeee.PDFLO_V20.repository.*;
import travelbeeee.PDFLO_V20.service.MemberService;
import travelbeeee.PDFLO_V20.utility.FileManager;
import travelbeeee.PDFLO_V20.utility.Sha256Encryption;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
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
    public Member login(LoginForm loginDto) throws PDFLOException, NoSuchAlgorithmException {
        Optional<Member> findMember = memberRepository.findByUsername(loginDto.getUsername());
        if(findMember.isEmpty()){ // 존재하지않는 회원아이디
            throw new PDFLOException(ErrorCode.LOGIN_INPUT_INVALID);
        }

        Member member = findMember.get();

        if(!member.getPassword().equals(sha256Encryption.sha256(loginDto.getPassword(), member.getSalt()))){ // 비밀번호가 틀림
            throw new PDFLOException(ErrorCode.LOGIN_INPUT_INVALID);
        }

        return member;
    }

    @Transactional
    @Override
    public void signUp(SignUpForm joinDto) throws PDFLOException, NoSuchAlgorithmException {
        Optional<Member> findMember = memberRepository.findByUsername(joinDto.getUsername());
        if(!findMember.isEmpty()){ // 이미 존재하는 회원아이디
            throw new PDFLOException(ErrorCode.MEMBER_NAME_DUPLICATION);
        }

        String salt = sha256Encryption.makeSalt();
        Member newMember = new Member(joinDto.getUsername(), sha256Encryption.sha256(joinDto.getPassword(), salt), salt, joinDto.getEmail(),
                MemberType.UNAUTHORIZATION, 0);

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
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        Member member = findMember.get();
        member.changeType(MemberType.AUTHORIZATION);
    }

    @Override
    public void checkPassword(Long memberId, String password) throws PDFLOException, NoSuchAlgorithmException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        Member member = findMember.get();
        if (!member.getPassword().equals(sha256Encryption.sha256(password, member.getSalt()))) {
            throw new PDFLOException(ErrorCode.PASSWORD_INPUT_INVALID);
        }
    }

    @Transactional
    @Override
    public void updatePassword(Long memberId, String newPassword) throws NoSuchAlgorithmException, PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        Member member = findMember.get();
        member.changePassword(sha256Encryption.sha256(newPassword, member.getSalt()));
    }

    @Transactional
    @Override
    public void usePoint(Long memberId, Integer amount, PointType pointType) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST); // 존재하지않는회원

        Member member = findMember.get();
        if(pointType == PointType.USE && member.getPoint() < amount){
            throw new PDFLOException(ErrorCode.MEMBER_INSUFFICIENT_BALANCE); // 잔액부족
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

    @Override
    public List<Item> findMemberItem(Long memberId) {
        return itemRepository.findByMember(memberId);
    }

    @Transactional
    @Override
    public void uploadProfile(Long memberId, ProfileForm profileForm) throws PDFLOException, NoSuchAlgorithmException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        Optional<Profile> findProfile = profileRepository.findProfileByMember(memberId);
        if(!findProfile.isEmpty()){ // 기존 프로필 지우기
            Profile deleteProfile = findProfile.get();
            fileManager.fileDelete(deleteProfile.getFileInfo().getLocation(),
                    deleteProfile.getFileInfo().getSaltedFileName(), deleteProfile.getFileInfo().getExtension());
            profileRepository.delete(deleteProfile);
        }

        MultipartFile profile = profileForm.getProfile();
        FileInformation fileInformation = fileManager.fileUpload(profile, FileType.PROFILE);
        Member member = findMember.get();

        Profile newProfile = new Profile(member, fileInformation);
        profileRepository.save(newProfile);
    }

    @Transactional
    @Override
    public void deleteProfile(Long memberId) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        Optional<Profile> findProfile = profileRepository.findProfileByMember(memberId);
        if(findProfile.isEmpty()) throw new PDFLOException(ErrorCode.PROFILE_NO_EXIST);

        Profile profile = findProfile.get();
        fileManager.fileDelete(profile.getFileInfo().getLocation(), profile.getFileInfo().getSaltedFileName(), profile.getFileInfo().getExtension());

        profileRepository.delete(profile);
    }

    @Override
    public Optional<Profile> findProfileByMember(Long memberId) {
        return profileRepository.findProfileByMember(memberId);
    }

    @Override
    public Member findMember(Long memberId) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);
        return findMember.get();
    }

    @Override
    public List<Order> findOrder(Long memberId) {
        return orderRepository.findAllByMemberWithItem(memberId);
    }

    @Override
    public List<Item> findSellingItem(Long memberId) {
        return itemRepository.findByMember(memberId);
    }

    @Override
    public List<OrderItem> findSellingHistory(Long itemId) {
        return orderItemRepository.findAllWithMemberByItem(itemId);
    }
}
