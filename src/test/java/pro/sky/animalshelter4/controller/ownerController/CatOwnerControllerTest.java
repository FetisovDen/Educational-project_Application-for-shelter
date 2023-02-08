package pro.sky.animalshelter4.controller.ownerController;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.animalshelter4.Generator;
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;
import pro.sky.animalshelter4.recordDTO.CatOwnerRecord;
import pro.sky.animalshelter4.service.ownerServise.CatOwnerService;

@WebMvcTest(CatOwnerController.class)
class CatOwnerControllerTest {
    @MockBean
    private CatOwnerService catOwnerService;
    @Autowired
    private MockMvc mockMvc;
    private final Generator generator = new Generator();
    private final Gson gson = new Gson();

    @Test
    void createCatOwner() throws Exception {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        when(catOwnerService.createCatOwner(catOwnerRecord)).thenReturn(catOwnerRecord);
        String jsonStr = gson.toJson(catOwnerRecord);
        mockMvc.perform(post("/cat/owner").contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.chatId", Matchers.equalTo(catOwnerRecord.getChatId().intValue())))
                .andExpect(jsonPath("$.ownerName", Matchers.equalTo(catOwnerRecord.getOwnerName())))
                .andExpect(jsonPath("$.catName", Matchers.equalTo(catOwnerRecord.getCatName())))
                .andExpect(jsonPath("$.dayToEndReporting", Matchers.equalTo(catOwnerRecord.getDayToEndReporting().intValue())));
    }

    @Test
    void readCatOwner() throws Exception {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(catOwnerService.readCatOwner(catOwnerEntity.getId())).thenReturn(catOwnerEntity);
        mockMvc.perform(MockMvcRequestBuilders.get("/cat/owner/" + catOwnerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(catOwnerEntity.getId().intValue())))
                .andExpect(jsonPath("$.ownerName", Matchers.equalTo(catOwnerEntity.getOwnerName())))
                .andExpect(jsonPath("$.catName", Matchers.equalTo(catOwnerEntity.getCatName())))
                .andExpect(jsonPath("$.dayToEndReporting", Matchers.equalTo(catOwnerEntity.getDayToEndReporting().intValue())))
                .andExpect(jsonPath("$.chatOwner.id", Matchers.equalTo(catOwnerEntity.getChatOwner().getId().intValue())))
                .andExpect(jsonPath("$.chatOwner.phone", Matchers.equalTo(catOwnerEntity.getChatOwner().getPhone())));
    }

    @Test
    void updateCatOwner() throws Exception {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        when(catOwnerService.updateCatOwner(catOwnerRecord)).thenReturn(catOwnerRecord);
        String jsonStr = gson.toJson(catOwnerRecord);
        mockMvc.perform(put("/cat/owner").contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId", Matchers.equalTo(catOwnerRecord.getChatId().intValue())))
                .andExpect(jsonPath("$.ownerName", Matchers.equalTo(catOwnerRecord.getOwnerName())))
                .andExpect(jsonPath("$.catName", Matchers.equalTo(catOwnerRecord.getCatName())))
                .andExpect(jsonPath("$.dayToEndReporting", Matchers.equalTo(catOwnerRecord.getDayToEndReporting().intValue())));
    }

    @Test
    void addDaysToEnd() throws Exception {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        when(catOwnerService.addDays(catOwnerEntity.getId(), 14)).thenReturn("14 days add to 586");
        String jsonStr = gson.toJson(catOwnerRecord);
        mockMvc.perform(put("/cat/owner/" + catOwnerEntity.getId() + "/add/?days=" + 14).contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(content().string(containsString("14 days add to 586")));
    }

    @Test
    void warnAboutPoorReport() throws Exception {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        doNothing().when(catOwnerService).warnAboutPoorReport(catOwnerEntity.getId());
        String jsonStr = gson.toJson(catOwnerRecord);
        mockMvc.perform(put("/cat/owner/" + catOwnerEntity.getId() + "/warn").contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
    }

    @Test
    void warnAboutApproval() throws Exception {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        doNothing().when(catOwnerService).warnAboutApproval(catOwnerEntity.getId());
        String jsonStr = gson.toJson(catOwnerRecord);
        mockMvc.perform(put("/cat/owner/" + catOwnerEntity.getId() + "/approval").contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
    }

    @Test
    void warnAboutRefuse() throws Exception {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        doNothing().when(catOwnerService).warnAboutRefuse(catOwnerEntity.getId());
        String jsonStr = gson.toJson(catOwnerRecord);
        mockMvc.perform(put("/cat/owner/" + catOwnerEntity.getId() + "/refuse").contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
    }

    @Test
    void deleteCatOwner() throws Exception {
        CatOwnerEntity catOwnerEntity = generator.generateCatOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        CatOwnerRecord catOwnerRecord = generator.generateCatOwnerRecord(catOwnerEntity, false);
        doNothing().when(catOwnerService).deleteCatOwner(catOwnerRecord.getChatId());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/cat/owner/" + catOwnerRecord.getChatId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}