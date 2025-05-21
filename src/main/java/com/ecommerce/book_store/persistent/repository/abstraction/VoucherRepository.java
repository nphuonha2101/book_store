package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query("SELECT v FROM Voucher v JOIN v.categories c WHERE c.id IN :categoryIds" +
            " AND v.minSpend <= :minSpend" +
            " AND v.expiredDate > CURRENT_TIMESTAMP")
    List<Voucher> getVoucherWithConditions(List<Long> categoryIds, Double minSpend);
    @Query("SELECT v FROM Voucher v WHERE v.code = :code AND v.expiredDate > CURRENT_TIMESTAMP")
    Voucher findByCode(String code);
}
