package com.bucket.storeManagingSystem.repository.dto;

import lombok.Data;

@Data
public class BrandDto {
    //BRAND 에 대한 DTO
    private int brandId;//브랜드아이디
    private String brandName;//브랜드명
    private int deliveryFee;//배송비정책
    private String userId;//브랜드관리자아이디
}
