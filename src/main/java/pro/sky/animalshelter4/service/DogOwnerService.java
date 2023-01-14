package pro.sky.animalshelter4.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter4.entity.DogOwner;
import pro.sky.animalshelter4.repository.DogOwnerRepository;

import java.util.Collection;

@Service
public class DogOwnerService {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesService.class);

    private final DogOwnerRepository dogOwnerRepository;

    public DogOwnerService(DogOwnerRepository dogOwnerRepository) {
        this.dogOwnerRepository = dogOwnerRepository;
    }

    /**
     * Create dog owner the repository method is used {@link JpaRepository#save(Object)}
     *
     * @param dogOwner by json
     * @return object dogOwner
     */

    public DogOwner create(DogOwner dogOwner) {
        logger.info("Use method create");
        return dogOwnerRepository.save(dogOwner);
    }

    /**
     * Find dog owner by id the repository method is used {@link JpaRepository#findById(Object)}
     *
     * @param id
     * @return object owner
     */

    public DogOwner read(long id) {
        logger.info("Use method read");
        return dogOwnerRepository.findById(id).get();
    }

    /**
     * Update dog owner the repository method is used {@link JpaRepository#save(Object)}
     *
     * @param dogOwner
     * @return object dogOwner
     */
    public DogOwner update(DogOwner dogOwner) {
        logger.info("Use method update");
        return dogOwnerRepository.save(dogOwner);
    }

    /**
     * Delete dog owner the repository method is used {@link JpaRepository#delete(Object)}
     *
     * @param id
     * @return object owner
     */
    public ResponseEntity<DogOwner> delete(long id) {
        logger.info("Use method delete");
        dogOwnerRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Find all dog owners the repository method is used {@link JpaRepository#findAll()}
     *
     * @return Collection DogOwner
     */
    public Collection<DogOwner> readAll() {
        logger.info("Use method update");
        return dogOwnerRepository.findAll();
    }
}
