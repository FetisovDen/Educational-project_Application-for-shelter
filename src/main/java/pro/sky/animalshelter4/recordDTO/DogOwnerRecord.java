package pro.sky.animalshelter4.recordDTO;
import java.util.Objects;


public class DogOwnerRecord {
    private Long chatId;
    private String ownerName;
    private String dogName;
    private Long dayToEndReporting;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public Long getDayToEndReporting() {
        return dayToEndReporting;
    }

    public void setDayToEndReporting(Long dayToEndReporting) {
        this.dayToEndReporting = dayToEndReporting;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DogOwnerRecord)) return false;
        DogOwnerRecord that = (DogOwnerRecord) o;
        return Objects.equals(getChatId(), that.getChatId()) && Objects.equals(getOwnerName(), that.getOwnerName()) && Objects.equals(getDogName(), that.getDogName()) && Objects.equals(getDayToEndReporting(), that.getDayToEndReporting());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId(), getOwnerName(), getDogName(), getDayToEndReporting());
    }

    @Override
    public String toString() {
        return "DogOwnerRecord{" +
                "chatId=" + chatId +
                ", ownerName='" + ownerName + '\'' +
                ", dogName='" + dogName + '\'' +
                ", dayToEndReporting=" + dayToEndReporting +
                '}';
    }
}
