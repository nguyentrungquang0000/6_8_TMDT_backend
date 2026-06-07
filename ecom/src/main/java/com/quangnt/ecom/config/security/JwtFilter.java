package com.quangnt.ecom.config.security;

import com.quangnt.common.tenant.TenantContextHolder;
import com.quangnt.ecom.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            var claims = parse(token);
            String userId = claims.get("userId", String.class);
            String scope = claims.get("scope", String.class);
            Object cinemaIdObj = claims.get("cinemaId");
            Integer cinemaId = cinemaIdObj != null
                    ? Integer.valueOf(cinemaIdObj.toString())
                    : null;

            if ("ADMIN".equals(scope)) {
                TenantContextHolder.setTenantId(cinemaId);
            }

            var auth = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(() -> "ROLE_" + scope)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        return (
                method.equals("POST") && (
                        path.equals("/v1/users/register") ||
                                path.equals("/v1/users/login")
                )
        );
    }

    public Claims parse(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        return Jwts.builder()
                .claim("scope", user.getRole().name())
                .claim("userId", user.getId())
                .claim("cinemaId", user.getCinemaId())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000 * 60 * 60))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}

