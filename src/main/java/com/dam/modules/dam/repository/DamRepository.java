package com.dam.modules.dam.repository;

import com.dam.modules.dam.model.Dam;
import com.dam.modules.dam.model.DamStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DamRepository extends JpaRepository<Dam, Long> {
    @Query(nativeQuery = true, value = "select * from dam ")
    List<Dam> getDams(Pageable pageable);

    List<Dam> findAll(Specification<Dam> spec, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from dam ")
    List<Dam> findDamByIsFahliOrhasOrHasLangesh(int isFahli, int hasLangesh, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from dam where id =:damId     ")
    Optional<Dam> findDam(@Param("damId") String damId);

    @Query(nativeQuery = true, value = "select * from dam  where damdari_id =:damdariId  and  " +
            "( :isFahli is null or  is_fahli = :isFahli ) and   " +
            "( :hasLangesh is null or  has_langesh = :hasLangesh ) and   " +
            "( :typeId is null or  type_id = :typeId )   " +
            " ")
    List<Dam> findDamsOfDamdari(@Param("damdariId") String damdariId, @Param("isFahli") String isFahli, @Param("hasLangesh") String hasLangesh, @Param("typeId") String typeId, Pageable pageable);

}
