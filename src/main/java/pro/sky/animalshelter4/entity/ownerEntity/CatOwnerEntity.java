package pro.sky.animalshelter4.entity.ownerEntity;

import pro.sky.animalshelter4.entity.chatEntity.Chat;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "cat_owner")
public class CatOwnerEntity {
    @Id
    private Long Id;
    private String ownerName;
    private String catName;
    private Long dayToEndReporting;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chatOwner;

    public CatOwnerEntity() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long chatId) {
        this.Id = chatId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Long getDayToEndReporting() {
        return dayToEndReporting;
    }

    public void setDayToEndReporting(Long dayToEndReporting) {
        this.dayToEndReporting = dayToEndReporting;
    }

    public Chat getChatOwner() {
        return chatOwner;
    }

    public void setChatOwner(Chat infoFromChatOwner) {
        this.chatOwner = infoFromChatOwner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String nameOwner) {
        this.ownerName = nameOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CatOwnerEntity)) return false;
        CatOwnerEntity that = (CatOwnerEntity) o;
        return getId().equals(that.getId()) && getOwnerName().equals(that.getOwnerName()) && getCatName().equals(that.getCatName()) && getDayToEndReporting().equals(that.getDayToEndReporting()) && getChatOwner().equals(that.getChatOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwnerName(), getCatName(), getDayToEndReporting(), getChatOwner());
    }

    @Override
    public String toString() {
        return "CatOwnerEntity{" +
                "Id=" + Id +
                ", nameOwner='" + ownerName + '\'' +
                ", catName='" + catName + '\'' +
                ", dayReporting=" + dayToEndReporting +
                ", chatOwner=" + chatOwner +
                '}';
    }
}
