package travelbeeee.PDFLO_V20.exception;

public enum ErrorCode {
    MEMBER_NAME_DUPLICATION(400, "ID가 이미 사용중입니다."),
    LOGIN_INPUT_INVALID(400, "ID, Password가 틀렸습니다."),
    SIGNUP_INPUT_INVALID(400, "ID, Password, Email을 양식에 맞게 작성해주세요."),
    PASSWORD_INPUT_INVALID(400, "Password가 틀렸습니다"),
    PASSWORD_UPDATE_SAME_PASSWORD(400, "동일한 비밀번호로는 변경할 수 없습니다."),
    COMMENT_NO_PERMISSION_BUYING(400, "구매자만 후기를 남길 수 있습니다."),
    COMMENT_ALREADY_WRITTEN(400, "이미 후기를 작성하셨습니다."),
    COMMENT_NO_PERMISSION_WRITER(400, "후기 작성자만 후기를 삭제/수정 할 수 있습니다."),
    COMMENT_INPUT_INVALID(400, "댓글과 점수를 모두 입력해주세요."),
    FILE_ERROR(500, "파일 저장/제거 중에 문제가 발생했습니다."),
    CONTENT_ALREADY_BOUGHT(400, "이미 구매한 상품입니다."),
    MEMBER_INSUFFICIENT_BALANCE(400, "잔액이 부족합니다."),
    MEMBER_NO_PERMISSION(400, "로그인 후 이용 가능합니다."),
    MEMBER_IS_SELLER(400, "자신의 상품은 구입할 수 없습니다."),
    MAIL_AUTHCODE_INCORRECT(400, "인증코드가 틀렸습니다. 다시 진행해주세요."),
    CONTENT_UPLOAD_ERROR(400, "양식에 맞게 다시 PDF 파일을 등록해주세요."),
    MEMBER_ALREADY_LOGIN(400, "이미 로그인 하셨습니다."),
    MEMBER_ALREADY_BOUGHT(400, "기존에 구매한 상품이 있습니다."),
    SELLER_CANT_BUY(400, "상품 판매자는 구매가 불가능합니다."),
    MEMBER_NOT_SELLER(400, "상품 판매자만 수정/삭제가 가능합니다."),
    MEMBER_ALREADY_REGISTERED_PROFILE(400, "이미 프로필 사진을 등록했습니다. 수정 기능을 이용해주세요."),
    DOWNLOAD_NO_PERMISSION(400, "구매자만 PDF 다운로드가 가능합니다."),
    MEMBER_NO_EXIST(400, "없는 회원입니다."),
    EMAIL_NO_EXIST(400, "입력한 이메일은 없는 이메일입니다."),
    ITEM_NO_EXIST(400, "없는 상품입니다."),
    PROFILE_NO_EXIST(400, "프로필 사진을 먼저 등록해주세요."),
    COMMENT_NO_EXIST(400, "존재하지 않는 후기입니다."),
    CART_NO_EXIST(400, "존재하지 않는 장바구니입니다"),
    CART_ALREADY_EXIST(400, "이미 장바구니에 담은 상품입니다");

    final int status;
    final String message;

    ErrorCode(int status, String message){
        this.status = status;
        this.message = message;
    }

    public int getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

}
