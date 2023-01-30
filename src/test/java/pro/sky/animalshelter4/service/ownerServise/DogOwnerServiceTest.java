package pro.sky.animalshelter4.service.ownerServise;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import pro.sky.animalshelter4.Generator;
import pro.sky.animalshelter4.entity.ownerEntity.DogOwnerEntity;
import pro.sky.animalshelter4.recordDTO.DogOwnerRecord;
import pro.sky.animalshelter4.repository.ownerReository.DogOwnerRepository;
import pro.sky.animalshelter4.service.chatTgService.ChatService;
import pro.sky.animalshelter4.service.mapperService.MapperEntityToRecordService;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotSenderService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class DogOwnerServiceTest {
    @Mock
    private TelegramBotSenderService telegramBotSenderService;
    @Mock
    private DogOwnerRepository dogOwnerRepository;
    @Mock
    private ChatService chatService;
    @Mock
    private MapperEntityToRecordService mapperEntityToRecordService;
    @InjectMocks
    private DogOwnerService dogOwnerService;
    private final Generator generator = new Generator();


    @Test
    void createDogOwner() {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        when(mapperEntityToRecordService.toEntity(dogOwnerRecord)).thenReturn(dogOwnerEntity);
        when(mapperEntityToRecordService.toRecord(dogOwnerEntity)).thenReturn(dogOwnerRecord);
        doNothing().when(chatService).makeChatIsOwnerTrue(dogOwnerRecord.getChatId());
        when(dogOwnerRepository.save(dogOwnerEntity)).thenReturn(dogOwnerEntity);
        assertThat(dogOwnerService.createDogOwner(dogOwnerRecord))
                .isNotNull()
                .isEqualTo(dogOwnerRecord);
    }

    @Test
    void readDogOwner() {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(dogOwnerRepository.findById(dogOwnerEntity.getId())).thenReturn(Optional.of(dogOwnerEntity));
        assertThat(dogOwnerService.readDogOwner(dogOwnerEntity.getId()))
                .isNotNull()
                .isEqualTo(dogOwnerEntity);
    }

    @Test
    void updateDogOwner() {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        when(mapperEntityToRecordService.toRecord(dogOwnerEntity)).thenReturn(dogOwnerRecord);
        when(dogOwnerRepository.save(dogOwnerEntity)).thenReturn(dogOwnerEntity);
        when(dogOwnerRepository.findById(dogOwnerRecord.getChatId())).thenReturn(Optional.of(dogOwnerEntity));
        assertThat(dogOwnerService.updateDogOwner(dogOwnerRecord))
                .isNotNull()
                .isEqualTo(dogOwnerRecord);
    }

    @Test
    void deleteDogOwner() {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(dogOwnerRepository.findById(dogOwnerEntity.getId())).thenReturn(Optional.of(dogOwnerEntity));
        dogOwnerService.deleteDogOwner(dogOwnerEntity.getId());
        ArgumentCaptor<DogOwnerEntity> argumentCaptor = ArgumentCaptor.forClass(DogOwnerEntity.class);
        verify(dogOwnerRepository).delete(argumentCaptor.capture());
        DogOwnerEntity actual = argumentCaptor.getValue();
        Assertions.assertThat(actual).isEqualTo(dogOwnerEntity);
    }

    @Test
    void addDays() {
        dogOwnerService.addDays(586L, 14);
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
        verify(dogOwnerRepository).addDaysForDogOwner(argumentCaptor1.capture(), argumentCaptor2.capture());
        Long actual1 = argumentCaptor1.getValue();
        Integer actual2 = argumentCaptor2.getValue();
        assertThat(actual1).isEqualTo(586L);
        assertThat(actual2).isEqualTo(14);
        assertThat(dogOwnerService.addDays(586L, 14)).isEqualTo("14 дней добавлено 586");
        assertThat(dogOwnerService.addDays(586L, 30)).isEqualTo("30 дней добавлено 586");
        assertThat(dogOwnerService.addDays(586L, 31)).isEqualTo("Дней можно добавить, либо 14, либо 30");
        when(dogOwnerRepository.findDayToEndReportingById(586L)).thenReturn(15);
        assertThat(dogOwnerService.addDays(586L, 30)).isEqualTo("Добавить дни можно только после окончания отчетного периода");
    }

    @Test
    void warnAboutPoorReport() {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(dogOwnerRepository.findById(dogOwnerEntity.getId())).thenReturn(Optional.of(dogOwnerEntity));
        dogOwnerService.warnAboutPoorReport(586L);
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        verify(telegramBotSenderService).warnAboutPoorReport(argumentCaptor1.capture());
        Long actual1 = argumentCaptor1.getValue();
        assertThat(actual1).isEqualTo(586L);
    }

    @Test
    void warnAboutRefuse() {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(dogOwnerRepository.findById(dogOwnerEntity.getId())).thenReturn(Optional.of(dogOwnerEntity));
        dogOwnerService.warnAboutRefuse(586L);
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        verify(telegramBotSenderService).warnAboutRefuse(argumentCaptor1.capture());
        Long actual1 = argumentCaptor1.getValue();
        assertThat(actual1).isEqualTo(586L);
    }

    @Test
    void warnAboutApproval() {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(dogOwnerRepository.findById(dogOwnerEntity.getId())).thenReturn(Optional.of(dogOwnerEntity));
        dogOwnerService.warnAboutApproval(586L);
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        verify(telegramBotSenderService).warnAboutApproval(argumentCaptor1.capture());
        Long actual1 = argumentCaptor1.getValue();
        assertThat(actual1).isEqualTo(586L);
    }

    @Test
    void dogOwnerFindById() {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(dogOwnerRepository.findById(dogOwnerEntity.getId())).thenReturn(Optional.of(dogOwnerEntity));
        assertThat(dogOwnerService.dogOwnerFindById(586L)).isEqualTo(true);
        assertThat(dogOwnerService.dogOwnerFindById(5861L)).isEqualTo(false);
    }

    @Test
    void findAllOwnersWithOverdueReportingDay() {
        when(dogOwnerRepository.findAllOverdueReportingDay()).thenReturn(List.of(5L, 8L));
        assertThat(dogOwnerService.findAllOwnersWithOverdueReportingDay()).isEqualTo(List.of(5L, 8L));
    }

    @Test
    void findAllOwnersWithOverdueReportingTwoDay() {
        when(dogOwnerRepository.findAllOwnersWithOverdueReportingTwoDay()).thenReturn(List.of(5L, 8L));
        assertThat(dogOwnerService.findAllOwnersWithOverdueReportingTwoDay()).isEqualTo(List.of(5L, 8L));
    }
}