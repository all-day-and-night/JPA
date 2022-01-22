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

* @Test(expected = 예외처리클래스)

> 테스트에서 발생한 예외처리 클래스가 expected로 지정할 경우 test는 성공한다.


## 실무 팁

* @Getter는 열어놓고 @Setter는 닫아놓자

* @ManyToMany를 지양

> 중간 Entity를 생성하여 @OneToMany @ManyToOne으로 매핑 

* 값 타입 변경 불가능하게 Entity(Class) 설계

> @Setter를 제거하고 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스 구현

> Entity나 임베디드 타입(@Embeddable)은 protected로 설정 

> JPA 구현 라이브러리가 객체를 생성할 때 리플랙션 같은 기술을 사용할 수 있도록 지원

* 동적 쿼리 사용시 

> JPQL로 처리하여 String을 동적으로 구현하여 사용

> 직관적일 수 있지만 유지보수성이 떨어짐

> JPA Criteria로 처리

> 직관적이지 않아 유지보수성이 너무 떨어짐

> QueryDsl을 사용하는 것이 가장 좋음 / 직관적이며 유지보수성이 뛰어남




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


## API

* api를 사용할 때 Entity를 전달해서는 안된다.

* DTO(Data Transfer Object)를 사용해서 전달

## Validation

* @NotNull, @NotEmpty, @NotBlank

> DTO에서 값을 받을 때 해당 요청의 parameter에 따라 오류 메시지와 조건을 제공하는 Annotation




## JPA 변경 감지와 병합

* 준영속성 엔티티(Entity)

> 영속성 엔티티가 더이상 관리하지 않는 엔티티

> DB에 한번 저장되어서 식별자가 존재한다

> 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다. 


* 준영속 엔티티를 수정하는 2가지 방법

1. 변경 감지 기능 사용
2. 병합(merge) 사용



