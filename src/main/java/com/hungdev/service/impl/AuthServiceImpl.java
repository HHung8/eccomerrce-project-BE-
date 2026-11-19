package com.hungdev.service.impl;

import com.hungdev.config.JwtProvider;
import com.hungdev.domain.USER_ROLE;
import com.hungdev.modal.Cart;
import com.hungdev.modal.User;
import com.hungdev.modal.VerificationCode;
import com.hungdev.repository.CartRepository;
import com.hungdev.repository.UserRepository;
import com.hungdev.repository.VerificationCodeRepository;
import com.hungdev.request.LoginRequest;
import com.hungdev.response.AuthResponse;
import com.hungdev.response.SignupRequest;
import com.hungdev.service.AuthService;
import com.hungdev.service.EmailService;
import com.hungdev.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;

    @Override
    public void sentLoginOtp(String email) throws Exception {
        String SIGNING_PREFIX = "signin_";
        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new Exception("user not exits with provided email");
            }
        }
        VerificationCode isExits = verificationCodeRepository.findByEmail(email);
        if (isExits != null) {
            verificationCodeRepository.delete(isExits);
        }
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);
        String subject = "zosh bazaar login/sign otp";
        String text = "your login/signup otp is -" + otp;
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Override
    public String createUser(SignupRequest req) {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new BadCredentialsException("wrong Otp");
        }
        User user = userRepository.findByEmail(req.getEmail());
        if (user == null) {
            User createUser = new User();
            createUser.setEmail(req.getEmail());
            createUser.setFullName(req.getFullName());
            createUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createUser.setMobile("89647654323");
            createUser.setPassword(passwordEncoder.encode(req.getOtp()));
            user = userRepository.save(createUser);
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest req) {
        String username = req.getEmail();
        String otp = req.getOtp();
        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login success");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("invalid username or password");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("wrong otp");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
