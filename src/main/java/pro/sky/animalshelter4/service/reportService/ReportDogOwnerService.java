package pro.sky.animalshelter4.service.reportService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.entity.reportEntity.ReportDogOwnerEntity;
import pro.sky.animalshelter4.exception.ReportDogOwnerNotFoundException;
import pro.sky.animalshelter4.repository.reportRepository.ReportDogOwnerRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@Service
public class ReportDogOwnerService {
    private final Logger logger = LoggerFactory.getLogger(ReportDogOwnerService.class);
    private final ReportDogOwnerRepository reportDogOwnerRepository;

    public ReportDogOwnerService(ReportDogOwnerRepository reportDogOwnerRepository) {
        this.reportDogOwnerRepository = reportDogOwnerRepository;
    }

    /**
     * Создание отчета в БД
     * @param report объект с информацией об отчете
     * @return созданный отчет
     */
    public ReportDogOwnerEntity create(ReportDogOwnerEntity report) {
        logger.debug("was invoking method create");
        ReportDogOwnerEntity ReportDogOwnerEntity = reportDogOwnerRepository.findByFilePath(report.getFilePath()).orElse(null);
        if (ReportDogOwnerEntity == null) {
            return reportDogOwnerRepository.save(report);
        } else {
            ReportDogOwnerEntity.setText(report.getText());
            return reportDogOwnerRepository.save(ReportDogOwnerEntity);
        }
    }

    /**
     * Поиск отчетов по дате
     * @param date дата
     * @return список отчетов по данной дате
     */
    public List<ReportDogOwnerEntity> readAllByDay(String date) {
        logger.debug("was invoking method readAllByDay");
        return reportDogOwnerRepository.findAllByTimeContains(date);
    }

    /**
     * Удаление отчетов по Id владельца
     * @param chatId id
     */
    public void clear(Long chatId) {
        logger.debug("was invoking method clear");
        reportDogOwnerRepository.deleteAllByChatId(chatId);
    }

    /**
     * Вывод изображения по Id отчета
     * @param id отчета
     * @return массив байт изображения
     * @throws ReportDogOwnerNotFoundException - не найден отчет с таким Id
     */
    public byte[] readFromFile(long id) throws IOException {
        logger.debug("was invoking method readFromFile");
        ReportDogOwnerEntity report = reportDogOwnerRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not report with id = " + id);
            throw new ReportDogOwnerNotFoundException();
        });
        Path path = Path.of(report.getFilePath());
        return Files.readAllBytes(path);
    }
}
