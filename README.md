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


* 엔티티를 수정하는 2가지 방법

1. 변경 감지 기능 사용

> 영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정

> 트랜잭션 안에서 엔티티를 다시 조회, 변경할 값 선택 -> 트랜잭션 커밋 시점에 변경감지(Dirty checking)이 동작해서 DB에 update sql 실행



2. 병합(merge) 사용

> 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능

> Parameter로 넘어온 준영속 상태의 엔티티의 식별자 값으로 영속 엔티티를 조회

> 영속 엔티티의 값을 준영속 엔티티의 값으로 모두 교체(merge)


* 엔티티를 수정할 때는 위의 2가지 방법이 존재하지만 변경 감지 기능을 사용하는 것이 좋다.

> 컨트롤러에서 어설프게 엔티티 생성 금지

> 트랜잭션이 있는 서비스 계층에 식별자와 변경할 데이터를 명확하게 전달(파라미터 or DTO)

> 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경

> 트랜잭션 커밋 시점에 변경 감지가 실행



# API 구현 및 성능 최적화

* API 구현

1. 회원 조회 API

2. 회원 수정 API

3. 회원 등록 API

> 중요한 것은 DTO(Data Transfer Object)를 사용하여 json을 리턴하고 entity를 외부로 노출하지 않는 것이 중요하다.


## 지연 로딩과 조회 성능 최적화 


* ex)

 + xToOne(ManyToOne, OneToOne)
 + Order
 + Order -> Member
 + Order -> Delivery

> 관계형 db에서는 서로가 외래키로 계속해서 참조하기 때문에 문제가 생긴다(무한 루프)

> 때문에 json 형태로 리턴할 경우 DB의 참조되어있는 관계에 명시적으로 JsonIgnore를 사용해야 한다.

* 지연로딩을 사용할 경우 연관 관계에 있는 모든 데이터를 조회하지 않지만, Proxy를 사용하지 않을 경우 문제가 생긴다

> 여기서는 hibernate5Module를 사용하여 해결했다.(Entity를 api 외부로 노출하는 것은 좋은 방법이 아니므로 DTO로 변환해서 리턴할 것이다.)

> api로 order 테이블을 조회할 경우 지연 로딩이기 때문에 참조되어 있는 객체는 null로 출력된다.

> 강제로 query 문을 실행하여 Lazy 강제 초기화 할 수 있다.

```
@GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all){
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }

        return all;
    }
```

* 엔티티를 DTO로 변환

```
@GetMapping("api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

@Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName(); // Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // Lazy 초기화
        }
    }
```

> 이 때 최악의 경우 N+1의 문제가 발생하는데 1번의 쿼리가 실행되면 참조되어 있던 다른 테이블을 쿼리문을 실행하여 조회하기 때문에 추가적인 쿼리가 발생한다.

> 지연로딩은 영속성 컨텍스트에서 조회하므로, 이미 조회된 경우 쿼리를 생략한다.

* fetch join 최적화(진짜 너무 강조해도 부족함이 없는 부분이다)

 + 주문 조회

```
public List<Order> findAllWithMemberDelivery() {
        List<Order> resultList = em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
        return resultList;
    }
}
```
> 다른 테이블을 참조하는 방식을 사용하지 않고 fetch join을 사용하여 한번의 쿼리문으로 모든 데이터를 가져온다

> DTO를 사용하더라도 이미 영속성 컨텍스트에서 조회됐기 때문에 추가적인 쿼리를 생략한다.


* 바로 DTO로 변환
```
// 조회한 결과 바로 dto로 변환
public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                    " from Order o" +
                    " join o.member m" +
                    " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }


// dto
@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address){
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
```

> 바로 위의 두가지 방법은 우열을 가리기 힘들 정도로 최적화 속도가 비슷하다.(쿼리문을 직접 쓰는 것이 미세하게 더 빠르다)

> 둘중에 logic을 수정하기 편하다는 점의 차이나 코드상의 깔끔한 점을 비교하여 선택하면 된다.


* 쿼리 방식 선택 권장순서

1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
2. 필요하면 fetch join으로 성능을 최적화 한다. -> 대부분의 성능 이슈가 해결된다.
3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template를 사용해서 SQL을 직접 사용한다.


## Collection 최적화

> 위 문제는 OneToOne, ManyToOne의 관계만 있었고 

> 이번에 다룰 문제는 OneToMany의 관계에서 컬렉션을 조회하고 최적화하는 방법을 알아볼 것이다.


* Worst version
```
@GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order: all){
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems){
                orderItem.getItem().getName();
            }
        }
        return all;
    }
```

> 1 대 다의 관계에서 위와 같이 api를 설계할 경우 하나의 데이터를 받을 때 여러 데이터와 연결되어 있는 테이블을 조회하기 때문에 수 많은 query가 생성된다.

* Dto에 담아서 변환

```
@GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    @Getter
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;


        public OrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto{

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
```

> 조회할 데이터를 Dto로 변환할 때 주의할 점은 1 대 다의 관계를 가지고 있는 데이터를 다시 참조할 때 

> 그 데이터 역시 entity가 아니라 Dto로 변환해주어야 한다.

> 하지만 여전히 N+1의 문제는 해결되지 않았기 때문에 fetch join을 통해 최적화를 해보자

* fetch join

```
//
@GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3(){
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }
   
//distinct로 중복을 날려주고 추가적인 쿼리문을 생성하지 않고 데이터를 가져온다
public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .getResultList();
    }
```
> fetch join으로 sql이 한번만 실행된다 또한 distinct로 중복을 제거한다.

> 하지만 paging(특정 범위만 가져오는 것)이 불가능하다.

> 따라서 1대 다의 관계에서 컬렉션 페치 조인은 1개만 사용하는 것을 추천한다. 


* 페치 조인 한계 돌파

> 우선 ToOne(OneToOne, ManyToOne)관계에서는 fetch join을 사용해도 괜찮다.

> OneToMany의 경우 문제가 생길 가능성이 있기 때문에 다음과 같은 해결책을 사용한다.

1. 컬렉션은 지연 로딩으로 조회한다.(fetch=lazy)

2. 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size(global setting), @BatchSize(개별 최적화)를 적용한다.

> hibernate.default_batch_fetch_size(global setting)는 application.yml 파일에 설정하면 된다.

```
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
#        show_sql: true
        format_sql: true
        default_batch_fetch_size: 100
```

> @BatchSize는 OneToMany의 관계에 있는 entity에 @BatchSize를 붙여주면 된다.(컬렉션은 컬렉션 필드에 엔티티는 엔티티 클래스에 적용)

> 이러한 옵션을 사용하면 컬렉션이나 프록시 객체를 한꺼번에 설정한 size만큼 in query로 조회한다.

> 1대 다의 관계의 데이터를 가져오기 위한 쿼리 호출이 1 + N -> 1 + 1로 최적화되고

> 조인 보다 DB 데이터 전송량이 최적화 된다.

> 또한 paging도 가능하다.

> batch size는 100 ~ 1000 사이를 선택하는 것을 권장한다.

* 이후에는 DTO로 변환해서 데이터를 조회하고 리턴하는 함수를 구현할 것이다.

> OneToMany의 관계일 때 Many의 관계에서 따로 DTO를 구현해야 한다.





















