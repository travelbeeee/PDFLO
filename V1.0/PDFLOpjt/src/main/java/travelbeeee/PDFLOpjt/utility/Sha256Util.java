package travelbeeee.PDFLOpjt.utility;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Component
public class Sha256Util {

    /**
     * UUID 를 이용해 Salting 을 위한 salt 값 생성
     */
    public String makeSalt() {
        return UUID.randomUUID().toString();
    }

    /**
     * SHA-256으로 해싱하는 메소드
     */
    public String sha256(String msg, String salt) throws NoSuchAlgorithmException {
        msg += salt;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());

        return bytesToHex(md.digest());
    }

    /**
     * 바이트를 헥스값으로 변환한다.
     */
    public String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
