package pro.sky.animalshelter4.service.reportService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import pro.sky.animalshelter4.Generator;
import pro.sky.animalshelter4.entity.reportEntity.ReportDogOwnerEntity;
import pro.sky.animalshelter4.exception.ReportDogOwnerNotFoundException;
import pro.sky.animalshelter4.repository.reportRepository.ReportDogOwnerRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class ReportDogOwnerServiceTest {
    @Mock
    private ReportDogOwnerRepository reportDogOwnerRepository;
    @Mock
    private Timestamp LocalDateTime;
    @InjectMocks
    private ReportDogOwnerService reportDogOwnerService;
    private final Generator generator = new Generator();

    @Test
    void create() {
        ReportDogOwnerEntity reportDogOwnerEntity = generator.generateReportDogOwnerEntity(58634L, 456L, null, true, "", "", null, true);
        when(reportDogOwnerRepository.findByFilePath(reportDogOwnerEntity.getFilePath())).thenReturn(Optional.of(reportDogOwnerEntity));
        when(reportDogOwnerRepository.save(reportDogOwnerEntity)).thenReturn(reportDogOwnerEntity);
        assertThat(reportDogOwnerService.create(reportDogOwnerEntity))
                .isNotNull()
                .isEqualTo(reportDogOwnerEntity);
        ReportDogOwnerEntity reportDogOwnerEntity1 = generator.generateReportDogOwnerEntity(58634L, 456L, null, true, "", "", null, true);
        when(reportDogOwnerRepository.findByFilePath(reportDogOwnerEntity1.getFilePath())).thenReturn(Optional.empty());
        when(reportDogOwnerRepository.save(reportDogOwnerEntity1)).thenReturn(reportDogOwnerEntity1);
        assertThat(reportDogOwnerService.create(reportDogOwnerEntity1))
                .isNotNull()
                .isEqualTo(reportDogOwnerEntity1);
    }

    @Test
    void readAllByDay() {
        ReportDogOwnerEntity reportDogOwnerEntity = generator.generateReportDogOwnerEntity(58634L, 456L, LocalDateTime, true, "", "", null, true);
        when(reportDogOwnerRepository.findAllByTimeContains(String.valueOf(LocalDateTime))).thenReturn(List.of(reportDogOwnerEntity));
        assertThat(reportDogOwnerService.readAllByDay(String.valueOf(LocalDateTime)))
                .isNotNull()
                .isEqualTo(List.of(reportDogOwnerEntity));
    }

    @Test
    void clear() {
        ReportDogOwnerEntity reportDogOwnerEntity = generator.generateReportDogOwnerEntity(58634L, 456L, LocalDateTime, true, "", "", null, true);
        doNothing().when(reportDogOwnerRepository).deleteAllByChatId(reportDogOwnerEntity.getId());
        reportDogOwnerService.clear(reportDogOwnerEntity.getId());
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(reportDogOwnerRepository).deleteAllByChatId(argumentCaptor.capture());
        Long actual = argumentCaptor.getValue();
        Assertions.assertThat(actual).isEqualTo(reportDogOwnerEntity.getId());
    }

    @Test
    void readFromFile() throws IOException {
        ReportDogOwnerEntity reportDogOwnerEntity = generator.generateReportDogOwnerEntity
                (
                        58634L,
                        456L,
                        null,
                        true,
                        "",
                        ".\\src\\test\\java\\pro\\sky\\animalshelter4\\materials\\report\\test_mops.jpg",
                        null,
                        true
                );
        when(reportDogOwnerRepository.findById(reportDogOwnerEntity.getId())).thenReturn(Optional.of(reportDogOwnerEntity));
        assertThat(reportDogOwnerService.readFromFile(reportDogOwnerEntity.getId()))
                .isNotNull()
                .isEqualTo(Files.readAllBytes(Path.of(reportDogOwnerEntity.getFilePath())));
        assertThrows(ReportDogOwnerNotFoundException.class, () -> reportDogOwnerService.readFromFile(1L));
    }
}