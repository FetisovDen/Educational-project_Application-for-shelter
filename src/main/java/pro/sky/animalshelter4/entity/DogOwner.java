package pro.sky.animalshelter4.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity(name = "dog_owner")
public class DogOwner {

    @Id
    private Long id;
    private String nameDog;
    @ManyToOne
    @JoinColumn(name = "id_chat_owner")
    private Chat chatOwner;

    public DogOwner() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameDog() {
        return nameDog;
    }

    public void setNameDog(String nameDog) {
        this.nameDog = nameDog;
    }

    public Chat getChatOwner() {
        return chatOwner;
    }

    public void setChatOwner(Chat chatOwner) {
        this.chatOwner = chatOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DogOwner)) return false;
        DogOwner dogOwner = (DogOwner) o;
        return getId().equals(dogOwner.getId()) && getNameDog().equals(dogOwner.getNameDog()) && getChatOwner().equals(dogOwner.getChatOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNameDog(), getChatOwner());
    }

    @Override
    public String toString() {
        return "DogOwner{" +
                "id=" + id +
                ", nameDog='" + nameDog + '\'' +
                ", chatOwner=" + chatOwner +
                '}';
    }
}
