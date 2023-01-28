package pro.sky.animalshelter4.controller.chatController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.animalshelter4.Generator;
import pro.sky.animalshelter4.entity.chatEntity.Chat;
import pro.sky.animalshelter4.service.chatTgService.ChatService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


@WebMvcTest(ChatController.class)
class ChatControllerTest {
    @MockBean
    private ChatService chatService;
    @Autowired
    private MockMvc mockMvc;
    private final Generator generator = new Generator();

    @Test
    void readChatById() throws Exception {
        Chat chat = generator.generateChat(586L, "Den", "test_test", "", false, false, false);
        when(chatService.findChat(chat.getId())).thenReturn(chat);
        mockMvc.perform(MockMvcRequestBuilders.get("/chat/" + chat.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(chat.getId().intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(chat.getName())))
                .andExpect(jsonPath("$.telegramName", Matchers.equalTo(chat.getTelegramName())))
                .andExpect(jsonPath("$.owner", Matchers.equalTo(false)));
    }
    @Test
    void readChatByTgName() throws Exception {
        Chat chat = generator.generateChat(58634L, "Den1", "test_test", "", true, false, false);
        Chat chat1 = generator.generateChat(586L, "Den", "test_test", "", false, false, false);
        List<Chat> chatList = new ArrayList<>(List.of(chat,chat1));
        when(chatService.findChatByTgName(chat.getTelegramName())).thenReturn(chatList);
        mockMvc.perform(MockMvcRequestBuilders.get("/chat/tgname/" + chat.getTelegramName())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(chat.getId().intValue())))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo(chat.getName())))
                .andExpect(jsonPath("$[0].telegramName", Matchers.equalTo(chat.getTelegramName())))
                .andExpect(jsonPath("$[0].owner", Matchers.equalTo(true)))
                .andExpect(jsonPath("$[1].id", Matchers.equalTo(chat1.getId().intValue())))
                .andExpect(jsonPath("$[1].name", Matchers.equalTo(chat1.getName())))
                .andExpect(jsonPath("$[1].telegramName", Matchers.equalTo(chat1.getTelegramName())))
                .andExpect(jsonPath("$[1].owner", Matchers.equalTo(false)));
    }

    @Test
    void deleteChat() throws Exception {
        Chat chat = generator.generateChat(58634L, "Den1", "test_test1", "", true, false, false);
        when(chatService.findChat(chat.getId())).thenReturn(chat);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/chat/" + chat.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}