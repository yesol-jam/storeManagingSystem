package com.bucket.storeManagingSystem.repository.dto;

import lombok.Data;

@Data
public class ItemDto {
    //ITEM 에 대한 dto
    private int itemId;//상품정보아이디
    private int brandId;//브랜드아이디
    private String itemName;//상품명
    private int price;//가격
    private String itemInfo;//상품설명
}
