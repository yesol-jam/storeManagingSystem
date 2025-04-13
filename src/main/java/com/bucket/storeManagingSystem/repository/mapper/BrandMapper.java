package com.bucket.storeManagingSystem.repository.mapper;

import com.bucket.storeManagingSystem.repository.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrandMapper {

    //Brand 정보를 insert
    int insertBrand(List<BrandDto> brandDtoList);

    //모든 Brand_id 를 select
    List<Integer> getAllBrandIdList();

    //Item 정보를 insert
    int insertItems(List<ItemDto> itemDtoList);

    //User 정보를 insert
    int insertUsers(List<UserDto> userDtoList);

    //access_log 정보를 insert
    int insertAccessLogs(List<AccessLogDto> accessLogDtoList);

    //모든 user_id 를 select
    List<String> getAllUserList();

    //order 정보를 insert
    int insertOrders(List<OrderDto> orderDtoList);

    //모든 item 정보를 select
    List<ItemDto> getAllItems();

    //order item 정보를 insert
    int insertOrderItems(List<OrderItemDto> orderItemDtoList);

    //모든 brand 정보를 select
    List<BrandDto> getAllBrandList();

    //order 가격/할인/배송비/총가격/브랜드 정보를 update
    int updateOrderList(List<OrderDto> orderDtoList);
}
