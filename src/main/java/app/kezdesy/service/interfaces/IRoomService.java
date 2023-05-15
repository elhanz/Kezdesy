package app.kezdesy.service.interfaces;

import app.kezdesy.entity.Message;
import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.model.EmailRoomId;
import app.kezdesy.model.RoomMessage;
import app.kezdesy.model.RoomRequest;

import java.util.Collection;
import java.util.List;

public interface IRoomService {

    List<Room> getAllRooms();

    List<Room> findRoom(RoomRequest roomRequest);

    List<Room> recommendRooms(String email);

    void createRoom(Room room);
    Message createMessage(User user);
    void deleteMessage(Long id);
    Message getMessageById(Long id);
    boolean kickUser (User user, Long roomId);
    boolean updateMessage (RoomMessage roomMessage);
    boolean joinRoom (EmailRoomId emailRoomId);

    boolean isUserInRoom(String email, Collection<User> users);
}
