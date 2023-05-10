//package app.kezdesy.entity;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.Collection;
//@Data
//@NoArgsConstructor
//@Entity
//@Table(name = "chat")
//public class Chat {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(name = "name")
//    private String name;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    private Collection<Message> messages = new ArrayList<>();
//
//
//
//
//    public Collection<Message> getMessages() {
//        return messages;
//    }
//
//    public void setMessages(Collection<Message> messages) {
//        this.messages = messages;
//    }
//
//}
