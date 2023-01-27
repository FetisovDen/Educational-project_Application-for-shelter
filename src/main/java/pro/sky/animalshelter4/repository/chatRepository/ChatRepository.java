package pro.sky.animalshelter4.repository.chatRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter4.entity.chatEntity.Chat;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat getChatById(Long id);

    @Query(value = "select * from chat where is_volunteer=true limit 1"
            , nativeQuery = true)
    Chat getChatOfVolunteer();
    @Query(value = "select id from chat where is_volunteer=true"
            , nativeQuery = true)
    List<Long> findAllVolunteer();

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE chat SET is_owner = true WHERE id = :id")
    void makeChatIsOwnerTrue(@Param(value = "id") Long chatId);

    List<Chat> findByTelegramName(String telegramName);

}
