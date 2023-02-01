package pro.sky.animalshelter4.service.mapperService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import pro.sky.animalshelter4.Generator;
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;
import pro.sky.animalshelter4.entity.ownerEntity.DogOwnerEntity;
import pro.sky.animalshelter4.recordDTO.CatOwnerRecord;
import pro.sky.animalshelter4.recordDTO.DogOwnerRecord;
import pro.sky.animalshelter4.repository.chatRepository.ChatRepository;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class MapperEntityToRecordServiceTest {
    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private MapperEntityToRecordService mapperEntityToRecordService;
    private final Generator generator = new Generator();

    @Test
    void toEntity() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        when(chatRepository.findById(catOwnerRecord.getChatId())).thenReturn(Optional.ofNullable(catOwnerEntity.getChatOwner()));
        Assertions.assertThat(mapperEntityToRecordService.toEntity(catOwnerRecord)).isEqualTo(catOwnerEntity);
    }

    @Test
    void toRecord() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        Assertions.assertThat(mapperEntityToRecordService.toRecord(catOwnerEntity)).isEqualTo(catOwnerRecord);
    }

    @Test
    void testToEntity() {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        when(chatRepository.findById(dogOwnerRecord.getChatId())).thenReturn(Optional.ofNullable(dogOwnerEntity.getChatOwner()));
        Assertions.assertThat(mapperEntityToRecordService.toEntity(dogOwnerRecord)).isEqualTo(dogOwnerEntity);
    }

    @Test
    void testToRecord() {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        Assertions.assertThat(mapperEntityToRecordService.toRecord(dogOwnerEntity)).isEqualTo(dogOwnerRecord);
    }
}