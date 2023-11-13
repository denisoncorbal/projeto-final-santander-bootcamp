package org.dgc.expensecontrol.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dgc.expensecontrol.model.RegisterUser;
import org.dgc.expensecontrol.security.jwt.token.Token;
import org.dgc.expensecontrol.security.jwt.token.TokenRepository;
import org.dgc.expensecontrol.security.jwt.token.TokenType;
import org.jose4j.jwk.EcJwkGenerator;
import org.jose4j.jwk.EllipticCurveJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.AudValidator;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.IssValidator;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.lang.JoseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class JwtService {

    private final long ACCESS_TOKEN_EXPIRATION = 30;
    private final long REFRESH_TOKEN_EXPIRATION = 480;
    private final long NOT_VALID_YET = 0;
    private final String ISSUER = "ExpenseControlBackend";
    private final String AUDIENCE = "ExpenseControlFrontend";

    public final static EllipticCurveJsonWebKey ELLIPTIC_CURVE_JSON_WEB_KEY = getKey();

    private static EllipticCurveJsonWebKey getKey() {
        try {
            EllipticCurveJsonWebKey key = EcJwkGenerator.generateJwk(EllipticCurves.P521);
            key.setKeyId("expensecontrol");
            return key;
        } catch (Exception e) {
            return null;
        }
    }

    private TokenRepository tokenRepository;
    private UserDetailsService userDetailsService;

    public JwtService(TokenRepository tokenRepository, UserDetailsService userDetailsService) {
        this.tokenRepository = tokenRepository;
        this.userDetailsService = userDetailsService;
    }

    public String extractUsername(String token) throws InvalidJwtException {

        return extractClaim(token, t -> {
            try {
                return t.getSubject();
            } catch (MalformedClaimException e) {
                return null;
            }
        });
    }

    public <T> T extractClaim(String token, Function<JwtClaims, T> claimsResolver) throws InvalidJwtException {
        final JwtClaims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) throws JoseException {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) throws JoseException {
        String token = buildToken(extraClaims, userDetails, ACCESS_TOKEN_EXPIRATION);
        return token;
    }

    public String generateRefreshToken(
            UserDetails userDetails) throws JoseException {
        String token = buildToken(new HashMap<>(), userDetails, REFRESH_TOKEN_EXPIRATION);
        tokenRepository.save(new Token(null, token, TokenType.REFRESH, false, false, (RegisterUser) userDetails));
        return token;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(ISSUER); // who creates the token and signs it
        claims.setAudience(AUDIENCE); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(expiration); // time when the token will expire (10 minutes from now)
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow(); // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(NOT_VALID_YET); // time before which the token is not yet valid (2 minutes
                                                            // ago)
        claims.setSubject(userDetails.getUsername()); // the subject/principal is whom the token is about
        claims.setStringListClaim("extra", extraClaims.values().stream().map(String::valueOf).toList());

        JsonWebSignature jws = new JsonWebSignature();

        jws.setPayload(claims.toJson());

        jws.setKey(ELLIPTIC_CURVE_JSON_WEB_KEY.getPrivateKey());

        jws.setKeyIdHeaderValue(ELLIPTIC_CURVE_JSON_WEB_KEY.getKeyId());

        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P521_CURVE_AND_SHA512);

        String token = jws.getCompactSerialization();

        return token;
    }

    public boolean isTokenValid(String token, UserDetails userDetails, TokenType type) throws InvalidJwtException {
        final String username = extractUsername(token);
        boolean result = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        if (!result && TokenType.REFRESH.equals(type)) {
            this.setExpiredRefreshToken(token);
        }
        return result;
    }

    public boolean isTokenValid(String token, TokenType type) throws InvalidJwtException {
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(this.extractUsername(token));
        return isTokenValid(token, userDetails, type);
    }

    private boolean isTokenExpired(String token) throws InvalidJwtException {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws InvalidJwtException {
        return new Date(extractClaim(token, t -> {
            try {
                return t.getExpirationTime();
            } catch (MalformedClaimException e) {
                e.printStackTrace();
                return null;
            }
        }).getValueInMillis());
    }

    private JwtClaims extractAllClaims(String token) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setSkipAllDefaultValidators()
                .registerValidator(new AudValidator(Set.of(AUDIENCE), true))
                .registerValidator(new IssValidator(ISSUER, true))
                .setVerificationKey(ELLIPTIC_CURVE_JSON_WEB_KEY.getPublicKey())
                .build();

        JwtContext jwtContext = jwtConsumer.process(token);
        return jwtContext.getJwtClaims();
    }

    public String[] login(String email) throws JoseException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String[] result = { this.generateToken(userDetails), this.generateRefreshToken(userDetails) };
        return result;
    }

    public String[] refresh(String refreshToken) throws JoseException, UsernameNotFoundException, InvalidJwtException {
        String[] result = { "", "" };

        if (this.isTokenValid(refreshToken, TokenType.REFRESH)) {
            /*
             * boolean tokenValidOnDb = tokenRepository.findByToken(refreshToken)
             * .map(t -> !t.isExpired() && !t.isRevoked())
             * .orElse(false);
             */
            boolean tokenValidOnDb = tokenRepository.findByTokenAndExpiredFalseAndRevokedFalse(refreshToken)
                    .map(t -> true)
                    .orElse(false);
            if (tokenValidOnDb) {
                String email = this.extractUsername(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                result[0] = this.generateToken(userDetails);
                result[1] = refreshToken;
            }
        }

        return result;
    }

    private void setExpiredRefreshToken(String token) {
        Token t = tokenRepository.findByToken(token).get();
        t.setExpired(true);
        tokenRepository.save(t);
    }

    @Transactional
    @Scheduled(cron = "0 */10 * * * *")
    public void invalidateDbExpiredTokens() {
        Logger.getAnonymousLogger().log(Level.SEVERE, "Invalidating DB Tokens");
        tokenRepository.saveAll(tokenRepository
                .findAllByExpiredFalseAndRevokedFalse()
                .stream().filter(t -> {
                    try {
                        this.isTokenValid(t.getToken(), TokenType.REFRESH);
                    } catch (InvalidJwtException e) {
                        t.setExpired(true);
                        return true;
                    }
                    return false;
                }).toList());
    }

    @Transactional
    @Scheduled(cron = "0 */10 * * * *")
    public void cleanDbExpiredTokens() {
        Logger.getAnonymousLogger().log(Level.SEVERE, "Cleaning invalid tokens");
        tokenRepository
                .deleteAllByExpiredTrueOrRevokedTrue();
    }
}
