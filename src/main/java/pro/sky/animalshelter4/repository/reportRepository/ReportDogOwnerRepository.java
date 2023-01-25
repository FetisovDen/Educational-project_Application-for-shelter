package pro.sky.animalshelter4.repository.reportRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter4.entity.reportEntity.ReportDogOwnerEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ReportDogOwnerRepository extends JpaRepository<ReportDogOwnerEntity, Long> {
    Optional<ReportDogOwnerEntity> findByFilePath(String filePath);
    @Modifying
    @Query(nativeQuery = true, value = "SELECT * FROM report_dog_owner WHERE DATE(time) = DATE(:date1)")
    List<ReportDogOwnerEntity> findAllByTimeContains(@Param(value = "date1")String date1);

    void deleteAllByChatId(Long chatId);

}
