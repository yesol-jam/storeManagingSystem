package com.bucket.storeManagingSystem.repository.dto;

import lombok.Data;

@Data
public class UserDto {
    //USER 에 대한 DTO
    private String userId; //회원아이디
    private String userName; //이름
    private String password; //비밀번호
    private String phone; //전화번호
    private String address; //집주소
    private String auth;//권한(ADMIN,MANAGER,USER)
}
