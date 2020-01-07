## Model02 : 다대일(ManyToOne) - 단방향

### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/32001.png" width="600px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/32002.png" width="600px" />
<br>
   
1. __엔티티의 연관관계를 매핑할 때는 다음의 세가지 조건을 기준으로 매핑한다.__
    1) **다중성**(Multiplicity, 데이터베이스 Relation에 가까운 개념)  
    2) **방향성**(Direction, 객체지향의 Association에 가까운 개념)  
    3) **연관관계의 주인(Owner of Associations)**  
   
2. __보통은 서비스(비즈니스 로직)에서 방향성을 찾는 경우가 많다.__
    1) 게시판의 경우, 게시판 리스트 화면에서 작성자를 표시해야 경우가 많다. (Board -> User, Navigational Association)  
    2) 반대로 특정 사용자의 글들을 가져와야 하는 경우는 경험상 흔치 않다.(필요하다면 User->Board 관계도 잡으면 된다.. 이 결정은 전적으로 개발하는 서비스에 달려있다.)  
    3) 따라서 이 모델에서는 단방향(Unidirection) 으로 결정한다.(Board -> User)  
   
3. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) ManyToOne, OneToMany 또는 OneToOne를 결정하는 것은 쉽다. 참조를 해야하는 엔티티 기준으로 결정하면 된다.(뒤에 설명하지만 연관관계의 주인이 왼쪽에 온다.) 
    2) A -> B 인 경우 A 기준이다. 따라서, Board -> User 단방향에서는 Board 기준이다.  
    3) 데이터 모델링 Relation에서, Board가 Many, User가 One 이므로 **ManyToOne** 이다.  
   
4. __단방향에서는 연관관계의 주인을 따지지 않아도 된다.__
    1) 연관관계가 하나밖에 없기 때문에 관계 설정을 한 필드가 주인이다.
    2) 이 필드에 값 설정에 따라 외래키가 변경되면 관계가 변경되기 때문에 이 필드가 있는 엔티티가 관계의 주인이 되는 것이다.

#### 1-2. Entity Class: User, Board
1. __User.java 엔티티 매핑 참고__
2. __Board.java 엔티티 매핑 참고__
3. __연관관계 매핑__
    1) Board.java
        ```
           .
           .
           .
           @ManyToOne
           @JoinColumn( name = "user_no" )
           private User user;
           .
           .
           .
           
        ```
        - 데이터베이스 테이블에서는 N:1 관계에서는 N쪽에 외래키를 둔다.
        - 엔티티 매핑에서도 마찬가지다. 다중성을 결정하는 어노테이션과 외래키(조인칼럼)을 @JoinColumn 어노테이션으로 지정해주면 끝이다.


### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. JPQL 그리고 QueryDSL, Spring Data JPA 기반의 각각의 레포지토리 구현 방법을 이해할 것
2. QueryDSL이 JPQL로 변환되는 것을 이해할 것
3. 하지만, JPQL 기본의 중요성과 QueryDSL의 사용성을 이해할 것
4. ManyToOne(단방향)에서 left outer join, inner join, join fetch 등의 차이점 그리고 주요 성능 이슈들을 반드시 이해할 것
5. ManyToOne(단방향)은 가장 기본이 되는 연관관계 매핑이다. 따라서 반드시 숙지할 것.
6. 프로젝션, 페이징, 소팅, Like 검색 등의 다양한 객체지향쿼리 작성방법을 세 종류(JPQL, QueryDSL, Spring Data JPA)의 레포지토리(Board, User 각각 3개씩 총 6개)에서 확인하고 이해할 것.
7. Spring Data JPA가 제공하는 기본메소드들과 이름 기반의 쿼리메소드들이 생성하는 SQL들을 검증해보고 '왜 QueryDSL과 통합해야 하는가?'를 꼭 이해할 것. 

