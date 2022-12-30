package pro.sky.animalshelter4.component;

import org.springframework.data.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Command {
    START(0, "/start", "START", true, true, false),
    INFO(1, "/info", "About shelter", false, true, false),
    EMPTY_CALLBACK_DATA_FOR_BUTTON(-1, "...", "", true, true, false);


    private final int order;
    private final String title;
    private final String nameButton;
    private final boolean isHide;
    private final boolean isPublic;
    private final boolean isVolunteer;

    Command(int order, String title, String nameButton, boolean isHide, boolean isPublic, boolean isVolunteer) {
        this.order = order;
        this.title = title;
        this.nameButton = nameButton;
        this.isHide = isHide;
        this.isPublic = isPublic;
        this.isVolunteer = isVolunteer;
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

    public static Command fromString(String text) {
        for (Command command : Command.values()) {
            if (command.getTitle().equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null;
    }

    public static String getAllValuesFromNewLineExcludeHideCommand() {
        StringBuilder sb = new StringBuilder();
        Stream.of(
                        Command.values()).
                filter(command -> !command.isHide).
                sorted(Comparator.comparingInt(Command::getOrder)).
                forEach(command -> {
                    sb.append(command.getTitle());
                    sb.append("\n");
                });
        return sb.toString();
    }

    public static List<String> getListValuesExcludeHideCommand() {
        return Stream.of(
                        Command.values()).
                filter(command -> !command.isHide).
                sorted(Comparator.comparingInt(Command::getOrder)).
                map(Command::getTitle).
                collect(Collectors.toList());
    }

    public static Pair<List<String>, List<String>> getListsForButtonExcludeHideCommand() {
        return Pair.of(
                Stream.of(
                                Command.values()).
                        filter(command -> !command.isHide).
                        sorted(Comparator.comparingInt(Command::getOrder)).
                        map(Command::getNameButton).
                        collect(Collectors.toList()),
                Stream.of(
                                Command.values()).
                        filter(command -> !command.isHide).
                        sorted(Comparator.comparingInt(Command::getOrder)).
                        map(Command::getTitle).
                        collect(Collectors.toList()));
    }
}
