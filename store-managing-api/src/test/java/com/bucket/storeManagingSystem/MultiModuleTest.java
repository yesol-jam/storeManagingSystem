package com.bucket.storeManagingSystem;

import com.bucket.storeManagingSystem.component.JsonReader;
import com.bucket.storeManagingSystem.repository.dto.OrderDto;
import org.junit.jupiter.api.Test;


public class MultiModuleTest {

    @Test
    void commonModuleTest() {
        OrderDto dto = new OrderDto();
        dto.setOrderId(1);
        if(dto.getOrderId() == 1){
            System.out.println("test success");
        }
    }

    @Test
    void coreModuleTest(){
        JsonReader jsonreader = new JsonReader();

        jsonreader.extractUserId();
    }
}
