package pro.sky.animalshelter4.controller.reportController;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter4.entity.reportEntity.ReportDogOwnerEntity;
import pro.sky.animalshelter4.service.reportService.ReportDogOwnerService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("dog/report")
public class ReportDogOwnerController {
    private final ReportDogOwnerService reportDogOwnerService;

    public ReportDogOwnerController(ReportDogOwnerService reportDogOwnerService) {
        this.reportDogOwnerService = reportDogOwnerService;
    }


    @Operation(summary = "Найти отчеты по дате",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "список отчетов по дате",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportDogOwnerEntity.class))
                            )
                    )
            }, tags = "DogOwnerReport"
    )
    @GetMapping()
    public ResponseEntity<List<ReportDogOwnerEntity>> findAllByDate(@Parameter(description = "Дата в формате ДД.ММ.ГГГГ")@RequestParam String date){
        return ResponseEntity.ok(reportDogOwnerService.readAllByDay(date));
    }

    @Operation(summary = "Удаление отчетов по id владельца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список удаленных отчетов",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportDogOwnerEntity.class))
                            )
                    )
            }, tags = "DogOwnerReport"
    )
    @DeleteMapping("{chatId}/delete")
    public ResponseEntity<List<ReportDogOwnerEntity>> deleteByChatId(@Parameter(description = "Owner chatId") @PathVariable Long chatId){
        reportDogOwnerService.clear(chatId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Вывод изображения из отчета по его Id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "изображение отчета",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = byte[].class)
                            )
                    )
            }, tags = "DogOwnerReport"
    )
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) throws IOException {
        byte[] image = reportDogOwnerService.readFromFile(id);
        return ResponseEntity.ok()
                .contentLength(image.length)
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
}