#### 2-2. 테스트 환경
 1. __Java SE 1.8__  
 2. __Spring Boot Starter Web 2.1.8.RELEASE (Spring Core, Context, Web ... etc 5.19.RELEASE)__   
 3. __Spring Boot Starter Data JPA 2.1.8.RELEASE (Spring Data JPA 2.1.10.RELEASE)__
 4. __Hibernate 5.4.4.Final__
 5. __QueryDsl JPA 4.2.1__
 6. __QueryDsl APT 3.7.4__ 
 7. __H2 Database 1.4.197__  
 8. __JUnit 4.12__
 9. __Spring Boot Starter Test 2.1.8.RELEASE (Spring Test 5.1.9.RELEASE)__   
10. __Gradle 5.4__    
 
#### 2-3. JpqlBoardRepository Test : JPQL 기반 Repository
1. __JpqlConfig.java__
    1) jpa03-model01 내용과 동일 
     
2. __JpqlBoardRepository.java__
    1) 저장(insert)을 위한 객체 영속화
    2) 삭제(delete) 지원
    3) 수정(update) 지원
    4) 조회(select) - fetch one 하는 두가지 방법
    5) 조회(select) - fetch list, inner join list, **fetch join** list, 
    6) 조회(select) - paging 및 like 검색
    7) JPQL 파라미터 인덱스 바인딩 & 이름 바인딩
    8) 집합함수: 통계 쿼리 스칼라 타입 조회
    9) **@ManyToOne Fetch.EAGER 기본 모드 이해**
   10) fetch one에서 join 문제 이해하기

