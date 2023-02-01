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
import pro.sky.animalshelter4.entity.reportEntity.ReportCatOwnerEntity;
import pro.sky.animalshelter4.exception.ReportCatOwnerNotFoundException;
import pro.sky.animalshelter4.repository.reportRepository.ReportCatOwnerRepository;

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
class ReportCatOwnerServiceTest {
    @Mock
    private ReportCatOwnerRepository reportCatOwnerRepository;
    @Mock
    private Timestamp LocalDateTime;
    @InjectMocks
    private ReportCatOwnerService reportCatOwnerService;
    private final Generator generator = new Generator();

    @Test
    void create() {
        ReportCatOwnerEntity reportCatOwnerEntity = generator.generateReportCatOwnerEntity(58634L, 456L, null, true, "", "", null, true);
        when(reportCatOwnerRepository.findByFilePath(reportCatOwnerEntity.getFilePath())).thenReturn(Optional.of(reportCatOwnerEntity));
        when(reportCatOwnerRepository.save(reportCatOwnerEntity)).thenReturn(reportCatOwnerEntity);
        assertThat(reportCatOwnerService.create(reportCatOwnerEntity))
                .isNotNull()
                .isEqualTo(reportCatOwnerEntity);
        ReportCatOwnerEntity reportCatOwnerEntity1 = generator.generateReportCatOwnerEntity(58634L, 456L, null, true, "", "", null, true);
        when(reportCatOwnerRepository.findByFilePath(reportCatOwnerEntity1.getFilePath())).thenReturn(Optional.empty());
        when(reportCatOwnerRepository.save(reportCatOwnerEntity1)).thenReturn(reportCatOwnerEntity1);
        assertThat(reportCatOwnerService.create(reportCatOwnerEntity1))
                .isNotNull()
                .isEqualTo(reportCatOwnerEntity1);
    }

    @Test
    void readAllByDay() {
        ReportCatOwnerEntity reportCatOwnerEntity = generator.generateReportCatOwnerEntity(58634L, 456L, LocalDateTime, true, "", "", null, true);
        when(reportCatOwnerRepository.findAllByTimeContains(String.valueOf(LocalDateTime))).thenReturn(List.of(reportCatOwnerEntity));
        assertThat(reportCatOwnerService.readAllByDay(String.valueOf(LocalDateTime)))
                .isNotNull()
                .isEqualTo(List.of(reportCatOwnerEntity));
    }

    @Test
    void clear() {
        ReportCatOwnerEntity reportCatOwnerEntity = generator.generateReportCatOwnerEntity(58634L, 456L, LocalDateTime, true, "", "", null, true);
        doNothing().when(reportCatOwnerRepository).deleteAllByChatId(reportCatOwnerEntity.getId());
        reportCatOwnerService.clear(reportCatOwnerEntity.getId());
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(reportCatOwnerRepository).deleteAllByChatId(argumentCaptor.capture());
        Long actual = argumentCaptor.getValue();
        Assertions.assertThat(actual).isEqualTo(reportCatOwnerEntity.getId());
    }

    @Test
    void readFromFile() throws IOException {
        ReportCatOwnerEntity reportCatOwnerEntity = generator.generateReportCatOwnerEntity
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
        when(reportCatOwnerRepository.findById(reportCatOwnerEntity.getId())).thenReturn(Optional.of(reportCatOwnerEntity));
        assertThat(reportCatOwnerService.readFromFile(reportCatOwnerEntity.getId()))
                .isNotNull()
                .isEqualTo(Files.readAllBytes(Path.of(reportCatOwnerEntity.getFilePath())));
        assertThrows(ReportCatOwnerNotFoundException.class, () -> reportCatOwnerService.readFromFile(1L));
    }
}