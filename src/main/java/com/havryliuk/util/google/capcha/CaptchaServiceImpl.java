//package com.havryliuk.util.google.capcha;
//
//import com.havryliuk.exceptions.ReCaptchaInvalidException;
//import com.havryliuk.exceptions.ReCaptchaUnavailableException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestOperations;
//
//import javax.servlet.http.HttpServletRequest;
//import java.net.URI;
//import java.util.regex.Pattern;
//
//@Slf4j
//@Service("captchaService")
//public class CaptchaServiceImpl implements CaptchaService {
//
//    @Autowired
//    protected HttpServletRequest request;
//
//    @Autowired
//    private CaptchaSettings captchaSettings;
//
//    @Autowired
//    private RestOperations restTemplate;
//
//    private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
//
//    @Override
//    public void processResponse(String response) {
//        if(!responseSanityCheck(response)) {
//            throw new ReCaptchaInvalidException("Response contains invalid characters");
//        }
//
//        URI verifyUri = URI.create(String.format(
//                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
//                getReCaptchaSecret(), response, getClientIP()));
//
//        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
//
//        if(!googleResponse.isSuccess()) {
//            throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
//        }
//    }
//
//    private boolean responseSanityCheck(String response) {
//        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
//    }
//
//    protected String getClientIP() {
//        final String xfHeader = request.getHeader("X-Forwarded-For");
//        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
//            return request.getRemoteAddr();
//        }
//        return xfHeader.split(",")[0];
//    }
//
//    @Override
//    public String getReCaptchaSecret() {
//        return captchaSettings.getSecret();
//    }
//
//
//}
