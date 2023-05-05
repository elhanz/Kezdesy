package kezdesy.repositories;

import kezdesy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);

    @Query(
            value = "select * from usr " +
                    "where id = :userId",
            nativeQuery = true
    )
    User myFindById(@Param("userId") Long id);
}
