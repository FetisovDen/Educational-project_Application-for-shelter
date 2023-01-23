package pro.sky.animalshelter4.repository.reportRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter4.entity.reportEntity.ReportDogOwnerEntity;

import java.util.List;
import java.util.Optional;

@Repository

public interface ReportDogOwnerRepository extends JpaRepository<ReportDogOwnerEntity, Long> {
    Optional<ReportDogOwnerEntity> findByFilePath(String filePath);
    List<ReportDogOwnerEntity> findAllByTimeContains(String date);

    void deleteAllByChatId(Long chatId);

}
