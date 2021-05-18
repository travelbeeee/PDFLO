# PDFLO 프로젝트

PDF 전자책 판매사이트

`Spring Boot 2.4.3`

`Java 11`

`lombok 1.18.18`

`Oracle ojdbc8 19.8.0.0`

`Mybatis 3.5.6`

1) 개발 목표

- 객체지향적인 개발
- 테스트지향적인 개발
- Controller 에서 입력받은 값 혹은 서비스 결과 에러처리 !!
- Service 에서는 로직에만 집중하기!!
- Repository 는 DB 에만 집중하기!!

2) 해야할 일

- [x]  DB 설계 및 틀짜기 (2021-02-23)
- [x]  회원 도메인, Repository, Service 개발하기 (2021-02-24)
- [x]  프로필 도메인, Repository 개발하기 (2021-02-25)
- [x]  썸네일 도메인, Repository 개발하기 (2021-02-25)
- [x]  콘텐츠 도메인, Repository 개발하기 (2021-02-25)
- [x]  PDF 파일 도메인, Repository 개발하기 (2021-02-25)
- [x]  댓글 도메인, Repository 개발하기(2021-02-25)
- [x]  주문 도메인, Repository 개발하기(2021-02-25)
- [x]  DB설계도그리기 (2021-02-26)
- [x]  콘텐츠 Service 개발(2021-02-26)
- [x]  회원 계좌 Service 개발(2021-02-26)
- [x]  댓글 Service 개발(2021-03-02)
- [x]  주문 Service 개발(2021-03-03)
- [x]  API설계하기(2021-03-03)
- [x]  에러처리성공하기(2021-03-03)
- [x]  에러처리할 수 있게 Service 뜯어고치기(2021-03-04)
- [x]  navbar 구성 및 thymeleaf 공부(2021-03-05)
- [x]  mainpage 프론트 구현(2021-03-05)
- [x]  회원 계좌에 충전하기(2021-03-08)
- [x]  콘텐츠 등록하기(2021-03-09)
- [x]  콘텐츠 구매하기(2021-03-10)
- [x]  로그인, 회원가입, 메인페이지 프론트 간단하게라도...!!(2021-03-10)
- [x]  구매내역 구현하기(2021-03-10)
- [x]  판매내역 구현하기(2021-03-10)
- [x]  사진 이미지 크기 자동 조절(2021-03-11)
- [x]  콘텐츠 삭제하기(2021-03-11)
- [x]  콘텐츠 수정하기(2021-03-12)
- [x]  PDF, 썸네일, 프로필 DB location 저장값 절대경로가 아니라 프로젝트 내 상대경로로 바꾸기.(2021-03-16)
- [x]  프로필 사진 등록하기.(2021-03-16)
- [x]  콘텐츠 다운로드(구매자만 가능) (2021-03-17)
- [x]  콘텐츠 후기 남기기(구매자만 가능) (2021-03-17)
- [x]  콘텐츠 후기 별점남기기(구매자만 가능) (2021-03-17)
- [x]  콘텐츠 후기 수정/삭제 구현하기(2021-03-18)
- [x]  콘텐츠 후기 view에 뿌려주기(2021-03-18)
- [x]  콘텐츠 후기 별점 js구현(2021-03-18)
- [ ]  아이디찾기
- [ ]  비밀번호찾기
- [x]  비밀번호수정
- [ ]  일정 금액 이상 구매한 회원 VIP —> Discount 5% VVIP —> Discount 10%
- [ ]  인기 콘텐츠 추천해주기! 흠... 어떤걸 기준으로 할까!
- [ ]  장바구니...!

**3) DB 구조**

![https://user-images.githubusercontent.com/59816811/109256680-dbbd0900-7839-11eb-87f1-f31842a29cdb.png](https://user-images.githubusercontent.com/59816811/109256680-dbbd0900-7839-11eb-87f1-f31842a29cdb.png)

**4) API 설계**

- /user/signUp    GET    회원가입페이지로 이동
- /user/signUp    POST    회원가입진행
- /user/login    GET    로그인페이지로 이동
- /user/login    POST    로그인진행
- /user/mypage    GET    마이페이지로이동하기
- /user/modify    GET    회원비밀번호수정페이지로 이동
- /user/modify    POST    회원비밀번호수정페이지
- /user/delete    POST    회원탈퇴
- /user/logout    GET    로그아웃
- /user/order    GET    내주문내역으로 이동
- /user/sell    GET    내판매내역으로 이동
- /user/deposit    GET    포인트충전페이지로 이동
- /user/deposit    POST    포인트충천하기
- /user/sending    GET    메일인증보내는페이지로 이동
- /user/sending    POST    메일로 인증코드 보내기
- /user/auth    POST    인증코드 확인하기
- /user/profile    GET    프로필등록폼 이동
- /user/profile    POST   프로필등록하기
- /user/findId    GET    아이디찾기폼 이동
- /user/findId    POST    이메일인증코드 보내기
- /user/findIdAuth    POST    이메일인증코드 입력받기
- /user/findPassword    GET     비밀번호찾기폼 이동
- /user/findPassword    POST     이메일인증코드 보내기
- /user/findPasswordAuth    POST    이메일인증코드 입력받기
- /user/findPasswordModify    POST    비밀번호 수정하기
- /main    GET    메인페이지
- /content/{contentId}    GET    글상세페이지로 이동
- /content/upload    GET    글작성페이지로 이동
- /content/upload    POST    글작성하기
- /content/modify/{contentId}     GET    글수정페이지로 이동
- /content/modify/{contentId}    POST    글수정하기
- /content/delete/{contentId}    POST    글삭제하기
- /content/buy/{contentId}    POST    글구매하기
- /content/download/{contentId}    POST    PDF다운로드하기
- /comment/write/{contentId}    POST    댓글작성하기
- /comment/modify/{commentId}    GET    댓글수정하는페이지로 이동
- /comment/modify/{commentId}    POST    댓글수정하기
- /comment/delete/{commentId}    POST    댓글삭제하기