3. __JpqlBoardRepositoryTest.java__
    
    1) test01Save
        - JpqlBoardRepository.save(Board)
        - 객체 영속화
    2) test02FindById1
        - JpqlBoardRepository.findById1(id)
        - Eager Fetch(@ManyToOne 기본 Fetch Mode)는 Proxy 객체 타입을 리턴하지 않는다. Lazy Fetch는 Proxy 객체를 리턴한다.(실제 User 객체가 아니다)
        - 영속화 객체 조회 에서는 Left Outer Join 으로 User 정보를 가져온다. (로그 확인 할 것)
    3) test03FindById2
        - JpqlBoardRepository.findById2(id)
        - Eager Fetch(@ManyToOne 기본 Fetch Mode)는 Proxy 객체 타입을 리턴하지 않는다. Lazy Fetch는 Proxy 객체를 리턴한다.(실제 User 객체가 아니다)
        - JPQL를 사용하면 User 정보를 가져오기 위해 Join 대신 Select 쿼리를 2번 실행한다. (로그 확인 할 것)  
    4) test04FindAll1
        - JpqlBoardRepository.findAll1()
        - Board 엔티티만 지정하면 join으로 한 번에 User 정보까지 가져오지 않는다는 것이다. 
        - 기본이 EAGER이기 때문에 각각의 Board가 참조하고 있는 User의 정보를 얻어오기 위해 Select 쿼리가 개별적으로 실행된다.
        - **성능이슈**: 대용량 게시판에선 문제가 될 수 있다.
    5) test05FindAll2
        - JpqlBoardRepository.findAll2()
        - test04의 문제를 해결하기 위해 join을 사용한다.
       
            ```
                select b from Board b inner join b.user u orders by b.regDate desc
         
            ```
            1) inner는 생략할 수 있다. 기본 join은 inner join 이다.
            2) 쿼리상으로 inner join이 걸리지만 test04의 문제가 되었던 User를 해결하기 위해서는 select에 User를 올려야 한다.
            3) 하지만, select에 타입이 2개가 되어 TypedQuery를 사용하지 못한다.
            4) 따라서 Board만 select하고 join을 실행한 쿼리로그를 확인해 보면, 
                ```
                    Hibernate: 
                        select
                            board0_.no as no1_0_,
                            board0_.contents as contents2_0_,
                            board0_.hit as hit3_0_,
                            board0_.reg_date as reg_date4_0_,
                            board0_.title as title5_0_,
                            board0_.user_no as user_no6_0_ 
                        from
                            board board0_ 
                        inner join
                            user user1_ 
                                on board0_.user_no=user1_.no 
                        order by
                            board0_.reg_date desc
                    Hibernate: 
                        select
                            user0_.no as no1_1_0_,
                            user0_.email as email2_1_0_,
                            user0_.gender as gender3_1_0_,
                            user0_.name as name4_1_0_,
                            user0_.password as password5_1_0_,
                            user0_.role as role6_1_0_ 
                        from
                            user user0_ 
                        where
                            user0_.no=?
                    Hibernate: 
                        select
                            user0_.no as no1_1_0_,
                            user0_.email as email2_1_0_,
                            user0_.gender as gender3_1_0_,
                            user0_.name as name4_1_0_,
                            user0_.password as password5_1_0_,
                            user0_.role as role6_1_0_ 
                        from
                            user user0_ 
                        where
                            user0_.no=?
               
                ```
                + User와 Join을 하여도 select에 Board만 있기 때문에 User를 가져오기 위해 Select 쿼리가 게시물 수(2개)만큼 더 실행된다.
            5) 해결방법
                + fetch join 사용하기 : JPQL 기반 레포지토리(JpqlBoardRepository)에 구현과 테스트를 하였다.(test06부터 적용했음)
                + join과 함께 프로젝션하기 : 베스트다.(따져보아야 한다. 전통적 SQL 기반 데이터베이스 프로그래밍 관점으로는 베스트) QueryDSL를 사용하여 해결한 예제는 JpaBoardRepository(Spring Data JPA + QueryDSL)에 구현과 테스트를 하였다.
              
    6) test06FindAll3
        - JpqlBoardRepository.findAll3()
        - Fecth Join을 사용했서 test05의 문제를 해결한다.
            ```
                select b from Board b join fetch b.user orders by b.regDate desc
            ```
            1) 실제 실행되는 쿼리로그를 보면 User가 select 되는 것을 확인할 수 있다.
                ```
                    select
                        board0_.no as no1_0_0_,
                        user1_.no as no1_1_1_,
                        board0_.contents as contents2_0_0_,
                        board0_.hit as hit3_0_0_,
                        board0_.reg_date as reg_date4_0_0_,
                        board0_.title as title5_0_0_,
                        board0_.user_no as user_no6_0_0_,
                        user1_.email as email2_1_1_,
                        user1_.gender as gender3_1_1_,
                        user1_.name as name4_1_1_,
                        user1_.password as password5_1_1_,
                        user1_.role as role6_1_1_ 
                    from
                        board board0_ 
                    inner join
                        user user1_ 
                            on board0_.user_no=user1_.no 
                    order by
                        board0_.reg_date desc

                ```
            2) 하지만, 프로젝션을 하지 못하는 것은 불만이다.
                + join fetch b.user 에서 엔티티에 별칭(alias)를 쓸 수 없다.(JPQL 기본원칙, Hibernate는 허용)
                + Fecth Join과 프로젝션은 함께 사용할 수 없다.(예외가 발생한다. 별칭을 허용하는 Hibernate도 마찬가지다. 별칭만 허용하는 것이다.)
                + 따라서 단순 join 으로만 프로젝션을 해야한다. 
    7) test07FindAll3
        - JpqlBoardRepository.findAll4(page)
        - Fetch Join 적용
        - Paging 적용(TypedQuery 의 setFirstResult(), setMaxResults() 메소드)
    8) test08FindAll3
        - JpqlBoardRepository.findAll3(keyword, page)
        - Fetch Join 적용
        - Paging 적용(TypedQuery 의 setFirstResult(), setMaxResults() 메소드)
        - like 검색 적용
    9) test09Update1
        - JpqlBoardRepository.update1(Board)
        - 영속객체를 사용한다.
        - 선별적 컬럼 업데이트처럼 보이지만 영속객체를 사용하기 때문에 전체 속성이 업데이트 되고 문제도 없다.
        - select와 update 쿼리가 2개 실행된다.
   10) test10Update2
        - JpqlBoardRepository.update2(Board)
        - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL 실행을 한다.
        - JPQL기반 update 쿼리만 실행된다.
        - 선별적 컬럼 업데이트가 가능하다.
   11) test11Delete1
        - JpqlBoardRepository.delete1(no)
        - 영속객체를 사용한다.
        - select와 delete 쿼리가 각각 실행된다.
   12) test12Delete2
        - JpqlBoardRepository.delete2(no)
        - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL 실행을 한다.
        - delete 쿼리만 실행된다.
   13) test13Delete2
        - JpqlBoardRepository.delete2(boardNo, userNo)
        - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL 실행을 한다.
        - delete 쿼리만 실행된다.
        - 게시판 삭제 비즈니스 로직에 맞게 작성된 메소드이다.
   14) JpqlUserRepository.count() 메소드
        - JPQL 통계함수 사용

