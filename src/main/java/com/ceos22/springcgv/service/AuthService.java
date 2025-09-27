package com.ceos22.springcgv.service;


import com.ceos22.springcgv.config.jwt.JWTUtil;
import com.ceos22.springcgv.domain.User;
import com.ceos22.springcgv.dto.LoginRequestDto;
import com.ceos22.springcgv.dto.SignUpRequestDto;
import com.ceos22.springcgv.dto.TokenResponseDto;
import com.ceos22.springcgv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Value("${spring.jwt.access-validity-seconds:900}")
    private long accessValiditySeconds; // 15분

    public void signUp(SignUpRequestDto req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(req.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .nickname(req.getNickname())
                .build();
        userRepository.save(user);
    }

    public TokenResponseDto login(LoginRequestDto req) {
        var user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        long expireMs = accessValiditySeconds * 1000L;

        String token = jwtUtil.createJwt(user.getEmail(), user.getRole().name(), expireMs);
        return new TokenResponseDto(token, accessValiditySeconds);
    }
}