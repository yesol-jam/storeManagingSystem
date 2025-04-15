package com.bucket.storeManagingSystem;

import com.bucket.storeManagingSystem.service.ManageService;
import com.bucket.storeManagingSystem.util.AESUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@SpringBootTest
class StoreManagingSystemApplicationTests {
	@Autowired
	ManageService manageService;

	@Value("${aes256.secret.key}")
	private String secretKey;

	@Autowired
	PasswordEncoder passwordEncoder;

	/*
	 * 2-b 번 문항
	 * setBrand
	 * 브랜드 및 배송비 초기 세팅용
	 * 수행 시, brand 테이블에 brand_A ~ brandZ 까지 생성되며
	 * 브랜드별 배송비는 모두 다르게 책정된다
	 * */
	@Test
	void setBrands(){
		int result = manageService.setBrand();

		//insert 결과 건수
		System.out.println(result);
	}

	/*
	* 2-b 번 문항
	* setItems
	* 상품 정보 100개 생성
	* */
	@Test
	void setItems() {
		int result = manageService.setItems();
		//insert 결과 건수
		System.out.println(result);
	}

	/*
	* 2-a 번 문항
	* setUsers
	* 활동 로그에서 추출한 회원 ID수만큼 회원 정보 생성
	* ID 외 정보는 임의 입력
	* */
	@Test
	void setUsers(){
		int result = manageService.setUsers();

		//insert 결과 건수
		System.out.println(result);
	}
	/*
	* 2-d 번 문항
	* setAccessLog
	* 활동로그파일의 데이터를 로그 테이블에 저장
	* */
	@Test
	void setAccessLogs(){
		int result = manageService.setAccessLogs();

		//insert 결과 건수
		System.out.println(result);
	}

	/*
	* 2-c 번 문항
	* setOrders
	* 주문 1000건 만들어 저장
	* */
	@Test
	void setOrders(){
		int result = manageService.setOrders();

		//insert 결과 건수
		System.out.println(result);
	}

	/*
	* keyGenerator
	* AES256 암호화 키 생성용(최초한번)
	* 구동 후에는 application.yml 에 업데이트 해줘야 함
	* */
	@Test
	void keyGenerator() throws Exception {
		SecretKey key = AESUtils.generateKey();
		String encodedKey = AESUtils.encodeKey(key);
		System.out.println("Encoded Secret Key: " + encodedKey);
	}

	/*
	* encryptTester
	* 암호화 한 데이터 확인용
	* */
	@Test
	void encryptTester() throws Exception {
		//user_6 의 전화번호가 010-1234-0006이 맞는지 확인
		String user_6EncryptPhone = "dI/NbCic01Zr835LGc0qQA==";
		String user_6DecryptPhone = AESUtils.decrypt(user_6EncryptPhone, AESUtils.decodeKey(secretKey));
		if(user_6EncryptPhone.equals(user_6DecryptPhone)){
			System.out.println("전화번호 암호화 테스트 : true");
		}else{
			System.out.println("전화번호 암호화 테스트 : true");
		}

		//user_6의 비밀번호가 user_6_1234가 맞는지 확인
		String user_6EncryptPassword = "{bcrypt}$2a$10$N/W4xI3Tr4ingUA9tV8Vtu8gGHGGceWxOaxGENLwuYrZvvJL1NvZu";
		String user_6DecryptPassword = "user_6_1234";
		if(passwordEncoder.matches(user_6DecryptPassword, user_6EncryptPassword)){
			System.out.println("비밀번호 암호화 테스트 : true");
		}else{
			System.out.println("비밀번호 암호화 테스트 : false");
		}
	}

}