#### 2-4. QueryDslBoardRepository Test : QueryDSL 기반 Repository
1. __JpqlConfig.java__
    1) jpa03-model01 내용과 동일 

2. __QueryDslBoardRepository.java__
    1) QueryDSL 기반으로 작성
    2) 저장을 위한 객체 영속화
    3) 다양한 쿼리함수 사용법
    4) JPQL Repository 메소드와 완전 동일(구현 내용만 다름) 
    5) JPQL Repository 메소드와 구현 및 실행 시 쿼리 로그 1:1 비교해 볼 것
    6) QueryDSL Fetch Join
    7) QueryDSL Like 검색  
    8) QueryDSL 통계 쿼리

3. __QueryDslBoardRepositoryTest.java__
    1) JpqlBoardRepositoryTest 와 완전 동일
    2) JpqlBoardRepositoryTest 쿼리로그 꼭 비교 분석할 것 (완전 일치)
    3) test01Save
        - QueryDslBoardRepository.save(Board)
        - 객체 영속화
    4) test02FindById1
        - QueryDslBoardRepository.findById1(id)
        - Eager Fetch(@ManyToOne 기본 Fetch Mode)는 Proxy 객체 타입을 리턴하지 않는다. Lazy Fetch는 Proxy 객체를 리턴한다.(실제 User 객체가 아니다)
        - 영속화 객체 조회에서는 Left Outer Join 으로 User 정보를 가져온다. (로그 확인 할 것)
    5) test03FindById2
        - QueryDslBoardRepository.findById2(id)
        - Eager Fetch(@ManyToOne 기본 Fetch Mode)는 Proxy 객체 타입을 리턴하지 않는다. Lazy Fetch는 Proxy 객체를 리턴한다.(실제 User 객체가 아니다)
        - JPQL를 사용하면 User 정보를 가져오기 위해 Join 대신 Select 쿼리를 2번 실행한다. (로그 확인 할 것)
        - from(), where(), fetchOne() 사용법
    6) test04FindAll1
        - QueryDslBoardRepository.findAll1()
        - Board 엔티티만 지정하면 join으로 User 정보까지 가져오지 않는다는 것이다. 
        - 기본이 EAGER이기 때문에 각각의 Board가 참조하고 있는 User의 정보를 얻어오기 위해 select가 여러번 실행된다.(JPQLBoardRepositoryTest의 test04 내용 참고)
        - from(), orderBy(), Q클래스 desc(), fetch() 사용법
    7) test05FindAll2
        - QueryDslBoardRepository.findAll2()
        - Inner Join을 사용해서 test04의 문제를 해결한다.(JPQLBoardRepositoryTest의 test05 내용 참고)
        - from(), innerJoin(), orderBy(), fetch() 사용법
    8) test06FindAll3
        - QueryDslBoardRepository.findAll3()
        - Fecth Join을 사용한다.(JPQLBoardRepositoryTest의 test05 내용 참고)
        - from(), innerJoin(), fetchJoin(), orderBy(), fetch() 사용
    9) test07FindAll3
        - QueryDslBoardRepository.findAll3(page, size)
        - Fetch Join 적용
        - Paging을 하기 위해 offset(), limit() 사용
        - from(), innerJoin(), fetchJoin(), orderBy(), offset(), limit(), fetch() 사용
        - page index 시작은 0
   10) test08FindAll3
        - QueryDslBoardRepository.findAll3(keyword, page, size)
        - Fetch Join 적용
        - Paging 적용(offset(), limit())
        - page index 시작은 0
        - like 검색 적용(Q클래스 contains() 메소드 사용)
        - from(), innerJoin(), fetchJoin(), where(), orderBy(), offset(), limit(), fetch() 사용
   11) test09Update1
        - QueryDslBoardRepository.update1(Board)
        - 영속객체를 사용한다.
        - 선별적 컬럼 업데이트이지만 영속객체를 사용하기 때문에 전체 속성이 업데이트 된다.
        - select와 update 쿼리가 2개 실행된다.
   12) test10Update2
        - QueryDslBoardRepository.update2(Board)
        - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL 실행
        - update 쿼리만 실행된다.
        - 선별적 컬럼 업데이트가 가능하다.
        - update(), set(), where(), execute() 함수 사용법 
   13) test11Delete1
        - QueryDslBoardRepository.delete1(no)
        - 영속객체를 사용한다.
        - select와 delete 쿼리가 2개 실행된다.
   14) test12Delete2
        - QueryDslBoardRepository.delete2(no)
        - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL 실행
        - delete 쿼리만 실행된다.
        - delete(), where(), execute() 함수 사용법
   15) test13Delete2
        - QueryDslBoardRepository.delete2(boardNo, userNo)
        - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL 실행
        - delete 쿼리만 실행된다.
        - 게시판 삭제 비즈니스 로직에 맞게 작성된 메소드이다.
        - delete(), where(), execute() 함수 사용법
   16) QueryDslBoardRepository.count() 메소드
        - QueryDSL의 fetchCount() 사용방법   

