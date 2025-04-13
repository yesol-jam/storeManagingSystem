package com.bucket.storeManagingSystem.component;

import com.bucket.storeManagingSystem.repository.dto.AccessLogDto;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JsonReader {
    /*
    * JsonReader
    * 특정 경로의 파일을 jsonParser 로 읽어오는 Class
    * */
    public static final String FILEPATH = "C:/Temp/jsonUpload/sample_user_log.json";
    
    /*
    * extractUserId
    * 파일에서 회원정보 수집
    * Set 구조로 중복 데이터 제외
    * */
    public Set<String> extractUserId(){

        Set<String> userIds = new HashSet<>();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = null;

        try {
            File file = new File(FILEPATH);
            if (!file.exists()) {
                System.err.println("파일이 존재하지 않습니다: " + FILEPATH);
                return userIds;
            }
            // JSON 파일 파서 생성
            parser = factory.createParser(file);

            // 파일을 순차적으로 읽어가며 user_id 추출
            while (parser.nextToken() != null) {
                if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        String fieldName = parser.getCurrentName();
                        if ("user_id".equals(fieldName)) {
                            parser.nextToken(); // 값으로 이동
                            String userId = parser.getText();
                            userIds.add(userId); // Set에 중복 없이 추가
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (parser != null) {
                try {
                    parser.close(); // 파일 리소스를 닫음
                } catch (IOException e) {
                    System.err.println("Error closing the parser: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return userIds;
    }
    
    /*
    * extractLog
    * 파일 로그 내용 Dto 로 추출
    * */
    public List<AccessLogDto> extractLog(){

        List<AccessLogDto> accessLogDtoList = new ArrayList<>();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = null;

        try {
            File file = new File(FILEPATH);
            if (!file.exists()) {
                System.err.println("파일이 존재하지 않습니다: " + FILEPATH);
                return accessLogDtoList;
            }
            // JSON 파일 파서 생성
            parser = factory.createParser(file);

            // 파일을 순차적으로 읽어가며 user_id 추출
            while (parser.nextToken() != null) {
                if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
                    AccessLogDto accessLogDto = new AccessLogDto();
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        String fieldName = parser.getCurrentName();
                        if ("access_timestamp".equals(fieldName)) {
                            parser.nextToken();
                            String accessTimestamp = parser.getText();
                            accessLogDto.setAccessTimestamp(accessTimestamp);
                        }
                        if ("user_id".equals(fieldName)) {
                            parser.nextToken();
                            String userId = parser.getText();
                            accessLogDto.setUserId(userId);
                        }
                        if ("page".equals(fieldName)) {
                            parser.nextToken();
                            String page = parser.getText();
                            accessLogDto.setPage(page);
                        }
                    }
                    accessLogDtoList.add(accessLogDto);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (parser != null) {
                try {
                    parser.close(); // 파일 리소스를 닫음
                } catch (IOException e) {
                    System.err.println("Error closing the parser: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return accessLogDtoList;
    }
}
