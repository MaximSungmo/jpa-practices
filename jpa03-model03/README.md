## Model03 : 다대일(ManyToOne) - 양방향


### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33009.png" width="500px" />
<br>
   
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 쇼핑몰 관리자는 주문조회에서 주문의 회원정보릃 확인해야 할 것이다.(Orders -> User, Navigational Association)  
    2) 회원의 경우, 자신의 주문리스트를 확인해 보는 서비스의 비지니스 로직이 필요하다. (Orders <- User, Navigational Association) 
    3) 따라서 주문<->회원 관계매핑은 양방향(Bidirection) 이다.  
   
2. __양방향에서는 연관관계의 주인을 따져야 한다.__
    1) 두 개의 엔티티의 관계를 설정하는 필드가 각각의 엔티티에 있는 데, 관계를 맺기 위해 값을 양쪽에 세팅하는 것은 불편하고 관리가 힘들다. 따라서 한 곳에만 주인이라 설정하고 그 필드만 값을 세팅해서 관계를 맺도록 한다.     
    1) user 필드(FK)가 있는 Orders 엔티티의 Orders.user가 관계의 주인이 된다.
    2) 외래키 세팅을 통한 관계의 변화는 Orders 엔티티의 user 필드를 세팅 할때만 변한다(그래서 관계의 주인이라 한다.)
    3) 반대편의 List<Orders>에 아무리 값을 설정해도 무시된다.(이 컬렉션 필드는 참조만 가능하다. 그래서 관계의 주인이 아니다.)
    4) 사실상, 외래키 설정을 하는 ManyToOne 에서 관계 설정은 끝난 것이다.
    5) 양방향으로 OneToMany를 하나 더 설정하는 것은 필요에 따라 객체 탐색의 편의성 때문에 하는 것이다.

3. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) 양방향에서는 ManyToOne, OneToMany가 다 존재하지만 관계의 주인이 되는 필드가 있는 엔티티 기준으로 ManyToOne으로 다중성을 잡는 것이 자연스럽다.
    2) 이런 이유로 양방향에서는 ManyToOne, OneToMany는 완전 동일하고 사실상 OneToMany 양방향은 없다. 

#### 1-2. Entity Class: User, Orders
1. __User 엔티티 매핑 참고__
2. __Orders 엔티티 매핑 참고__
3. __연관관계 매핑__
    1) ManyToOne(Order 엔티티)
        
        ```
            .
            .
            @ManyToOne(fetch = FetchType.EAGER)   
            @JoinColumn( name = "user_no" )
            private User user;      
            .
            .
        ```
        - ManytoOne, OneToOne에서 Default Fetch Mode는 EAGER 이다.  
      
    2) OneToMany(User 엔티티)  
        
        ```
             .
             .
	    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	    private List<Orders> orders = new ArrayList<Orders>();
             .
             .
        ```
        - mappedBy = "user" 주인이 누구임을 설정하는 부분이다. 반대편 엔티티의 user 필드이다.
        - mappedBy로 설정된 필드는 주인이 아님을 설정하는 것이다.
        - OneToMany 에서는 Default Fetch Mode는 LAZY 이다.
      
    3) 객체 관계 설정에 주의 할점
        - 영속성과 상관없이 순수 객체들과의 관계도 고려한 엔티티 클래스 작성을 해야 한다.
        - 양방향 관계이기 때문에 순수 객체에서도 양방향 관계를 맺어주는 것이 좋다.
        - 관계를 맺는 주인 필드가 세팅되는 Orders.setUser() 코드에서 양방향 관계를 맺는 안전한 코드 예시이다.
        
        ```
            public void setUser(User user) {
                this.user = user;

                if(!user.getOrders().contains(this)) {
                    user.getOrders().add(this);
                }
            }
                   
        ``` 
        - toString() 오버라이딩 하는 것도 주의해야 한다.
        
        ```
          public String toString() {
             return "Orders{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", totalPrice=" + totalPrice +
                ", regDate=" + regDate +
                // 무한루프조심!!!
                // ", user=" + user +
                '}';
          }         
        ``` 


### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. QueryDSL 통합하는 Spring Data JPA 기반의 레포지토리 작성방법
2. 양방향(Bidirectional)에서 관계 필드 변경과 Update 실행 여부
3. ManyToOne 양방향(Bidirection) 매핑에서 OneToMany 방향 엔티티는 Collection(List)가 연관 필드가 된다. 이 때, Join에서 발생할 수 있는 문제점
    1) OneToMany의 켈렉션 조인(inner join, outer join, fetch join)의 문제점 및 해결방법 이해
    2) OneToMany의 Default Fetch Mode인 Lazy Loading 때문에 발생하는 N+1 문제 및 해결방법 이해  
    3) 페이징 자체가 불가능함을 알고 그 해결방법 이해

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

#### 2-3. JpaUserRepository Test : Spring Data JPA 기반 Repository
1. __JpaUserRepository.java__
    1) User 엔티티(user 테이블)의 CRUD관련 메소드를 사용할 수 있는 인터페이스다.
    2) 기본메소드
        - 상속 Hierachy:   
            JpaUserRepository -> JpaRepository -> PagingAndSortingRepository -> CrudRepository -> Repository
        - 상속을 통해 상위 인터페이스 JpaRepository, PagingAndSortingRepository, CrudRepositor의 메소드들을 구현없이 사용할 수 있는데 이를 기본메소드라 한다.
    3) 쿼리메소드
        - 메소드 이름기반으로 자동으로 JPQL을 생성하는 메소드가 구현되는데 이를 쿼리메소드라 한다.
        - 예를 들어, findByEmailAndPassword(String, String)와 같은 메소드이다.

2. __JpaUserQryDslRepository.java__
    1) 기본메소드나 쿼리메소드에 성능이슈가 발생하거나 원하는 쿼리를 생성하지 못하면 직접 메소드를 직접 만들어 JPQL를 실행해야 한다.
    2) @Query에 JPQL를 직접 넣는 방법이 있으나 문자열 기반이고 기능상 제약이 많다. QueryDSL 통합을 많이 선호한다.
    3) 이를 위해 구현해야하는 메소드들을 정의해 놓은 인터페이스다.
    4) 애플리케이션 또는 서비스 게층에서 직접 사용하는 인터페이스인 JpaUserRepository가 JpaUserQryDslRepository를 상속한다.
    5) 메소드 예를 몇 개 들어보면,
        - findById2(no)는 기본메소드 findById(no)의 성능문제와 비즈니스 요구사항 때문에 QueryDSL로 직접 구현한 메소드이다.
        - update(user)는 영속객체를 사용한 update의 성능문제 때문에 jpa03-model02의 JpaUserRepository에 @Query 어노테이션을 사용해서 JPQL를 직접 사용했는데 메소드 파라미터 문제로 QueryDSL로 직접 구현한 메소드이다. 

3. __JpaUserQryDslRepositoryImp.java__
    1) JpaUserQryDslRepository 인터페이스의 메소드를 QueryDSL로 구현한다.
    2) 정리하면,
        - 기본메소드, 쿼리메소드의 구현은 Spring Data JPA가 해주는 것이다
        - JpaUserQryDslRepository 인터페이스 구현은 직접 한다.
        - JPA는 JpaUserQryDslRepository 인터페이스의 구현체를 스캔해야하기 때문에 네이밍 규칙이 있다. 인터페이스이름에 **"Impl"** 을 붙힌다.
            
            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33003.png" width="500px" />
            <br>
       