#### 2-4. JpaBoardRepository Test : Spring Data JPA 기반 Repository
1. __JpaConfig.java__
    1) jpa03-model01 내용과 동일
      
2. __JpaBoardRepository.java__
    1) 실무에서는 Spring Data JPA 기반의 레포지토리를 많이 사용한다.
    2) 하지만 JPQL(QueryDSL) 기반의 Repository를 만들 수 있어야 최적화된 Repository 작성이 가능하다.
    3) JPQL(QueryDSL) Repository Test를 통해 성능문제가 있는 JPQL(QueryDSL) Repository의 메소드는 JpaBoardRepository에서 따로 구현하지 않고 기본메소드, 퀄리메소드를 사용해야 할 것이다.
    4) 반대로, 성능 문제가 있는 기본 메소드와 쿼리 메소드도 있다. 그리고 기본메소드 또는 쿼리메소드로 실행할 수 없는 쿼리도 있기 때문에 다음 세가지 방법으로 구현해야 한다.
        - @Query 어노테이션과 함께 JPQL 직접 작성  
        - QueryDSL 통합
    5) JPQL은 QueryDSL로 작성하고 변환이 되기 때문에 문자열 JPQL 보다는 객체지향쿼리 모습에 좀더 가까운 QueryDSL 통합을 선호하고 추천한다.
    6) 결론은 JPQL에 대한 완벽한 이해를 기본으로 Spring Data JPA 기반의 Repository를 구현하되 성능이슈와 비지니스 요구사항을 고려하여 QueryDSL과 통합된 최적의 Repository를 작성해야 한다. 
        - QueryDSL로 작성해야 하는 메소드는 JpaBoardQryDslRepository 인터페이스에 정의 하였다.
        - JpaBoardQryDslRepository 인터페이스 구현은 JpaBoardQryDslRepositoryImpl에 QuerydslRepositorySupport 클래스를 상속하여 구현한다.
        - @Query과 함께 JPQL 직접 작성하는 방식은 부가적인 인터페이스 정의와 구현 클래스가 없어 간편해 보이기는 하지만 문자열 JPQL이 Repository 인터페아스 코드에 섞여있기 때문에 오히려 더 복잡하다.
        - 이 문제를 해결하는 방법이 NamedQuery 이다.(MyBatis Mapper XML과 유사) 하지만, 역시 복잡성을 근본적으로 해결하지는 못한다.
        - 테스트에서는 기본메소드, 쿼리메소드와 비교하여 성능을 검증해 본다.
    7) QueryDSL과 통합 설명은 jpa03-model03 JpaUserRepository 구현에서 자세히 설명했다.  
       
