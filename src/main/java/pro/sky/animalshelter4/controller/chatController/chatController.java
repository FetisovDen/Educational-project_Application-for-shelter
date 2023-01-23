package pro.sky.animalshelter4.controller.chatController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter4.entity.chatEntity.Chat;
import pro.sky.animalshelter4.recordDTO.CatOwnerRecord;
import pro.sky.animalshelter4.service.chatTgService.ChatService;

import java.util.List;

@RestController
@RequestMapping("chat")
public class chatController {
    private final ChatService chatService;

    public chatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @Operation(summary = "Поиск владельца чата по его Id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о чате, найденная по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "Chat"
    )
    @GetMapping("{id}")
    public ResponseEntity<Chat> readChatById(@PathVariable(name = "id") long chatId){
        return ResponseEntity.ok(chatService.findChat(chatId));
    }

    @Operation(summary = "Поиск владельца чата по его User Name в Telegram",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о чате, найденная по User Name в Telegram",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "Chat"
    )
    @GetMapping("tgname/{telegramName}")
    public ResponseEntity<List<Chat>> readChatByTgName(@PathVariable(name = "telegramName") String telegramName){
        return ResponseEntity.ok(chatService.findChatByTgName(telegramName));
    }




    @Operation(summary = "Удаление чата из БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Чат удален",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatOwnerRecord.class)
                            )
                    )
            }, tags = "Chat"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Chat> deleteChat(@PathVariable(name = "id") long chatId){
        chatService.deleteChat(chatId);
        return ResponseEntity.ok().build();
    }
}
