package pro.sky.animalshelter4.entity.reportEntity;

import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "report_cat_owner")
public class ReportCatOwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private Timestamp time;
    private boolean completedToday;
    private String text;
    private String filePath;
    @ManyToOne
    @JoinColumn(name = "cat_owner_id")
    private CatOwnerEntity catOwner;

    public ReportCatOwnerEntity() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public boolean isCompletedToday() {
        return completedToday;
    }

    public void setCompletedToday(boolean completedToday) {
        this.completedToday = completedToday;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public CatOwnerEntity getCatOwner() {
        return catOwner;
    }

    public void setCatOwner(CatOwnerEntity catOwner) {
        this.catOwner = catOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportCatOwnerEntity)) return false;
        ReportCatOwnerEntity that = (ReportCatOwnerEntity) o;
        return isCompletedToday() == that.isCompletedToday() && Objects.equals(getId(), that.getId()) && Objects.equals(getChatId(), that.getChatId()) && Objects.equals(getTime(), that.getTime()) && Objects.equals(getText(), that.getText()) && Objects.equals(getFilePath(), that.getFilePath()) && Objects.equals(getCatOwner(), that.getCatOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getChatId(), getTime(), isCompletedToday(), getText(), getFilePath(), getCatOwner());
    }

    @Override
    public String toString() {
        return "ReportCatOwnerEntity{" +
                "Id=" + id +
                ", chatId=" + chatId +
                ", time=" + time +
                ", completedToday=" + completedToday +
                ", text='" + text + '\'' +
                ", filePath='" + filePath + '\'' +
                ", catOwner=" + catOwner +
                '}';
    }
}
