# CapstoneProject2019-1
Project LetEatGo(Miribom)

## POS Opensource from dPOS

## Implements
1.  src-pos패키지 내 클래스 추가
    -   forms
        -   AES256Util:         점주의 정보 암호화를 위한 클래스
        -   Geocoding:          식당 위경도를 얻어오는 클래스
        -   LoginFrame:         점주 로그인 Gui
        -   LoginOwner:         점주 로그인 방식 정의
        -   Owner:              점주의 정보 정의 클래스
        -   RegistOwner:        점주 회원가입 방식 정의
        -   RegistRestaurant:   점주의 식당 등록 방식 정의
        -   Restaurnt:          식당 정보 정의 클래스
    -   sales.restaurant
        -   PlaceStatus:        테이블의 총 테이블 수와 사용중인 테이블 수의 상태 저장
        -   SeatsInfoSender:    잔여좌석 정보 전송 방식 정의
        -   MenuEditor:         식당 메뉴 관리 서버 통신방식 정의
2.  src-data패키지 내 클래스 수정
    -   user
        -   BrowableData:        메뉴 수정, 삭제, 삽입 동기화   