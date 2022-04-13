package jp.co.plus.core.mfa;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

/**
 * Google AuthenticatorのTOTPジェネレータークラス
 *
 * @see <a href="https://github.com/google/google-authenticator">Google Authenticator OpenSource</a>
 * @see <a href="https://tools.ietf.org/html/rfc6238">TOTP</a>
 * @see <a href="https://qiita.com/mfqmagic/items/d1150a95f13e3f44a995">Qiita</a>
 */
public class MFAAuthenticatorUtil {

    // taken from Google pam docs - we probably don't need to mess with these
    public static final int SECRET_SIZE = 10;

    // 乱数生成用シード
    public static final String SEED = "g8GjEvTbW5oVSV7avLBdwIHqGlUYNzKFI7izOF8GwLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";

    // 乱数生成用アルゴリズム
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";

    /**
     * ランダムな秘密鍵を生成する。
     * これはサーバーによって保存され、ユーザーアカウントに関連付けられて、
     * Google認証システムによって表示されるコードを検証する必要があります。
     * ユーザーはこの秘密鍵をデバイスに登録する必要があります。
     *
     * @return 秘密鍵
     */
    public static String generateSecretKey() {
        SecureRandom sr;
        try {
            // 乱数生成
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            sr.setSeed(Base64.decodeBase64(SEED));
            byte[] buffer = sr.generateSeed(SECRET_SIZE);

            // 乱数をBase32に変換して秘密鍵とする。
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            return new String(bEncodedKey);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("乱数生成エラー");
        }
        return null;
    }


}