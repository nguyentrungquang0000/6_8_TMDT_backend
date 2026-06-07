package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.config.security.JwtFilter;
import com.quangnt.ecom.dto.LoginRequest;
import com.quangnt.ecom.dto.LoginResponse;
import com.quangnt.ecom.dto.Role;
import com.quangnt.ecom.dto.UserCreateRequest;
import com.quangnt.ecom.dto.UserResponse;
import com.quangnt.ecom.dto.UserUpdateRequest;
import com.quangnt.ecom.entity.Media;
import com.quangnt.ecom.entity.User;
import com.quangnt.ecom.repository.MediaRepository;
import com.quangnt.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final PasswordEncoder passwordEncoder;
    private final MediaService mediaService;
    private final JwtFilter jwtFilter;
    public UserResponse create(UserCreateRequest request, Role role) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException(ResponseCode.EMAIL_EXISTED);
        }
        User user = User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .fullName(request.getFullName())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    @Transactional
    public UserResponse update(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Media avatar = null;
        if (request.getAvatarMediaId() != null) {
            avatar = mediaRepository.findById(request.getAvatarMediaId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.MEDIA_NOTFOUND));
        }

        Media media = mediaService.getMediaById(request.getAvatarMediaId());
        user.setAvatarId(request.getAvatarMediaId());

        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        User saved = userRepository.save(user);

        media.setStatus(true);

        return mapToResponse(saved);
    }

    public void delete(List<String> ids) {
        List<User> userList = userRepository.findAllById(ids);
        for (User user : userList) {
            user.setLock(true);
            userRepository.save(user);
        }
    }

    public UserResponse getOne(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(user);
    }

    public Page<UserResponse> search(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::mapToResponse);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .fullName(user.getFullName())
                .role(user.getRole())
                .avatarUrl(mediaService.getPreviewUrl(user.getAvatarId()))
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .isLock(user.isLock())
                .build();
    }

    public LoginResponse login(LoginRequest request) {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BusinessException(ResponseCode.LOGIN_FAILED));
            if (user.isLock()) {
                throw new BusinessException(ResponseCode.ACCOUNT_LOCKED);
            }
            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                throw new BusinessException(ResponseCode.LOGIN_FAILED);
            }
            return LoginResponse.builder()
                    .accessToken(jwtFilter.generateToken(user))
                    .refreshToken("fake-refresh-token")
                    .role(user.getRole().name())
                    .fullName(user.getFullName())
                    .avatar("https://www.vecteezy.com/vector-art/9292244-default-avatar-icon-vector-of-social-media-user")
                    .build();
    }

    public UserResponse getProfile() {
        String userId = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        if (userId == null) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(user);
    }
}
