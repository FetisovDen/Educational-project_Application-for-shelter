package pro.sky.animalshelter4;

import com.github.javafaker.Faker;
import com.pengrad.telegrambot.model.*;
import pro.sky.animalshelter4.entity.chatEntity.Chat;
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;
import pro.sky.animalshelter4.entity.ownerEntity.DogOwnerEntity;
import pro.sky.animalshelter4.entity.reportEntity.ReportCatOwnerEntity;
import pro.sky.animalshelter4.entity.reportEntity.ReportDogOwnerEntity;
import pro.sky.animalshelter4.recordDTO.CatOwnerRecord;
import pro.sky.animalshelter4.recordDTO.DogOwnerRecord;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public class Generator {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    public int genInt(int max) {
        return random.nextInt(max);
    }

    public int genInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public LocalDateTime generateDateTime(boolean isPast, LocalDateTime localDateTime) {
        LocalDateTime tldt = LocalDateTime.now();
        if (isPast) {
            tldt = localDateTime.plusYears(1L);
            while (tldt.isBefore(localDateTime)) {
                LocalDate localDate = LocalDate.of(genInt(2020, 2022), genInt(12), genInt(25));
                LocalTime localTime = LocalTime.of(genInt(23), genInt(59));
                tldt = LocalDateTime.of(localDate, localTime);
            }
        } else {
            tldt = localDateTime.minusYears(1L);
            while (tldt.isAfter(localDateTime)) {
                LocalDate localDate = LocalDate.of(genInt(2020, 2022), genInt(12), genInt(25));
                LocalTime localTime = LocalTime.of(genInt(23), genInt(59));
                tldt = LocalDateTime.of(localDate, localTime);
            }
        }
        return tldt;
    }

    public Chat mapUpdateToChat(Update update) {
        Chat chat = new Chat();
        if (update.message() != null) {
            chat.setId(update.message().chat().id());
            chat.setName(generateNameIfEmpty(update.message().chat().firstName()));
        } else if (update.callbackQuery() != null) {
            chat.setId(update.callbackQuery().from().id());
            chat.setName(generateNameIfEmpty(update.callbackQuery().from().firstName()));
        }
        chat.setVolunteer(false);
        chat.setTelegramName(generatePhoneIfEmpty(""));
        return chat;
    }

    public Chat generateChat(Long idChat, String name, String telegramName, String phone,boolean isOwner, boolean isVolunteer, boolean needGenerate) {
        if (needGenerate) {
            idChat = generateIdIfEmpty(idChat);
            name = generateNameIfEmpty(name);
            telegramName = generateNameIfEmpty(telegramName);
            phone = generatePhoneIfEmpty(phone);
        }
        Chat chat = new Chat();
        chat.setId(idChat);
        chat.setName(name);
        chat.setTelegramName(telegramName);
        chat.setPhone(phone);
        chat.setOwner(isOwner);
        chat.setVolunteer(isVolunteer);
        return chat;
    }
    public CatOwnerEntity generateCatOwnerEntity(Long id, String ownerName, String petName, Timestamp startDate, Long dayToEndReporting, Chat chatOwner, boolean needGenerate) {
        if (needGenerate) {
            id = generateIdIfEmpty(id);
            ownerName = generateNameIfEmpty(ownerName);
            petName = generateNameIfEmpty(petName);
            startDate = generateStartDate(startDate);
            dayToEndReporting = generateIdIfEmpty(id);
            chatOwner = generateChat(id, ownerName,"","",false,false,true);
        }
        CatOwnerEntity catOwnerEntity = new CatOwnerEntity();
        catOwnerEntity.setId(id);
        catOwnerEntity.setOwnerName(ownerName);
        catOwnerEntity.setCatName(petName);
        catOwnerEntity.setStartDate(startDate);
        catOwnerEntity.setDayToEndReporting(dayToEndReporting);
        catOwnerEntity.setChatOwner(chatOwner);
        return catOwnerEntity;
    }
    public DogOwnerEntity generateDogOwnerEntity(Long id, String ownerName, String petName, Timestamp startDate, Long dayToEndReporting, Chat chatOwner, boolean needGenerate) {
        if (needGenerate) {
            id = generateIdIfEmpty(id);
            ownerName = generateNameIfEmpty(ownerName);
            petName = generateNameIfEmpty(petName);
            startDate = generateStartDate(startDate);
            dayToEndReporting = generateIdIfEmpty(id);
            chatOwner = generateChat(id, ownerName,"","",false,false,true);
        }
        DogOwnerEntity dogOwnerEntity = new DogOwnerEntity();
        dogOwnerEntity.setId(id);
        dogOwnerEntity.setOwnerName(ownerName);
        dogOwnerEntity.setDogName(petName);
        dogOwnerEntity.setStartDate(startDate);
        dogOwnerEntity.setDayToEndReporting(dayToEndReporting);
        dogOwnerEntity.setChatOwner(chatOwner);
        return dogOwnerEntity;
    }
    public CatOwnerRecord generateCatOwnerRecord(CatOwnerEntity catOwnerEntity, boolean needGenerate) {
        CatOwnerRecord catOwnerRecord  = new CatOwnerRecord();
        catOwnerRecord.setChatId(catOwnerEntity.getId());
        catOwnerRecord.setOwnerName(catOwnerEntity.getOwnerName());
        catOwnerRecord.setCatName(catOwnerEntity.getCatName());
        catOwnerRecord.setDayToEndReporting(catOwnerEntity.getDayToEndReporting());
        return catOwnerRecord;
    }
    public DogOwnerRecord generateDogOwnerRecord(DogOwnerEntity dogOwnerEntity, boolean needGenerate) {
        DogOwnerRecord dogOwnerRecord  = new DogOwnerRecord();
        dogOwnerRecord.setChatId(dogOwnerEntity.getId());
        dogOwnerRecord.setOwnerName(dogOwnerEntity.getOwnerName());
        dogOwnerRecord.setDogName(dogOwnerEntity.getDogName());
        dogOwnerRecord.setDayToEndReporting(dogOwnerEntity.getDayToEndReporting());
        return dogOwnerRecord;
    }
    public ReportCatOwnerEntity generateReportCatOwnerEntity(Long id, Long chatId, Timestamp time, boolean completedToday, String text, String filePath, CatOwnerEntity catOwner, boolean needGenerate) {
        if(needGenerate){
        id = generateIdIfEmpty(id);
        chatId = generateIdIfEmpty(chatId);
        time = generateStartDate(time);
        text = generateMessageIfEmpty(text);
        filePath = generateAddressIfEmpty(filePath);
        catOwner = generateCatOwnerEntity(id, "", "", null, null, null, true);}
        ReportCatOwnerEntity reportCatOwnerEntity = new ReportCatOwnerEntity();
        reportCatOwnerEntity.setId(id);
        reportCatOwnerEntity.setChatId(chatId);
        reportCatOwnerEntity.setTime(time);
        reportCatOwnerEntity.setText(text);
        reportCatOwnerEntity.setFilePath(filePath);
        reportCatOwnerEntity.setCatOwner(catOwner);
        return reportCatOwnerEntity;

    }
    public ReportDogOwnerEntity generateReportDogOwnerEntity(Long id, Long chatId, Timestamp time, boolean completedToday, String text, String filePath, DogOwnerEntity dogOwnerEntity, boolean needGenerate) {
        if(needGenerate){
            id = generateIdIfEmpty(id);
            chatId = generateIdIfEmpty(chatId);
            time = generateStartDate(time);
            text = generateMessageIfEmpty(text);
            filePath = generateAddressIfEmpty(filePath);
            dogOwnerEntity = generateDogOwnerEntity(id, "", "", null, null, null, true);}
        ReportDogOwnerEntity reportDogOwnerEntity = new ReportDogOwnerEntity();
        reportDogOwnerEntity.setId(id);
        reportDogOwnerEntity.setChatId(chatId);
        reportDogOwnerEntity.setTime(time);
        reportDogOwnerEntity.setText(text);
        reportDogOwnerEntity.setFilePath(filePath);
        reportDogOwnerEntity.setDogOwner(dogOwnerEntity);
        return reportDogOwnerEntity;

    }
    public Update generateUpdateCallbackQueryWithReflection(String userName,
                                                            String firstName,
                                                            String lastName,
                                                            Long chatId,
                                                            String callbackQueryData,
                                                            boolean needGenerate) {
        if (needGenerate) {
            userName = generateNameIfEmpty(userName);
            firstName = generateNameIfEmpty(firstName);
            lastName = generateNameIfEmpty(lastName);
            chatId = generateIdIfEmpty(chatId);
            callbackQueryData = generateMessageIfEmpty(callbackQueryData);
        }

        Update update = new Update();
        CallbackQuery callbackQuery = new CallbackQuery();
        User user = new User(0L);

        try {
            Field userNameField = user.getClass().getDeclaredField("username");
            userNameField.setAccessible(true);
            Field firstNameField = user.getClass().getDeclaredField("first_name");
            firstNameField.setAccessible(true);
            Field lastNameField = user.getClass().getDeclaredField("last_name");
            lastNameField.setAccessible(true);
            Field userId = user.getClass().getDeclaredField("id");
            userId.setAccessible(true);
            userNameField.set(user, userName);
            firstNameField.set(user, firstName);
            lastNameField.set(user, lastName);
            userId.set(user, chatId);

            Field callbackUserField = callbackQuery.getClass().getDeclaredField("from");
            callbackUserField.setAccessible(true);
            Field callbackDataField = callbackQuery.getClass().getDeclaredField("data");
            callbackDataField.setAccessible(true);
            callbackUserField.set(callbackQuery, user);
            callbackDataField.set(callbackQuery, callbackQueryData);

            Field updateCallbackField = update.getClass().getDeclaredField("callback_query");
            updateCallbackField.setAccessible(true);
            updateCallbackField.set(update, callbackQuery);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return update;
    }

    public Update generateUpdateMessageWithReflection() {
        return generateUpdateMessageWithReflection("", "", "", -1L,"","", true);
    }

    public Update generateUpdateMessageWithReflection(String userName,
                                                      String firstName,
                                                      String lastName,
                                                      Long chatId,
                                                      String messageText,
                                                      String reportText,
                                                      boolean needGenerate) {
        if (needGenerate) {
            userName = generateNameIfEmpty(userName);
            firstName = generateNameIfEmpty(firstName);
            lastName = generateNameIfEmpty(lastName);
            messageText = generateMessageIfEmpty(messageText);
            chatId = generateIdIfEmpty(chatId);
            reportText = generateReportTextIfEmpty(reportText);
        }

        Update update = new Update();
        Message message = new Message();
        com.pengrad.telegrambot.model.Chat chat = new com.pengrad.telegrambot.model.Chat();
        User user = new User(0L);
        PhotoSize photoSize = new PhotoSize();


        try {
            Field userNameField = user.getClass().getDeclaredField("username");
            userNameField.setAccessible(true);
            Field firstNameField = user.getClass().getDeclaredField("first_name");
            firstNameField.setAccessible(true);
            Field lastNameField = user.getClass().getDeclaredField("last_name");
            lastNameField.setAccessible(true);
            Field userId = user.getClass().getDeclaredField("id");
            userId.setAccessible(true);
            userNameField.set(user, userName);
            firstNameField.set(user, firstName);
            lastNameField.set(user, lastName);
            userId.set(user, chatId);


            Field chatIdField = chat.getClass().getDeclaredField("id");
            chatIdField.setAccessible(true);
            chatIdField.set(chat, chatId);

            Field messageTextField = message.getClass().getDeclaredField("text");
            messageTextField.setAccessible(true);
            Field messageChatField = message.getClass().getDeclaredField("chat");
            messageChatField.setAccessible(true);
            Field messageUserField = message.getClass().getDeclaredField("from");
            messageUserField.setAccessible(true);
            messageTextField.set(message, messageText);
            messageChatField.set(message, chat);
            messageUserField.set(message, user);

            Field updateMessageField = update.getClass().getDeclaredField("message");
            updateMessageField.setAccessible(true);
            updateMessageField.set(update, message);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return update;
    }


    public String generateAddressIfEmpty(String address) {
        if (address == null || address.length() == 0) {
            return faker.address().streetAddress();
        }
        return address;
    }

    public String generatePhoneIfEmpty(String phone) {
        if (phone == null || phone.length() == 0) {
            String tempPhone = faker.phoneNumber().phoneNumber();
            if (tempPhone.length() > 15) {
                tempPhone = tempPhone.substring(0, 14);
            }
            return tempPhone;
        }
        return phone;
    }

    public String generateNameIfEmpty(String name) {
        if (name == null || name.length() == 0) {
            return faker.name().username();
        }
        return name;
    }

    public Long generateIdIfEmpty(Long id) {
        if (id == null || id < 0) {
            long idTemp = -1L;
            //id with <100 I leave for my needs
            while (idTemp < 100) {
                idTemp = faker.random().nextLong(999_999_999 - 100_000_000) + 100_000_000;
            }
            return idTemp;
        }
        return id;
    }

    public String generateMessageIfEmpty(String message) {
        if (message == null || message.length() == 0) {
            return faker.lordOfTheRings().character();
        }
        return message;
    }
    private String generateReportTextIfEmpty(String reportText) {
        if (reportText == null || reportText.length() == 0) {
            return faker.lordOfTheRings().location();
        }
        return reportText;
    }
    private Timestamp generateStartDate(Timestamp startDate) {
        if (startDate==null) {
            return null;
        }
        return startDate;
    }

    public Update generateUpdateMessagePhotoWithReflection(String userName,
                                                           String firstName,
                                                           String lastName,
                                                           Long chatId,
                                                           String captionText,
                                                           String file_id,
                                                           boolean needGenerate) {
        if (needGenerate) {
            userName = generateNameIfEmpty(userName);
            firstName = generateNameIfEmpty(firstName);
            lastName = generateNameIfEmpty(lastName);
            captionText = generateMessageIfEmpty(captionText);
            file_id = generateMessageIfEmpty(file_id);
            chatId = generateIdIfEmpty(chatId);
        }

        Update update = new Update();
        Message message = new Message();
        com.pengrad.telegrambot.model.Chat chat = new com.pengrad.telegrambot.model.Chat();
        com.pengrad.telegrambot.model.User user = new com.pengrad.telegrambot.model.User(0L);
        PhotoSize[] photo = {new PhotoSize(), new PhotoSize(), new PhotoSize()};

        try {
            Field userNameField = user.getClass().getDeclaredField("username");
            userNameField.setAccessible(true);
            Field firstNameField = user.getClass().getDeclaredField("first_name");
            firstNameField.setAccessible(true);
            Field lastNameField = user.getClass().getDeclaredField("last_name");
            lastNameField.setAccessible(true);
            Field userId = user.getClass().getDeclaredField("id");
            userId.setAccessible(true);
            userNameField.set(user, userName);
            firstNameField.set(user, firstName);
            lastNameField.set(user, lastName);
            userId.set(user, chatId);


            Field chatIdField = chat.getClass().getDeclaredField("id");
            chatIdField.setAccessible(true);
            chatIdField.set(chat, chatId);

            Field photo0 = photo[0].getClass().getDeclaredField("file_id");
            Field photo01 = photo[0].getClass().getDeclaredField("file_unique_id");
            Field photo1 = photo[0].getClass().getDeclaredField("width");
            Field photo2 = photo[0].getClass().getDeclaredField("height");
            Field photo3 = photo[0].getClass().getDeclaredField("file_size");
            photo0.setAccessible(true);
            photo01.setAccessible(true);
            photo1.setAccessible(true);
            photo2.setAccessible(true);
            photo3.setAccessible(true);
            photo0.set(photo[0], file_id);
            photo01.set(photo[0],"AQADvsUxGwQ3mUp4");
            photo1.set(photo[0], 90);
            photo2.set(photo[0], 60);
            photo3.set(photo[0], 1274L);

            Field photo00 = photo[1].getClass().getDeclaredField("file_id");
            Field photo02 = photo[0].getClass().getDeclaredField("file_unique_id");
            Field photo11 = photo[1].getClass().getDeclaredField("width");
            Field photo22 = photo[1].getClass().getDeclaredField("height");
            Field photo33 = photo[1].getClass().getDeclaredField("file_size");
            photo00.setAccessible(true);
            photo02.setAccessible(true);
            photo11.setAccessible(true);
            photo22.setAccessible(true);
            photo33.setAccessible(true);
            photo00.set(photo[1], file_id);
            photo02.set(photo[1],"AQADvsUxGwQ3mUp4");
            photo11.set(photo[1], 90);
            photo22.set(photo[1], 60);
            photo33.set(photo[1], 1274L);

            Field photo000 = photo[2].getClass().getDeclaredField("file_id");
            Field photo03 = photo[0].getClass().getDeclaredField("file_unique_id");
            Field photo111 = photo[2].getClass().getDeclaredField("width");
            Field photo222 = photo[2].getClass().getDeclaredField("height");
            Field photo333 = photo[2].getClass().getDeclaredField("file_size");
            photo03.setAccessible(true);
            photo000.setAccessible(true);
            photo111.setAccessible(true);
            photo222.setAccessible(true);
            photo333.setAccessible(true);
            photo000.set(photo[2], file_id);
            photo03.set(photo[2],"AQADvsUxGwQ3mUp4");
            photo111.set(photo[2], 90);
            photo222.set(photo[2], 60);
            photo333.set(photo[2], 1274L);


            Field messageChatField = message.getClass().getDeclaredField("chat");
            messageChatField.setAccessible(true);
            Field messageUserField = message.getClass().getDeclaredField("from");
            messageUserField.setAccessible(true);
            Field messagePhotoSize = message.getClass().getDeclaredField("photo");
            messagePhotoSize.setAccessible(true);
            messageChatField.set(message, chat);
            messageUserField.set(message, user);
            messagePhotoSize.set(message, photo);

            Field updateMessageField = update.getClass().getDeclaredField("message");
            updateMessageField.setAccessible(true);
            updateMessageField.set(update, message);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return update;
    }


}
