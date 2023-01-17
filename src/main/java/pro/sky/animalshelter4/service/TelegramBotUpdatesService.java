package pro.sky.animalshelter4.service;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.model.Command;
import pro.sky.animalshelter4.model.UpdateDTO;

import java.io.IOException;

@Service
public class TelegramBotUpdatesService {
    private int choosingShelter;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesService.class);
    private final TelegramBotSenderService telegramBotSenderService;
    private final MapperService mapperService;
    private final TelegramBotContentSaver telegramBotContentSaver;
    private final CallRequestService callRequestService;

    public TelegramBotUpdatesService(TelegramBotSenderService telegramBotSenderService, MapperService mapperService, TelegramBotContentSaver telegramBotContentSaver, CallRequestService callRequestService) {
        this.telegramBotSenderService = telegramBotSenderService;
        this.mapperService = mapperService;
        this.telegramBotContentSaver = telegramBotContentSaver;
        this.callRequestService = callRequestService;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            logger.debug("Method processUpdate detected null update");
            return;
        }
        if (detectEmptyCommand(update)) {
            logger.debug("Method processUpdate detected empty command");
            return;
        }
        UpdateDTO updateDTO = mapperService.toDTO(update);
        if (updateDTO == null) {
            logger.debug("Method processUpdate detected null updateDTO");
            return;
        }

        switch (updateDTO.getInteractionUnit()) {
            case PHOTO:
                logger.debug("ChatId={}; Method processUpdate detected photo in message()", updateDTO.getIdChat());
                try {
                    telegramBotContentSaver.savePhoto(update);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            case MESSAGE:
                telegramBotSenderService.sendSorryIKnowThis(updateDTO.getIdChat(),0);
                return;
            case COMMAND:
                logger.info("ChatId={}; Method processUpdate start process command = {}",
                        updateDTO.getIdChat(), updateDTO.getCommand());
                if (updateDTO.getCommand() == null) {
                    telegramBotSenderService.sendUnknownProcess(updateDTO.getIdChat());
                    telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),0);
                } else switch (updateDTO.getCommand()) {
                    case START:
                        System.out.println("Detected enter : " +
                                updateDTO.getIdChat() + " / " + updateDTO.getUserName());
                        telegramBotSenderService.sendStartButtons(updateDTO.getIdChat(), updateDTO.getUserName(),0);
                        break;
                    case CAT_SHELTER:
                        choosingShelter = 1;
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),1);
                        break;
                    case DOG_SHELTER:
                        choosingShelter = 2;
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),1);
                        break;
                    case INFO:
                        telegramBotSenderService.sendInfoAboutShelter(updateDTO.getIdChat());
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),2);
                        break;
                    case SCHEDULE:
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),2);
                        break;
                    case CAR_PASS:
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),2);
                        break;
                    case SAFETY_PRECAUTIONS:
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),2);
                        break;
                    case LEAVE_NUMBER:
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),2);
                        break;

                    case HOW:
                        telegramBotSenderService.sendDogDatingRules(updateDTO.getIdChat());
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),3);
                        break;

                    case CALL_REQUEST:
                        callRequestService.process(updateDTO);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),5);
                        break;
                    case RETURN:
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),0);
                        break;
                    case EMPTY_CALLBACK_DATA_FOR_BUTTON:
                        return;
                }
        }
    }

    private boolean detectEmptyCommand(Update update) {
        return update.callbackQuery() != null &&
                update.callbackQuery().data() != null &&
                update.callbackQuery().data().equals(Command.EMPTY_CALLBACK_DATA_FOR_BUTTON.getTitle());
    }

}

