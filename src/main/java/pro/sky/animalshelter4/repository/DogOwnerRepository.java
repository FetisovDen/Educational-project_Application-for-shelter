package pro.sky.animalshelter4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter4.entity.DogOwner;


public interface DogOwnerRepository extends JpaRepository<DogOwner, Long> {
}
