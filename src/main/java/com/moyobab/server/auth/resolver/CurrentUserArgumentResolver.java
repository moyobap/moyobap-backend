package com.moyobab.server.auth.resolver;

import com.moyobab.server.auth.exception.AuthErrorCase;
import com.moyobab.server.global.config.security.jwt.JwtTokenProvider;
import com.moyobab.server.global.exception.ApplicationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && (Long.class.equals(parameter.getParameterType())
                || long.class.equals(parameter.getParameterType()));
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mav,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        // SecurityContextHolder에서 Authentication 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();

            if (principal instanceof Long l) return l;
            if (principal instanceof Integer i) return i.longValue();
            if (principal instanceof String s && s.chars().allMatch(Character::isDigit)) {
                return Long.parseLong(s);
            }

            String name = auth.getName();
            if (name != null && name.chars().allMatch(Character::isDigit)) {
                return Long.parseLong(name);
            }
        }

        // Authorization 헤더에서 토큰 추출
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) throw new ApplicationException(AuthErrorCase.UNAUTHORIZED);

        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long userId = extractUserIdFromToken(token);
            if (userId != null) return userId;
        }

        // accessToken 쿠키에서 토큰 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    Long userId = extractUserIdFromToken(token);
                    if (userId != null) return userId;
                }
            }
        }

        throw new ApplicationException(AuthErrorCase.UNAUTHORIZED);
    }

    private Long extractUserIdFromToken(String token) {
        try {
            return jwtTokenProvider.getUserId(token);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(AuthErrorCase.UNAUTHORIZED);
        }
    }
}
