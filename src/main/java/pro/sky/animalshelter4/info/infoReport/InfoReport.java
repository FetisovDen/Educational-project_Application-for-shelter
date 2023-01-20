package pro.sky.animalshelter4.info.infoReport;

import org.springframework.stereotype.Component;

@Component
public class InfoReport {
    public final String reportForm() {
        return "    В ежедневный отчет входит следующая информация:\n\n " +
                        "1.Фото животного.\n" +
                        "2.Рацион животного.\n" +
                        "3.Общее самочувствие и привыкание к новому месту.\n" +
                        "4.Изменение в поведении: отказ от старых привычек, приобретение новых.\n";
    }
}