4. __JpaUserRepositoryTest.java__
    1) test01Save()
        - CrudRepository.save(s) 테스트
    2) test02Update
        - JpaQryDslUserRepositoryImp.update(user) 테스트
        - QueryDSL 기반 메소드 직접 구현하고 JpaUserRepository 인터페이스와 통합
    3) test03OneToManyCollectionJoinProblem
        - JpaUserQryDslRepositoryImpl.findAllCollectionJoinProblem() 테스트
        - OneToMany Collection Join(inner, outer, fetch)에서 발생하는 문제 테스트 
        - 기본메소드 findAll()과 같지만, Collection Join(inner join)를 QueryDSL로 작성하였다.
        - assert에도 있지만, user 카운트가 orders 카운트와 같은 문제가 있다.
 
            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33004.png" width="800px" />
            <br>       
        
            1) User와 Orders가 조인되었기 때문에 연결된 Orders의 개수만큼 User도 나오는 것이 당연하다.
            2) 이는 따지고 보면 문제가 아니다. 관계형데이터베이스와 객체지향프로그래밍 차이에서 발생하는 문제점이라 볼 수 있다.
            
    4) test04OCollectionJoinProblemSolved
        - JpaUserQryDslRepositoryImpl.findAllCollectionJoinProblemSolved() 테스트
        - Collection Join 문제 해결은 의외로 간단하다. distinct를 사용한다. QueryDSL에서는 selectDistinct()를 사용한다.
        - 실제 쿼리에서는 left outer join이 실행된다.
        - OneToMany Collection join 시,  outer 뿐만 아니라 inner, fetch join에도 해당되는 내용으로 반드시 selectDistinct() 함수를 사용한다.
        
    5) test05NplusOneProblem
        - N+1 문제를 테스트 한다.
        - 테스트 코드는 총 Orders 카운트를 먼저 가져오고 전체 User List에서 개별 User의 Orders List 사이즈를 모두 더해 같은 지 보는 것이다.
        - 당연히 같을 것이다.
        - 테스트 통과 조건은 실행된 쿼리수와 전체 User를 가져오기 위한 쿼리수(1)와 Lazy 때문에 각 User 별로 Orders List를 가져오기 위해 실행된 쿼리수(N)과 합이 같은 것이다.
        - 각 User 별로 Orders List를 가져오기 위해 쿼리가 실행된 것을 예상할 수 있는 근거를 이해하는 것이 중요하다.
          1) Lazy 때문에 User 엔티티 객체의 Orders List는 Proxy 객체로 실제 Orders가 담긴 List가 아니다.  
          2) Proxy 객체이면 result.size() 또는 result.get(0) 등, List에 있는 Orders 객체에 접근할 때 쿼리가 실행될 것이기 때문에 쿼리수를 카운팅을 할 수 있다.  
          3) 다음은 프락시 객체 초기화 상태을 체크하는 코드다.  
        
            ```
               if(!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(orders)){
                  qryCount++;
               }
                    
            ```     
          4) PersistenceUnitUtil.isLoaded(Entity) 반환이 false이면 초기화 되지 않은 Proxy객체이며 지연로딩 중인 것이다.  
        - 테스트 결과는 N+1번으로 쿼리가 실행된 것을 확인할 수 있다.(쿼리 로그에서 세어도 괜챦다.)
        
    6) test06NplusOneProblemNotSolvedYet
        - test04의 Collection Join 문제(다중row)를 해결한 findAllCollectionJoinProblemSolved()를 사용해 보는 테스트이다. 
        - N+1번 쿼리가 실행되기 때문에 이 메소드는 다중row만 해결할 수 있고 N+1문제는 해결하지 못한다.
        
    7) test07NplusOneProblemSolved
        - N+1 문제를 해결한 JpaUserQryDslRepositoryImpl.findAllCollectionJoinAndNplusOneProblemSolved()를 테스트 한다.
        - 이름은 길지만 innerJoin() + fetchJoin() 으로 작성된 QueryDSL 컬렉션페치조인을 하는 메소드이다.
        - 테스트 통과 조건인 1번 쿼리수가 나온다.
        - @Transactional 를 사용하지 않은 것도 주목하자. join은 Lazy가 무시되기 때문에 Proxy 객체를 사용하지 않는다. 
        - OneToMany에서 객체그래프를 통해 컬렉션 페치에서 발생하는 다중 Row 문제와  N+1 Lazy 로딩 문제를 해결하기 위해서는 selectDistinct(), fecthJoin()을 사용하면 된다.
        - 중요한 것은 Lazy로 객체그래프를 통해 컬렉션에 접근하는 것이 반드시 성능에 안좋다는 오해를 하지 않는 것이다. 
            1) 상황에 따라 적용을 다르게 해야 한다는 것을 기억해야 한다. 
            2) 실무에서는 Global Fetch 전략은 보통 LAZY이다.
            3) N+1 문제는 LAZY, EAGER가 공통으로 가지고 있다. 
            4) 문제라고 볼 수도 없는 것이 N번 쿼리는 발생한다. 막을 수 있는 것이 아니고 해야할 때와 하지 말아야 할 때를 구분하는 것이다.
            5) 'N번 쿼리가 언제 일어나는가?'를 알고 전략적으로 사용하면 오히려 조인보다 캐쉬효과로 성능 향상을 기대할 수 있다.  
            6) 지연로딩을 해도 상관없는 비즈니스나 뷰에 EAGER로 설정해서 N번을 무조건 하고 있는 것을 인지 못하는 것이 더 위험하고 안좋다.     
    
    8) test08findOrdersByNo
        - findOrdersByNo(no)를 테스트 한다.
        - 최적화된 findAllCollectionJoinAndNplusOneProblemSolved() 기반으로 특정 사용자의 주문내역을 조회하는 메소드다
            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33005.png" width="800px" />
            <br>       
                    
        - 쿼리를 보면 만족스럽다.
        - 지금까지의 과정을 이해하면 페이징 자체가 컬레션조인에서는 의미가 없고 가능하지 않을 것 같다는 느낌이 올 것이다.
        - 사실, 페이징 API를 사용하면 예외는 발생하지 않지만 JPA가 무시해 버린다.
        - 페이징이 필요하다면 반대편 ManyToOne Orders Repository에서 하자. 이 것이 자연스럽고 구현도 가능하다.(Orders Repository 구현 참고)
    
