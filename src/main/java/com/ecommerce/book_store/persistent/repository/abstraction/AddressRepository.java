package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.Address;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.userId = :userId ORDER BY a.isDefault DESC")
    List<Address> findByUserId(Long userId);
    Address findByUserIdAndIsDefaultTrue(Long userId);
    @Transactional
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.userId = :userId AND (:addressId IS NULL OR a.id <> :addressId)")
    void updateDefaultAddressStatus(@Param("userId") Long userId, @Param("addressId") Long addressId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Address a WHERE a.id = :addressId AND a.userId = :userId")
    void deleteByIdAndUserId(@Param("addressId") Long addressId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Address a WHERE a.id = :addressId")
    void deleteById(@Param("addressId") Long addressId);
}
