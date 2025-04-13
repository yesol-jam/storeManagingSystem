package com.bucket.storeManagingSystem.service;

import com.bucket.storeManagingSystem.component.AESUtils;
import com.bucket.storeManagingSystem.component.JsonReader;
import com.bucket.storeManagingSystem.repository.dto.*;
import com.bucket.storeManagingSystem.repository.mapper.BrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ManageService {

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    JsonReader jsonReader;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${aes256.secret.key}")
    private String secretKey;

    /*
    * setBrand
    * brand_A ~ brand_Z 까지의 브랜드를 생성하고, DB 에 삽입한다
    * */
    public int setBrand(){

        List<BrandDto> brandDtoList = new ArrayList<>();

        int basicDeliveryFee = 1000;

        for(char c = 'A' ; c <= 'Z' ; c++){
            BrandDto brand = new BrandDto();
            brand.setBrandName("brand_" + c);
            brand.setDeliveryFee(basicDeliveryFee);
            brandDtoList.add(brand);
            basicDeliveryFee += 100;
        }

        return brandMapper.insertBrand(brandDtoList);
    }
    
    /*
    * setItems
    * 상품정보 생성
    * 가정 1 ) 상품명은 3자리 숫자 + _ + 색상으로 조합한다. 상품명은 unique 하다
    * 가정 2 ) 상품의 브랜드는 랜덤으로 지정되나 한번도 지정되지 않은 브랜드는 없게 구성
    * 가정 3 ) 3자리 숫자가 일치하면 같은 브랜드의 제품이다
    * 가정 4 ) 상품의 가격은 9000~ 900000 사이의 수이고, 3자리 숫자가 일치하면 같은 가격이어야 한다
    * */
    public int setItems(){
        System.out.println("ITEM SETTING START ==============");
        //아이템 리스트
        List<ItemDto> itemDtoList = new ArrayList<>();
        //아이템 이름 중복체크용 set
        Set<String> uniqueItemNames = new HashSet<>();
        //아이템코드 별 가격 저장 map
        Map<Integer, Integer> priceMap = new HashMap<>();
        // 3자리 숫자별로 브랜드 저장 map
        Map<Integer, Integer> brandMap = new HashMap<>();
        // 브랜드 아이디 리스트
        List<Integer> brandIdList = brandMapper.getAllBrandIdList();


        // 사용할 색상 배열
        String[] colors = {"BLACK", "RED", "WHITE", "GREEN", "YELLOW"};

        Random random = new Random();
        int brandIndex = 0 ; //브랜드 아이디 리스트 인덱스

        // 100개의 아이템을 생성할 때까지 반복
        while (itemDtoList.size() < 100) {

            //ITEM NAME SETTING////////////////////////////////////////////////////////////////
            int num = 100 + random.nextInt(900); // 100 ~ 999 사이의 3자리 숫자 생성
            // 색상 배열에서 랜덤으로 색상 선택
            String color = colors[random.nextInt(colors.length)];
            // 아이템 이름 만들기
            String itemName = num + "_" + color;

            // 이미 동일한 조합이 있으면 스킵
            if (uniqueItemNames.contains(itemName)) {
                continue;  // 중복되면 현재 반복을 건너뛰고 다음 반복으로 넘어감
            }
            uniqueItemNames.add(itemName);  // 중복되지 않으면 Set에 추가

            //ITEM PRICE SETTING////////////////////////////////////////////////////////////////
            int price;
            int itemNumber = num;  // 3자리 숫자 부분 (가격을 일치시킬 기준)
            if (priceMap.containsKey(itemNumber)) {
                price = priceMap.get(itemNumber);  // 이미 가격이 정해져 있으면 그 가격을 재사용
            } else {
                price = 9000 + random.nextInt(81000);  // 9000 ~ 90000 사이의 랜덤 가격
                priceMap.put(itemNumber, price);  // 새로운 가격을 맵에 저장
            }

            // BRAND ID SETTING////////////////////////////////////////////////////////////////
            // 조건 1 ) 동일한 3자리 숫자를 가진 아이템은 같은 브랜드를 가져야 함
            // 조건 2 ) 브랜드 리스트를 1회 순환하며 item 을 배정하고, 이후는 랜덤으로 배정
            int brandId;
            if (brandMap.containsKey(itemNumber)) {
                brandId = brandMap.get(itemNumber);
            } else {
                if(brandIndex >= brandIdList.size()){
                    brandId = brandIdList.get(random.nextInt(brandIdList.size()));
                    brandMap.put(itemNumber, brandId);
                }else{
                    brandId = brandIdList.get(brandIndex);
                    brandIndex += 1;
                    brandMap.put(itemNumber, brandId);
                }

            }

            // 새로운 ItemDto 객체 생성 및 필드 설정
            ItemDto item = new ItemDto();
            item.setItemName(itemName);
            item.setPrice(price);
            item.setBrandId(brandId);

            // 생성한 ItemDto를 리스트에 추가
            itemDtoList.add(item);

        }

        return brandMapper.insertItems(itemDtoList);
    }

    /*
    * setUsers
    * 사용자 정보 생성
    * 가정 1 ) 주어진 로그파일에서 user_id 를 조회하여 사용자 아이디로 생성
    * 가정 2 ) 이름은 사용자 아이디로 지정
    * 가정 3 ) 비밀번호는 user_id + _1234 로 하되 복호화 불가능하게 암호화
    * 가정 4 ) 전화번호는 010-1234-00+ user_id의 끝자리 두자리로 하되 복호화 가능하게 암호화
    * 가정 5 ) 집주소는 user_id + _address 로 하되 복호화 가능하게 암호화
    * 가정 6 ) 권한은 일괄 USER로 지정
    * */
    public int setUsers(){
        List<UserDto> userDtoList = new ArrayList<>();
        Set<String> userIds = jsonReader.extractUserId();

        // Set의 모든 값을 출력
        for (String userId : userIds) {
            System.out.print(userId + " , ");
            UserDto userDto = new UserDto();
            userDto.setUserId(userId);
            userDto.setUserName(userId);

            String rawPassword = userId + "_1234";
            String encodedPassword = passwordEncoder.encode(rawPassword);
            userDto.setPassword(encodedPassword);

            String numberPart = userId.replaceAll("user_", ""); // "user_"를 제거
            String regex = String.format("%02d", Integer.parseInt(numberPart));  // 두 자릿수로 포맷
            String rawPhone = "010-1234-00" + regex;

            String rawAddress = userId + "_address";

            try{
                String encodedPhone = AESUtils.encrypt(rawPhone, AESUtils.decodeKey(secretKey));
                String encodedAddress = AESUtils.encrypt(rawAddress, AESUtils.decodeKey(secretKey));
                userDto.setPhone(encodedPhone);
                userDto.setAddress(encodedAddress);
            } catch (Exception e) {
                System.err.println("Error Encoding phone : " + e.getMessage());
            }

            userDto.setAuth("USER");

            userDtoList.add(userDto);

        }

        return brandMapper.insertUsers(userDtoList);
    }


    /*
    * setAccessLogs
    * 엑세스 로그 생성
    * 파일의 데이터를 ACCESS_LOG 테이블에 저장
    * */
    public int setAccessLogs(){
        List<AccessLogDto> accessLogDtoList = jsonReader.extractLog();

        return brandMapper.insertAccessLogs(accessLogDtoList);
    }

    /*
    * setOrders
    * 주문정보 생성
    * 가정 1 ) 주문 1000건 만들어 저장
    * 가정 2 ) 주문일자 2024-01-01 ~ 2025-04-12, 주문 0건인 날짜는 없음
    * 가정 3 ) 한 건의 주문에는 n개의 상품별 주문이 포함됨
    * */
    public int setOrders(){
        //최초 주문 SETTING////////////////////////////////////////////////////////////////
        List<OrderDto> orderDtoList = new ArrayList<>();
        // 유저 아이디 리스트
        List<String> userIdList = brandMapper.getAllUserList();
        // 브랜드 아이디 리스트
        List<Integer> brandIdList = brandMapper.getAllBrandIdList();
        //모든 상품 아이템
        List<ItemDto> itemDtoList = brandMapper.getAllItems();
        //key:브랜드아이디 - value:브랜드에 속한 아이템
        Map<Integer, List<ItemDto>> brandMap = new HashMap<>();
        //브랜드 리스트
        List<BrandDto> brandList = brandMapper.getAllBrandList();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 4, 12);
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1; //2024-01-01 ~ 2025-04-12 까지의 날짜 카운트
        Random random = new Random();


        for(int i = 0; i < 1000; i++){
            OrderDto orderDto = new OrderDto();
            
            //주문일시
            if(i < totalDays){  //최소 하루 포함되도록 순차 배정
                LocalDate currentDate = startDate.plusDays(i);
                orderDto.setOrderDate(currentDate.format(formatter));
            }else{ //순차 배정 이후 날짜 랜덤배정
                int randomDay = random.nextInt((int) totalDays);
                LocalDate randomDate = endDate.minusDays(randomDay);
                orderDto.setOrderDate(randomDate.format(formatter));
            }
            
            //유저아이디
            int randomUserIndex = random.nextInt(userIdList.size());
            orderDto.setUserId(userIdList.get(randomUserIndex));
            
            //브랜드아이디
            int randomBrandIndex = random.nextInt(brandIdList.size());
            orderDto.setBrandId(brandIdList.get(randomBrandIndex));

            //배송지
            orderDto.setDeliveryAddress("sample address");

            //주문상태
            orderDto.setOrderStatus("END");


            orderDtoList.add(orderDto);
        }

        //order 1000건 insert + orderDtoList 의 orderId 업데이트 되어 리턴됨
        brandMapper.insertOrders(orderDtoList);

        //아이템 주문정보 SETTING////////////////////////////////////////////////////////////////

        for (ItemDto item : itemDtoList) {
            Integer brandId = item.getBrandId();
            List<ItemDto> itemsOfBrand = brandMap.get(brandId);

            if (itemsOfBrand == null) {
                itemsOfBrand = new ArrayList<>();
                brandMap.put(brandId, itemsOfBrand);
            }
            itemsOfBrand.add(item);
        }

        //아이템 주문정보 insert
        List<OrderItemDto> insertedItems = setOrderItems(orderDtoList,brandMap);


        //주문 가격 업데이트 ////////////////////////////////////////////////////////////////
        
        //order 가격/할인/배송비/총가격/브랜드 정보 재구성
        orderDtoList = setBrandIdAndTotalPrice(orderDtoList,insertedItems,brandList);
        
        //order 가격/할인/배송비/총가격/브랜드 정보 DB 업데이트
        return brandMapper.updateOrderList(orderDtoList);
    }

    /*
    * setItemOrders
    * 주문정보에 파생되는 아이템주문정보 생성
    * 주문에 파생되는 아이템은 동일한 브랜드이며, 1개 ~ 3개의 order_item 이 포함된다
    *
    * */
    public List<OrderItemDto> setOrderItems(List<OrderDto> orderDtoList, Map<Integer, List<ItemDto>> brandMap){

        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        Random random = new Random();

        for (OrderDto orderDto : orderDtoList) {
            int orderId = orderDto.getOrderId();
            int brandId = orderDto.getBrandId();

            int orderDtoCount = random.nextInt(3) + 1; //주문정보에 파생되는 아이템주문정보는 1~3개 중 하나이다

            for(int i = 0 ; i < orderDtoCount ; i ++){
                OrderItemDto orderItemDto = new OrderItemDto();
                // orderItemDto SETTING ////////////////////////////////////////////////////////////////////////////////
                orderItemDto.setOrderId(orderId); //주문아이디
                List<ItemDto> itemList = brandMap.get(brandId);
                int randomIndex = random.nextInt(itemList.size());
                orderItemDto.setItemId(itemList.get(randomIndex).getItemId());//아이템아이디
                orderItemDto.setUnitPrice(itemList.get(randomIndex).getPrice());//개당가격
                orderItemDto.setQuantity(random.nextInt(5) + 1);//수량(임의대로 1~5개까지중 하나로 지정)
                orderItemDto.setSize("M");//사이즈(임의대로 M으로 지정);
                orderItemDto.setTotalPrice(orderItemDto.getUnitPrice() * orderItemDto.getQuantity());//주문상품 총금액
                orderItemDto.setBrandId(brandId);//브랜드아이디

                orderItemDtoList.add(orderItemDto);
            }
        }
        brandMapper.insertOrderItems(orderItemDtoList);

        return orderItemDtoList;
    }

    /*
    * setBrandIdAndTotalPrice
    * 아이템 주문정보로 order 내용 채우기
    * */
    public List<OrderDto> setBrandIdAndTotalPrice(List<OrderDto> orderDtoList, List<OrderItemDto> insertedItems,List<BrandDto> brandList){
        //key : 주문 아이디 - value : 주문 아이템정보
        Map<Integer, List<OrderItemDto>> orderItemMap = new HashMap<>();
        //key : 브랜드 아이디 - value : 브랜드 배송비
        Map<Integer, List<BrandDto>> brandDeliveryFeeMap = new HashMap<>();

        //주문아이디별 주문아이템정보 map 생성
        for (OrderItemDto orderItem : insertedItems) {
            Integer orderId = orderItem.getOrderId();
            List<OrderItemDto> orderLst = orderItemMap.get(orderId);

            if (orderLst == null) {
                orderLst = new ArrayList<>();
                orderItemMap.put(orderId, orderLst);
            }
            orderLst.add(orderItem);
        }

        //브랜드별 배송비 map 생성
        for (BrandDto brand : brandList) {
            Integer brandId = brand.getBrandId();
            List<BrandDto> brandLst = brandDeliveryFeeMap.get(brandId);

            if (brandLst == null) {
                brandLst = new ArrayList<>();
                brandDeliveryFeeMap.put(brandId, brandLst);
            }
            brandLst.add(brand);
        }

        // orderDtoList SETTING ////////////////////////////////////////////////////////////////////////////////
        for(int i = 0 ; i < orderDtoList.size() ; i ++){
            int orderId = orderDtoList.get(i).getOrderId();
            int brandId = orderItemMap.get(orderId).get(0).getBrandId();
            orderDtoList.get(i).setBrandId(brandId);//브랜드아이디 업데이트
            int totalPrice = 0;
            for(OrderItemDto item : orderItemMap.get(orderId)){
                totalPrice += item.getTotalPrice();
            }
            orderDtoList.get(i).setTotalPrice(totalPrice);//총가격 업데이트
            int fee = brandDeliveryFeeMap.get(brandId).get(0).getDeliveryFee();
            orderDtoList.get(i).setDeliveryFee(fee);//배송비 업데이트

            //할인정책
            int discount = 0 ;
            if(totalPrice >= 30000 && totalPrice < 70000){
                discount = 1000;
            }else if(totalPrice >= 70000 && totalPrice < 100000){
                discount = 2000;
            }else if(totalPrice >= 100000 && totalPrice < 200000){
                discount = 3000;
            }else if(totalPrice >= 200000){
                discount = 4000;
            }
            orderDtoList.get(i).setDiscountAmount(discount);
            orderDtoList.get(i).setOrderTotalPrice(totalPrice - discount + fee);
        }

        return orderDtoList;
    }

}
