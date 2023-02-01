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
import pro.sky.animalshelter4.entity.reportEntity.ReportCatOwnerEntity;
import pro.sky.animalshelter4.service.reportService.ReportCatOwnerService;

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


@WebMvcTest(ReportCatOwnerController.class)
class ReportCatOwnerControllerTest {
    @MockBean
    private ReportCatOwnerService reportCatOwnerService;
    @Autowired
    private MockMvc mockMvc;
    private final Generator generator = new Generator();

    @Test
    void findAllByDate() throws Exception {
        ReportCatOwnerEntity reportCatOwnerEntity = generator.generateReportCatOwnerEntity(58634L, 456L, null, true, "", "", null, true);
        ReportCatOwnerEntity reportCatOwnerEntity1 = generator.generateReportCatOwnerEntity(586L, 567L, null, true, "", "", null, true);
        List<ReportCatOwnerEntity> chatList = new ArrayList<>(List.of(reportCatOwnerEntity, reportCatOwnerEntity1));
        reportCatOwnerEntity.setTime(Timestamp.valueOf(LocalDateTime.now()));
        reportCatOwnerEntity1.setTime(Timestamp.valueOf(LocalDateTime.now()));
        when(reportCatOwnerService.readAllByDay(reportCatOwnerEntity.getTime().toString())).thenReturn(chatList);
        mockMvc.perform(MockMvcRequestBuilders.get("/cat/report/date/?date=" + reportCatOwnerEntity.getTime())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(reportCatOwnerEntity.getId().intValue())))
                .andExpect(jsonPath("$[0].chatId", Matchers.equalTo(reportCatOwnerEntity.getChatId().intValue())))
                .andExpect(jsonPath("$[0].text", Matchers.equalTo(reportCatOwnerEntity.getText())))
                .andExpect(jsonPath("$[0].filePath", Matchers.equalTo(reportCatOwnerEntity.getFilePath())))
                .andExpect(jsonPath("$[1].id", Matchers.equalTo(reportCatOwnerEntity1.getId().intValue())))
                .andExpect(jsonPath("$[1].chatId", Matchers.equalTo(reportCatOwnerEntity1.getChatId().intValue())))
                .andExpect(jsonPath("$[1].text", Matchers.equalTo(reportCatOwnerEntity1.getText())))
                .andExpect(jsonPath("$[1].filePath", Matchers.equalTo(reportCatOwnerEntity1.getFilePath())));
    }

    @Test
    void deleteByChatId() throws Exception {
        ReportCatOwnerEntity reportCatOwnerEntity = generator.generateReportCatOwnerEntity(58634L, 456L, null, true, "", "", null, true);
        doNothing().when(reportCatOwnerService).clear(reportCatOwnerEntity.getId());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/cat/report/" + reportCatOwnerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void downloadAvatar() throws Exception {
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
        when(reportCatOwnerService.readFromFile(reportCatOwnerEntity.getId())).thenReturn(Files.readAllBytes(Path.of(reportCatOwnerEntity.getFilePath())));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cat/report/" + reportCatOwnerEntity.getId() + "/image")
                        .contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isOk());
    }
}