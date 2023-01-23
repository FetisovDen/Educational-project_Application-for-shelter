package pro.sky.animalshelter4.service.reportService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.repository.chatRepository.ChatRepository;
import pro.sky.animalshelter4.service.ownerServise.CatOwnerService;
import pro.sky.animalshelter4.service.ownerServise.DogOwnerService;
import pro.sky.animalshelter4.service.tgBotService.TelegramBotSenderService;

import java.util.List;

@Service
public class ScheduleService {
    private final CatOwnerService catOwnerService;
    private final DogOwnerService dogOwnerService;
    private final TelegramBotSenderService telegramBotSenderService;
    private final ChatRepository chatRepository;
    private final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    public ScheduleService(CatOwnerService catOwnerService,
                           TelegramBotSenderService telegramBotSenderService,
                           ChatRepository chatRepository,
                           DogOwnerService dogOwnerService) {
        this.catOwnerService = catOwnerService;
        this.telegramBotSenderService = telegramBotSenderService;
        this.chatRepository = chatRepository;
        this.dogOwnerService = dogOwnerService;
    }

    @Scheduled(cron = "0 0 21 * * *")
    public void scheduledReport() {
        logger.info("scheduledReport started");
        List<Long> ownersWithOverdueReportingDay = catOwnerService.findAllOwnersWithOverdueReportingDay();
       ownersWithOverdueReportingDay.addAll(dogOwnerService.findAllOwnersWithOverdueReportingDay());
        ownersWithOverdueReportingDay.forEach(telegramBotSenderService::sendWarnAboutOverdue);
        ownersWithOverdueReportingDay = catOwnerService.findAllOwnersWithOverdueReportingTwoDay();
       ownersWithOverdueReportingDay.addAll(dogOwnerService.findAllOwnersWithOverdueReportingTwoDay());
        if (!ownersWithOverdueReportingDay.isEmpty()) {
            List<Long> listOwnerOverdueTwoDay = ownersWithOverdueReportingDay;
            chatRepository.findAllVolunteer().forEach(volunteersId ->
                    telegramBotSenderService.sendWarnForVolunteersAboutOverdueTwoDay(volunteersId, listOwnerOverdueTwoDay.toString()));
        }
    }
}
