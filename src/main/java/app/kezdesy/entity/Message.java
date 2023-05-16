package app.kezdesy.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Type(type = "text")
    private String content;
    private String sender;
    private Long senderId;

    private boolean isChanged;

    public Message(MessageType type, Timestamp createdAt, Timestamp updatedAt, String content, String sender, Long senderId) {
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
        this.sender = sender;
        this.senderId = senderId;
        this.isChanged = false;
    }

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        PICTURE
    }

}

