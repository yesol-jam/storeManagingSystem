package com.bucket.storeManagingSystem.repository.dto;

import lombok.Data;

@Data
public class AccessLogDto {
    //ACCESS_LOG 에 대한 DTO
    private String accessLogId;//접근로그정보아이디
    private String accessTimestamp;//로그일시
    private String userId;//회원아이디
    private String page;//페이지

}
