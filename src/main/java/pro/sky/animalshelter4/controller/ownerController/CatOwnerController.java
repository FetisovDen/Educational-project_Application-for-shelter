package pro.sky.animalshelter4.controller.ownerController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;
import pro.sky.animalshelter4.recordDTO.CatOwnerRecord;
import pro.sky.animalshelter4.service.ownerServise.CatOwnerService;

@RestController
@RequestMapping("cat/owner")
public class CatOwnerController {
    private final CatOwnerService catOwnerService;

    public CatOwnerController(CatOwnerService catOwnerService) {
        this.catOwnerService = catOwnerService;
    }

    @Operation(summary = "Создать нового владельца кошки",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "владелец создан",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "CatOwner"
    )
    @PostMapping
    public ResponseEntity<CatOwnerRecord> createCatOwner(@RequestBody CatOwnerRecord record){
        return ResponseEntity.status(HttpStatus.CREATED).body(catOwnerService.createCatOwner(record));
    }

    @Operation(summary = "Поиск владельца кошки по Id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о владельце, найденная по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "CatOwner"
    )
    @GetMapping("{id}")
    public ResponseEntity<CatOwnerEntity> readCatOwner(@PathVariable(name = "id") long chatId){
        return ResponseEntity.ok(catOwnerService.readCatOwner(chatId));
    }

    @Operation(summary = "Изменить данные владельца кошки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Корректировки внесены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "CatOwner"
    )
    @PutMapping
    public ResponseEntity<CatOwnerRecord> updateCatOwner(@RequestBody CatOwnerRecord record){
        return ResponseEntity.ok(catOwnerService.updateCatOwner(record));
    }

    @Operation(summary = "Добавление дней испытательного срока по id (Добавить можно 14, либо 30 дней)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Дни до окончания отчетного периода обновленны",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "CatOwner"
    )
    @PutMapping("{id}/add")
    public ResponseEntity<String> addDaysToEnd(@PathVariable(name = "id") long chatId,
                                                       @RequestParam int days){
        return ResponseEntity.ok(catOwnerService.addDays(chatId, days));
    }

    @Operation(summary = "Предупредить о том, что отчет заполняется плохо. Необходимо ввести Id владельца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Владелец уведомлен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "CatOwner"
    )
    @PutMapping("{id}/warn")
    public ResponseEntity<CatOwnerRecord> warnAboutPoorReport(@PathVariable(name = "id") long chatId){
        catOwnerService.warnAboutPoorReport(chatId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Поздравить владельца с успешным окончанием отчетного периода.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Владелец уведомлен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "CatOwner"
    )
    @PutMapping("{id}/approval")
    public ResponseEntity<CatOwnerRecord> warnAboutApproval(@PathVariable(name = "id") long chatId){
        catOwnerService.warnAboutApproval(chatId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Отказть владельцу. Сообщить ему об этом и отослать дальнейшие действия",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Владелец уведомлен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "CatOwner"
    )
    @PutMapping("{id}/refuse")
    public ResponseEntity<CatOwnerRecord> warnAboutRefuse(@PathVariable(name = "id") long chatId){
        catOwnerService.warnAboutRefuse(chatId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Удаление владельца кошки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные удалены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "CatOwner"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<CatOwnerRecord> deleteCatOwner(@PathVariable(name = "id") long chatId){
        catOwnerService.deleteCatOwner(chatId);
        return ResponseEntity.ok().build();
    }
}
