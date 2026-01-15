package com.campusform.server.identity.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusform.server.identity.application.dto.response.UserExistsResponse;
import com.campusform.server.identity.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserExistsResponse findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> UserExistsResponse.found(user.getId(), user.getNickname(), user.getEmail(),
                        user.getProfileImageUrl()))
                .orElse(UserExistsResponse.notFound(email));
    }
}
