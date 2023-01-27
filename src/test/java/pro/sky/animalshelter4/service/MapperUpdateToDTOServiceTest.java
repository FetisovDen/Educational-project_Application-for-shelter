package pro.sky.animalshelter4.service;

import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter4.Generator;
import pro.sky.animalshelter4.model.Command;
import pro.sky.animalshelter4.model.InteractionUnit;
import pro.sky.animalshelter4.recordDTO.UpdateDTO;
import pro.sky.animalshelter4.service.mapperService.MapperUpdateToDTOService;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MapperUpdateToDTOServiceTest {
    private static final Generator GENERATOR = new Generator();
    private final MapperUpdateToDTOService mapperUpdateToDTOService = new MapperUpdateToDTOService();

    @ParameterizedTest
    @MethodSource("paramForToDTO")
    void toDPO(Update updateTdo, UpdateDTO updateDTO) {
        UpdateDTO actual = updateDTO;
        UpdateDTO expected = mapperUpdateToDTOService.toDTO(updateTdo);

        if (actual != null) {
            assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
            assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
            assertThat(actual.getIdMedia()).isEqualTo(expected.getIdMedia());
            assertThat(actual.getIdChat()).isEqualTo(expected.getIdChat());
            assertThat(actual.getCommand()).isEqualTo(expected.getCommand());
            assertThat(actual.getInteractionUnit()).isEqualTo(expected.getInteractionUnit());
        }
        Assertions.assertEquals(actual, expected);
    }

    public static Stream<Arguments> paramForToDTO() {
        return Stream.of(
                //standard positive
                Arguments.of(
                        GENERATOR.generateUpdateMessageWithReflection(
                                "123",
                                "456",
                                "789",
                                50L,
                                Command.START.getTitle(),
                                null,
                                false),
                        new UpdateDTO(
                                50L,
                                "456",
                                "123",
                                Command.START,
                                "",
                                null,
                                null,
                                InteractionUnit.COMMAND
                        )
                ),
//                message text = Command + " " + text
                Arguments.of(
                        GENERATOR.generateUpdateMessageWithReflection(
                                "123",
                                "456",
                                "789",
                                50L,
                                Command.START.getTitle() + " fsfdsfs",
                                null,
                                false),
                        new UpdateDTO(
                                50L,
                                "456",
                                "123",
                                Command.START,
                                "fsfdsfs",
                                null,
                                null,
                                InteractionUnit.COMMAND
                        )
                ),
           //firstName = "", lastName = null, message = Command + " fsfdsfs sdfsdf sdf s "
                Arguments.of(
                        GENERATOR.generateUpdateMessageWithReflection(
                                "123",
                                "",
                                null,
                                50L,
                                Command.START.getTitle() + " fsfdsfs sdfsdf sdf s ",
                                "rer",
                                false),
                        new UpdateDTO(
                                50L,
                                "123",
                                "123",
                                Command.START,
                                "fsfdsfs sdfsdf sdf s",
                                null,
                                null,
                                InteractionUnit.COMMAND
                        )
                ),
         //firstName = null, lastName = null, userName=null, message = Command + Command
                Arguments.of(
                        GENERATOR.generateUpdateMessageWithReflection(
                                "123",
                                "",
                                null,
                                50L,
                                Command.START.getTitle() + " " + Command.INFO.getTitle(),
                                "rer",
                                false),
                        new UpdateDTO(
                                50L,
                                "123",
                                "123",
                                Command.START,
                                Command.INFO.getTitle(),
                                null,
                                null,
                                InteractionUnit.COMMAND
                        )
                ),
                //chatId = null
                Arguments.of(
                        GENERATOR.generateUpdateMessageWithReflection(
                                "123",
                                "456",
                                "789",
                                null,
                                Command.START.getTitle() + " " + Command.INFO.getTitle(),
                                "rer",
                                false),
                        null
                ),
                //Command = /sagfasd
                Arguments.of(
                        GENERATOR.generateUpdateMessageWithReflection(
                                "123",
                                "456",
                                "789",
                                50L,
                                "/sagfasd",
                                "rer",
                                false),
                        new UpdateDTO(
                                50L,
                                "456",
                                "123",
                                null,
                                "/sagfasd",
                                null,
                                null,
                                InteractionUnit.COMMAND
                        )
                ),
                //chatId < 0
                Arguments.of(
                        GENERATOR.generateUpdateMessageWithReflection(
                                "123",
                                "456",
                                "789",
                                -50L,
                                "/sagfasd",
                                "rer",
                                false),
                        null
                )
        );
    }


}