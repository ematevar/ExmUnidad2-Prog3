package pe.edu.upt.efjavbiblioteca.infrastructure.security;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class EfjavTotpService {

    private static final int TIME_STEP_SECONDS = 30;
    private static final int DIGITS = 6;
    private static final int WINDOW = 1; // Permite ventana de +- 30s de desfase

    public String efjavFncGenerarNuevoSecret() {
        byte[] buffer = new byte[20];
        new SecureRandom().nextBytes(buffer);
        Base32 base32 = new Base32();
        return base32.encodeToString(buffer).replace("=", "");
    }

    public boolean efjavFncVerificarCodigo(String secretBase32, String codigoStr) {
        if (secretBase32 == null || codigoStr == null || codigoStr.trim().length() != DIGITS) {
            return false;
        }
        try {
            int codigoIngresado = Integer.parseInt(codigoStr.trim());
            long currentTime = System.currentTimeMillis() / 1000L;
            long currentStep = currentTime / TIME_STEP_SECONDS;

            for (int i = -WINDOW; i <= WINDOW; i++) {
                long step = currentStep + i;
                int codigoCalculado = efjavFncGenerarCodigoParaStep(secretBase32, step);
                if (codigoCalculado == codigoIngresado) {
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    private int efjavFncGenerarCodigoParaStep(String secretBase32, long step) {
        Base32 base32 = new Base32();
        byte[] key = base32.decode(secretBase32);

        byte[] data = new byte[8];
        long value = step;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "RAW"));
            byte[] hash = mac.doFinal(data);

            int offset = hash[hash.length - 1] & 0xF;
            long truncatedHash = 0;
            for (int i = 0; i < 4; ++i) {
                truncatedHash <<= 8;
                truncatedHash |= (hash[offset + i] & 0xFF);
            }
            truncatedHash &= 0x7FFFFFFF;
            truncatedHash %= Math.pow(10, DIGITS);

            return (int) truncatedHash;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error al generar codigo TOTP", e);
        }
    }
}
