package pro.sky.animalshelter4.service.reportService;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.entity.chatEntity.Chat;
import pro.sky.animalshelter4.entity.reportEntity.ReportCatOwnerEntity;
import pro.sky.animalshelter4.entity.reportEntity.ReportDogOwnerEntity;
import pro.sky.animalshelter4.recordDTO.UpdateDTO;
import pro.sky.animalshelter4.service.chatTgService.ChatService;
import pro.sky.animalshelter4.service.ownerServise.CatOwnerService;
import pro.sky.animalshelter4.service.ownerServise.DogOwnerService;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotSenderService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TelegramBotContentSaver {
    private final String materialsDir;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotContentSaver.class);
    private final TelegramBotSenderService telegramBotSenderService;
    private final TelegramBot telegramBot;
    private final CatOwnerService catOwnerService;
    private final ChatService chatService;
    private final DogOwnerService dogOwnerService;
    private final ReportCatOwnerService reportCatOwnerService;
    private final ReportDogOwnerService reportDogOwnerService;

    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TelegramBotContentSaver(@Value("${path.to.materials.folder}") String materialsDir,
                                   TelegramBotSenderService telegramBotSenderService,
                                   TelegramBot telegramBot,
                                   ChatService chatService,
                                   CatOwnerService catOwnerService,
                                   DogOwnerService dogOwnerService,
                                   ReportCatOwnerService reportCatOwnerService,
                                   ReportDogOwnerService reportDogOwnerService
    ) {
        this.materialsDir = materialsDir;
        this.telegramBotSenderService = telegramBotSenderService;
        this.telegramBot = telegramBot;
        this.reportDogOwnerService = reportDogOwnerService;
        this.dogOwnerService = dogOwnerService;
        this.catOwnerService = catOwnerService;
        this.reportCatOwnerService = reportCatOwnerService;
        this.chatService = chatService;
    }

    public void saveReport(Update update, String reportText) throws IOException {
        logger.debug("method savePhoto started");
        int maxPhotoIndex = update.message().photo().length - 1;
        Long idChat = update.message().chat().id();
        String checkOwner = checkOwner(idChat);
        if (checkOwner != null) {
            if (reportText == null) {
                telegramBotSenderService.sendAddText(idChat);
            } else {
                logger.debug("ChatId={}; Method savePhoto go to save photo: width = {}, heugh = {}, file size = {}",
                        idChat,
                        update.message().photo()[maxPhotoIndex].width(),
                        update.message().photo()[maxPhotoIndex].height(),
                        update.message().photo()[maxPhotoIndex].fileSize());
                GetFile getFile = new GetFile(update.message().photo()[maxPhotoIndex].fileId());
                String url = telegramBot.getFullFilePath(telegramBot.execute(getFile).file());
                Path path = Path.of(materialsDir, idChat + "_" + LocalDateTime.now().format(format) + ".jpg");
                try (InputStream is = new URL(url).openStream()) {
                    byte[] img = is.readAllBytes();
                    Files.createDirectories(path.getParent());
                    Files.write(path, img);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (checkOwner.equals("cat")) {
                    ReportCatOwnerEntity reportCatOwner = new ReportCatOwnerEntity();
                    reportCatOwner.setChatId(idChat);
                    reportCatOwner.setTime(Timestamp.valueOf(LocalDateTime.now()));
                    reportCatOwner.setCompletedToday(true);
                    reportCatOwner.setText(reportText);
                    reportCatOwner.setFilePath(path.toString());
                    reportCatOwner.setCatOwner(catOwnerService.findCatOwnerById(idChat));
                    reportCatOwnerService.create(reportCatOwner);
                    telegramBotSenderService.successfulReportMessage(idChat);
                } else if (checkOwner.equals("dog")) {
                    ReportDogOwnerEntity reportDogOwner = new ReportDogOwnerEntity();
                    reportDogOwner.setChatId(idChat);
                    reportDogOwner.setTime(Timestamp.valueOf(LocalDateTime.now()));
                    reportDogOwner.setCompletedToday(true);
                    reportDogOwner.setText(reportText);
                    reportDogOwner.setFilePath(path.toString());
                    reportDogOwner.setDogOwner(dogOwnerService.findDogOwnerById(idChat));
                    reportDogOwnerService.create(reportDogOwner);
                    telegramBotSenderService.successfulReportMessage(idChat);
                }
            }
        } else {
            telegramBotSenderService.sendSorryIKnowThis(idChat, 0, null);
        }
    }

    public void savePhone(UpdateDTO updateDTO) {
        logger.info("method savePhone started");
        if (updateDTO.getMessage().matches("^\\+\\d{11}$|^\\d{11}$")) {
            logger.info("method savePhone found phone and save it");
            Chat chat = chatService.getChatByIdOrNewWithName(updateDTO.getIdChat(), updateDTO.getName(), updateDTO.getUserName());
            chat.setPhone(updateDTO.getMessage());
            chatService.addChat(chat);
            telegramBotSenderService.successfulPhoneSave(chat.getId());
        } else {
            logger.info("method savePhone did not find phone");
            telegramBotSenderService.unSuccessfulPhoneSave(updateDTO.getIdChat());
        }
    }


    public String checkOwner(Long idChat) {
        if (catOwnerService.catOwnerFindById(idChat)) {
            return "cat";
        } else if (dogOwnerService.dogOwnerFindById(idChat)) {
            return "dog";
        }
        logger.debug("Method checkOwner == false");
        return null;
    }


}
