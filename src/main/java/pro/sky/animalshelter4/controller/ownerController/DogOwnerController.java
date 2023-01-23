package pro.sky.animalshelter4.controller.ownerController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter4.entity.ownerEntity.DogOwnerEntity;
import pro.sky.animalshelter4.recordDTO.DogOwnerRecord;
import pro.sky.animalshelter4.service.ownerServise.DogOwnerService;

@RestController
@RequestMapping("dog/owner")
public class DogOwnerController {
    private final DogOwnerService dogOwnerService;

    public DogOwnerController(DogOwnerService dogOwnerService) {
        this.dogOwnerService = dogOwnerService;
    }

    @Operation(summary = "Создать нового владельца собаки",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "владелец создан",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwnerRecord.class)
                            )
                    )
            }, tags = "DogOwner"
    )
    @PostMapping
    public ResponseEntity<DogOwnerRecord> createDogOwner(@RequestBody DogOwnerRecord record){
        return ResponseEntity.status(HttpStatus.CREATED).body(dogOwnerService.createDogOwner(record));
    }

    @Operation(summary = "Поиск владельца собаки по Id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о владельце, найденная по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwnerRecord.class)
                            )
                    )
            }, tags = "DogOwner"
    )
    @GetMapping("{id}")
    public ResponseEntity<DogOwnerEntity> readDogOwner(@PathVariable(name = "id") long chatId){
        return ResponseEntity.ok(dogOwnerService.readDogOwner(chatId));
    }

    @Operation(summary = "Изменить данные владельца собаки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Корректировки внесены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwnerRecord.class)
                            )
                    )
            }, tags = "DogOwner"
    )
    @PutMapping
    public ResponseEntity<DogOwnerRecord> updateDogOwner(@RequestBody DogOwnerRecord record){
        return ResponseEntity.ok(dogOwnerService.updateDogOwner(record));
    }

    @Operation(summary = "Добавление дней испытательного срока по id (Добавить можно 14, либо 30 дней)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Дни до окончания отчетного периода обновленны",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwnerRecord.class)
                            )
                    )
            }, tags = "DogOwner"
    )
    @PutMapping("{id}/add")
    public ResponseEntity<String> addDaysToEnd(@PathVariable(name = "id") long chatId,
                                                       @RequestParam int days){
        return ResponseEntity.ok(dogOwnerService.addDays(chatId, days));
    }

    @Operation(summary = "Предупредить о том, что отчет заполняется плохо. Необходимо ввести Id владельца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Владелец уведомлен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwnerRecord.class)
                            )
                    )
            }, tags = "DogOwner"
    )
    @PutMapping("{id}/warn")
    public ResponseEntity<DogOwnerRecord> warnAboutPoorReport(@PathVariable(name = "id") long chatId){
        dogOwnerService.warnAboutPoorReport(chatId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Поздравить владельца с успешным окончанием отчетного периода.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Владелец уведомлен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwnerRecord.class)
                            )
                    )
            }, tags = "DogOwner"
    )
    @PutMapping("{id}/approval")
    public ResponseEntity<DogOwnerRecord> warnAboutApproval(@PathVariable(name = "id") long chatId){
        dogOwnerService.warnAboutApproval(chatId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Отказть владельцу. Сообщить ему об этом и отослать дальнейшие действия",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Владелец уведомлен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwnerRecord.class)
                            )
                    )
            }, tags = "DogOwner"
    )
    @PutMapping("{id}/refuse")
    public ResponseEntity<DogOwnerRecord> warnAboutRefuse(@PathVariable(name = "id") long chatId){
        dogOwnerService.warnAboutRefuse(chatId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Удаление владельца собаки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные удалены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwnerRecord.class)
                            )
                    )
            }, tags = "DogOwner"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<DogOwnerRecord> deleteDogOwner(@PathVariable(name = "id") long chatId){
        dogOwnerService.deleteDogOwner(chatId);
        return ResponseEntity.ok().build();
    }
}
