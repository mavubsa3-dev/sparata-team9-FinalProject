package com.example.demo.domain.portone.webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class PortOneWebhookVerifier {

    private static final String ALGORITHM = "HmacSHA256";

    public boolean verify(String webhookId, String webhookTimestamp, String rawBody,
                          String signatureHeader, String webhookSecret) {
        try {
            String secretBase64 = webhookSecret.startsWith("whsec_")
                    ? webhookSecret.substring("whsec_".length())
                    : webhookSecret;
            byte[] secretBytes = Base64.getDecoder().decode(secretBase64);

            String signedContent = webhookId + "." + webhookTimestamp + "." + rawBody;

            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(secretBytes, ALGORITHM));
            byte[] computed = mac.doFinal(signedContent.getBytes(StandardCharsets.UTF_8));
            String computedBase64 = Base64.getEncoder().encodeToString(computed);

            for (String part : signatureHeader.split(" ")) {
                String[] versioned = part.split(",", 2);
                if (versioned.length == 2 && versioned[1].equals(computedBase64)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}