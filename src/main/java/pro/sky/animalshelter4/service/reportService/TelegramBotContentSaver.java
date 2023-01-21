package pro.sky.animalshelter4.service.reportService;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.entity.reportEntity.ReportCatOwnerEntity;
import pro.sky.animalshelter4.service.ownerServise.CatOwnerService;
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

    private final ReportCatOwnerService reportCatOwnerService;

    public TelegramBotContentSaver(@Value("${path.to.materials.folder}") String materialsDir,
                                   TelegramBotSenderService telegramBotSenderService,
                                   TelegramBot telegramBot,
                                   CatOwnerService catOwnerService,
                                   ReportCatOwnerService reportCatOwnerService
    ) {
        this.materialsDir = materialsDir;
        this.telegramBotSenderService = telegramBotSenderService;
        this.telegramBot = telegramBot;
        this.catOwnerService = catOwnerService;
        this.reportCatOwnerService = reportCatOwnerService;
    }

    public void savePhoto(Update update, String reportText) throws IOException {
        logger.debug("method savePhoto started");
        int maxPhotoIndex = update.message().photo().length - 1;
        Long idChat = update.message().chat().id();

        if (checkOwner(idChat)) {
            if (reportText == null){
                telegramBotSenderService.sendAddText(idChat);
            }
            else {
            logger.debug("ChatId={}; Method savePhoto go to save photo: width = {}, heugh = {}, file size = {}",
                    idChat,
                    update.message().photo()[maxPhotoIndex].width(),
                    update.message().photo()[maxPhotoIndex].height(),
                    update.message().photo()[maxPhotoIndex].fileSize());
            GetFile getFile = new GetFile(update.message().photo()[maxPhotoIndex].fileId());
            String url = telegramBot.getFullFilePath(telegramBot.execute(getFile).file());
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Path path = Path.of(materialsDir, idChat + "_" + LocalDateTime.now().format(format) + ".jpg");
            try (InputStream is = new URL(url).openStream()) {
                byte[] img = is.readAllBytes();
                Files.createDirectories(path.getParent());
                Files.write(path, img);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
                ReportCatOwnerEntity reportCatOwner = new ReportCatOwnerEntity();
                reportCatOwner.setChatId(idChat);
                reportCatOwner.setTime(Timestamp.valueOf(LocalDateTime.now()));
                reportCatOwner.setCompletedToday(true);
                reportCatOwner.setText(reportText);
                reportCatOwner.setFilePath(path.toString());
                reportCatOwner.setCatOwner(catOwnerService.findCatOwnerById(idChat));
                reportCatOwnerService.create(reportCatOwner);
                telegramBotSenderService.successfulReportMessage(idChat);

            }
        } else {
            telegramBotSenderService.sendSorryIKnowThis(idChat, 0, null);
        }
    }

    public boolean checkOwner(Long idChat) {
        if (catOwnerService.catOwnerFindById(idChat)) {
            return true;
        }
        logger.debug("Method checkOwner == false");
        return false;
    }


}
