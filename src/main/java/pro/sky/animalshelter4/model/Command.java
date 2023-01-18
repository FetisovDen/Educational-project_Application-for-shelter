package pro.sky.animalshelter4.model;

import org.springframework.data.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * private final int order;<br>
 * private final String title;<br>
 * private final String nameButton;<br>
 * private final boolean isHide (<b>isHide = true</b> скрытая для всех);<br>
 * private final boolean isPublic (<b>isPublic = true</b> видна для всех(не волонтёры));<br>
 * private final boolean isVolunteer (<b>isVolunteer = true</b> видна для админов(волонтёров));<br>
 * private int <b>stage</b>  (список значений:<br>
 * 0 - команды старта и выбора приюта; <br>
 * 1 - команды развилки на инфо о самом приюте и на инфо о том, как взять животное из приюта, а также на меню отчетов;<br>
 * 2 - команды инфо о самом приюте;<br>
 * 3 - команды о том, как взять животное из приюта;<br>
 * 4 - команды меню отчетов;<br>
 * 5 - команды, которые появляются на разных уровнях, в завсимости от прочих условий. Регулируются в методе Command.regulatingCommands;<br>
 *99 - команды, которые отключены, либо временно недоступны из-за регулировки метомом Command.regulatingCommands ;
 */
public enum Command {

    //stage 0 (старт и выбор приюта)
    START(0, "/start", "START", true, true, true, 0),
    CAT_SHELTER(1, "/cat", "Приют для кошек", false, true, true, 0),
    DOG_SHELTER(2, "/dog", "Приют для собак", false, true, true, 0),
    EMPTY_CALLBACK_DATA_FOR_BUTTON(-1, "...", "", true, true, true, 99),

    //stage 1 ( меню команд развилки на инфо о самом приюте и на инфо о том, как взять животное из приюта)
    INFO(3, "/info", "О нас", false, true, true, 1),
    HOW(4, "/how", "Как взять питомца?", false, true, true, 1),

    //stage 2 ( меню команд инфо о самом приюте)
    SCHEDULE_ADDRESS(5, "/schedule", "График работы и адрес", false, true, true, 2),
    CAR_PASS(6, "/pass", "Оформление пропуска для машины", false, true, true, 2),
    SAFETY_REGULATIONS(7, "/safety", "Тех.безопасность на территории", false, true, true, 2),

    //stage3 (меню инфо о том, как взять животное из приюта)
    DATING_RULES(8, "/dating", "Правила знакомства", false, true, true, 3),
    DOCS_FOR_TAKING(9, "/docs", "Необходимые документы", false, true, true, 3),
    TRANSPORTATION(10, "/transportation", "Правила транспортировки", false, true, true, 3),
    HOME_EQUIP_FOR_BABY(11, "/equipB", "Правила обустройства дома для детеныша", false, true, true, 3),
    HOME_EQUIP_FOR_ADULT(12, "/equipA", "Правила обустройства дома для взрослого питомца", false, true, true, 3),
    HOME_EQUIP_FOR_PET_WITH_DISABILITIES(13, "/equipD", "Правила обустройства дома для питомца c ограниченными возможностями", false, true, true, 3),
    REASONS_FOR_REFUSAL(14, "/refusal", "Причины отказа", false, true, true, 3),
    CYTOLOGIST_FIRST(15, "/cytologistFirst", "Советы кинолога на начальном этапе знакомства с собакой", false, true, true, 3),
    CYTOLOGIST_CONTACTS(16, "/cytologistContacts", "Контакты кинологов", false, true, true, 3),
    //stage 4 (меню команд отчетов)

    //stage 5 (команды которые появляются на разных уровнях, в завсимости от прочих условий. Регулируются в методе Command.regulatingCommands;)
    LEAVE_NUMBER(17, "/phone", "Оставить номер", false, true, true, 5),
    CALL_REQUEST(18, "/CALL_REQUEST", "Позвать волонтера", false, true, false, 5),
    CALL_CLIENT(19, "/CALL_CLIENT", "Связаться с клиентом", false, false, true, 5),
    RETURN(20, "/return", "Обратно", false, true, true, 5);

    private final int order;
    private final String title;
    private final String nameButton;
    private final boolean isHide;
    private final boolean isPublic;
    private final boolean isVolunteer;
    private int stage;

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

    public void setStage(int stage) {
        this.stage = stage;
    }

    public static Command fromString(String text) {
        for (Command command : Command.values()) {
            if (command.getTitle().equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null;
    }

    public static void regulatingCommands(int stage, String pet) {
        if (stage != 0) {
            Command.RETURN.setStage(stage);
            Command.CALL_CLIENT.setStage(stage);
            Command.CALL_REQUEST.setStage(stage);
        }
        if (stage != 0 && stage != 1){
            Command.LEAVE_NUMBER.setStage(stage);
        }
        if (pet != null && pet.equals("cat") ){
            Command.CYTOLOGIST_FIRST.setStage(99);
            Command.CYTOLOGIST_CONTACTS.setStage(99);
        }
        if (pet != null && pet.equals("dog") && stage == 3){
            Command.CYTOLOGIST_FIRST.setStage(stage);
            Command.CYTOLOGIST_CONTACTS.setStage(stage);
        }
    }


    public static Pair<List<String>, List<String>> getPairListsForButtonExcludeHide(boolean forVolunteer, int stage) {
        if (!forVolunteer) {
            return Pair.of(
                    Stream.of(
                                    Command.values()).
                            filter(command -> !command.isHide).
                            filter(command -> command.isPublic).
                            filter(command -> command.stage == stage).
                            sorted(Comparator.comparingInt(Command::getOrder)).
                            map(Command::getNameButton).
                            collect(Collectors.toList()),
                    Stream.of(
                                    Command.values()).
                            filter(command -> !command.isHide).
                            filter(command -> command.isPublic).
                            filter(command -> command.stage == stage).
                            sorted(Comparator.comparingInt(Command::getOrder)).
                            map(Command::getTitle).
                            collect(Collectors.toList()));
        } else {
            return Pair.of(
                    Stream.of(
                                    Command.values()).
                            filter(command -> !command.isHide).
                            filter(command -> command.isVolunteer).
                            filter(command -> command.stage == stage).
                            sorted(Comparator.comparingInt(Command::getOrder)).
                            map(Command::getNameButton).
                            collect(Collectors.toList()),
                    Stream.of(
                                    Command.values()).
                            filter(command -> !command.isHide).
                            filter(command -> command.isVolunteer).
                            filter(command -> command.stage == stage).
                            sorted(Comparator.comparingInt(Command::getOrder)).
                            map(Command::getTitle).
                            collect(Collectors.toList()));
        }
    }
}
