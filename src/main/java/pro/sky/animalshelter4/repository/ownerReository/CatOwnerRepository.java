package pro.sky.animalshelter4.repository.ownerReository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface CatOwnerRepository extends JpaRepository<CatOwnerEntity, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE cat_owner SET day_to_end_reporting = day_to_end_reporting + :days WHERE id = :id")
    void addDaysForCatOwner(
            @Param(value = "id") long chatId,
            @Param(value = "days") int days);

    @Query(nativeQuery = true, value = "SELECT day_to_end_reporting FROM cat_owner  WHERE id = :id")
    Integer findDayToEndReportingById( @Param(value = "id") long chatId);

    @Query(nativeQuery = true, value = "SELECT DISTINCT cat_owner_id FROM (SELECT cat_owner_id, max(time) FROM report_cat_owner GROUP BY cat_owner_id HAVING max(time) < current_date) as new")
    List<Long> findAllOverdueReportingDay ();
    @Query(nativeQuery = true, value = "SELECT DISTINCT cat_owner_id FROM (SELECT cat_owner_id, max(time) FROM report_cat_owner GROUP BY cat_owner_id HAVING max(time) < (current_date-1)) as new")
    List<Long> findAllOwnersWithOverdueReportingTwoDay();
}
