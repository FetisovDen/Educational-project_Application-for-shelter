package pro.sky.animalshelter4.entity.ownerEntity;

import pro.sky.animalshelter4.entity.chatEntity.Chat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "cat_owner")
public class CatOwnerEntity {
    @Id
    private Long id;
    private String ownerName;
    private String catName;
    private Timestamp startDate;
    private Long dayToEndReporting;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chatOwner;

    public CatOwnerEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp date) {
        this.startDate = date;
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

    public void setChatOwner(Chat chatOwner) {
        this.chatOwner = chatOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CatOwnerEntity)) return false;
        CatOwnerEntity that = (CatOwnerEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOwnerName(), that.getOwnerName()) && Objects.equals(getCatName(), that.getCatName()) && Objects.equals(getStartDate(), that.getStartDate()) && Objects.equals(getDayToEndReporting(), that.getDayToEndReporting()) && Objects.equals(getChatOwner(), that.getChatOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwnerName(), getCatName(), getStartDate(), getDayToEndReporting(), getChatOwner());
    }

    @Override
    public String toString() {
        return "CatOwnerEntity{" +
                "Id=" + id +
                ", ownerName='" + ownerName + '\'' +
                ", catName='" + catName + '\'' +
                ", date=" + startDate +
                ", dayToEndReporting=" + dayToEndReporting +
                ", chatOwner=" + chatOwner +
                '}';
    }
}
