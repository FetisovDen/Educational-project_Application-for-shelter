package pro.sky.animalshelter4.listener;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import pro.sky.animalshelter4.Generator;
import pro.sky.animalshelter4.entity.chatEntity.CallRequest;
import pro.sky.animalshelter4.model.Command;
import pro.sky.animalshelter4.entity.chatEntity.Chat;
import pro.sky.animalshelter4.repository.chatRepository.CallRequestRepository;
import pro.sky.animalshelter4.repository.chatRepository.ChatRepository;
import pro.sky.animalshelter4.service.chatTgService.CallRequestService;
import pro.sky.animalshelter4.service.chatTgService.ChatService;
import pro.sky.animalshelter4.service.mapperService.MapperUpdateToDTOService;
import pro.sky.animalshelter4.service.ownerServise.CatOwnerService;
import pro.sky.animalshelter4.service.ownerServise.DogOwnerService;
import pro.sky.animalshelter4.service.reportService.ReportCatOwnerService;
import pro.sky.animalshelter4.service.reportService.ReportDogOwnerService;
import pro.sky.animalshelter4.service.reportService.TelegramBotContentSaver;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotSenderService;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotUpdatesService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@Profile("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TelegramBotUpdatesListenerTest {
    @MockBean
    private TelegramBot telegramBot;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private CallRequestRepository callRequestRepository;
    @Autowired
    private CallRequestService callRequestService;
    @Autowired
    private CatOwnerService catOwnerService;
    @Autowired
    private DogOwnerService dogOwnerService;
    @Autowired
    private TelegramBotSenderService telegramBotSenderService;
    @Autowired
    private MapperUpdateToDTOService mapperUpdateToDTOService;
    @Autowired
    private ReportCatOwnerService reportCatOwnerService;
    @Autowired
    private ReportDogOwnerService reportDogOwnerService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private TelegramBotContentSaver telegramBotContentSaver = new TelegramBotContentSaver(
            "./materials/report",
            telegramBotSenderService,
            telegramBot,
            chatService,
            catOwnerService,
            dogOwnerService,
            reportCatOwnerService,
            reportDogOwnerService);
    @Autowired
    private TelegramBotUpdatesService telegramBotUpdatesService;
    @Autowired
    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;
    private final Generator generator = new Generator();


    @BeforeEach
    public void generateData() {
        callRequestRepository.deleteAll();
        chatRepository.deleteAll();

        for (int i = 0; i < 1; i++) {
            Chat chatVolunteer = generator.generateChat(-1L, "", "","" ,false, true, true);
            chatRepository.save(chatVolunteer);
            for (int j = 0; j < 10; j++) {
                Chat chatClient = generator.generateChat(-1L, "", "", "",false, false, true);
                chatRepository.save(chatClient);
                CallRequest callRequest = new CallRequest();
                callRequest.setChatVolunteer(chatVolunteer);
                callRequest.setChatClient(chatClient);
                callRequest.setOpen(false);
                callRequest.setLocalDateTimeClose(generator.generateDateTime(true, LocalDateTime.now()));
                callRequest.setLocalDateTimeOpen(generator.generateDateTime(true, callRequest.getLocalDateTimeClose()));
                callRequestRepository.save(callRequest);
            }
        }
    }

    @AfterEach
    public void clearData() {
        callRequestRepository.deleteAll();
        chatRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(telegramBot).isNotNull();
        assertThat(chatRepository).isNotNull();
        assertThat(chatService).isNotNull();
        assertThat(callRequestRepository).isNotNull();
        assertThat(callRequestService).isNotNull();
        assertThat(mapperUpdateToDTOService).isNotNull();
        assertThat(telegramBotSenderService).isNotNull();
        assertThat(telegramBotContentSaver).isNotNull();
        assertThat(telegramBotUpdatesService).isNotNull();
        assertThat(telegramBotUpdatesListener).isNotNull();
    }

    @Test
    public void STARTTest() {
        Long id = 50L;
        String command = Command.START.getTitle();
        List<Update> updateList = new ArrayList<>(List.of(
                generator.generateUpdateMessageWithReflection("", "", "", id, command,"", true)));
        telegramBotUpdatesListener.process(updateList);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actualList = argumentCaptor.getAllValues();
        Assertions.assertThat(actualList.size()).isEqualTo(2);
        SendMessage actual0 = actualList.get(0);
        SendMessage actual1 = actualList.get(1);
        Assertions.assertThat(actual0.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual0.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_HELLO +
                updateList.get(0).message().from().firstName() + ".\n");
        Assertions.assertThat(actual1.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual1.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SELECT_COMMAND);
    }

    @Test
    public void INFOTest() {
        Long id = 50L;
        String command = Command.INFO.getTitle();
        List<Update> updateList = new ArrayList<>(List.of(
                generator.generateUpdateMessageWithReflection("", "", "", id, command,"", true),
                generator.generateUpdateMessageWithReflection("", "", "", id, command + " 1","", true),
                generator.generateUpdateCallbackQueryWithReflection("", "", "", id, command, true)));
        telegramBotUpdatesListener.process(updateList);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(6)).execute(argumentCaptor.capture());
        List<SendMessage> actualList = argumentCaptor.getAllValues();
        Assertions.assertThat(actualList.size()).isEqualTo(6);
        SendMessage actual0 = actualList.get(0);
        SendMessage actual1 = actualList.get(1);
        SendMessage actual2 = actualList.get(2);
        SendMessage actual3 = actualList.get(3);
        SendMessage actual4 = actualList.get(4);
        SendMessage actual5 = actualList.get(5);
        Assertions.assertThat(actual0.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual0.getParameters().get("text")).isEqualTo(TelegramBotSenderService.DEFAULT_MESSAGE_WITHOUT_CHOOSING_SHELTER);
        Assertions.assertThat(actual1.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual1.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SELECT_COMMAND);
        Assertions.assertThat(actual2.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual2.getParameters().get("text")).isEqualTo(TelegramBotSenderService.DEFAULT_MESSAGE_WITHOUT_CHOOSING_SHELTER);
        Assertions.assertThat(actual3.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual3.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SELECT_COMMAND);
        Assertions.assertThat(actual4.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual4.getParameters().get("text")).isEqualTo(TelegramBotSenderService.DEFAULT_MESSAGE_WITHOUT_CHOOSING_SHELTER);
        Assertions.assertThat(actual5.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual5.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SELECT_COMMAND);
    }

    @Test
    public void HOWTest() {
        Long id = 50L;
        String command = Command.HOW.getTitle();
        String command1 = Command.CAT_SHELTER.getTitle();
        List<Update> updateList;
        updateList = new ArrayList<>(List.of(
                generator.generateUpdateMessageWithReflection("", "", "", id, command1,"", true),
                generator.generateUpdateCallbackQueryWithReflection("", "", "", id, command1, true)));
        telegramBotUpdatesListener.process(updateList);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actualList = argumentCaptor.getAllValues();
        Assertions.assertThat(actualList.size()).isEqualTo(2);
        SendMessage actual0 = actualList.get(0);
        SendMessage actual1 = actualList.get(1);
        Assertions.assertThat(actual0.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual0.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SELECT_COMMAND);
        Assertions.assertThat(actual1.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual1.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SELECT_COMMAND);
    }

    @Test
    public void UNKNOWNCommandTest() {
        Long id = 50L;
        String command = "/fegfdhesfhdgmghrfdgg";
        List<Update> updateList = new ArrayList<>(List.of(
                generator.generateUpdateMessageWithReflection("", "", "", id, command,"", true)));
        telegramBotUpdatesListener.process(updateList);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actualList = argumentCaptor.getAllValues();
        Assertions.assertThat(actualList.size()).isEqualTo(2);
        SendMessage actual0 = actualList.get(0);
        SendMessage actual1 = actualList.get(1);
        Assertions.assertThat(actual0.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual0.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SORRY_I_DONT_KNOW_COMMAND);
        Assertions.assertThat(actual1.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual1.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SELECT_COMMAND);
    }

    @Test
    public void UnknownTextTest() {
        Long id = 50L;
        String command = "fegfdhesfhdgmghrfdgg";
        List<Update> updateList = new ArrayList<>(List.of(
                generator.generateUpdateMessageWithReflection("", "", "", id, command,"", true)));
        telegramBotUpdatesListener.process(updateList);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actualList = argumentCaptor.getAllValues();
        Assertions.assertThat(actualList.size()).isEqualTo(2);
        SendMessage actual0 = actualList.get(0);
        SendMessage actual1 = actualList.get(1);
        Assertions.assertThat(actual0.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual0.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SORRY_I_KNOW_THIS);
        Assertions.assertThat(actual1.getParameters().get("chat_id")).isEqualTo(id);
        Assertions.assertThat(actual1.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SELECT_COMMAND);
    }

    @Test
    public void EmptyCommandTest() {
        Long id = 50L;
        String command = Command.EMPTY_CALLBACK_DATA_FOR_BUTTON.getTitle();
        List<Update> updateList = new ArrayList<>(List.of(
                generator.generateUpdateCallbackQueryWithReflection("", "", "", id, command, true)));
        telegramBotUpdatesListener.process(updateList);
        Mockito.verify(telegramBot, times(0)).execute(any());
    }

    @Test
    public void CALL_REQUESTTest() {
        Long id1 = 50L;
        Long id2 = 51L;
        String command = Command.CALL_REQUEST.getTitle();
        String name1 = generator.generateNameIfEmpty("");
        String telegramName1 = generator.generateNameIfEmpty("");
        String phone1 = generator.generatePhoneIfEmpty("");
        String name2 = generator.generateNameIfEmpty("");
        String telegramName2 = generator.generateNameIfEmpty("");;

        List<Update> updateList = new ArrayList<>(List.of(
                generator.generateUpdateCallbackQueryWithReflection(telegramName1, name1, "", id1, command, false),
                generator.generateUpdateCallbackQueryWithReflection(telegramName2, name2, "", id2, command, false)));
        Chat chatExist = generator.generateChat(id1, name1, telegramName1, phone1, false,true
                , false);
        chatRepository.save(chatExist);
        Chat chatVolunteer = chatService.getChatOfVolunteer();

        telegramBotUpdatesListener.process(updateList);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(5)).execute(argumentCaptor.capture());
        List<SendMessage> actualList = argumentCaptor.getAllValues();
        Assertions.assertThat(actualList.size()).isEqualTo(5);
        SendMessage actual0 = actualList.get(0);
        SendMessage actual1 = actualList.get(1);
        SendMessage actual2 = actualList.get(2);
        SendMessage actual3 = actualList.get(3);
        SendMessage actual4 = actualList.get(4);

        Assertions.assertThat(actual0.getParameters().get("chat_id")).isEqualTo(id1);
        Assertions.assertThat(actual0.getParameters().get("text")).isEqualTo(CallRequestService.MESSAGE_OK_VOLUNTEERS_FOUND);

        Assertions.assertThat(actual1.getParameters().get("chat_id")).isEqualTo(chatVolunteer.getId());
        Assertions.assertThat(actual1.getParameters().get("text")).isEqualTo(
                CallRequestService.MESSAGE_ABOUT_CALL_REQUEST + name1 + "\n"+"Телеграм ID: @"+telegramName1 + "\n" + "Номер телефона: " +phone1 +"\n");

        Assertions.assertThat(actual2.getParameters().get("chat_id")).isEqualTo(id1);
        Assertions.assertThat(actual2.getParameters().get("text")).isEqualTo(TelegramBotSenderService.MESSAGE_SELECT_COMMAND);

        Assertions.assertThat(actual3.getParameters().get("chat_id")).isEqualTo(id2);
        Assertions.assertThat(actual3.getParameters().get("text")).isEqualTo(TelegramBotSenderService.DEFAULT_MESSAGE_WITHOUT_CHOOSING_SHELTER);

    }

}