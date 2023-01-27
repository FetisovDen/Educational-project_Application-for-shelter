package pro.sky.animalshelter4.repository.reportRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter4.entity.reportEntity.ReportCatOwnerEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ReportCatOwnerRepository extends JpaRepository<ReportCatOwnerEntity, Long> {
    Optional<ReportCatOwnerEntity> findByFilePath(String filePath);
    @Modifying
    @Query(nativeQuery = true, value = "SELECT * FROM report_cat_owner WHERE DATE(time) = DATE(:date1)")
    List<ReportCatOwnerEntity> findAllByTimeContains(String date1);

    void deleteAllByChatId(Long chatId);

}
