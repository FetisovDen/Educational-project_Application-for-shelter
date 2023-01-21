package pro.sky.animalshelter4.info.infoShelter;

import org.springframework.stereotype.Component;

@Component
public interface InfoShelter {
    default String infoAboutShelter() {
        return "Мы-негосударственный приют из Астаны.\n\n" +
                "Приют был открыт 15 октября 2013 года неравнодушными девушками.\n" +
                "Существует исключительно за счет добровольных пожертвований людей, любящих животных.\n" +
                "В данный момент мы не имеем своего собственного помещения под приют. Арендуем небольшой домик в частном секторе.\n" +
                "Если вы желаете помочь приюту, то нам всегда необходима помощь.\n\n";
    }

    default String infoAboutScheduleAndAddress() {
        return "Время работы:... \n"
                + "Приют для собак расположен по адресу: .....";
    }

    default String infoAboutCarPass() {
        return "Для оформления пропуска на машину Вам потребуется: ...";
    }

    default String infoAboutSafetyRegulations() {
        return "На территории приюта для собак рекомендуем соблюдать" +
                "следующие правила безопасности: ...";
    }

    default String leaveNumber() {
        return "Чтобы мы могли связаться с вами, просим вас отсавить свой номер в формате: +7**********.";
    }
    String datingRules();
    default String docsForTaking() {
        return "Для усыновления питомца Вам " +
                "потребуются следующие документы: ...";
    }

    default String transportation() {
        return "При перевозке питомца рекомендовано соблюдать следующие правила:";
    }
    String homeEquipForBaby();
    String homeEquipForAdult();
    String homeEquipForPetWithDisabilities();

    default String reasonsForRefusal() {
        return "Причины, по которым Вам могут отказать " +
                "в усыновлении питомца:...";
    }

}
