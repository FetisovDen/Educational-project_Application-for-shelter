package pro.sky.animalshelter4.controller.ownerController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<CatOwnerRecord> readCatOwner(@PathVariable(name = "id") long chatId){
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

    @Operation(summary = "Добавление дней испытательного срока по id",
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
    public ResponseEntity<CatOwnerRecord> addDaysToEnd(@PathVariable(name = "id") long chatId,
                                                       @RequestParam int days){
        catOwnerService.addDays(chatId, days);
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
