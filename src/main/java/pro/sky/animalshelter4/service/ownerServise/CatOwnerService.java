package pro.sky.animalshelter4.service.ownerServise;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;
import pro.sky.animalshelter4.recordDTO.CatOwnerRecord;
import pro.sky.animalshelter4.repository.ownerReository.CatOwnerRepository;
import pro.sky.animalshelter4.service.mapperService.MapperEntityToRecordService;

import java.sql.Timestamp;

@Service
public class CatOwnerService {
    private final CatOwnerRepository catOwnerRepository;
    private final TelegramBot telegramBot;
    private final MapperEntityToRecordService mapper;

    public CatOwnerService(CatOwnerRepository catOwnerRepository, TelegramBot telegramBot, MapperEntityToRecordService mapper) {
        this.catOwnerRepository = catOwnerRepository;
        this.telegramBot = telegramBot;
        this.mapper = mapper;
    }

    /**
     * Создание владельца
     * @param record DTO(Record) с информацией о владельце
     * @return созданный владелец
     */
    public CatOwnerRecord createCatOwner(CatOwnerRecord record) {
        CatOwnerEntity catOwnerEntity = mapper.toEntity(record);
        catOwnerEntity.setDayToEndReporting(30L);
        return mapper.toRecord(catOwnerRepository.save(catOwnerEntity));
    }

    /**
     * Поиск информации о владельце в БД через контроллер по record
     * @param chatId id владельца
     * @return найденый владелец
     */
    public CatOwnerRecord readCatOwner(Long chatId) {
        return mapper.toRecord(catOwnerRepository.findById(chatId).orElseThrow(RuntimeException::new));
    }
    /**
     * Поиск информации о владельце в БД через сервис по entity
     * @param chatId id владельца
     * @return найденый владелец
     */
    public CatOwnerEntity findCatOwnerById(Long chatId) {
        return catOwnerRepository.findById(chatId).orElseThrow(RuntimeException::new);
    }

    /**
     * Изменение информации о владельце в БД
     * @param catOwnerRecord DTO(Record) с информацией о владельце
     * @return обновленные данные владельца
     */
    public CatOwnerRecord updateCatOwner(CatOwnerRecord catOwnerRecord) {
        CatOwnerEntity catOwnerEntity = mapper.toEntity(readCatOwner(catOwnerRecord.getChatId()));
        catOwnerEntity.setId(catOwnerEntity.getId());
        catOwnerEntity.setOwnerName(catOwnerRecord.getOwnerName());
        catOwnerEntity.setCatName(catOwnerRecord.getCatName());
        catOwnerEntity.setChatOwner(catOwnerEntity.getChatOwner());
        return mapper.toRecord(catOwnerRepository.save(catOwnerEntity));
    }

    /**
     * Удаление информации о владельце из БД
     * @param chatId id
     */
    public void deleteCatOwner(long chatId) {
        catOwnerRepository.delete(mapper.toEntity(readCatOwner(chatId)));
    }

    /**
     * Продление отчетного периода для владельца по Id
     * @param chatId владельца
     * @param days   количество дней
     */
    public void addDays(long chatId, int days) {
//        catOwnerRepository.addDays(chatId, days);
        SendMessage msg = new SendMessage(chatId, "Вам был продлен испытательный срок на " + days + " дней");
        telegramBot.execute(msg);
    }
    /**
     * Поиск в БД владельца по Id
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

}
