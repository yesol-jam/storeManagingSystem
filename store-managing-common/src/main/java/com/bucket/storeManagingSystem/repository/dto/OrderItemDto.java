package com.bucket.storeManagingSystem.repository.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    //ORDER_ITEM 에 대한 DTO
    private int orderItemId;//주문상품목록아이디
    private int orderId;//주문아이디
    private int itemId;//상품정보아이디
    private String size;//사이즈
    private int quantity;//수량
    private int unitPrice;//상품별가격
    private int unitExtraCharge;//상품별추가요금
    private int totalPrice;//주문상품총금액((상품별가격x수량)+추가요금)
    private int brandId;//브랜드아이디

}