3. __JpaBoardRepositoryTest.java__
    1) JpqlBoardRepositoryTest 쿼리로그 꼭 비교 분석할 것 (완전 일치)
    2) test01Save()
        - 기본 메소드 CrudRepository.save(s)
        - CrudRepository.save(s) 에 대한 오해
            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/32008.png" width="600px" />
            <br>
            1) insert만 하는 메소드가 아니다.
            2) update도 한다.
            3) insert가 되면 파라미터로 전달받은 객체는 영속화가 된다.
            4) 하지만, update에서는 파라미터로 전달받은 객체는 값만 복사하는데 사용되기 때문에 영속화가 되지 않는다.
            5) 따라서 save() 메소드 호출 후, 영속객체를 사용해야 하면 반드시 반환되는 객체를 사용하여야 한다.(반환되는 객체는 영속화를 보장한다.)
            6) 이와 관련된 테스트는 Model08 UserRepositoryTest에서 한다.             
    3) test02FindById
        - 기본 메소드 CrudRepository.findById()를 사용하면 Outer Join이 기본이고 JPA에서는 left outer join만 지원한다.
        - JPQL 사용해서 구현하는 것은 JPQL Repository Test에서 확인 한 것 처럼 Select 쿼리가 2번 실행하기 때문에 고려의 대상이 안된다.
        - Left Outer Join 쿼리 로그 확인해 볼 것. 
    4) test03FindById2
        - JpaBoardQryDslRepositoryImpl.findById2(no)
        - 기본 메소드 CrudRepository.findById()는 join은 되지만 Projection이 이슈가 되면 사용하지 못한다.
        - QueryDSL Projection 으로 해결한다.
        - Inner Join을 사용한다.
        - Projection에서는 setter를 사용하는 Projections.fields()를 사용했다.
        - Projection 타겟 객체는 BoardDto 이다.
    5) test04FindAllByOrderByRegDateDesc
        - 쿼리메소드 JpaBoardRepository.findAllByOrderByRegDateDesc()
        - 쿼리로그를 보면, join을 하지 않는 것을 확인할 수 있다.
        - 기본이 EAGER이기 때문에 각각의 Board가 참조하고 있는 User의 정보를 얻어오기 위해 select가 여러번 실행된다.
        - User가 영속객체이기 때문에 1차 캐시가 되면(중복되는 게시물 작성자) User를 가져오기 위해 게시물 전체에 대한 select User는 하지 않을 것이다.
        - **성능이슈**: 하지만, 대용량 게시판에선 문제가 될 수 있다.
    6) test05FindAllByOrderByRegDateDesc2
        - JpaBoardQryDslRepositoryImpl.findAllByOrderByRegDateDesc2()는 test04의 문제를 해결한다.
        - QueryDSL Fetch Join을 사용한다.
    7) test06FindAllByOrderByRegDateDesc3
        - JpaBoardQryDslRepositoryImpl.findAllByOrderByRegDateDesc3()은 test05의 findAllByOrderByRegDateDesc2()에 Projection 기능을 추가하였다.
        - QueryDSL Inner Join을 사용한다.
        - Projections.fields()를 통해 setter를 활용한다.
        - 주의할 것은 Projection을 사용하면 fetch join되는 엔티티는 별칭을 가질 수 없기 때문에 fetch join을 사용할 수 없다.(test02에서도 마찬 가지이다.)
        - Projection을 하는 경우 Inner Join만 사용한다. 
    8) test07FindAllByOrderByRegDateDesc3
        - JpaBoardQryDslRepositoryImpl.findAllByOrderByRegDateDesc3(page, size)는 test06의 findAllByOrderByRegDateDesc3()에 Paging 기능을 오버로딩 하였다.
        - QueryDSL의 offset(), limit()를 사용해서 Paging을 구현하였다.          
        - page index는 0부터 시작 
    9) test08FindAll3
        - JpaBoardQryDslRepositoryImpl.findAll3(pageable)는 기능과 만들어지는 쿼리는 test07의 findAllByOrderByRegDateDesc3(page, size)와 같다.
        - 차이점은 Paging과 Sorting을 위해 Pageable 인터페이스 구현체를 파라미터로 받아 QueryDSL에 적용하고 있다.
        - 따라서 OrderBy 필드를 외부에서 지정할 수 있다.
        - page index는 0부터 시작 
   10) test09FindAll3
        - JpaBoardQryDslRepositoryImpl.findAll3(keyword, pageable)는 Like검색을 위한 keyword를 추가하였다.
   11) test10Update
        - 특별난 메소드를 제공하지 않는다.
        - 영속객체를 사용한다.
        - 선별적 컬럼 업데이트이지만 영속객체를 사용하기 때문에 전체 속성이 업데이트 된다.
        - select와 update 실행
   12) test11Update
        - test10의 문제점을 해결하기 위해 QueryDSL로 구현된 JpaBoardQryDslRepositoryImpl.update(Board) 이다.
        - update 쿼리만 실행된다.
        - 선별적 컬럼 업데이트가 가능하다.
        - update(), set(), where(), execute() 함수 사용법 
   13) test12Delete
        - 기본 메소드 CrudRepository.delete(board)
        - 영속객체를 사용한다.
        - select와 delete 실행
   14) test13Delete
        - 기본 메소드 CrudRepository.deleteById(no)
        - 영속객체를 사용한다.
        - select와 delete 실행
        - test12의 CrudRepository.delete(board)와 동작은 완전 동일하다.
   15) test14Delete
        - JpaBoardQryDslRepositoryImpl.delete(no)
        - QueryDSL로 구현된 메소드다.
        - delete 쿼리만 실행된다.
   16) test15Delete
        - JpaBoardQryDslRepositoryImpl.delete(boardNo, userNo)
        - QueryDSL로 구현된 메소드다.
        - delete 쿼리만 실행된다.
        - 게시판 삭제 비즈니스 로직에 맞게 작성된 메소드이다.
   17) JpaBoardRepository.count() 메소드
        - 기본 메소드 CrudRepository.count()
       
