package pro.sky.animalshelter4.model;

import org.springframework.data.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Command {
    /**
     * private final int order;<br>
     * private final String title;<br>
     * private final String nameButton;<br>
     * private final boolean isHide (<b>isHide = true</b> скрытая для всех);<br>
     * private final boolean isPublic (<b>isPublic = true</b> видна для всех(не волонтёры));<br>
     * private final boolean isVolunteer (<b>isVolunteer = true</b> видна для админов(волонтёров));<br>
     */

    START(0, "/start", "START", true, true, true,5),
    INFO(1, "/info", "О нас", false, true, true,1),
    HOW(2, "/HOW", "Как взять собаку?", false, true, true,1),
    CALL_REQUEST(3, "/CALL_REQUEST", "Позвать волонтера", false, true, false,5),
    CALL_CLIENT(4, "/CALL_CLIENT", "Связаться с клиентом", false, false, true,5),
    RETURN(5,"/return","Обратно",false,true,true,5),
    EMPTY_CALLBACK_DATA_FOR_BUTTON(-1, "...", "", true, true, true,5);


    private final int order;
    private final String title;
    private final String nameButton;
    private final boolean isHide;
    private final boolean isPublic;
    private final boolean isVolunteer;
    private final int stage;

    Command(int order, String title, String nameButton, boolean isHide, boolean isPublic, boolean isVolunteer, int stage) {
        this.order = order;
        this.title = title;
        this.nameButton = nameButton;
        this.isHide = isHide;
        this.isPublic = isPublic;
        this.isVolunteer = isVolunteer;
        this.stage = stage;
    }

    public int getOrder() {
        return order;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isVolunteer() {
        return isVolunteer;
    }

    public boolean isHide() {
        return isHide;
    }

    public String getTitle() {
        return title;
    }

    public String getNameButton() {
        return nameButton;
    }

    public int getStage() {
        return stage;
    }

    public static Command fromString(String text) {
        for (Command command : Command.values()) {
            if (command.getTitle().equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null;
    }

    public static String getAllTitlesAsListExcludeHide(boolean forVolunteer) {
        StringBuilder sb = new StringBuilder();
        if (!forVolunteer) {
            Stream.of(
                            Command.values()).
                    filter(command -> !command.isHide).
                    filter(command -> command.isPublic).
                    sorted(Comparator.comparingInt(Command::getOrder)).
                    forEach(command -> {
                        sb.append(command.getTitle());
                        sb.append("\n");
                    });
        } else {
            Stream.of(
                            Command.values()).
                    filter(command -> !command.isHide).
                    filter(command -> command.isPublic).
                    sorted(Comparator.comparingInt(Command::getOrder)).
                    forEach(command -> {
                        sb.append(command.getTitle());
                        sb.append("\n");
                    });
        }
        return sb.toString();
    }

    public static List<String> getAllTitlesExcludeHide(boolean forVolunteer) {
        if (!forVolunteer) {
            return Stream.of(
                            Command.values()).
                    filter(command -> !command.isHide).
                    filter(command -> command.isPublic).
                    sorted(Comparator.comparingInt(Command::getOrder)).
                    map(Command::getTitle).
                    collect(Collectors.toList());
        } else {
            return Stream.of(
                            Command.values()).
                    filter(command -> !command.isVolunteer).
                    filter(command -> command.isPublic).
                    sorted(Comparator.comparingInt(Command::getOrder)).
                    map(Command::getTitle).
                    collect(Collectors.toList());
        }
    }

    public static Pair<List<String>, List<String>> getPairListsForButtonExcludeHide(boolean forVolunteer, int stage) {
        if (!forVolunteer) {
            return Pair.of(
                    Stream.of(
                                    Command.values()).
                            filter(command -> !command.isHide).
                            filter(command -> command.isPublic).
                            filter(command -> command.stage > stage).
                            sorted(Comparator.comparingInt(Command::getOrder)).
                            map(Command::getNameButton).
                            collect(Collectors.toList()),
                    Stream.of(
                                    Command.values()).
                            filter(command -> !command.isHide).
                            filter(command -> command.isPublic).
                            filter(command -> command.stage > stage).
                            sorted(Comparator.comparingInt(Command::getOrder)).
                            map(Command::getTitle).
                            collect(Collectors.toList()));
        } else {
            return Pair.of(
                    Stream.of(
                                    Command.values()).
                            filter(command -> !command.isHide).
                            filter(command -> command.isVolunteer).
                            filter(command -> command.stage > stage).
                            sorted(Comparator.comparingInt(Command::getOrder)).
                            map(Command::getNameButton).
                            collect(Collectors.toList()),
                    Stream.of(
                                    Command.values()).
                            filter(command -> !command.isHide).
                            filter(command -> command.isVolunteer).
                            filter(command -> command.stage > stage).
                            sorted(Comparator.comparingInt(Command::getOrder)).
                            map(Command::getTitle).
                            collect(Collectors.toList()));
        }
    }
}
