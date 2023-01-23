package pro.sky.animalshelter4.service.ownerServise;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.entity.ownerEntity.DogOwnerEntity;
import pro.sky.animalshelter4.exception.ReportDogOwnerNotFoundException;
import pro.sky.animalshelter4.recordDTO.DogOwnerRecord;
import pro.sky.animalshelter4.repository.ownerReository.DogOwnerRepository;
import pro.sky.animalshelter4.service.mapperService.MapperEntityToRecordService;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotSenderService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DogOwnerService {
    private final DogOwnerRepository dogOwnerRepository;

    private final pro.sky.animalshelter4.service.chatTgService.ChatService chatService;
    private final MapperEntityToRecordService mapper;
    private final TelegramBotSenderService telegramBotSenderService;

    public DogOwnerService(DogOwnerRepository dogOwnerRepository,
                           pro.sky.animalshelter4.service.chatTgService.ChatService chatService, MapperEntityToRecordService mapper,
                           TelegramBotSenderService telegramBotSenderService) {
        this.dogOwnerRepository = dogOwnerRepository;
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
    public DogOwnerRecord createDogOwner(DogOwnerRecord record) {
        DogOwnerEntity dogOwnerEntity = mapper.toEntity(record);
        dogOwnerEntity.setDayToEndReporting(30L);
        dogOwnerEntity.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        chatService.makeChatIsOwnerTrue(record.getChatId());
        return mapper.toRecord(dogOwnerRepository.save(dogOwnerEntity));
    }

    /**
     * Поиск информации о владельце в БД через контроллер по record
     *
     * @param chatId id владельца
     * @return найденый владелец
     */
    public DogOwnerEntity readDogOwner(Long chatId) {
        return dogOwnerRepository.findById(chatId).orElseThrow(RuntimeException::new);
    }

    /**
     * Поиск информации о владельце в БД через сервис по entity
     *
     * @param chatId id владельца
     * @return найденый владелец
     */
    public DogOwnerEntity findDogOwnerById(Long chatId) {
        return dogOwnerRepository.findById(chatId).orElseThrow(RuntimeException::new);
    }

    /**
     * Изменение информации о владельце в БД
     *
     * @param dogOwnerRecord DTO(Record) с информацией о владельце
     * @return обновленные данные владельца
     */
    public DogOwnerRecord updateDogOwner(DogOwnerRecord dogOwnerRecord) {
        DogOwnerEntity dogOwnerEntity = readDogOwner(dogOwnerRecord.getChatId());
        dogOwnerEntity.setId(dogOwnerEntity.getId());
        dogOwnerEntity.setOwnerName(dogOwnerRecord.getOwnerName());
        dogOwnerEntity.setDogName(dogOwnerRecord.getDogName());
        dogOwnerEntity.setChatOwner(dogOwnerEntity.getChatOwner());
        return mapper.toRecord(dogOwnerRepository.save(dogOwnerEntity));
    }

    /**
     * Удаление информации о владельце из БД
     *
     * @param chatId id
     */
    public void deleteDogOwner(long chatId) {
        dogOwnerRepository.delete(readDogOwner(chatId));
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
        int DayToEndReporting = dogOwnerRepository.findDayToEndReportingById(chatId);
        if (days == 14 || days == 30) {
            if (DayToEndReporting < 1) {
                dogOwnerRepository.addDaysForDogOwner(chatId, days);
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
     * @throws ReportDogOwnerNotFoundException если владелец не найден по Id
     */
    public void warnAboutPoorReport(long chatId) {
        dogOwnerRepository.findById(chatId).orElseThrow(ReportDogOwnerNotFoundException::new);
        telegramBotSenderService.warnAboutPoorReport(chatId);
    }

    /**
     * Поиск в БД владельца по Id
     * Отправка предупреждения об отказе, если он найден
     *
     * @param chatId id владельца
     * @throws ReportDogOwnerNotFoundException если владелец не найден по Id
     */
    public void warnAboutRefuse(long chatId) {
        dogOwnerRepository.findById(chatId).orElseThrow(ReportDogOwnerNotFoundException::new);
        telegramBotSenderService.warnAboutRefuse(chatId);
    }

    /**
     * Поиск в БД владельца по Id
     * Отправка сообщения об успешном прохождении отчетного периода, если он найден
     *
     * @param chatId id владельца
     * @throws ReportDogOwnerNotFoundException если владелец не найден по Id
     */
    public void warnAboutApproval(long chatId) {
        dogOwnerRepository.findById(chatId).orElseThrow(ReportDogOwnerNotFoundException::new);
        telegramBotSenderService.warnAboutApproval(chatId);
    }

    /**
     * Поиск в БД владельца по Id
     *
     * @param chatId id владельца
     * @return boolean
     */
    public boolean dogOwnerFindById(Long chatId) {
        try {
            dogOwnerRepository.findById(chatId).orElseThrow(RuntimeException::new);
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
        return dogOwnerRepository.findAllOverdueReportingDay();
    }

    /**
     * Поиск всех владельцев, которые просрочили отправку отчетов за 2 дня
     *
     * @return List<chat_id>
     */
    public List<Long> findAllOwnersWithOverdueReportingTwoDay() {
        return dogOwnerRepository.findAllOwnersWithOverdueReportingTwoDay();
    }
}
