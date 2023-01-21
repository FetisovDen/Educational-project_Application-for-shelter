package pro.sky.animalshelter4.service.reportService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.entity.reportEntity.ReportCatOwnerEntity;
import pro.sky.animalshelter4.exception.ReportCatOwnerNotFoundException;
import pro.sky.animalshelter4.repository.reportRepository.ReportCatOwnerRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@Service
public class ReportCatOwnerService {
    private final Logger logger = LoggerFactory.getLogger(ReportCatOwnerService.class);
    private final ReportCatOwnerRepository reportCatOwnerRepository;
    
    public ReportCatOwnerService(ReportCatOwnerRepository reportCatOwnerRepository) {
        this.reportCatOwnerRepository = reportCatOwnerRepository;
    }

    /**
     * Создание отчета в БД
     * @param report объект с информацией об отчете
     * @return созданный отчет
     */
    public ReportCatOwnerEntity create(ReportCatOwnerEntity report) {
        logger.debug("was invoking method create");
        ReportCatOwnerEntity ReportCatOwnerEntity = reportCatOwnerRepository.findByFilePath(report.getFilePath()).orElse(null);
        if (ReportCatOwnerEntity == null) {
            return reportCatOwnerRepository.save(report);
        } else {
            ReportCatOwnerEntity.setText(report.getText());
            return reportCatOwnerRepository.save(ReportCatOwnerEntity);
        }
    }

    /**
     * Поиск отчетов по дате
     * @param date дата
     * @return список отчетов по данной дате
     */
    public List<ReportCatOwnerEntity> readAllByDay(String date) {
        logger.debug("was invoking method readAllByDay");
        return reportCatOwnerRepository.findAllByTimeContains(date);
    }

    /**
     * Удаление отчетов по Id владельца
     * @param chatId id
     */
    public void clear(long chatId) {
        logger.debug("was invoking method clear");
        reportCatOwnerRepository.deleteAllByChatId(chatId);
    }

    /**
     * Вывод изображения по Id отчета
     * @param id отчета
     * @return массив байт изображения
     * @throws ReportCatOwnerNotFoundException - не найден отчет с таким Id
     */
    public byte[] readFromFile(long id) throws IOException {
        logger.debug("was invoking method readFromFile");
        ReportCatOwnerEntity report = reportCatOwnerRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not report with id = " + id);
            throw new ReportCatOwnerNotFoundException();
        });
        Path path = Path.of(report.getFilePath());
        return Files.readAllBytes(path);
    }
}
