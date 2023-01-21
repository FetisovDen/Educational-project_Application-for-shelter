package pro.sky.animalshelter4.repository.chatRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter4.entity.chatEntity.Chat;


@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat getChatById(Long id);

    @Query(value = "select * from chat where is_volunteer=true limit 1"
            , nativeQuery = true)
    Chat getChatOfVolunteer();

}
