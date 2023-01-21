package pro.sky.animalshelter4.model;

import org.springframework.stereotype.Component;

import java.util.Objects;

//Data Transfer Object
@Component
public class UpdateDTO {
    private Long idChat;
    private String userName;
    private Command command;
    private String message;
    private String idMedia;
    private InteractionUnit interactionUnit;

    public UpdateDTO() {
    }

    public UpdateDTO(Long idChat, String userName, Command command, String message, String idMedia, InteractionUnit interactionUnit) {
        this.idChat = idChat;
        this.userName = userName;
        this.command = command;
        this.message = message;
        this.idMedia = idMedia;
        this.interactionUnit = interactionUnit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public String getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(String idMedia) {
        this.idMedia = idMedia;
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

    public InteractionUnit getInteractionUnit() {
        return interactionUnit;
    }

    public void setInteractionUnit(InteractionUnit interactionUnit) {
        this.interactionUnit = interactionUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateDTO updateDTO = (UpdateDTO) o;
        return idChat.equals(updateDTO.idChat) && userName.equals(updateDTO.userName) && command == updateDTO.command && Objects.equals(message, updateDTO.message) && Objects.equals(idMedia, updateDTO.idMedia) && interactionUnit == updateDTO.interactionUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idChat, userName, command, message, idMedia, interactionUnit);
    }
}
