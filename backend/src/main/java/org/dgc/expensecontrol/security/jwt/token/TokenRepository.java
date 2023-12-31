package org.dgc.expensecontrol.security.jwt.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Long>{
    @Query(value = """
      select t from Token t inner join RegisterUser u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(Integer id);

  Optional<Token> findByToken(String token);

  Optional<Token> findByTokenAndExpiredFalseAndRevokedFalse(String token);

  List<Token> findAllByExpiredFalseAndRevokedFalse();

  void deleteAllByExpiredTrueOrRevokedTrue();
}