package pro.sky.animalshelter4.controller.ownerController;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.animalshelter4.Generator;
import pro.sky.animalshelter4.entity.ownerEntity.DogOwnerEntity;
import pro.sky.animalshelter4.recordDTO.DogOwnerRecord;
import pro.sky.animalshelter4.service.ownerServise.DogOwnerService;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DogOwnerController.class)
class DogOwnerControllerTest {

    @MockBean
    private DogOwnerService dogOwnerService;
    @Autowired
    private MockMvc mockMvc;
    private final Generator generator = new Generator();
    private final Gson gson = new Gson();

    @Test
    void createDogOwner() throws Exception {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        when(dogOwnerService.createDogOwner(dogOwnerRecord)).thenReturn(dogOwnerRecord);
        String jsonStr = gson.toJson(dogOwnerRecord);
        mockMvc.perform(post("/dog/owner").contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.chatId", Matchers.equalTo(dogOwnerRecord.getChatId().intValue())))
                .andExpect(jsonPath("$.ownerName", Matchers.equalTo(dogOwnerRecord.getOwnerName())))
                .andExpect(jsonPath("$.dogName", Matchers.equalTo(dogOwnerRecord.getDogName())))
                .andExpect(jsonPath("$.dayToEndReporting", Matchers.equalTo(dogOwnerRecord.getDayToEndReporting().intValue())));
    }

    @Test
    void readDogOwner() throws Exception {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        when(dogOwnerService.readDogOwner(dogOwnerEntity.getId())).thenReturn(dogOwnerEntity);
        mockMvc.perform(MockMvcRequestBuilders.get("/dog/owner/" + dogOwnerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(dogOwnerEntity.getId().intValue())))
                .andExpect(jsonPath("$.ownerName", Matchers.equalTo(dogOwnerEntity.getOwnerName())))
                .andExpect(jsonPath("$.dogName", Matchers.equalTo(dogOwnerEntity.getDogName())))
                .andExpect(jsonPath("$.dayToEndReporting", Matchers.equalTo(dogOwnerEntity.getDayToEndReporting().intValue())))
                .andExpect(jsonPath("$.chatOwner.id", Matchers.equalTo(dogOwnerEntity.getChatOwner().getId().intValue())))
                .andExpect(jsonPath("$.chatOwner.phone", Matchers.equalTo(dogOwnerEntity.getChatOwner().getPhone())));
    }

    @Test
    void updateDogOwner() throws Exception {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        when(dogOwnerService.updateDogOwner(dogOwnerRecord)).thenReturn(dogOwnerRecord);
        String jsonStr = gson.toJson(dogOwnerRecord);
        mockMvc.perform(put("/dog/owner").contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId", Matchers.equalTo(dogOwnerRecord.getChatId().intValue())))
                .andExpect(jsonPath("$.ownerName", Matchers.equalTo(dogOwnerRecord.getOwnerName())))
                .andExpect(jsonPath("$.dogName", Matchers.equalTo(dogOwnerRecord.getDogName())))
                .andExpect(jsonPath("$.dayToEndReporting", Matchers.equalTo(dogOwnerRecord.getDayToEndReporting().intValue())));
    }

    @Test
    void addDaysToEnd() throws Exception {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        when(dogOwnerService.addDays(dogOwnerEntity.getId(), 14)).thenReturn("14 days add to 586");
        String jsonStr = gson.toJson(dogOwnerRecord);
        mockMvc.perform(put("/dog/owner/" + dogOwnerEntity.getId() + "/add/?days=" + 14).contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(content().string(containsString("14 days add to 586")));
    }

    @Test
    void warnAboutPoorReport() throws Exception {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        doNothing().when(dogOwnerService).warnAboutPoorReport(dogOwnerEntity.getId());
        String jsonStr = gson.toJson(dogOwnerRecord);
        mockMvc.perform(put("/dog/owner/" + dogOwnerEntity.getId() + "/warn").contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
    }

    @Test
    void warnAboutApproval() throws Exception {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        doNothing().when(dogOwnerService).warnAboutApproval(dogOwnerEntity.getId());
        String jsonStr = gson.toJson(dogOwnerRecord);
        mockMvc.perform(put("/dog/owner/" + dogOwnerEntity.getId() + "/approval").contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
    }

    @Test
    void warnAboutRefuse() throws Exception {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        doNothing().when(dogOwnerService).warnAboutRefuse(dogOwnerEntity.getId());
        String jsonStr = gson.toJson(dogOwnerRecord);
        mockMvc.perform(put("/dog/owner/" + dogOwnerEntity.getId() + "/refuse").contentType(MediaType.APPLICATION_JSON_VALUE).content(String.valueOf(jsonStr))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
    }

    @Test
    void deleteDogOwner() throws Exception {
        DogOwnerEntity dogOwnerEntity = generator.generateDogOwnerEntity(586L, "Den", "test_test", null, null, null, true);
        DogOwnerRecord dogOwnerRecord = generator.generateDogOwnerRecord(dogOwnerEntity, false);
        doNothing().when(dogOwnerService).deleteDogOwner(dogOwnerRecord.getChatId());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/dog/owner/" + dogOwnerRecord.getChatId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}