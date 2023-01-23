package pro.sky.animalshelter4.entity.ownerEntity;

import pro.sky.animalshelter4.entity.chatEntity.Chat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "dog_owner")
public class DogOwnerEntity {
    @Id
    private Long Id;
    private String ownerName;
    private String dogName;
    private Timestamp startDate;
    private Long dayToEndReporting;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chatOwner;

    public DogOwnerEntity() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
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
        if (!(o instanceof DogOwnerEntity)) return false;
        DogOwnerEntity that = (DogOwnerEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOwnerName(), that.getOwnerName()) && Objects.equals(getDogName(), that.getDogName()) && Objects.equals(getStartDate(), that.getStartDate()) && Objects.equals(getDayToEndReporting(), that.getDayToEndReporting()) && Objects.equals(getChatOwner(), that.getChatOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwnerName(), getDogName(), getStartDate(), getDayToEndReporting(), getChatOwner());
    }

    @Override
    public String toString() {
        return "dogOwnerEntity{" +
                "Id=" + Id +
                ", ownerName='" + ownerName + '\'' +
                ", dogName='" + dogName + '\'' +
                ", date=" + startDate +
                ", dayToEndReporting=" + dayToEndReporting +
                ", chatOwner=" + chatOwner +
                '}';
    }
}
