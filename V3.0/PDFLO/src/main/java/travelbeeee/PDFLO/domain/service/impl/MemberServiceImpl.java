package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ReturnCode delete(Long memberId) {
        if(memberRepository.findById(memberId).isEmpty()){
            return ReturnCode.MEMBER_NO_EXIST;
        }
        memberRepository.deleteById(memberId);
        return ReturnCode.SUCCESS;
    }

    @Override
    public void checkPassword(Long memberId, String password) throws PDFLOException, NoSuchAlgorithmException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow( () -> new PDFLOException(ReturnCode.MEMBER_NO_EXIST));
        if (!member.getPassword().equals(sha256Encryption.sha256(password, member.getSalt()))) {
            throw new PDFLOException(ReturnCode.PASSWORD_INPUT_INVALID);
        }
    }

    @Transactional
    @Override
    public void updatePassword(Long memberId, String newPassword) throws NoSuchAlgorithmException, PDFLOException {
        Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new PDFLOException(ReturnCode.MEMBER_NO_EXIST));

        member.changePassword(sha256Encryption.sha256(newPassword, member.getSalt()));
    }

    @Transactional
    @Override
    public void usePoint(Long memberId, Integer amount, PointType pointType) throws PDFLOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.MEMBER_NO_EXIST));

        if(pointType == PointType.USE && member.getPoint() < amount){
            throw new PDFLOException(ReturnCode.MEMBER_INSUFFICIENT_BALANCE); // 잔액부족
        }

        if(pointType == PointType.USE) member.losePoint(amount);
        else member.getPoint(amount);

        PointHistory pointHistory = new PointHistory(member, amount, pointType);
        pointHistoryRepository.save(pointHistory);
    }

    @Override
    public Page<PointHistory> findPointHistoryBymember(Long memberId, Pageable pageable) {
        return pointHistoryRepository.findMemberPointHistory(memberId, pageable);
    }

    @Transactional
    @Override
    public void uploadProfile(Long memberId, ProfileForm form) throws PDFLOException, NoSuchAlgorithmException, IOException {
        log.info("uploadProfile 메소드 실행");
        
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.MEMBER_NO_EXIST));

        MultipartFile inputProfile = form.getProfile();
        FileInformation fileInformation = fileManager.fileSave(inputProfile, FileType.PROFILE);

        Optional<Profile> findProfile = profileRepository.findProfileByMember(memberId);
        if(findProfile.isPresent()){ // 기존 프로필 지우기
            Profile profile = findProfile.get();
            fileManager.fileDelete(profile.getFileInfo().getLocation(), profile.getFileInfo().getSaltedFileName());
            profile.changeProfile(fileInformation);
        }
        else{
            Profile newProfile = new Profile(member, fileInformation);
            profileRepository.save(newProfile);
        }
    }

    @Transactional
    @Override
    public ReturnCode deleteProfile(Long memberId) throws PDFLOException {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.MEMBER_NO_EXIST));

        Profile profile = profileRepository.findProfileByMember(memberId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.PROFILE_NO_EXIST));

        fileManager.fileDelete(profile.getFileInfo().getLocation(), profile.getFileInfo().getSaltedFileName());

        profileRepository.delete(profile);
        return ReturnCode.SUCCESS;
    }

    @Override
    public Optional<Profile> findProfileByMember(Long memberId) {
        return profileRepository.findProfileByMember(memberId);
    }

    @Override
    public Member findMember(Long memberId) throws PDFLOException {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.MEMBER_NO_EXIST));
    }

    @Override
    public Optional<Member> findMemberByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public Page<Order> findOrderByMember(Long memberId, Pageable pageable) {
        return orderRepository.findAllByMember(memberId, pageable);
    }

    /**
     *
     */
    @Override
    public Page<OrderItem> findMemberSellHistory(Long memberId, Long itemId, Pageable pageable) throws PDFLOException {
        memberRepository.findById(memberId).orElseThrow(() -> new PDFLOException(ReturnCode.MEMBER_NO_EXIST));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new PDFLOException(ReturnCode.ITEM_NO_EXIST));

        if(item.getMember().getId() != memberId){
            throw new PDFLOException(ReturnCode.MEMBER_NO_PERMISSION_ITEM);
        }

        return orderItemRepository.findPagingWithOrderMemberByItem(itemId, pageable);
    }
}
