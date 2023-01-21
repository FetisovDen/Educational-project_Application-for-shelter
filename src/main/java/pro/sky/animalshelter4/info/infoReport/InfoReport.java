package pro.sky.animalshelter4.info.infoReport;

import org.springframework.stereotype.Component;

@Component
public class InfoReport {
    public final static String reportForm() {
        return " Правила отправки боту отчета:\n\n" +
                "  1.При добавлении фото питомца с компьютера необходимо сжимать файл\n" +
                "  2.Текст отчета необходимо вкладывать в подпись к фото, если этого не сделать, бот не пример ваш отчет.\n" +
                " В ежедневный отчет входит следующая информация:\n\n " +
                        "1.Фото животного.\n" +
                        "2.Рацион животного.\n" +
                        "3.Общее самочувствие и привыкание к новому месту.\n" +
                        "4.Изменение в поведении: отказ от старых привычек, приобретение новых.\n";
    }
}