#### 2-4. JpaOrdersRepository Test : Spring Data JPA 기반 Repository
1. __JpaOrdersRepository.java__
    1) Orders 엔티티(orders 테이블)의 CRUD관련 메소드를 사용할 수 있는 인터페이스다.
    2) 기본메소드
        - 상속을 통해 상위 인터페이스 JpaRepository, PagingAndSortingRepository, CrudRepositor의 메소드들을 구현없이 사용 가능한데 이를 기본메소드라 한다.
        - 부모 인터페이스의 메소드들은 꽤 많다. 신뢰성 있는 영속성 엔티티 객체의 핸들링을 보장해 준다.
    3) 쿼리메소드 - 당연하지만, 쿼리메소드들도 오버로딩을 사용해서 만들어 낼 수 있다. 
        - findAllByUserNo(userNo)
        - findAllByUserNo(userNo, sort)
        - countAllByUserNo(userNo)

2. __JpaOrdersQryDslRepository.java__
    1) 쿼리메소드 findAllByUserNo()가 2회 쿼리가 실행되는 것에 불만이 있다면 다음 메소드들로 대체할 수 있다.
    2) QueryDSL로 작성했으며 파라미터로 넘어오는 Sort, Pageable 객체를 QueryDSL로 바꾸는 코드는 눈여겨 볼만 하다.
        - findAllByUserNo2(userNo);
        - findAllByUserNo2(userNo, sort);
        - findAllByUserNo2(userNo, pageable);
    3) countAllGroupByUser()는 QueryDSL에서 GroupBy를 사용하는 예제 메소드이다. 
    
3. __JpaOrdersQryDslRepositoryImp.java__
    1) JpaUserQryDslRepository 인터페이스의 메소드를 QueryDSL로 구현한다.
       
