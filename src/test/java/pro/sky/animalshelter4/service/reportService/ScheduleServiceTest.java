package pro.sky.animalshelter4.service.reportService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import pro.sky.animalshelter4.Generator;
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;
import pro.sky.animalshelter4.entity.ownerEntity.DogOwnerEntity;
import pro.sky.animalshelter4.repository.chatRepository.ChatRepository;
import pro.sky.animalshelter4.service.ownerServise.CatOwnerService;
import pro.sky.animalshelter4.service.ownerServise.DogOwnerService;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotSenderService;

import java.util.List;

import static org.mockito.Mockito.*;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    @Mock
    private CatOwnerService catOwnerService;
    @Mock
    private DogOwnerService dogOwnerService;
    @Mock
    private TelegramBotSenderService telegramBotSenderService;
    @Mock
    private ChatRepository chatRepository;
    @InjectMocks
    private ScheduleService scheduleService;
    private final Generator generator = new Generator();

    @Test
    void scheduledReport() {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(186L, "Den1", "test_test", null, null, null, true);
        CatOwnerEntity catOwnerEntity1 = generator.generateCatOwnerEntity(286L, "Den2", "test_test", null, null, null, true);
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(386L, "Den3", "test_test", null, null, null, true);
        DogOwnerEntity dogOwnerEntity1 = generator.generateDogOwnerEntity(486L, "Den4", "test_test", null, null, null, true);
        when(catOwnerService.findAllOwnersWithOverdueReportingDay()).thenReturn(List.of(catOwnerEntity.getId(), catOwnerEntity1.getId()));
        when(dogOwnerService.findAllOwnersWithOverdueReportingDay()).thenReturn(List.of(dogOwnerEntity.getId(), dogOwnerEntity1.getId()));
        when(catOwnerService.findAllOwnersWithOverdueReportingTwoDay()).thenReturn(List.of(catOwnerEntity.getId(), catOwnerEntity1.getId()));
        when(dogOwnerService.findAllOwnersWithOverdueReportingTwoDay()).thenReturn(List.of(dogOwnerEntity.getId(), dogOwnerEntity1.getId()));
        when(chatRepository.findAllVolunteer()).thenReturn(List.of(1L));
        scheduleService.scheduledReport();
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(telegramBotSenderService, times(4)).sendWarnAboutOverdue(argumentCaptor1.capture());
        List<Long> actualList = argumentCaptor1.getAllValues();
        Assertions.assertThat(actualList.size()).isEqualTo(4);
        Long actual0 = actualList.get(0);
        Long actual1 = actualList.get(1);
        Long actual2 = actualList.get(2);
        Long actual3 = actualList.get(3);
        Assertions.assertThat(actual0).isEqualTo(186L);
        Assertions.assertThat(actual1).isEqualTo(286L);
        Assertions.assertThat(actual2).isEqualTo(386L);
        Assertions.assertThat(actual3).isEqualTo(486L);
        Mockito.verify(telegramBotSenderService, times(1)).sendWarnForVolunteersAboutOverdueTwoDay(1L, actualList.toString());
    }
}