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
import pro.sky.animalshelter4.entity.reportEntity.ReportCatOwnerEntity;
import pro.sky.animalshelter4.service.reportService.ReportCatOwnerService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("cat/report")
public class ReportCatOwnerController {
    private final ReportCatOwnerService reportCatOwnerService;

    public ReportCatOwnerController(ReportCatOwnerService reportCatOwnerService) {
        this.reportCatOwnerService = reportCatOwnerService;
    }


    @Operation(summary = "Найти отчеты по дате",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "список отчетов по дате",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportCatOwnerEntity.class))
                            )
                    )
            }, tags = "CatOwnerReport"
    )
    @GetMapping("/date")
    public ResponseEntity<List<ReportCatOwnerEntity>> findAllByDate(@RequestParam String date){
        return ResponseEntity.ok(reportCatOwnerService.readAllByDay(date));
    }

    @Operation(summary = "Удаление отчетов по id владельца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список удаленных отчетов",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportCatOwnerEntity.class))
                            )
                    )
            }, tags = "CatOwnerReport"
    )
    @DeleteMapping("{chatId}")
    public ResponseEntity<List<ReportCatOwnerEntity>> deleteByChatId(@Parameter(description = "Owner chatId") @PathVariable Long chatId){
        reportCatOwnerService.clear(chatId);
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
            }, tags = "CatOwnerReport"
    )
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) throws IOException {
        byte[] image = reportCatOwnerService.readFromFile(id);
        return ResponseEntity.ok()
                .contentLength(image.length)
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
}
