package pro.sky.animalshelter4.repository.ownerReository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter4.entity.ownerEntity.CatOwnerEntity;




@Repository
public interface CatOwnerRepository extends JpaRepository<CatOwnerEntity, Long> {

}
