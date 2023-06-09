package app.kezdesy.repository;

import app.kezdesy.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByCityContainsAndHeaderContainsAndMinAgeLimitGreaterThanEqualAndMaxAgeLimitLessThanEqualAndMaxMembersLessThanEqual
            (String city, String hc, int minage, int maxage, int maxmembers);

    @Query(
            value = "select * from room " +
                    "where city like %:city% " +
                    "and header like %:header% " +
                    "and min_age >= :minLimit " +
                    "and max_age <= :maxLimit " +
                    "and max_members <= :members",
            nativeQuery = true
    )
    List<Room> myFind(@Param("city") String city, @Param("header") String header, @Param("minLimit") int min,
                      @Param("maxLimit") int max, @Param("members") int members);

    @Query(
            value = "select * from (" +
                    "select * from room " +
                    "where city like %:city% " +
                    "and header like %:header% " +
                    "and min_age >= :minLimit " +
                    "and max_age <= :maxLimit " +
                    "and max_members <= :members" +
                    ") as a " +
                    "where id in (" +
                    "select room_id from room_interests " +
                    "where interest in (:interests)" +
                    "group by room_id " +
                    "having count(distinct interest) = :size" +
                    ")",
            nativeQuery = true)
    List<Room> findByInterestsMy(@Param("interests") String[] is, @Param("size") int size,
                                 @Param("city") String city, @Param("header") String header, @Param("minLimit") int min,
                                 @Param("maxLimit") int max, @Param("members") int members);

    @Query(
            value = "select * from room " +
                    "join room_users on room.id = room_users.rooms_id " +
                    "where users_id = :id",
            nativeQuery = true)
    List<Room> myRooms(@Param("id") Long id);


    @Query(
            value = "select * from room " +
                    "where id = :id",
            nativeQuery = true)
    Room findRoomById(@Param("id") Long id);

    @Query(
            value = "select exists(select 1 from room_users where rooms_id = :roomId and  users_id = :userId )",
            nativeQuery = true)
    boolean existsByRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);


    @Transactional
    @Modifying
    @Query(
            value = "delete from room_users " +
                    "where rooms_id = :roomId and  users_id = :userId",
            nativeQuery = true)
    void kickUser(@Param("roomId") Long roomId, @Param("userId") Long userId);


    @Transactional
    @Modifying
    @Query(
            value = "delete from room_messages " +
                    "where messages_id = :id ",
            nativeQuery = true)
    void deleteMessage(@Param("id") Long id);

}

