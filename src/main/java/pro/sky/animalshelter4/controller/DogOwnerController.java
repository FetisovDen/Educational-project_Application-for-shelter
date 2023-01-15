package pro.sky.animalshelter4.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter4.entity.DogOwner;
import pro.sky.animalshelter4.service.DogOwnerService;

import java.util.Collection;
/**
 * Class Controller DogOwnerController
 * CRUD - Create, Read, Update, Delete
 */
@RestController
@RequestMapping(path = "/dog_owner")
public class DogOwnerController {


    private final DogOwnerService dogOwnerService;

    public DogOwnerController(DogOwnerService dogOwnerService) {
        this.dogOwnerService = dogOwnerService;
    }

    @Operation(summary = "Create dog owner",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Create dog owner",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwner.class)
                            )
                    )
            })
    @PostMapping("/creat")
    public DogOwner create(@RequestBody DogOwner dogOwner) {
        return dogOwnerService.create(dogOwner);
    }

    @Operation(summary = "Find dog owner by id ",
            responses = {

                    @ApiResponse(
                            responseCode = "200",
                            description = "Find dog owner by id ",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = DogOwner.class))
                            )
                    )
            })
    @GetMapping("/read/{id}")
    public DogOwner read(@PathVariable long id) {
        return dogOwnerService.read(id);

    }

    @Operation(summary = "Update info about dog owner",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Update info about dog owner",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DogOwner.class)
                    )

            )
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<DogOwner> update(@RequestBody DogOwner dogOwner) {
        DogOwner dgOwner = dogOwnerService.update(dogOwner);
        if (dgOwner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dgOwner);

    }

    @Operation(summary = "Delete dog owner by id",
            responses = {

                    @ApiResponse(
                            responseCode = "200",
                            description = "Delete dog owner by id ",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = DogOwner.class))
                            )
                    )
            })

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {
        dogOwnerService.delete(id);

    }

    @Operation(summary = "Find all dog owners",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find all dog owners",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwner[].class)
                            )
                    )
            })
    @GetMapping("/readAll")
    public Collection<DogOwner> read() {
        return dogOwnerService.readAll();
    }

}