#### 2-6. JpqlUserRepository Test : JPQL 기반 Repository
1. __JpqlConfig.java__
    1) jpa03-model01 내용과 동일 

2. __JpqlUserRepository.java__
    1) JPQL 기반으로 작성
    2) 저장을 위한 객체 영속화 
    3) TypedQuery와 Update 구현시 TypedQuery 대신 Query 객체 사용하는 방법
    4) DTO 객체를 활용한 Projection 방법
    5) 집합함수: 통계 쿼리 스칼라 타입 조회

3. __JpqlUserRepositoryTest.java__
    
    1) test01Save
        - JpqlUserRepository.save(User)
        - 객체 영속화
    2) test02FindAllById
        - JpqlUserRepository.findById(id)
        - 영속화 객체 조회 이기 때문에 영속화 컨텍스트에서 찾고 없으면 select 쿼리를 통해 DB에서 가져온다.(1차 캐시)
         
    3) test03UpdatePersisted
        - JpqlUserRepository.update1(User)
        - 영속화 객체를 사용한 수정(업데이트)
        - 성능이슈: update 이전에 select 쿼리가 실행되는 문제점이 있다.(쿼리 로그 확인해 볼 것) 
    4) test04FindByEmailAndPassword
        - JpqlUserRepository.findByEmailAndPassword(Long, String)
        - 영속화 객체를 사용하지 않는다. 따라서 무조건 select 쿼리를 통해 DB에서 가져온다.
        - TypedQuery 객체 사용
        - 주로 사용자 인증(로그인)에 사용하게 될 메소드이다.
        - 따라서, 모든 정보를 담은 User 엔티티 객체륿 반환할 이유가 없다. UserDto를 사용한 Projection 구현  
        - 이름 기반 파라미터 바인딩
    5) test05Update2
        - JpqlUserRepository.update2(User)
        - 영속화 객체를 사용하지 않고 JPQL 기반의 업데이트
        - JpqlUserRepository.update1(User)에 비하여 select 쿼리가 실행되지 않는 장점이 있다(쿼리 로그 확인해 볼 것)
        - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL를 실행시킨다.
        - 이름 기반의 파라미터 바인딩을 사용하는 것은 TypedQuery와 다르지 않다.
    6) JpqlUserRepository.count() 메소드
        - JPQL에서 통계함수 사용

#### 2-7. QueryDslUserRepository Test : QueryDSL 기반 Repository
1. __JpqlConfig.java__
    1) jpa03-model01 내용과 동일 
     
2. __QueryDslUserRepository.java__
    1) QueryDSL 기반으로 작성
    2) 저장을 위한 객체 영속화
    3) 다양한 쿼리함수 사용법 
    4) QueryDSL DTO 객체를 활용한 Projection 방법
    5) QueryDSL 통계 쿼리

