package pro.sky.animalshelter4.info;

import org.springframework.stereotype.Component;

@Component
public class InfoDogShelterImpl implements InfoShelter {

    @Override
    public String datingRules() {
        return "Рекомендации для первой встречи с собакой: ...";
    }
    @Override
    public String homeEquipForBaby() {
        return "Чтобы маленькому песику было комфортно жить " +
                "в Вашем доме, рекомендуем следующее: ...";
    }

    @Override
    public String homeEquipForAdult() {
        return "Чтобы взрослому песику было комфортно жить " +
                "в Вашем доме, рекомендуем следующее: ...";
    }

    @Override
    public String homeEquipForPetWithDisabilities() {
        return "Чтобы песику с ограниченными " +
                "возможностями было комфортно жить в Вашем доме, рекомендуем следующее: ...";
    }

    public static String cytologistFirst(){return "Кинологи рекомендуют первое время " +
            "при общении с собакой соблюдать правила:...";}
    public static String cytologistContacts(){
        return  "Если у Вас возникли сложности в общении с собакой, " +
                "рекомендуем обратиться к проверенным специалистам-кинологам:...";
    }
}
