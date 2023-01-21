package pro.sky.animalshelter4.entity.chatEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
@Entity(name = "chat")
public class Chat {
    @Id
    private Long id;

    private String name;

    private String telegramName;

    private String phone;

    boolean isVolunteer;
    boolean isOwner;

    public Chat() {
    }

    public boolean isVolunteer() {
        return isVolunteer;
    }

    public void setVolunteer(boolean volunteer) {
        isVolunteer = volunteer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelegramName() {
        return telegramName;
    }

    public void setTelegramName(String phone) {
        this.telegramName = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String address) {
        this.phone = address;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}

