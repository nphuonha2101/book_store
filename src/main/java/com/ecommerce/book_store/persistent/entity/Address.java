package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "addresses")
public class Address extends AbstractEntity {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "province")
    private String province;
    @Column(name = "district")
    private String district;
    @Column(name = "ward")
    private String ward;
    @Column(name = "add_info")
    private String addInfo;
    @Column(name = "is_default")
    private boolean isDefault;
    public Address() {
    }
    public Address(Long userId, String fullName, String phone, String province, String district, String ward, String addInfo, boolean isDefault) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.addInfo = addInfo;
        this.isDefault = isDefault;
    }


}