4. __JpaOrdersRepositoryTest.java__
    1) test01Save()
        - 기본 메소드 CrudRepository.save(S) 테스트 및 테스트 데이터 생성
    2) test02UpdateUser01()
        - Orders의 User를 변경하는 테스트이다.
        - ManyToOne뿐만 아니라 모든 양방향(Bidirectional)에서 주의할 것은 두 엔티티의 관계 필드가 순수 객체에서 변경되었을 때 데이터베이스 테이블의 외래키(user_no) update 여부다.
        - Orders.user, User.orders 가 연관관계 필드이고 실제로 외래키 관리는 관계주인 Orders.user이다. 그리고 테이블 외래키는 orders 테이블의 user_no이다.
        - 변경은 관계주인 Orders.user만 가능하고 당연히 DB 업데이트도 관계주인 Orders.user가 변했을 때이다.
        - 따라서 이 테스트에서는 no(PK)가 3L인 사용자의 orders 개수가 하나 더 늘지 않을 것이다. 
    3) test03UpdateUser02
        - 이 테스트에서는 orders의 user 변경은 DB 업데이트로 반영될 것이다.
        - 따라서 이 테스트에서는 no(PK)가 3L인 사용자의 orders 개수가 하나 더 늘어 난 것을 확인할 수 있다.
        - 쿼리로그를 확인하면, 업데이트 쿼리가 실행 되었다.
            ```
                update
                    orders 
                set
                    address=?,
                    name=?,
                    reg_date=?,
                    total_price=?,
                    user_no=? 
                where
                    no=?          
            ```
    4) test04FindAllByUserNo
        - 쿼리메소드 findAllByUserNo(userNo)를 테스트 한다.
        - ManyToOne Fetch는 EAGER가 default이기 때문에 Orders들을 가져온 후, Orders의 user를 세팅하기 위해 select쿼리가 바로 실행된다.
        - Orders를 가져오는 데, outer join을 사용하지만 select에는 user가 없다.
        - PersistenceUnitUtil().isLoaded(Entity)로 영속 Entity 객체임을 확인할 수 있다.
    5) test05FindAllByUserNo
        - 쿼리메소드 findAllByUserNo(userNo, sort)를 테스트 한다.
        - test02와 같고 sorting(Order By) 여부만 테스트한다.
        - 테스트 통과 조건에 sorting 여부의 테스트 코드를 추가하지 않았다. 쿼리 로그로 Order By절이 추가되었는 지 확인한다.
    6) test06FindAllByUserNo2
        - test03, test03의 2번 쿼리 실행을 해결한 QueryDSL로 작성한 findAllByUserNo2() 이다.
        - innerJoin()은 조인은 되지만 문제가 select에 User 필드를 포함하지 않는 것이다.
        - fetchJoin()을 함께 해야 한다.
        - 쿼리로그 확인 할 것
    7) test07FindAllByUserNo2
        - findAllByUserNo2()를 오버로딩해서 Sort를 받아 Order By를 하는 메소드 findAllByUserNo2(userNo, sort)를 테스트 한다.
        - QueryDSL에 Sort를 적용하는 방법 예시다.
        - Order By 필드를 여러 개 적용하는 방법도 테스트 코드에 있다.
        - 외부에서 sorting 필드를 세팅할 수 있는 장점이 있다.
    8) test08FindAllByUserNo2    
        - findAllByUserNo2()를 오버로딩해서 Pageable를 받아 Order By와 limit을 함께 구현한 메소드 findAllByUserNo2(userNo, pageable)를 테스트 한다.
        - QueryDSL에 Pageable를 적용하는 방법 예시다.
        - 외부에서 Paging Size, Index, Sorting Field들을 세팅할 수 있는 장점이 있는 매우 유용한 방법이라 할 수 있다.
    9) test09CountAllGroupByUser
        - countAllGroupByUser() 테스트 
        - QueryDSL의 groupBy() 사용 예시 
        - Group By뿐만 아니라 집계함수등이 사용되면(counting만 하는 것은 제외) 일단 Entity를 select에 둘수 없기 때문에 Projection과 DTO 사용을 먼저 생각해야 한다.
        - 테스트 통과 조건은 전체 Orders 수를 counting하고 사용자별 Orders 수를 저장한 DTO 들을 순회하면서 더한 값과 같은지 확인하는 것이다.
        - 참고로 Having도 QueryDSL에서 사용 가능하다.