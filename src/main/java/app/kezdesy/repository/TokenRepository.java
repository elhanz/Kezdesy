package app.kezdesy.repository;

import app.kezdesy.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);


    @Query(
            value = "select * from verification_token " +
                    "where user_id = :userId",
            nativeQuery = true
    )
    VerificationToken findByUserId(@Param("userId") Long userId);


}
