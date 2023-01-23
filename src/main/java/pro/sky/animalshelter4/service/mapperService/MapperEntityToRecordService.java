package pro.sky.animalshelter4.service.mapperService;



import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;
import pro.sky.animalshelter4.entity.ownerEntity.DogOwnerEntity;
import pro.sky.animalshelter4.exception.ChatNotFoundException;
import pro.sky.animalshelter4.recordDTO.CatOwnerRecord;
import pro.sky.animalshelter4.recordDTO.DogOwnerRecord;
import pro.sky.animalshelter4.repository.chatRepository.ChatRepository;


@Service
public class MapperEntityToRecordService {

    private final ChatRepository chatRepository;

    public MapperEntityToRecordService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }


    public CatOwnerEntity toEntity(CatOwnerRecord record){
        CatOwnerEntity entity = new CatOwnerEntity();
        entity.setId(chatRepository.findById(record.getChatId()).orElseThrow(ChatNotFoundException::new).getId());
        entity.setOwnerName(record.getOwnerName());
        entity.setCatName(record.getCatName());
        entity.setDayToEndReporting(record.getDayToEndReporting());
        entity.setChatOwner(chatRepository.findById(record.getChatId()).orElseThrow(ChatNotFoundException::new));
        return entity;
    }

    public CatOwnerRecord toRecord(CatOwnerEntity entity){
        CatOwnerRecord record = new CatOwnerRecord();
        record.setChatId(entity.getId());
        record.setOwnerName(entity.getOwnerName());
        record.setCatName(entity.getCatName());
        record.setDayToEndReporting(entity.getDayToEndReporting());
        return record;
    }

    public DogOwnerEntity toEntity(DogOwnerRecord record){
        DogOwnerEntity entity = new DogOwnerEntity();
        entity.setId(chatRepository.findById(record.getChatId()).orElseThrow(ChatNotFoundException::new).getId());
        entity.setOwnerName(record.getOwnerName());
        entity.setDogName(record.getDogName());
        entity.setDayToEndReporting(record.getDayToEndReporting());
        entity.setChatOwner(chatRepository.findById(record.getChatId()).orElseThrow(ChatNotFoundException::new));
        return entity;
    }

    public DogOwnerRecord toRecord(DogOwnerEntity entity){
        DogOwnerRecord record = new DogOwnerRecord();
        record.setChatId(entity.getId());
        record.setOwnerName(entity.getOwnerName());
        record.setDogName(entity.getDogName());
        record.setDayToEndReporting(entity.getDayToEndReporting());
        return record;
    }
}

