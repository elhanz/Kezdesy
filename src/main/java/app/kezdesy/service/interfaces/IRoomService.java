package app.kezdesy.service.interfaces;

import app.kezdesy.entity.Message;
import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.model.EmailRoomIdRequest;
import app.kezdesy.model.RoomMessageRequest;
import app.kezdesy.model.RoomRequest;
import app.kezdesy.model.UpdateRoomRequest;

import java.util.Collection;
import java.util.List;

public interface IRoomService {

    List<Room> getAllRooms();

    List<Room> findRoom(RoomRequest roomRequest);

    List<Room> recommendRooms(String email);

    void createRoom(Room room);

    void updateRoom(UpdateRoomRequest updateRoomRequest);

    Message createMessage(User user);

    void deleteMessage(Long id);

    void deleteRoom(Long id);

    Message getMessageById(Long id);

    boolean kickUser(User user, Long roomId);

    boolean updateMessage(RoomMessageRequest roomMessageRequest);

    boolean joinRoom(EmailRoomIdRequest emailRoomIdRequest);

    boolean isUserInRoom(String email, Collection<User> users);
}
