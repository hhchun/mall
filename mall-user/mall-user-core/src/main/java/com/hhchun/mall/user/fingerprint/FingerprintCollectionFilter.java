package com.hhchun.mall.user.fingerprint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 指纹收集过滤器
 */
@Slf4j
public class FingerprintCollectionFilter implements Filter {
    public static final int FILTER_REGISTRATION_ORDER = Integer.MIN_VALUE;

    public static final String FINGERPRINT_HEADER_NAME = "fingerprint";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String fingerprintStr = ((HttpServletRequest) request).getHeader(FINGERPRINT_HEADER_NAME);
        if (StringUtils.hasLength(fingerprintStr)) {
            try {
                Fingerprint fingerprint = objectMapper.readValue(fingerprintStr, new TypeReference<Fingerprint>() {
                });
                FingerprintHolder.setFingerprint(fingerprint);
            } catch (JsonProcessingException e) {
                log.info("deserialize Fingerprint instance error!");
            }
        }
        chain.doFilter(request, response);
        FingerprintHolder.clearFingerprint();
    }
}
