package pro.sky.animalshelter4.info.infoShelter;

import org.springframework.stereotype.Component;

@Component
public class InfoCatShelterImpl implements InfoShelter {
    @Override
    public String datingRules() {
        return "Рекомендации для первой встречи с кошкой: ...";
    }

    @Override
    public String homeEquipForBaby() {
        return "Чтобы маленькому котику было комфортно жить " +
                "в Вашем доме, рекомендуем следующее: ...";
    }

    @Override
    public String homeEquipForAdult() {
        return "Чтобы взрослому котику было комфортно жить " +
                "в Вашем доме, рекомендуем следующее: ...";
    }

    @Override
    public String homeEquipForPetWithDisabilities() {
        return "Чтобы котику с ограниченными " +
                "возможностями было комфортно жить в Вашем доме, рекомендуем следующее: ...";
    }

}
