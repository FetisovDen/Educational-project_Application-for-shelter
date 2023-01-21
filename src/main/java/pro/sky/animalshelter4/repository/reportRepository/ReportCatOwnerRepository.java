package pro.sky.animalshelter4.repository.reportRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter4.entity.reportEntity.ReportCatOwnerEntity;

import java.util.List;
import java.util.Optional;

@Repository

public interface ReportCatOwnerRepository extends JpaRepository<ReportCatOwnerEntity, Long> {
    Optional<ReportCatOwnerEntity> findByFilePath(String filePath);
    List<ReportCatOwnerEntity> findAllByTimeContains(String date);

    void deleteAllByChatId(Long chatId);

}
