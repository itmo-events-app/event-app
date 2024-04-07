package org.itmo.eventapp.main.security.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.repository.UserLoginInfoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserLoginInfoRepository userLoginInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userLoginInfoRepository.getUserLoginInfoByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " + username));
    }
}
