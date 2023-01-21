package pro.sky.animalshelter4.recordDTO;
import java.util.Objects;


public class CatOwnerRecord {
    private Long chatId;

    private String ownerName;
    private String catName;
    private Long dayToEndReporting;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CatOwnerRecord)) return false;
        CatOwnerRecord that = (CatOwnerRecord) o;
        return Objects.equals(getChatId(), that.getChatId()) && Objects.equals(getOwnerName(), that.getOwnerName()) && Objects.equals(getCatName(), that.getCatName()) && Objects.equals(getDayToEndReporting(), that.getDayToEndReporting());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId(), getOwnerName(), getCatName(), getDayToEndReporting());
    }

    @Override
    public String toString() {
        return "CatOwnerRecord{" +
                "chatId=" + chatId +
                ", ownerName='" + ownerName + '\'' +
                ", catName='" + catName + '\'' +
                ", dayToEndReporting=" + dayToEndReporting +
                '}';
    }
}
