package com.example.userserviceecom.services;

import com.example.userserviceecom.models.Token;
import com.example.userserviceecom.models.User;
import com.example.userserviceecom.repositories.TokenRepository;
import com.example.userserviceecom.repositories.UserRepository;
import com.example.userserviceecom.exceptions.InvalidPasswordException;
import com.example.userserviceecom.exceptions.InvalidTokenException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private  final TokenRepository tokenRepository;

    UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public User signUp(String name, String email, String password) {

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(true);

        //save the user object to the DB.
        return userRepository.save(user);
    }
    public Token login(String email, String password) throws InvalidPasswordException {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            //User with given email isn't present in DB.
            return null;
        }
        User user = optionalUser.get();
        if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            throw new InvalidPasswordException("Please enter correct Password");
        }
        Token token = generateToken(user);
        assert token != null;
        return tokenRepository.save(token);
    }
    private Token generateToken(User user) {
        LocalDate currentTime = LocalDate.now(); // current time.
        LocalDate thirtyDaysFromCurrentTime = currentTime.plusDays(30);
        Date expiryDate =   Date.from(thirtyDaysFromCurrentTime.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(expiryDate);


        //Token value is a randomly generated String of 128 characters.
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);

        return token;
    }
    public void logout(String tokenValue) throws InvalidTokenException {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(tokenValue,false);
        if (optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid Token");
        }
        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
        return;
    }
    public User validateToken(String token) throws InvalidTokenException {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(token, false);

        if (optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid token passed.");
        }

        return optionalToken.get().getUser();
    }
}
