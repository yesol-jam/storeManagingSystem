package com.bucket.storeManagingSystem.repository.dto;

import lombok.Data;

@Data
public class OrderDto {
    //ORDER 에 대한 DTO
    private int orderId;//주문아이디
    private String userId;//회원아이디
    private String orderDate;//주문일시
    private String deliveryAddress;//배송지
    private String orderStatus;//주문상태
    private int totalPrice;//주문상품총금액
    private int discountAmount;//별도할인금액
    private int deliveryFee;//배송비
    private int orderTotalPrice;//주문총금액(주문상품총금액-할인금액-배송비)
    private int brandId;//브랜드아이디
}
