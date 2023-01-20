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
    private String choosingShelter;
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
                telegramBotSenderService.sendSorryIKnowThis(updateDTO.getIdChat(), 0,choosingShelter);
                return;
            case COMMAND:
                logger.info("ChatId={}; Method processUpdate start process command = {}",
                        updateDTO.getIdChat(), updateDTO.getCommand());
                if (updateDTO.getCommand() == null) {
                    telegramBotSenderService.sendUnknownProcess(updateDTO.getIdChat());
                    telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 0,choosingShelter);
                } else switch (updateDTO.getCommand()) {
                    case START:
                        choosingShelter = null;
                        System.out.println("Detected enter : " +
                                updateDTO.getIdChat() + " / " + updateDTO.getUserName());
                        telegramBotSenderService.sendStartButtons(updateDTO.getIdChat(), updateDTO.getUserName(), 0,choosingShelter);
                        break;
                    case CAT_SHELTER:
                        choosingShelter = "cat";
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 1,choosingShelter);
                        break;
                    case DOG_SHELTER:
                        choosingShelter = "dog";
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 1,choosingShelter);
                        break;
                    case INFO:
                        telegramBotSenderService.sendInfoAboutShelter(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 2,choosingShelter);
                        break;
                    case SCHEDULE_ADDRESS:
                        telegramBotSenderService.sendInfoAboutScheduleAndAddress(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 2,choosingShelter);
                        break;
                    case CAR_PASS:
                        telegramBotSenderService.sendInfoAboutCarPass(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 2,choosingShelter);
                        break;
                    case SAFETY_REGULATIONS:
                        telegramBotSenderService.sendInfoAboutSafetyRegulations(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 2,choosingShelter);
                        break;

                    case HOW:
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 3,choosingShelter);
                        break;
                    case DATING_RULES:
                        telegramBotSenderService.sendDogDatingRules(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 3,choosingShelter);
                        break;
                    case DOCS_FOR_TAKING:
                        telegramBotSenderService.sendDocsForTaking(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 3,choosingShelter);
                        break;
                    case TRANSPORTATION:
                        telegramBotSenderService.sendTransportationInfo(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 3,choosingShelter);
                        break;
                    case HOME_EQUIP_FOR_BABY:
                        telegramBotSenderService.sendHomeEquipForBaby(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 3,choosingShelter);
                        break;
                    case HOME_EQUIP_FOR_ADULT:
                        telegramBotSenderService.sendHomeEquipForAdult(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 3,choosingShelter);
                        break;
                    case HOME_EQUIP_FOR_PET_WITH_DISABILITIES:
                        telegramBotSenderService.sendHomeEquipForPetWithDisabilities(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 3,choosingShelter);
                        break;
                    case CYTOLOGIST_FIRST:
                        telegramBotSenderService.sendCytologistFirst(updateDTO.getIdChat());
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 3,choosingShelter);
                        break;
                    case CYTOLOGIST_CONTACTS:
                        telegramBotSenderService.sendCytologistContacts(updateDTO.getIdChat());
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 3,choosingShelter);
                        break;
                    case REASONS_FOR_REFUSAL:
                        telegramBotSenderService.sendReasonsForRefusal(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 3,choosingShelter);
                        break;
                    case REPORT:
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(),4,choosingShelter);

                    case LEAVE_NUMBER:
                        telegramBotSenderService.sendInfoAboutLeaveNumber(updateDTO.getIdChat(),choosingShelter);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 5,choosingShelter);
                        break;
                    case CALL_REQUEST:
                        callRequestService.process(updateDTO);
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 5,choosingShelter);
                        break;
                    case RETURN:
                        choosingShelter = null;
                        telegramBotSenderService.sendButtonsCommandForChat(updateDTO.getIdChat(), 0,choosingShelter);
                        break;
                    case EMPTY_CALLBACK_DATA_FOR_BUTTON:
                }
        }
    }

    private boolean detectEmptyCommand(Update update) {
        return update.callbackQuery() != null &&
                update.callbackQuery().data() != null &&
                update.callbackQuery().data().equals(Command.EMPTY_CALLBACK_DATA_FOR_BUTTON.getTitle());
    }

}