3. __QueryDslUserRepository Test.java__
    1) test01Save
        - QueryDslUserRepository.save(User)
        - 객체 영속화
    2) test02FindAllById
        - QueryDslUserRepository.findById(id)
        - 영속화 객체 조회 이기 때문에 영속화 컨텍스트에서 찾고 없으면 select 쿼리를 통해 DB에서 가져온다.(1차 캐시)
    3) test03UpdatePersisted
        - QueryDslUserRepository.update1(User)
        - 영속화 객체를 사용한 수정(업데이트)
        - 성능 이슈: update 이전에 select 쿼리가 실행되는 문제점이 있다.(쿼리 로그 확인해 볼 것) 
    4) test04FindAllById2
        - QueryDslUserRepository.findById2(id)
        - 영속화 객체를 사용하지 않는다. 따라서 무조건 select 쿼리를 통해 DB에서 가져온다.
        - 하지만, 반환되는 Entity 객체는 영속객체다.
        - 쿼리함수 from(), where(), fetchOne()
    5) test05Update2
        - QueryDslUserRepository.update2(User)
        - 영속화 객체를 사용하지 않고 JPQL 기반의 업데이트
        - QueryDslUserRepository.update1(User)에 비하여 select 쿼리가 실행되지 않는 장점이 있다(쿼리 로그 확인해 볼 것)
        - 쿼리함수 update(), where(), set(), execute() 
    6) test05FindByEmailAndPassword
        - QueryDslUserRepository.findByEmailAndPassword(Long, String)
        - 영속화 객체를 사용하지 않는다. 따라서 무조건 select 쿼리를 통해 DB에서 가져온다.
        - 주로 사용자 인증(로그인)에 사용하게 될 메소드이다.
        - QueryDSL에서 UserDto를 사용한 Projection 구현 (Projections.constructor) 
        - 쿼리함수 select(), from(), where(), fetchOne()
    7) QueryDslUserRepository.count() 메소드
        - QueryDSL의 fetchCount() 사용방법

#### 2-8. JpaUserRepository Test : Spring Data JPA 기반 Repository
1. __JpaConfig.java__
    1) jpa03-model01 내용과 동일
      
2. __JpaUserRepository.java__
    1) 기본 메소드
        - save(User)         
        - findById(id)
    2) 쿼리 메소드
        - findByEmailAndPassword(email, password)
    3) @Query 어노테이션을 사용한 메소드에 쿼리 정의
        - findById2(id)
        - update(User)
3. __JpaUserRepositoryTest.java__
    1) test01Save()
        - 기본 메소드 CrudRepository.save(S)
    2) test02FindById
        - 기본 메소드 CrudRepository.findById()
    3) test03Update()  
        - 기본 메소드 CrudRepository.findById()를 통해 영속객체를 얻어오고 업데이트를 한다.
        - **성능이슈: update 쿼리 이전에 select 쿼리 실행**
    4) test04FindById2()
        - JPQL 기반 메소드 직접 구현  
        - JpaUserRepository.findById2(id)
        - @Query 어노테이션을 사용한 메소드쿼리(JPQL) 정의
        - JPQL Projection
    5) test05Update
        - JPQL 기반 메소드 직접 구현
        - JpaUserRepository.update(...)
        - @Query 어노테이션을 사용한 메소드 쿼리(JPQL) 정의
        - JPQL 이름 바인딩
        - 이름 바인딩은 객체 이름 경로를 사용할 수 없기 때문에 메소드의 파라미터가 많다.
        - 이를 해결하기 위해서는 QueryDSL과 통합해야 함(jpa03-model03의 JpaUserRepository 참고) 
    6) test06FindByEmailAndPassword()  
        - JpaUserRepository.findByEmailAndPassword(email, password)
        - 쿼리메소드 예시
        - 프로젝션 하지 않음
        - 프로젝션을 하기 위해서는 앞의 @Query 메소드쿼리 정의 또는 QueryDSL 통합을 해야 한다.
        - jpa03-model03의 JpaUserRepository 참고