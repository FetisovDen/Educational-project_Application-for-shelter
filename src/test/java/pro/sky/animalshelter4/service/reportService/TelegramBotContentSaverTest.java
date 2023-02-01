package pro.sky.animalshelter4.service.reportService;


import com.pengrad.telegrambot.model.Update;
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
import pro.sky.animalshelter4.entity.chatEntity.Chat;
import pro.sky.animalshelter4.model.Command;
import pro.sky.animalshelter4.model.InteractionUnit;
import pro.sky.animalshelter4.recordDTO.UpdateDTO;
import pro.sky.animalshelter4.service.chatTgService.ChatService;
import pro.sky.animalshelter4.service.ownerServise.CatOwnerService;
import pro.sky.animalshelter4.service.ownerServise.DogOwnerService;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotSenderService;

import static org.mockito.Mockito.*;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class TelegramBotContentSaverTest {
    @Mock
    private TelegramBotSenderService telegramBotSenderService;
    @Mock
    private CatOwnerService catOwnerService;
    @Mock
    private ChatService chatService;
    @Mock
    private DogOwnerService dogOwnerService;
    @InjectMocks
    private TelegramBotContentSaver telegramBotContentSaver;
    private final Generator generator = new Generator();



    @Test
    void saveReport() {
        Update update = generator.generateUpdateMessagePhotoWithReflection("123", "1", "", 586L, "", "AgACAgIAAxkBAAIIx2PaUSb9TxL8NhLMXUweoeO59co_AAK-xTEbBDeZSt1moFmnwaOOAQADAgADcwADLgQ", false);
    }

    @Test
    void savePhone() {
        UpdateDTO updateDTO = new UpdateDTO(50L, "456", "123", Command.START, "+79093809480", null, null, InteractionUnit.COMMAND);
        Chat chat = generator.generateChat(50L, "456", "123", null, false, false, false);
        when(chatService.getChatByIdOrNewWithName(updateDTO.getIdChat(), updateDTO.getName(), updateDTO.getUserName())).thenReturn(chat);
        telegramBotContentSaver.savePhone(updateDTO);
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(telegramBotSenderService, times(1)).successfulPhoneSave(argumentCaptor1.capture());
        Long actual1 = argumentCaptor1.getValue();
        Assertions.assertThat(actual1).isEqualTo(50L);
        updateDTO.setMessage("def");
        telegramBotContentSaver.savePhone(updateDTO);
        ArgumentCaptor<Long> argumentCaptor2 = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(telegramBotSenderService, times(1)).unSuccessfulPhoneSave(argumentCaptor2.capture());
        Long actual2 = argumentCaptor2.getValue();
        Assertions.assertThat(actual2).isEqualTo(50L);
    }

    @Test
    void checkOwner() {
        when(catOwnerService.catOwnerFindById(0L)).thenReturn(false);
        when(dogOwnerService.dogOwnerFindById(0L)).thenReturn(false);
        Assertions.assertThat(telegramBotContentSaver.checkOwner(0L)).isEqualTo(null);
        when(catOwnerService.catOwnerFindById(1L)).thenReturn(true);
        Assertions.assertThat(telegramBotContentSaver.checkOwner(1L)).isEqualTo("cat");
        when(dogOwnerService.dogOwnerFindById(2L)).thenReturn(true);
        Assertions.assertThat(telegramBotContentSaver.checkOwner(2L)).isEqualTo("dog");

    }
}