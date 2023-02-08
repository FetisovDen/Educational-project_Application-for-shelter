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
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;
import pro.sky.animalshelter4.recordDTO.CatOwnerRecord;
import pro.sky.animalshelter4.repository.ownerReository.CatOwnerRepository;
import pro.sky.animalshelter4.service.chatTgService.ChatService;
import pro.sky.animalshelter4.service.mapperService.MapperEntityToRecordService;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotSenderService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class CatOwnerServiceTest {
    @Mock
    private TelegramBotSenderService telegramBotSenderService;
    @Mock
    private CatOwnerRepository catOwnerRepository;
    @Mock
    private ChatService chatService;
    @Mock
    private MapperEntityToRecordService mapperEntityToRecordService;
    @InjectMocks
    private CatOwnerService catOwnerService;
    private final Generator generator = new Generator();


    @Test
    void createCatOwner() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        when(mapperEntityToRecordService.toEntity(catOwnerRecord)).thenReturn(catOwnerEntity);
        when(mapperEntityToRecordService.toRecord(catOwnerEntity)).thenReturn(catOwnerRecord);
        doNothing().when(chatService).makeChatIsOwnerTrue(catOwnerRecord.getChatId());
        when(catOwnerRepository.save(catOwnerEntity)).thenReturn(catOwnerEntity);
        assertThat(catOwnerService.createCatOwner(catOwnerRecord))
                .isNotNull()
                .isEqualTo(catOwnerRecord);
    }

    @Test
    void readCatOwner() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(catOwnerRepository.findById(catOwnerEntity.getId())).thenReturn(Optional.of(catOwnerEntity));
        assertThat(catOwnerService.readCatOwner(catOwnerEntity.getId()))
                .isNotNull()
                .isEqualTo(catOwnerEntity);
    }

    @Test
    void updateCatOwner() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        when(mapperEntityToRecordService.toRecord(catOwnerEntity)).thenReturn(catOwnerRecord);
        when(catOwnerRepository.save(catOwnerEntity)).thenReturn(catOwnerEntity);
        when(catOwnerRepository.findById(catOwnerRecord.getChatId())).thenReturn(Optional.of(catOwnerEntity));
        assertThat(catOwnerService.updateCatOwner(catOwnerRecord))
                .isNotNull()
                .isEqualTo(catOwnerRecord);
    }

    @Test
    void deleteCatOwner() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(catOwnerRepository.findById(catOwnerEntity.getId())).thenReturn(Optional.of(catOwnerEntity));
        catOwnerService.deleteCatOwner(catOwnerEntity.getId());
        ArgumentCaptor<CatOwnerEntity> argumentCaptor = ArgumentCaptor.forClass(CatOwnerEntity.class);
        verify(catOwnerRepository).delete(argumentCaptor.capture());
        CatOwnerEntity actual = argumentCaptor.getValue();
        Assertions.assertThat(actual).isEqualTo(catOwnerEntity);
    }

    @Test
    void addDays() {
        catOwnerService.addDays(586L, 14);
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
        verify(catOwnerRepository).addDaysForCatOwner(argumentCaptor1.capture(), argumentCaptor2.capture());
        Long actual1 = argumentCaptor1.getValue();
        Integer actual2 = argumentCaptor2.getValue();
        assertThat(actual1).isEqualTo(586L);
        assertThat(actual2).isEqualTo(14);
        assertThat(catOwnerService.addDays(586L, 14)).isEqualTo("14 дней добавлено 586");
        assertThat(catOwnerService.addDays(586L, 30)).isEqualTo("30 дней добавлено 586");
        assertThat(catOwnerService.addDays(586L, 31)).isEqualTo("Дней можно добавить, либо 14, либо 30");
        when(catOwnerRepository.findDayToEndReportingById(586L)).thenReturn(15);
        assertThat(catOwnerService.addDays(586L, 30)).isEqualTo("Добавить дни можно только после окончания отчетного периода");
    }

    @Test
    void warnAboutPoorReport() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(catOwnerRepository.findById(catOwnerEntity.getId())).thenReturn(Optional.of(catOwnerEntity));
        catOwnerService.warnAboutPoorReport(586L);
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        verify(telegramBotSenderService).warnAboutPoorReport(argumentCaptor1.capture());
        Long actual1 = argumentCaptor1.getValue();
        assertThat(actual1).isEqualTo(586L);
    }

    @Test
    void warnAboutRefuse() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(catOwnerRepository.findById(catOwnerEntity.getId())).thenReturn(Optional.of(catOwnerEntity));
        catOwnerService.warnAboutRefuse(586L);
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        verify(telegramBotSenderService).warnAboutRefuse(argumentCaptor1.capture());
        Long actual1 = argumentCaptor1.getValue();
        assertThat(actual1).isEqualTo(586L);
    }

    @Test
    void warnAboutApproval() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(catOwnerRepository.findById(catOwnerEntity.getId())).thenReturn(Optional.of(catOwnerEntity));
        catOwnerService.warnAboutApproval(586L);
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        verify(telegramBotSenderService).warnAboutApproval(argumentCaptor1.capture());
        Long actual1 = argumentCaptor1.getValue();
        assertThat(actual1).isEqualTo(586L);
    }

    @Test
    void catOwnerFindById() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(catOwnerRepository.findById(catOwnerEntity.getId())).thenReturn(Optional.of(catOwnerEntity));
        assertThat(catOwnerService.catOwnerFindById(586L)).isEqualTo(true);
        assertThat(catOwnerService.catOwnerFindById(5861L)).isEqualTo(false);
    }

    @Test
    void findAllOwnersWithOverdueReportingDay() {
        when(catOwnerRepository.findAllOverdueReportingDay()).thenReturn(List.of(5L, 8L));
        assertThat(catOwnerService.findAllOwnersWithOverdueReportingDay()).isEqualTo(List.of(5L, 8L));
    }

    @Test
    void findAllOwnersWithOverdueReportingTwoDay() {
        when(catOwnerRepository.findAllOwnersWithOverdueReportingTwoDay()).thenReturn(List.of(5L, 8L));
        assertThat(catOwnerService.findAllOwnersWithOverdueReportingTwoDay()).isEqualTo(List.of(5L, 8L));
    }
}