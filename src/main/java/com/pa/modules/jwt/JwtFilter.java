package com.pa.modules.jwt;

import com.pa.modules.user.model.Users;
import com.pa.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {


    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Value("${general.user.ignoreToken.enable}")
    private boolean ignoreToken;

    @Autowired
    public JwtFilter(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(new JwtUtils().HEADER_STRING);
// && SecurityContextHolder.getContext().getAuthentication() == null
        try {
            String pathInfo = request.getRequestURI();

            if (pathInfo.indexOf("auth") > 0 || pathInfo.indexOf("verify") > 0) {
                //do nothing
            } else if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                if (jwt.startsWith(new JwtUtils().TOKEN_PREFIX)) {
                    String username = jwtUtils.getUsername(jwt);
                    if (username != null) {
                        Users users = (Users) userService.loadUserByUsername(username);

                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(users, null, users.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("exception", ex);
        }

        filterChain.doFilter(request, response);


    }
}
