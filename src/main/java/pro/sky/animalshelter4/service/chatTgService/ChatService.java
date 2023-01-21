package pro.sky.animalshelter4.service.chatTgService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.entity.chatEntity.Chat;
import pro.sky.animalshelter4.repository.chatRepository.ChatRepository;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat getChatByIdOrNewWithName(Long id, String name,String userName) {
        logger.info("Method getChatByIdOrNew was start for find Chat by id = {}, or return new Chat", id);
        Chat chat = chatRepository.getChatById(id);
        if (chat == null) {
            logger.debug("Method getChatByIdOrNew will return the new chat");
            chat = new Chat();
            chat.setId(id);
        }
        chat.setName(name);
        chat.setTelegramName(userName);
        chatRepository.save(chat);
        logger.debug("Method getChatByIdOrNew will return the found chat");
        return chat;
    }

    public Chat addChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Chat findChat(Long id) {
        return chatRepository.getChatById(id);
    }

    public void deleteChat(Long id) {
        Chat chat = new Chat();
        chat.setId(id);
        deleteChat(chat);
    }

    public void deleteChat(Chat chat) {
        chatRepository.delete(chat);
    }

    public boolean isVolunteer(Long id) {
        logger.info("Method isVolunteer was start for to check if the chat with id = {} is a volunteer", id);
        Chat chat = findChat(id);
        if (chat == null || !chat.isVolunteer()) {
            logger.debug("Method isVolunteer detected volunteer by idChat = {}", id);
            return false;
        }
        logger.debug("Method isVolunteer don't detected volunteer by idChat = {}", id);
        return true;
    }
    public boolean isOwner(Long id) {
        logger.info("Method isOwner was start for to check if the chat with id = {} is a owner", id);
        Chat chat = findChat(id);
        if (chat == null || !chat.isOwner()) {
            logger.debug("Method isVolunteer detected volunteer by idChat = {}", id);
            return false;
        }
        logger.debug("Method isVolunteer don't detected volunteer by idChat = {}", id);
        return true;
    }

    public Chat getChatOfVolunteer() {
        return chatRepository.getChatOfVolunteer();
//        return null;
    }


}
