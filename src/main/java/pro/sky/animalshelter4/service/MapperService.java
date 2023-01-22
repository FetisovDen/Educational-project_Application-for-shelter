package pro.sky.animalshelter4.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.model.Command;
import pro.sky.animalshelter4.model.InteractionUnit;
import pro.sky.animalshelter4.model.UpdateDTO;

@Service
public class MapperService {

    private final Logger logger = LoggerFactory.getLogger(MapperService.class);

    public UpdateDTO toDTO(Update update) {
//update
        if (update == null) {
            logger.debug("Method toDTO detected null update");
            return null;
        }
        UpdateDTO updateDTO = new UpdateDTO();
//message!=null
        if (update.message() != null) {
            logger.debug("Method toDTO detected message into update");
//message from
            if (update.message().from() != null &&
                    update.message().from().id() != null) {
                if (update.message().from().id() < 0) {
                    logger.error("Method toDTO detected userId < 0");
                    return null;
                }
                updateDTO.setIdChat(update.message().from().id());
                updateDTO.setUserName(toUserName(update.message().from()));
                logger.debug("ChatId={}; Method toDTO detected idChat", updateDTO.getIdChat());
            } else {
                logger.error("Method toDTO detected null user in update.message()");
                return null;
            }
//message photo
            if (update.message().photo() != null) {
                logger.debug("ChatId={}; Method toDTO detected photo in message()", updateDTO.getIdChat());
                updateDTO.setInteractionUnit(InteractionUnit.PHOTO);
                int maxPhotoIndex = update.message().photo().length - 1;
                if (update.message().photo()[maxPhotoIndex].fileId() != null) {
                    updateDTO.setIdMedia(update.message().photo()[maxPhotoIndex].fileId());
                } else {
                    logger.debug("ChatId={}; Method toDTO detected null fileId in photo", updateDTO.getIdChat());
                }
            }
//message text
            if (update.message().text() != null) {
                logger.debug("ChatId={}; Method toDTO detected text in message()", updateDTO.getIdChat());
                updateDTO.setInteractionUnit(InteractionUnit.MESSAGE);
                updateDTO.setMessage(update.message().text().trim());
//callbackQuery!=null
            }
        } else if (update.callbackQuery() != null) {
            logger.debug("Method toDTO detected callbackQuery into update");
            updateDTO.setInteractionUnit(InteractionUnit.CALLBACK_QUERY);
//callbackQuery from
            if (update.callbackQuery().from() != null &&
                    update.callbackQuery().from().id() != null) {
                if (update.callbackQuery().from().id() < 0) {
                    logger.error("Method toDTO detected userId < 0");
                    return null;
                }
                updateDTO.setIdChat(update.callbackQuery().from().id());
                updateDTO.setUserName(toUserName(update.callbackQuery().from()));
                logger.debug("ChatId={}; Method toDTO detected idChat", updateDTO.getIdChat());
            } else {
                logger.error("Method toDTO detected null user in update.callbackQuery()");
                return null;
            }
//callbackQuery data
            if (update.callbackQuery().data() != null) {
                logger.debug("ChatId={}; Method toDPO detected data in callbackQuery()", updateDTO.getIdChat());
                updateDTO.setInteractionUnit(InteractionUnit.MESSAGE);
                updateDTO.setMessage(update.callbackQuery().data().trim());
            }
        }
//updateDpo.Message -> Command
        if (updateDTO.getMessage() != null && updateDTO.getMessage().startsWith("/")) {
            updateDTO.setInteractionUnit(InteractionUnit.COMMAND);
            updateDTO.setCommand(Command.fromString(
                    toWord(updateDTO.getMessage(), 0)));
            if (updateDTO.getCommand() != null) {
                logger.debug("ChatId={}; Method toDTO detected command = {}",
                        updateDTO.getIdChat(), updateDTO.getCommand().getTitle());
                if (updateDTO.getCommand().getTitle().trim().length() >= updateDTO.getCommand().getTitle().length()) {
                    updateDTO.setMessage(updateDTO.getMessage().
                            substring(
                                    updateDTO.getCommand().getTitle().length()).
                            trim());
                }
            }
        } else {
            logger.debug("ChatId={}; Method toDTO don't detected command in callbackQuery()", updateDTO.getIdChat());
            updateDTO.setCommand(null);
        }
        return updateDTO;
    }

    private boolean isNotNullOrEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public String toWord(String s, int indexWord) {
        logger.debug("Method toWord was start for parse from string = {} word # = {}", s, indexWord);

        if (!s.contains(TelegramBotSenderService.REQUEST_SPLIT_SYMBOL)) {
            logger.debug("Method toWord don't found REQUEST_SPLIT_SYMBOL = {} and return",
                    TelegramBotSenderService.REQUEST_SPLIT_SYMBOL);
            return s;
        }
        String[] sMas = s.split(TelegramBotSenderService.REQUEST_SPLIT_SYMBOL);

        if (indexWord >= sMas.length) {
            logger.debug("Method toWord detect index of word bigger of sum words in string and return empty string");
            return "";
        }
        logger.debug("Method toWord return {}", sMas[indexWord]);
        return sMas[indexWord];
    }

    public Long toChatId(Update update) {
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

    public String toUserName(User user) {
        String name = "";
        if (isNotNullOrEmpty(user.firstName())) {
            name = user.firstName();
        } else if (isNotNullOrEmpty(user.lastName())) {
            name = user.lastName();
        } else if (isNotNullOrEmpty(user.username())) {
            name = user.username();
        }
        return name;
    }
}