package pro.sky.animalshelter4.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.info.InfoAboutShelter;
import pro.sky.animalshelter4.component.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class TelegramBotSenderService {
    public static final String EMPTY_SYMBOL_FOR_BUTTON = " ";

    public static final String REQUEST_SPLIT_SYMBOL = " ";
    public static final String MESSAGE_SELECT_COMMAND = "Select action";

    private final static String MESSAGE_UNKNOWN = "I don't know this command";

    private final Logger logger = LoggerFactory.getLogger(TelegramBotSenderService.class);
    private final TelegramBot telegramBot;

    public TelegramBotSenderService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public SendResponse sendMessage(Long idChat, String textMessage) {
        logger.info("ChatId={}; Method sendMessage was started for send a message : {}", idChat, textMessage);
        SendMessage sendMessage = new SendMessage(idChat, textMessage);
        SendResponse response = telegramBot.execute(sendMessage);
        if (response.isOk()) {
            logger.debug("ChatId={}; Method sendMessage has completed sending the message", idChat);
        } else {
            logger.debug("ChatId={}; Method sendMessage received an error : {}", idChat, response.errorCode());
        }
        return response;
    }

    public void sendUnknownProcess(Update update) {
        String firstName = update.message().from().firstName();
        Long idChat = getChatId(update);
        logger.info("ChatId={}; Method sendUnknownProcess was started for send a message about unknown command", idChat);
        sendMessage(idChat, "Sorry, " + firstName + ". " + MESSAGE_UNKNOWN);
    }

    public void sendStart(Update update) {
        String firstName = update.message().from().firstName();
        Long idChat = getChatId(update);
        logger.info("ChatId={}; Method sendStart was started for send a welcome message", idChat);
        sendMessage(idChat, "Hello " + firstName + ".\n" +
                "I know some command:\n" + Command.getAllValuesFromNewLineExcludeHideCommand());
    }

    public void sendStartButtons(Update update) {
        String firstName = update.message().from().firstName();
        Long idChat = getChatId(update);
        logger.info("ChatId={}; Method sendStartButtons was started for send a welcome message", idChat);
        sendMessage(idChat, "Hello " + firstName + ".\n");
        sendButtonsCommandForChat(update);
    }

    public void sendSorryWhatICan(Update update) {
        Long idChat = getChatId(update);
        logger.info("ChatId={}; Method processWhatICan was started for send ability", idChat);
        sendMessage(idChat, "Sorry." + "\n" +
                "I know only this command:\n" + Command.getAllValuesFromNewLineExcludeHideCommand());
    }

    public void sendInfoAboutShelter(Update update) {
        Long idChat = getChatId(update);
        logger.info("ChatId={}; Method sendInfoAboutShelter was started for send send info about shelter", idChat);
        sendMessage(idChat, InfoAboutShelter.getInfoEn());
    }

    public void sendButtonsWithOneData(Long idChat, String caption, String command, List<String> nameButtons, List<String> dataButtons, int width, int height) {
        logger.info("ChatId={}; Method sendButtonsWithCommonData was started for send buttons", idChat);
        if (nameButtons.size() != dataButtons.size()) {
            logger.debug("ChatId={}; Method sendButtonsWithCommonData detect different size of Lists", idChat);
            return;
        }
        InlineKeyboardButton[][] tableButtons = new InlineKeyboardButton[height][width];
        int countNameButtons = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (countNameButtons < nameButtons.size()) {
                    tableButtons[i][j] = new InlineKeyboardButton(nameButtons.get(countNameButtons))
                            .callbackData(command + REQUEST_SPLIT_SYMBOL + dataButtons.get(countNameButtons));
                } else {
                    tableButtons[i][j] = new InlineKeyboardButton(EMPTY_SYMBOL_FOR_BUTTON)
                            .callbackData(Command.EMPTY_CALLBACK_DATA_FOR_BUTTON.getTitle());
                }
                countNameButtons++;
            }
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(tableButtons);
        SendMessage message = new SendMessage(idChat, caption).replyMarkup(inlineKeyboardMarkup);
        SendResponse response = telegramBot.execute(message);
        if (response.isOk()) {
            logger.debug("ChatId={}; Method sendButtonsWithCommonData has completed sending the message", idChat);
        } else {
            logger.debug("ChatId={}; Method sendButtonsWithCommonData received an error : {}", idChat, response.errorCode());
        }
    }

    public void sendButtonsWithDifferentData(Long idChat, String caption, List<String> nameButtons, List<String> dataButtons, int width, int height) {
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
        if (response.isOk()) {
            logger.debug("ChatId={}; Method sendButtonsWithDifferentData has completed sending the message", idChat);
        } else {
            logger.debug("ChatId={}; Method sendButtonsWithDifferentData received an error : {}", idChat, response.errorCode());
        }
    }


    public void sendListCommandForChat(Update update) {
        Long idChat = getChatId(update);
        logger.info("ChatId={}; Method sendListCommandForChat was started for send list of command", idChat);
        sendMessage(idChat, Command.getAllValuesFromNewLineExcludeHideCommand());
    }

    public void sendButtonsCommandForChat(Update update) {
        Long idChat = getChatId(update);
        logger.info("ChatId={}; Method sendListCommandForChat was started for send list of command", idChat);
        int countButtons = Command.getListsForButtonExcludeHideCommand().getFirst().size();
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
        }
        sendButtonsWithDifferentData(
                idChat,
                MESSAGE_SELECT_COMMAND,
                Command.getListsForButtonExcludeHideCommand().
                        getFirst(),
                Command.getListsForButtonExcludeHideCommand().
                        getSecond(),
                width, height);
    }

    public Long getChatId(Update update) {
        if (update.message() != null &&
                update.message().from() != null &&
                update.message().from().id() != null) {
            return update.message().from().id();
        } else if (update.callbackQuery() != null &&
                update.callbackQuery().from() != null &&
                update.callbackQuery().from().id() != null) {
            return update.callbackQuery().from().id();
        }
        return null;
    }

    public void sendPhoto(Update update, String pathFile) throws IOException {
        Long idChat = update.message().chat().id();
        Path path = Paths.get(pathFile);
        byte[] file = Files.readAllBytes(path);
        SendPhoto sendPhoto = new SendPhoto(idChat, file);
        telegramBot.execute(sendPhoto).message();
    }

}