**5) 회원가입처리**

**6) 파일처리**

**6-1) 파일경로**

[ 현재코드 ]

resources/static/files 폴더 안에

resources/static/files/PDF

resources/static/files/PROFILE

resources/static/files/THUMBNAIL 폴더를 만들고 각각 입력된 파일들을 저장했고, 파일들을 로컬에 저장할 때 절대 경로가 필요해 다음과 같이 해결함.

[application.properties](http://application.properties) 에 기본 절대경로를 셋팅하고 그 경로를 가지고 와서 pdf, thumbnial, profile Multipartfile 을 저장할 때 사용하고 있었다.

```java
// application.properties
# 업로드 파일들 기본 공통경로
files-location=C://Users/HyunSeok/Desktop/studyWithMe/gitHub/Spring_Practice/PDFLO/PDFLOpjt/src/main/resources/static/files

// .class
@Value("${files-location}")
private String filesLocation;

// 썸네일 파일 예시
filesLocation + "/THUMBNAIL/" + thumbnail.getSaltedFileName()
```

—> 다른 폴더로 프로젝트가 옮겨지는 등 상황마다 절대경로를 다시 설정해줘야하는 문제점이 있다.

—> 절대 경로가 아니라 프로젝트 내 상대경로로 저장 하는 방법은 없을까...?? 

**7) 에러처리**

- `Enum` 타입의 `ReturnCode` 상황별로 에러를 만들어둔다.
- `ReturnCode` 를 가지고 있는 `PDFLOException` 예클래스를 만든다.
- Business Exception 이 발생하는 경우 `PDFLOException` 을 throw 하며 상황에 맞는 `ReturnCode` 를 넘겨준다.
- `@ControllerAdvice` 에서 `ModelAndView` 를 이용해 상황에 맞는 에러 메시지와 에러 페이지에 해당하는 view로 넘겨준다.

( 되게 간단해보이지만,,,, 정말 많은 구글링과 구글링 끝에 드디어 성공!!!!!! )

**8) 포인트, 돈 처리**

실제로 결제 기능을 구현하면 좋지만 사업자 번호가 필요해서 간단하게 계정마다 원하는 만큼 포인트를 충전할 수 있고, 포인트를 이용해서 구매를 할 수 있게 셋팅한다.

**9) 이미지 크기 동일하게만들기**

```java
@Service
public class ImageResizer {

    public void resizeImage(String oPath, int tWidth, int tHeight){ //원본 경로를 받아와 resize 후 저장한다.
        File oFile = new File(oPath);

        int index = oPath.lastIndexOf(".");
        String ext = oPath.substring(index + 1); // 파일 확장자

        String tPath = oFile.getParent() + File.separator + "t-" + oFile.getName(); // 썸네일저장 경로
        File tFile = new File(tPath);

        try {
            BufferedImage oImage = ImageIO.read(oFile); // 원본이미지

            BufferedImage tImage = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_3BYTE_BGR); // 썸네일이미지
            Graphics2D graphic = tImage.createGraphics();
            Image image = oImage.getScaledInstance(tWidth, tHeight, Image.SCALE_SMOOTH);
            graphic.drawImage(image, 0, 0, tWidth, tHeight, null);
            graphic.dispose(); // 리소스를 모두 해제

            ImageIO.write(tImage, ext, tFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

입력된 경로에 있는 파일을 불러와서 원하는 사이즈로 `BufferedImage` `Graphics2D` `Image` 객체를 이용해서 만들어 새롭게 저장한다. 이때, resize 된 Image는 파일 이름 앞에 `t-` 를 동일하게 입력해줘서 resize 된 Image가 필요하면 `t-` 를 경로와 파일 이름 사이에 입력해서 불러오면 된다. 

**10) 아이디 찾기**

10-1) 이메일 입력받기 ( 이메일 인증 미완료면 이메일 인증부터...!! )

10-2) 이메일로 인증코드 보내기

10-3) 인증코드가 맞다면 아이디 알려주기!

**11) 비밀번호 찾기**

11-1) 아이디 / 이메일 입력받기 ( 이메일 인증 미완료면 이메일 인증부터! )

11-2) 이메일로 인증코드 보내기

11-3) 인증코드가 맞다면 비밀번호 재설정!

// 처리해야될것

- [ ]  jfif 확장자 파일은 File 저장은 되는데 Resize 후 저장이 안된다...!