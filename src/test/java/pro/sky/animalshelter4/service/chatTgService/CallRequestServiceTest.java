package pro.sky.animalshelter4.service.chatTgService;

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
import pro.sky.animalshelter4.entity.chatEntity.CallRequest;
import pro.sky.animalshelter4.entity.chatEntity.Chat;
import pro.sky.animalshelter4.model.Command;
import pro.sky.animalshelter4.model.InteractionUnit;
import pro.sky.animalshelter4.recordDTO.UpdateDTO;
import pro.sky.animalshelter4.repository.chatRepository.CallRequestRepository;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotSenderService;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class CallRequestServiceTest {
    @Mock
    private ChatService chatService;
    @Mock
    private CallRequestRepository callRequestRepository;
    @Mock
    private TelegramBotSenderService telegramBotSenderService;

    @InjectMocks
    private CallRequestService callRequestService;
    private final Generator generator = new Generator();


    @Test
    void process() {
        UpdateDTO updateDTO = new UpdateDTO(50L, "456", "123", Command.START, "+79093809480", null, null, InteractionUnit.COMMAND);
        Chat chat = generator.generateChat(50L, "456", "123", "122345", false, false, false);
        when(chatService.getChatOfVolunteer()).thenReturn(null);
        when(chatService.getChatByIdOrNewWithName(updateDTO.getIdChat(), updateDTO.getName(), updateDTO.getUserName())).thenReturn(chat);
        callRequestService.process(updateDTO,"cat");
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
        Mockito.verify(telegramBotSenderService, times(1)).sendMessage(argumentCaptor1.capture(), argumentCaptor2.capture());
        Long actual1 = argumentCaptor1.getValue();
        String actual2 = argumentCaptor2.getValue();
        Assertions.assertThat(actual1).isEqualTo(50L);
        Assertions.assertThat(actual2).isEqualTo("Сейчас все волонтеры заняты, просьба обратиться позже");

        UpdateDTO updateDTO1 = new UpdateDTO(50L, "456", "123", Command.START, "+79093809480", null, null, InteractionUnit.COMMAND);
        Chat chat1 = generator.generateChat(50L, "456", "123", null, false, false, false);
        when(chatService.getChatByIdOrNewWithName(updateDTO1.getIdChat(), updateDTO1.getName(), updateDTO1.getUserName())).thenReturn(chat1);
        callRequestService.process(updateDTO1,"cat");
        ArgumentCaptor<Long> argumentCaptor3 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> argumentCaptor4 = ArgumentCaptor.forClass(String.class);
        Mockito.verify(telegramBotSenderService, times(1)).sendInfoAboutLeaveNumber(argumentCaptor3.capture(), argumentCaptor4.capture());
        Long actual3 = argumentCaptor3.getValue();
        String actual4 = argumentCaptor4.getValue();
        Assertions.assertThat(actual3).isEqualTo(50L);
        Assertions.assertThat(actual4).isEqualTo("cat");


        UpdateDTO updateDTO2 = new UpdateDTO(501L, "456", "123", Command.START, "+79093809480", null, null, InteractionUnit.COMMAND);
        Chat chat2 = generator.generateChat(501L, "456", "123", "233746", false, false, false);
        Chat chat3 = generator.generateChat(5013L, "4562", "1232", "22342", false, true, false);
        when(chatService.getChatByIdOrNewWithName(updateDTO2.getIdChat(), updateDTO2.getName(), updateDTO2.getUserName())).thenReturn(chat2);
        when(chatService.getChatOfVolunteer()).thenReturn(chat3);
        callRequestService.process(updateDTO2,"cat");
        ArgumentCaptor<Long> argumentCaptor5 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> argumentCaptor6 = ArgumentCaptor.forClass(String.class);
        Mockito.verify(telegramBotSenderService, times(2)).sendMessage(argumentCaptor5.capture(), argumentCaptor6.capture());
        Long actual5 = argumentCaptor5.getValue();
        String actual6 = argumentCaptor6.getValue();
        Assertions.assertThat(actual5).isEqualTo(501);
        Assertions.assertThat(actual6).isEqualTo("Хорошо, Волонтер свяжется с вашми");

        UpdateDTO updateDTO3 = new UpdateDTO(50L, "456", "123", Command.START, "+79093809480", null, null, InteractionUnit.COMMAND);
        Chat chat4 = generator.generateChat(50L, "456", "123", "233", false, false, false);
        Chat chat5 = generator.generateChat(501L, "4562", "1232", null, false, true, false);
        when(chatService.getChatByIdOrNewWithName(updateDTO3.getIdChat(), updateDTO3.getName(), updateDTO3.getUserName())).thenReturn(chat4);
        when(chatService.getChatOfVolunteer()).thenReturn(chat5);
        when(callRequestRepository.getFirstOpenByChatClientId(chat4.getId())).thenReturn(new CallRequest());
        callRequestService.process(updateDTO3,"cat");
        ArgumentCaptor<Long> argumentCaptor7 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> argumentCaptor8 = ArgumentCaptor.forClass(String.class);
        Mockito.verify(telegramBotSenderService, times(3)).sendMessage(argumentCaptor7.capture(), argumentCaptor8.capture());
        Long actual7 = argumentCaptor5.getValue();
        String actual8 = argumentCaptor6.getValue();
        Assertions.assertThat(actual7).isEqualTo(501L);
        Assertions.assertThat(actual8).isEqualTo("Хорошо, Волонтер свяжется с вашми");

    }

    @Test
    void sendNotificationAboutAllCallRequest() {
        Chat chat = generator.generateChat(586L, "123", "1", "+890", false, false, false);
        CallRequest callRequest = new CallRequest();
        callRequest.setId(586L);
        callRequest.setChatClient(chat);
        when(callRequestRepository.getAllOpenByChatId(callRequest.getId())).thenReturn(List.of(callRequest));
        callRequestService.sendNotificationAboutAllCallRequest(callRequest.getId());
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
        Mockito.verify(telegramBotSenderService, times(1)).sendMessage(argumentCaptor1.capture(), argumentCaptor2.capture());
        Long actual1 = argumentCaptor1.getValue();
        String actual2 = argumentCaptor2.getValue();
        Assertions.assertThat(actual1).isEqualTo(586L);
        Assertions.assertThat(actual2).isEqualTo("Поступил запрос от пользователя, имя - 123\n" +
                "Телеграм ID: @1\n" +
                "Номер телефона: +890\n" +
                "");

    }

    @Test
    void getAllOpenByChat() {
        CallRequest callRequest = new CallRequest();
        callRequest.setId(586L);
        callRequestService.getAllOpenByChat(callRequest.getId());
        ArgumentCaptor<Long> argumentCaptor1 = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(callRequestRepository, times(1)).getAllOpenByChatId(argumentCaptor1.capture());
        Long actual1 = argumentCaptor1.getValue();
        Assertions.assertThat(actual1).isEqualTo(586L);
    }

    @Test
    void add() {
        CallRequest callRequest = new CallRequest();
        callRequest.setId(586L);
        callRequestService.add(callRequest);
        ArgumentCaptor<CallRequest> argumentCaptor1 = ArgumentCaptor.forClass(CallRequest.class);
        Mockito.verify(callRequestRepository, times(1)).save(argumentCaptor1.capture());
        CallRequest actual1 = argumentCaptor1.getValue();
        Assertions.assertThat(actual1.getId()).isEqualTo(586L);
    }
}