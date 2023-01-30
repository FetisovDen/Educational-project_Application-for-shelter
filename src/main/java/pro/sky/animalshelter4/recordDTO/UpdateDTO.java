package pro.sky.animalshelter4.recordDTO;

import pro.sky.animalshelter4.model.Command;
import pro.sky.animalshelter4.model.InteractionUnit;

import java.util.Objects;

//Data Transfer Object
public class UpdateDTO {
    private Long idChat;
    private String name;
    private String userName;
    private Command command;
    private String message;
    private String idMedia;
    private String reportText;
    private InteractionUnit interactionUnit;

    public UpdateDTO() {
    }

    public UpdateDTO(Long idChat, String name, String userName, Command command, String message, String idMedia, String reportText, InteractionUnit interactionUnit) {
        this.idChat = idChat;
        this.name = name;
        this.userName = userName;
        this.command = command;
        this.message = message;
        this.idMedia = idMedia;
        this.reportText = reportText;
        this.interactionUnit = interactionUnit;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(String idMedia) {
        this.idMedia = idMedia;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public InteractionUnit getInteractionUnit() {
        return interactionUnit;
    }

    public void setInteractionUnit(InteractionUnit interactionUnit) {
        this.interactionUnit = interactionUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateDTO)) return false;
        UpdateDTO updateDTO = (UpdateDTO) o;
        return Objects.equals(getIdChat(), updateDTO.getIdChat()) && Objects.equals(getName(), updateDTO.getName()) && Objects.equals(getUserName(), updateDTO.getUserName()) && getCommand() == updateDTO.getCommand() && Objects.equals(getMessage(), updateDTO.getMessage()) && Objects.equals(getIdMedia(), updateDTO.getIdMedia()) && Objects.equals(getReportText(), updateDTO.getReportText()) && getInteractionUnit() == updateDTO.getInteractionUnit();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdChat(), getName(), getUserName(), getCommand(), getMessage(), getIdMedia(), getReportText(), getInteractionUnit());
    }

    @Override
    public String toString() {
        return "UpdateDTO{" +
                "idChat=" + idChat +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", command=" + command +
                ", message='" + message + '\'' +
                ", idMedia='" + idMedia + '\'' +
                ", reportText='" + reportText + '\'' +
                ", interactionUnit=" + interactionUnit +
                '}';
    }
}