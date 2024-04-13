package org.itmo.eventapp.main.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.security.service.UserDetailsServiceImpl;
import org.itmo.eventapp.main.security.util.JwtTokenUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtTokenUtil jwtTokenUtil;

    private String getTokenFromRequest(HttpServletRequest request) {

        String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        return null;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getTokenFromRequest((request));

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String login = jwtTokenUtil.extractUserLogin(token);

            if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(login);

                if (jwtTokenUtil.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));

                    context.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (ResponseStatusException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), ex.getMessage());
        }
    }
}
