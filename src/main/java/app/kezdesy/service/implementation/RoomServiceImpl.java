package app.kezdesy.service.implementation;

import app.kezdesy.entity.Interest;
import app.kezdesy.entity.Message;
import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.model.EmailRoomIdRequest;
import app.kezdesy.model.RoomMessageRequest;
import app.kezdesy.model.RoomRequest;
import app.kezdesy.model.UpdateRoomRequest;
import app.kezdesy.repository.MessageRepository;
import app.kezdesy.repository.RoomRepository;
import app.kezdesy.repository.UserRepository;
import app.kezdesy.service.interfaces.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomServiceImpl implements IRoomService {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;


    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> findRoom(RoomRequest roomRequest) {

        User user = userRepository.findByEmail(roomRequest.getEmail());
        if (Objects.equals(roomRequest.getCity(), "")) {
            roomRequest.setCity(user.getCity());
        }

        if (roomRequest.getInterests().size() == 0) {
            List<Room> rooms = roomRepository.findByCityContainsAndHeaderContainsAndMinAgeLimitGreaterThanEqualAndMaxAgeLimitLessThanEqualAndMaxMembersLessThanEqual(
                    roomRequest.getCity(), roomRequest.getHeader(), roomRequest.getMinAgeLimit(), roomRequest.getMaxAgeLimit(),
                    roomRequest.getMaxMembers());
            if (roomRequest.getMinAgeLimit() == 0) {
                rooms.removeIf(room -> room.getMinAgeLimit() > user.getAge());
            }
            if (roomRequest.getMaxAgeLimit() == 120) {
                rooms.removeIf(room -> room.getMaxAgeLimit() < user.getAge());
            }
            return rooms;
        }

        List<Room> rooms2 = roomRepository.findByInterestsMy(
                setToStringConverter(roomRequest.getInterests()), roomRequest.getInterests().size(),
                roomRequest.getCity(), roomRequest.getHeader(), roomRequest.getMinAgeLimit(), roomRequest.getMaxAgeLimit(),
                roomRequest.getMaxMembers()
        );
        if (roomRequest.getMinAgeLimit() == 0) {
            rooms2.removeIf(room -> room.getMinAgeLimit() > user.getAge());
        }
        if (roomRequest.getMaxAgeLimit() == 120) {
            rooms2.removeIf(room -> room.getMaxAgeLimit() < user.getAge());
        }
        return rooms2;
    }

    @Override
    public List<Room> recommendRooms(String email) {

        User user = userRepository.findByEmail(email);
        List<Room> rooms = roomRepository.findByCityContainsAndHeaderContainsAndMinAgeLimitGreaterThanEqualAndMaxAgeLimitLessThanEqualAndMaxMembersLessThanEqual(
                user.getCity(), "", 18, 110, 20);
        rooms.removeIf(room -> room.getMinAgeLimit() > user.getAge());
        rooms.removeIf(room -> room.getMaxAgeLimit() < user.getAge());
        rooms.removeIf(room -> room.getInterests().stream().noneMatch(user.getInterests()::contains));
        return rooms;

    }



    @Override
    public void createRoom(Room room) {
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(UpdateRoomRequest updateRoomRequest) {
        Room existRoom = roomRepository.findRoomById(updateRoomRequest.getId());

        existRoom.setCity(updateRoomRequest.getCity());
        existRoom.setMaxMembers(updateRoomRequest.getMaxMembers());
        existRoom.setMinAgeLimit(updateRoomRequest.getMinAgeLimit());
        existRoom.setMaxAgeLimit(updateRoomRequest.getMaxAgeLimit());
        existRoom.setDescription(updateRoomRequest.getDescription());
        existRoom.setHeader(updateRoomRequest.getHeader());
        existRoom.setInterests(updateRoomRequest.getInterests());
        existRoom.setOwner(updateRoomRequest.getOwner());
        roomRepository.save(existRoom);
    }

    @Override
    public Message createMessage(User user) {
        Message messageJoined = new Message();
        messageJoined.setType(Message.MessageType.JOIN);
        messageJoined.setSender(user.getFirst_name() + ' ' + user.getLast_name());
        messageJoined.setSenderId(user.getId());
        messageRepository.save(messageJoined);

        return messageJoined;
    }

    @Override
    public void deleteMessage(Long id) {
        roomRepository.deleteMessage(id);
        messageRepository.deleteById(id);
    }

    @Override
    public void deleteRoom(Long id) {
    roomRepository.deleteById(id);
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository.getById(id);
    }

    @Override
    public boolean kickUser(User user, Long roomId) {
        Room room = roomRepository.findRoomById(roomId);

        if (room != null && roomRepository.existsByRoomIdAndUserId(room.getId(), user.getId())) {

            Message messageJoined = new Message();
            messageJoined.setType(Message.MessageType.LEAVE);
            messageJoined.setSender(user.getFirst_name() + ' ' + user.getLast_name());
            messageJoined.setSenderId(user.getId());
            room.getMessages().add(messageJoined);
            messageRepository.save(messageJoined);
            roomRepository.kickUser(room.getId(), user.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateMessage(RoomMessageRequest roomMessageRequest) {

        Message message = messageRepository.getById(roomMessageRequest.getId());
        if (message != null) {
            message.setContent(roomMessageRequest.getContent());
            message.setChanged(true);
            messageRepository.save(message);
            return true;
        } else return false;

    }

    @Override
    public boolean joinRoom(EmailRoomIdRequest emailRoomIdRequest) {
        Room room = roomRepository.findById(emailRoomIdRequest.getRoomId()).orElse(null);

        if (!isUserInRoom(emailRoomIdRequest.getEmail(), room.getUsers())) {

            User user = userRepository.findByEmail(emailRoomIdRequest.getEmail());

            room.getUsers().add(user);
            Message messageJoined = new Message();
            messageJoined.setType(Message.MessageType.JOIN);
            messageJoined.setSender(user.getFirst_name() + ' ' + user.getLast_name());
            messageJoined.setSenderId(user.getId());
            room.getMessages().add(messageJoined);
            messageRepository.save(messageJoined);

            roomRepository.save(room);
            return true;
        }

        return false;
    }


    public String[] setToStringConverter(Set<Interest> interests) {
        Set<String> strings = new HashSet<>();
        for (Interest i : interests) {
            strings.add(i.name());
        }

        String[] mArray = new String[strings.size()];
        strings.toArray(mArray);
        return mArray;
    }

    public boolean isUserInRoom(String email, Collection<User> users) {
        for (User user : users) {
            if (Objects.equals(user.getEmail(), email)) {
                return true;
            }
        }
        return false;
    }


}
