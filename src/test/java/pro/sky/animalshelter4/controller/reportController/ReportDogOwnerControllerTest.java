package pro.sky.animalshelter4.controller.reportController;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.animalshelter4.Generator;
import pro.sky.animalshelter4.entity.reportEntity.ReportDogOwnerEntity;
import pro.sky.animalshelter4.service.reportService.ReportDogOwnerService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ReportDogOwnerController.class)
class ReportDogOwnerControllerTest {
    @MockBean
    private ReportDogOwnerService reportDogOwnerService;
    @Autowired
    private MockMvc mockMvc;
    private final Generator generator = new Generator();

    @Test
    void findAllByDate() throws Exception {
        ReportDogOwnerEntity reportDogOwnerEntity = generator.generateReportDogOwnerEntity(58634L, 456L, null, true, "", "", null, true);
        ReportDogOwnerEntity reportDogOwnerEntity1 = generator.generateReportDogOwnerEntity(586L, 567L, null, true, "", "", null, true);
        reportDogOwnerEntity.setTime(Timestamp.valueOf(LocalDateTime.now()));
        reportDogOwnerEntity1.setTime(Timestamp.valueOf(LocalDateTime.now()));
        List<ReportDogOwnerEntity> chatList = new ArrayList<>(List.of(reportDogOwnerEntity, reportDogOwnerEntity1));
        when(reportDogOwnerService.readAllByDay(reportDogOwnerEntity.getTime().toString())).thenReturn(chatList);
        mockMvc.perform(MockMvcRequestBuilders.get("/dog/report/date/?date=" + reportDogOwnerEntity.getTime())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(reportDogOwnerEntity.getId().intValue())))
                .andExpect(jsonPath("$[0].chatId", Matchers.equalTo(reportDogOwnerEntity.getChatId().intValue())))
                .andExpect(jsonPath("$[0].text", Matchers.equalTo(reportDogOwnerEntity.getText())))
                .andExpect(jsonPath("$[0].filePath", Matchers.equalTo(reportDogOwnerEntity.getFilePath())))
                .andExpect(jsonPath("$[1].id", Matchers.equalTo(reportDogOwnerEntity1.getId().intValue())))
                .andExpect(jsonPath("$[1].chatId", Matchers.equalTo(reportDogOwnerEntity1.getChatId().intValue())))
                .andExpect(jsonPath("$[1].text", Matchers.equalTo(reportDogOwnerEntity1.getText())))
                .andExpect(jsonPath("$[1].filePath", Matchers.equalTo(reportDogOwnerEntity1.getFilePath())));
    }

    @Test
    void deleteByChatId() throws Exception {
        ReportDogOwnerEntity reportDogOwnerEntity = generator.generateReportDogOwnerEntity(58634L, 456L, null, true, "", "", null, true);
        doNothing().when(reportDogOwnerService).clear(reportDogOwnerEntity.getId());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/dog/report/" + reportDogOwnerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void downloadAvatar() throws Exception {
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
        when(reportDogOwnerService.readFromFile(reportDogOwnerEntity.getId())).thenReturn(Files.readAllBytes(Path.of(reportDogOwnerEntity.getFilePath())));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog/report/" + reportDogOwnerEntity.getId() + "/image")
                        .contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isOk());
    }
}