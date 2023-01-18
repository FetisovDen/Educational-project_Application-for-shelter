package pro.sky.animalshelter4.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import pro.sky.animalshelter4.info.InfoCatShelterImpl;
import pro.sky.animalshelter4.info.InfoDogShelterImpl;
import pro.sky.animalshelter4.info.InfoShelter;
import pro.sky.animalshelter4.model.Command;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class TelegramBotSenderService {
    public static final String EMPTY_SYMBOL_FOR_BUTTON = " ";

    public static final String REQUEST_SPLIT_SYMBOL = " ";
    public static final String MESSAGE_SELECT_COMMAND = "Выбор действия:";
    public static final String MESSAGE_SORRY_I_DONT_KNOW_COMMAND = "Прошу прощения, я не знаю такой команды";
    public static final String MESSAGE_SORRY_I_KNOW_THIS = "Прошу прощения.\n Я знаю только данные команды:\n";
    public static final String MESSAGE_HELLO = "Привет, ";


    private final Logger logger = LoggerFactory.getLogger(TelegramBotSenderService.class);
    private final TelegramBot telegramBot;
    private final ChatService chatService;

    public TelegramBotSenderService(TelegramBot telegramBot, ChatService chatService) {
        this.telegramBot = telegramBot;
        this.chatService = chatService;
    }

    public void sendMessage(Long idChat, String textMessage) {
        logger.info("ChatId={}; Method sendMessage was started for send a message : {}", idChat, textMessage);
        SendMessage sendMessage = new SendMessage(idChat, textMessage);
        SendResponse response = telegramBot.execute(sendMessage);
        if (response == null) {
            logger.debug("ChatId={}; Method sendMessage did not receive a response", idChat);
        } else if (response.isOk()) {
            logger.debug("ChatId={}; Method sendMessage has completed sending the message", idChat);
        } else {
            logger.debug("ChatId={}; Method sendMessage received an error : {}", idChat, response.errorCode());
        }
    }

    public void sendUnknownProcess(Long idChat) {
        logger.info("ChatId={}; Method sendUnknownProcess was started for send a message about unknown command",
                idChat);
        sendMessage(idChat, MESSAGE_SORRY_I_DONT_KNOW_COMMAND);
    }

    public void sendStartButtons(Long idChat, String userName, int stage, String choosingShelter) {
        logger.info("ChatId={}; Method sendStartButtons was started for send a welcome message", idChat);
        sendMessage(idChat, MESSAGE_HELLO + userName + ".\n");
        sendButtonsCommandForChat(idChat, stage, choosingShelter);
    }

    public void sendSorryIKnowThis(Long idChat, int stage, String choosingShelter) {
        logger.info("ChatId={}; Method processWhatICan was started for send ability", idChat);
        sendMessage(idChat, MESSAGE_SORRY_I_KNOW_THIS);
        sendButtonsCommandForChat(idChat, stage, choosingShelter);
    }

    public InfoShelter checkWhatShelter(String choosingShelter) {
        if (choosingShelter.equals("cat")) {
            return new InfoCatShelterImpl();
        }
        if (choosingShelter.equals("dog")) {
            return new InfoDogShelterImpl();
        }
        return new InfoShelter() {
            @Override
            public String datingRules() {
                return null;
            }

            @Override
            public String homeEquipForBaby() {
                return null;
            }

            @Override
            public String homeEquipForAdult() {
                return null;
            }

            @Override
            public String homeEquipForPetWithDisabilities() {
                return null;
            }
        };
    }

    public void sendInfoAboutShelter(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendInfoAboutShelter was started for send info about shelter", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).infoAboutShelter());
    }

    public void sendInfoAboutScheduleAndAddress(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendInfoAboutScheduleAndAddress was started for send info about shelter", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).infoAboutScheduleAndAddress());
    }

    public void sendInfoAboutCarPass(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendInfoAboutCarPass was started for send info about shelter", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).infoAboutCarPass());
    }

    public void sendInfoAboutSafetyRegulations(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendInfoAboutSafetyRegulations was started for send info about shelter", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).infoAboutSafetyRegulations());
    }

    public void sendInfoAboutLeaveNumber(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendInfoAboutLeaveNumber was started for send info about shelter", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).leaveNumber());
    }

    public void sendDogDatingRules(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendDogDatingRules was started for send how take a dog", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).datingRules());
    }
    public void sendDocsForTaking(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendDocsForTaking was started for send how take a dog", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).docsForTaking());
    }

    public void sendTransportationInfo(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendTransportationInfo was started for send how take a dog", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).transportation());
    }

    public void sendHomeEquipForBaby(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendHomeEquipForBaby was started for send how take a dog", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).homeEquipForBaby());
    }

    public void sendHomeEquipForAdult(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendHomeEquipForAdult was started for send how take a dog", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).homeEquipForAdult());
    }

    public void sendHomeEquipForPetWithDisabilities(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendHomeEquipForPetWithDisabilities was started for send how take a dog", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).homeEquipForPetWithDisabilities());
    }

    public void sendCytologistFirst(Long idChat) {
        logger.info("ChatId={}; Method sendCytologistFirst was started for send how take a dog", idChat);
        sendMessage(idChat, InfoDogShelterImpl.cytologistFirst());
    }

    public void sendCytologistContacts(Long idChat) {
        logger.info("ChatId={}; Method sendCytologistContacts was started for send how take a dog", idChat);
        sendMessage(idChat, InfoDogShelterImpl.cytologistContacts());
    }

    public void sendReasonsForRefusal(Long idChat, String choosingShelter) {
        logger.info("ChatId={}; Method sendReasonsForRefusal was started for send how take a dog", idChat);
        sendMessage(idChat, checkWhatShelter(choosingShelter).reasonsForRefusal());
    }

    public void sendButtonsWithDifferentData(
            Long idChat,
            String caption,
            List<String> nameButtons,
            List<String> dataButtons,
            int width, int height) {
        logger.info("ChatId={}; Method sendButtonsWithDifferentData was started for send buttons", idChat);
        if (nameButtons.size() != dataButtons.size()) {
            logger.debug("ChatId={}; Method sendButtonsWithDifferentData detect different size of Lists", idChat);
            return;
        }
        InlineKeyboardButton[][] tableButtons = new InlineKeyboardButton[height][width];
        int indexLists = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (indexLists < nameButtons.size()) {
                    tableButtons[i][j] = new InlineKeyboardButton(nameButtons.get(indexLists))
                            .callbackData(dataButtons.get(indexLists));
                } else {
                    tableButtons[i][j] = new InlineKeyboardButton(EMPTY_SYMBOL_FOR_BUTTON)
                            .callbackData(Command.EMPTY_CALLBACK_DATA_FOR_BUTTON.getTitle());
                }
                indexLists++;
            }
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(tableButtons);
        SendMessage message = new SendMessage(idChat, caption).replyMarkup(inlineKeyboardMarkup);
        SendResponse response = telegramBot.execute(message);
        if (response == null) {
            logger.debug("ChatId={}; Method sendButtonsWithDifferentData did not receive a response", idChat);
        } else if (response.isOk()) {
            logger.debug("ChatId={}; Method sendButtonsWithDifferentData has completed sending the message", idChat);
        } else {
            logger.debug("ChatId={}; Method sendButtonsWithDifferentData received an error : {}",
                    idChat, response.errorCode());
        }
    }

    public void sendButtonsCommandForChat(Long idChat, int stage, String pet) {
        logger.info("ChatId={}; Method sendListCommandForChat was started for send list of command", idChat);
        boolean isVolunteer = chatService.isVolunteer(idChat);
        Command.regulatingCommands(stage, pet);
        List<String> nameList = Command.getPairListsForButtonExcludeHide(isVolunteer, stage).getFirst();
        List<String> dataList = Command.getPairListsForButtonExcludeHide(isVolunteer, stage).getSecond();
        int countButtons = nameList.size();
        int width = 0;
        int height = 0;

        if (countButtons == 0) {
            logger.debug("ChatId={}; Method sendButtonsCommandForChat detected count of command = 0", idChat);
            return;
        }
        if (countButtons == 1) {
            width = 1;
            height = 1;
        } else if (countButtons % 7 == 0) {
            width = 7;
            height = countButtons / 7;
        } else if (countButtons % 5 == 0) {
            width = 5;
            height = countButtons / 5;
        } else if (countButtons % 3 == 0) {
            width = 3;
            height = countButtons / 3;
        } else if (countButtons % 2 == 0) {
            width = 2;
            height = countButtons / 2;
        } else if (countButtons % 9 == 0) {
            width = 9;
            height = countButtons / 9;
        }
        sendButtonsWithDifferentData(
                idChat,
                MESSAGE_SELECT_COMMAND,
                nameList,
                dataList,
                width, height);
    }

    public void sendPhoto(Long idChat, String pathFile) throws IOException {
        Path path = Paths.get(pathFile);
        byte[] file = Files.readAllBytes(path);
        SendPhoto sendPhoto = new SendPhoto(idChat, file);
        telegramBot.execute(sendPhoto).message();
    }
}

