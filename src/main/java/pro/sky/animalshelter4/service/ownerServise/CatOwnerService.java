package pro.sky.animalshelter4.service.ownerServise;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;
import pro.sky.animalshelter4.exception.ReportCatOwnerNotFoundException;
import pro.sky.animalshelter4.recordDTO.CatOwnerRecord;
import pro.sky.animalshelter4.repository.ownerReository.CatOwnerRepository;
import pro.sky.animalshelter4.service.chatTgService.ChatService;
import pro.sky.animalshelter4.service.mapperService.MapperEntityToRecordService;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotSenderService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CatOwnerService {
    private final CatOwnerRepository catOwnerRepository;

    private final ChatService chatService;
    private final MapperEntityToRecordService mapper;
    private final TelegramBotSenderService telegramBotSenderService;

    public CatOwnerService(CatOwnerRepository catOwnerRepository,
                           pro.sky.animalshelter4.service.chatTgService.ChatService chatService, MapperEntityToRecordService mapper,
                           TelegramBotSenderService telegramBotSenderService) {
        this.catOwnerRepository = catOwnerRepository;
        this.chatService = chatService;
        this.mapper = mapper;
        this.telegramBotSenderService = telegramBotSenderService;
    }

    /**
     * Создание владельца
     *
     * @param record DTO(Record) с информацией о владельце
     * @return созданный владелец
     */
    public CatOwnerRecord createCatOwner(CatOwnerRecord record) {
        CatOwnerEntity catOwnerEntity = mapper.toEntity(record);
        catOwnerEntity.setDayToEndReporting(30L);
        catOwnerEntity.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        chatService.makeChatIsOwnerTrue(record.getChatId());
        return mapper.toRecord(catOwnerRepository.save(catOwnerEntity));
    }

    /**
     * Поиск информации о владельце в БД через контроллер по record
     *
     * @param chatId id владельца
     * @return найденый владелец
     */
    public CatOwnerEntity readCatOwner(Long chatId) {
        return catOwnerRepository.findById(chatId).orElseThrow(RuntimeException::new);
    }

    /**
     * Поиск информации о владельце в БД через сервис по entity
     *
     * @param chatId id владельца
     * @return найденый владелец
     */
    public CatOwnerEntity findCatOwnerById(Long chatId) {
        return catOwnerRepository.findById(chatId).orElseThrow(RuntimeException::new);
    }

    /**
     * Изменение информации о владельце в БД
     *
     * @param catOwnerRecord DTO(Record) с информацией о владельце
     * @return обновленные данные владельца
     */
    public CatOwnerRecord updateCatOwner(CatOwnerRecord catOwnerRecord) {
        CatOwnerEntity catOwnerEntity = readCatOwner(catOwnerRecord.getChatId());
        catOwnerEntity.setId(catOwnerEntity.getId());
        catOwnerEntity.setOwnerName(catOwnerRecord.getOwnerName());
        catOwnerEntity.setCatName(catOwnerRecord.getCatName());
        catOwnerEntity.setChatOwner(catOwnerEntity.getChatOwner());
        return mapper.toRecord(catOwnerRepository.save(catOwnerEntity));
    }

    /**
     * Удаление информации о владельце из БД
     *
     * @param chatId id
     */
    public void deleteCatOwner(long chatId) {
        catOwnerRepository.delete(readCatOwner(chatId));
    }

    /**
     * Продление отчетного периода для владельца по Id
     * Добавить дни можно только после окончания отчетного периода
     * Дней можно добавить, либо 14, либо 30
     *
     * @param chatId владельца
     * @param days   количество дней 14 или 30
     */
    public String addDays(long chatId, int days) {
        int DayToEndReporting = catOwnerRepository.findDayToEndReportingById(chatId);
        if (days == 14 || days == 30) {
            if (DayToEndReporting < 1) {
                catOwnerRepository.addDaysForCatOwner(chatId, days);
                telegramBotSenderService.addDaysForOwner(chatId, days);
                return days + " дней добавлено " + chatId;
            } else {
                return "Добавить дни можно только после окончания отчетного периода";
            }
        } else {
            return "Дней можно добавить, либо 14, либо 30";
        }
    }

    /**
     * Поиск в БД владельца по Id
     * Отправка предупреждения о плохом отчете, если он найден
     *
     * @param chatId id владельца
     * @throws ReportCatOwnerNotFoundException если владелец не найден по Id
     */
    public void warnAboutPoorReport(long chatId) {
        catOwnerRepository.findById(chatId).orElseThrow(ReportCatOwnerNotFoundException::new);
        telegramBotSenderService.warnAboutPoorReport(chatId);
    }

    /**
     * Поиск в БД владельца по Id
     * Отправка предупреждения об отказе, если он найден
     *
     * @param chatId id владельца
     * @throws ReportCatOwnerNotFoundException если владелец не найден по Id
     */
    public void warnAboutRefuse(long chatId) {
        catOwnerRepository.findById(chatId).orElseThrow(ReportCatOwnerNotFoundException::new);
        telegramBotSenderService.warnAboutRefuse(chatId);
    }

    /**
     * Поиск в БД владельца по Id
     * Отправка сообщения об успешном прохождении отчетного периода, если он найден
     *
     * @param chatId id владельца
     * @throws ReportCatOwnerNotFoundException если владелец не найден по Id
     */
    public void warnAboutApproval(long chatId) {
        catOwnerRepository.findById(chatId).orElseThrow(ReportCatOwnerNotFoundException::new);
        telegramBotSenderService.warnAboutApproval(chatId);
    }

    /**
     * Поиск в БД владельца по Id
     *
     * @param chatId id владельца
     * @return boolean
     */
    public boolean catOwnerFindById(Long chatId) {
        try {
            catOwnerRepository.findById(chatId).orElseThrow(RuntimeException::new);
            return true;
        } catch (RuntimeException r) {
            return false;
        }
    }

    /**
     * Поиск всех владельцев, которые просрочили отправку отчетов за сегодня
     *
     * @return List<chat_id>
     */
    public List<Long> findAllOwnersWithOverdueReportingDay() {
        return catOwnerRepository.findAllOverdueReportingDay();
    }

    /**
     * Поиск всех владельцев, которые просрочили отправку отчетов за 2 дня
     *
     * @return List<chat_id>
     */
    public List<Long> findAllOwnersWithOverdueReportingTwoDay() {
        return catOwnerRepository.findAllOwnersWithOverdueReportingTwoDay();
    }
}
