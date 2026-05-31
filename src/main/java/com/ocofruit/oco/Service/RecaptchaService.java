package com.ocofruit.oco.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RecaptchaService {

    @Value("${recaptcha.secret}")
    private String secretKey;

    private static final String VERIFY_URL =
        "https://www.google.com/recaptcha/api/siteverify";

    public boolean verify(String captchaResponse) {
        if (captchaResponse == null || captchaResponse.isEmpty()) return false;

        RestTemplate restTemplate = new RestTemplate();
        String url = VERIFY_URL + "?secret=" + secretKey + "&response=" + captchaResponse;

        try {
            Map response = restTemplate.postForObject(url, null, Map.class);
            return response != null && Boolean.TRUE.equals(response.get("success"));
        } catch (Exception e) {
            return false;
        }
    }
}