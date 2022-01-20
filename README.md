JPA
=============

## 도메인 설계

![image](https://user-images.githubusercontent.com/94096054/150071826-a32a6495-04ac-4866-a081-0df227555ca1.png)

### 기능 목록

* 회원 기능

  + 회원 등록
  + 회원 조회

* 상품 기능

  + 상품 등록
  + 상품 수정
  + 상품 조회

* 주문 기능

  + 상품 주문
  + 주문 내역 조회
  + 주문 취소

* 기타 요구사항

  + 상품 재고관리 
  + 상품의 종류는 도서, 음반, 영화
  + 상품 카테고리 구분


### 도메인 모델과 테이블 설계

* 도메인 모델

![image](https://user-images.githubusercontent.com/94096054/150072061-092b5f2e-3707-41cf-9dad-2ff2b0a9d0e9.png)

* 테이블 설계

![image](https://user-images.githubusercontent.com/94096054/150072140-e667dc5f-dd7a-403c-85e3-a9da52d7c635.png)







## Question

* @Entity

* @Embedded and @Embedable

* @OneToOne @OneToMany @ManyToOne @ManyToMany

* @JoinTable


## 실무 팁

* @Getter는 열어놓고 @Setter는 닫아놓자

* @ManyToMany를 지양

> 중간 Entity를 생성하여 @OneToMany @ManyToOne으로 매핑 

* 값 타입 변경 불가능하게 Entity(Class) 설계

> @Setter를 제거하고 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스 구현

> Entity나 임베디드 타입(@Embeddable)은 protected로 설정 

> JPA 구현 라이브러리가 객체를 생성할 때 리플랙션 같은 기술을 사용할 수 있도록 지원


## Entity 설계시 주의점

* Entity에는 가급적 Setter를 사용하지 말자

> Setter가 모든 Entity에 열려있을 경우, 변경 포인트가 너무 많아서 유지보수가 어렵다.

* 모든 연관관계는 지연로딩으로 설정

> 즉시 로딩은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다. 특히 JPQL을 실행할 때 N+1 문제가 자주 발생

> 실무에서 모든 연관관계는 지연로딩(LAZY)로 설정

> @XToOne(OneToOne, ManyToOne)관계는 기본이 즉시 로딩이므로 직접 지연로딩으로 설정해야 한다.

* 컬렉션은 필드에서 초기화 하자

> null 문제에서 안전하다

> 하이버네이트는 엔티티를 영속화 할 때, 컬렉션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다. 

> 만약 임이의 메서드에서 컬렉션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있다.

> 따라서 필드 레벨에서 생성하는 것이 가장 안전하고 코드도 간결하다.


## Test Driven Development

* @RunWith(SpringRunner.class): 스프링과 테스트 통합

* @SpringBootTest: 스프링 부트를 띄우고 테스트(명시하지 않으면 @Autowired 실패)

* @Transactional: 반복 가능한 테스트 지원, 각각의 테스트를 실행할 때마다 트랜잭션을 시작하고 테스트가 끝나면 트랜잭션을 강제로 롤백




## 도메인 모델 패턴 vs 트랜잭션 스크립트 패턴

* 도메인 모델 패턴

> 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것

* 트랜잭션 스크립트 패턴

> 엔티티가 비즈니스 로직이 거의 없고 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것
