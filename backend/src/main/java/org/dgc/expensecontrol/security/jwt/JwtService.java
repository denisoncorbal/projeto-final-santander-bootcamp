package org.dgc.expensecontrol.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.lang.JoseException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final long ACCESS_TOKEN_EXPIRATION = 5;
    private final long REFRESH_TOKEN_EXPIRATION = 5;
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

    public JwtService(TokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
    }

    public String extractUsername(String token) throws InvalidJwtException {
        // return extractClaim(token, Claims::getSubject);
        // TODO

        return extractClaim(token, t -> {
            try {
                return t.getSubject();
            } catch (MalformedClaimException e) {
                // TODO Auto-generated catch block
                return null;
            }
        });
    }

    public <T> T extractClaim(String token, Function<JwtClaims, T> claimsResolver) throws InvalidJwtException {
        // final Claims claims = extractAllClaims(token);
        // return claimsResolver.apply(claims);
        // TODO
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
        tokenRepository.save(new Token(null, token, TokenType.BEARER, false, false, (RegisterUser) userDetails));
        return token;
    }

    public String generateRefreshToken(
            UserDetails userDetails) throws JoseException {
        String token = buildToken(new HashMap<>(), userDetails, REFRESH_TOKEN_EXPIRATION);
        tokenRepository.save(new Token(null, token, TokenType.BEARER, false, false, (RegisterUser) userDetails));
        return token;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) throws JoseException {
        // return Jwts
        // .builder()
        // .setClaims(extraClaims)
        // .setSubject(userDetails.getUsername())
        // .setIssuedAt(new Date(System.currentTimeMillis()))
        // .setExpiration(new Date(System.currentTimeMillis() + expiration))
        // .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        // .compact();
        // TODO
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
        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS so we create a JsonWebSignature object.
        JsonWebSignature jws = new JsonWebSignature();

        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());

        // The JWT is signed using the private key
        jws.setKey(ELLIPTIC_CURVE_JSON_WEB_KEY.getPrivateKey());

        // Set the Key ID (kid) header because it's just the polite thing to do.
        // We only have one key in this example but a using a Key ID helps
        // facilitate a smooth key rollover process
        jws.setKeyIdHeaderValue(ELLIPTIC_CURVE_JSON_WEB_KEY.getKeyId());

        // Set the signature algorithm on the JWT/JWS that will integrity protect the
        // claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P521_CURVE_AND_SHA512);

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        // If you wanted to encrypt it, you can simply set this jwt as the payload
        // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
        String token = jws.getCompactSerialization();
        
        return token;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws InvalidJwtException {
        final String username = extractUsername(token);
        System.out.println("Username == UserDetails.username: " + username.equals(userDetails.getUsername()));
        System.out.println("Is token expired? " + isTokenExpired(token));
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) throws InvalidJwtException {
        System.out.println("Expiration time from token: " + extractExpiration(token));
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws InvalidJwtException {
        // return extractClaim(token, Claims::getExpiration);
        // TODO
        return new Date(extractClaim(token, t -> {
            try {
                return t.getExpirationTime();
            } catch (MalformedClaimException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }).getValueInMillis());
    }

    private JwtClaims extractAllClaims(String token) throws InvalidJwtException {
        // return Jwts
        // .parserBuilder()
        // .setSigningKey(getSignInKey())
        // .build()
        // .parseClaimsJws(token)
        // .getBody();
        // TODO
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build();

        JwtContext jwtContext = jwtConsumer.process(token);
        JwtClaims res = jwtContext.getJwtClaims();
        System.out.println(res);
        return res;
    }
}