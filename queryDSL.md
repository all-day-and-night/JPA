QueryDSL
============

> [우아콘2020] 수십억건에서 queryDsl 사용하기 

# 1. 워밍업

## extends, implements 사용하지 않기

> 상속/구현 받지 않고 Entity를 지정하지 않더라도 queryDsl 사용하기

* JPAQeuryFactory 사용하기 

* 동적쿼리 

> BooleanBuilder를 사용 -> 어떤 쿼리인지 한 눈에 알 수 없음

> BooleanExpression 사용하기! 모든 조건이 null이 발생하는 경우는 대장에 발생

> 내부에 private 함수로 조건 처리 



# 2. 성능개선 - SELECT

## exist 금지!

* sql.exist vs sql.count(1) > 0

> exist는 존재하면 query종료, count는 모든 데이터 확인(count가 더 오래걸림)

> queryDsl의 exist는 count로 수행되기 때문에 사용지양! 하지만 exist를 사용하지 않을 수 없기 때문에 limit 1로 조회를 제한한다. 

> fetchFirst == limit(1).fetch()

## Cross Join 회피!

> 묵시적 join으로 Cross join 발생 

> Hibernate 이슈기 때문에 jpa에서도 발생

> 명시적 조인으로 inner join을 사용하자!

## Entity 보다는 DTO 우선!

* Entity 조회시

> hibernate 캐시 

> 불필요한 컬럼 조회

> OneToOne N + 1 쿼리 발생

> 실시간으로 Entity 변경이 필요한 경우


* DTO 조회 

> 고강도 성능 개선 or 대량의 데이터 조회가 필요한 경우


## 성능 개선 

1. 조회 컬럼 최소화하기 

> 기존에 알고 있던 값은 Expression.as 표현식으로 대체

> as 컬럼은 select에서 제외!

2. Select 컬럼에 Entity 자제 

> 연관관계 매핑에서 데이터 추출 시 연관된 table의 조회결과를 Entity로 신규 생성하여 가져옴

> OneToOne관계에서 LazyLoading이 안되기 때문에 N+1 문제가 항상 발생한다. 

> 연관된 Entity 전체를 조회하지 말고 JoinColumn의 Id만 가져오기 (마찬가지로 as 표현식 사용)

3. GroupBy 최적화

> MySql에서 GroupBy 실행하면 FileSort가 필수로 발생(index가 아닌 경우)

> order by null(queryDsl에서는 지원하지 않기 때문에 직접 조건 클래스 구현하여 적용)

> 정렬이 필요한 경우 적은 데이터라면 WAS에서 정렬(DB 보다 자원이 여유로운 경우)

> 페이징일 경우 Order By null 사용하지 못함 

4. 커버링 인덱스 

> 쿼리를 충족시키는데 필요한 모든 컬럼을 가지고 있는 인덱스 

> 페이징 조회 성능을 향상시키는 가장 보편적인 방법 

> JPQL에서 From절의 subQuery는 지원하지 않기 때문에 다른 방식을 사용

> 커버링 인덱스로 PK를 빠르게 조회하고 조회된 key로 Select 컬럼을 후속 조회한다. (기존 페이징 방식보다 훨씬 빠른 속도를 보여줌)


# 3. 성능개선 - Update/insert

## 일괄 Update 최적화

> 변경감지로 인한 업데이트 방식인 diityChecking을 사용할 경우 데이터가 많아질수록 성능이 낮아짐


* DirtyChecking

- 실시간 비즈니스 처리, 실시간 단건 처리시 사용

* QueryDsl.update

- 대량의 데이터를 일괄로 Update 처리시 






























































































































































