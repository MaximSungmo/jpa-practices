## Model05 : 일대다(OneToMany) - 양방향(Bidirectional)

### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/35001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/35002.png" width="500px" />
<br>

1. __OneToMany 양방향(Bidirectioanl)은 존재하지 않는다__
    1) OneToMany 양방향(Bidirectinal)은 ManyToOne 양방향(Bidirectional)과 완전 동일하다. 
    2) ManyToOne 양방향(Bidirectional)을 사용해야 한다.
    3) 연관관계의 방향에서 주인을 좌측으로 인지하기 때문에 OneToMany 양방향에서는 One쪽이 주인이 되어야 하는데 이는 RDBMS 특성상 불가능하기 때문에 존재하지 않는다.
    4) 왜냐믄, 보통 외래키를 관리하는 곳이 연관관계의 주인인데 외래키는 Many쪽에 있기 때문이다.
    5) 데이터베이스에서 존재할 수 없지만 JPA 관계 매핑은 가능하다.
        
2. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 회원의 경우, 마이페이지에서 자신의 주문리스트를 확인해 보는 서비스의 비지니스 로직이 필요하다. (User -> Orders, Navigational Association) 
    2) 반대 방향 User <- Orders 도 당연히 탐색(Navigational)이 가능해야 한다. 따라서 양방향(Bidirectioanl)이다.
   
3. __양방향에서는 연관관계의 주인을 따져야 한다.__
    1) user 필드(FK)가 있는 Orders 엔티티가 이 관계의 주인이 된다.
    2) 하지만 OneToMany 양방향(Bidirectional)에서는 User 엔티티가 주인이 되어야 하지만 RDBMS와 특성상 주인이 될 수 없다.
    3) 대신, 양쪽에 @JoinColumn의 이름을 같게 하고 Many쪽의 관계 필드(user)는 읽기 전용이 되어야 한다. 양쪽에서 관계를 컨트롤하는 것은 복잡하고 피해야한다.(이런 이유로 관계 주인을 세우는 것이다.)
    4) 쉽게 생각해보면, OneToMany 단방향(Unidirectional)에 반대편 Many에 읽기전용 연관 필드를 하나더 두는 관계매핑이라 보면 된다.
    5) OneToMany Unidirectional의 저장 문제점을 그대로 가지고 있다.(이런 이유로 비추천)
    6) 사용하는 경우를 실무에서 찾아보면, OSIV에서 주문 조회 페이지 같은 View에서 사용할 수 있다. 주문자 변경을 하지 못하기 때문에 OSIV의 보안 취약점을 해결하는 데 사용할 수 있다.
    7) 하지만 6)은 ManyToOne 양방향에서도 설정할 수 있기 때문에 OneToMany 양방향만의 장점이라 볼 수 없다.
    8) 반드시 OneToMany 양방향(Bidirectioanal) 보다는 ManyToOne 양방향(Bidirectional) 사용을 권장한다.
    
4. __생성 스키마 DDL__    
    <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/35003.png" width="600px" />
    <br>
    1) OneToMany 단방향과 같다.
    2) Many쪽에 지정한 이름으로 외래키(FK)가 설정되어 있음을 알 수 있다.

#### 1-2. Entity Class: User, Orders
1. __User 엔티티 매핑 참고__
2. __Orders 엔티티 매핑 참고__
3. __연관관계 매핑__
    1) OneToMany(User 엔티티)
        
        ```
            .
            .
            @OneToMany(fetch = FetchType.LAZY)   
            @JoinColumn( name = "user_no" )
            private List<Orders> orders = new ArrayList<Orders>(); 
            .
            .
        ```
        - OneToMany 에서는 Default Fetch Mode는 LAZY로 유지한다.
      
    2) ManyToOne(Orders 엔티티)  
        
        ```
             .
             .
            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "user_no", insertable = false, updatable = false)
            private User user;
             .
             .
             .
            public void setUser(User user) {
                this.user = user;
            //  if(!user.getOrders().contains(this)) {
            //         user.getOrders().add(this);
            //  }
              .
              .       
        ```
        + ManytoOne, OneToOne에서 Default Fetch Mode는 EAGER (Global Fetch 전략 LAZY) 로 수정
        + insertable = false, updatable = false를 두어 ReadOnly 필드로 세팅했다. User 변경을 하지 못한다(FK변경 금지)
        + 하지만 User 조회는 가능하다.
        + OneToMany 단방향(Unidirectioanl)과는 다르게 양방향에서는 두 군데서 insert, update 되지 못하도록 setUser setter에 Collection을 가져와 채우는 부분을 없애야 한다. 


### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. OneToMany Bidirectional 단점 이해
2. Order -> User Read Only 확인
3. OneToMany 양방향(Bidirectioanal) 보다는 ManyToOne 양방향(Bidirectional) 사용 권고 


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


#### 2-3. JpaOrderRepository Test : Spring Data JPA 기반 Repository
1. __JpaUserRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

2. __JpaOrdersRepository.java__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

3. __JpaOrdersQryDslRepository__
    1) QueryDSL 통합 인터페이스 이다.
    2) Orders(주문) 저장을 위한 편의메소드 save(Long userNo, Orders ...orders)를 정의하였다.

4. __JpaOrdersQryDslRepositoryImpl__
    1) QueryDSL 인터페이스 구현 클래스이다.
    2) Orders(주문) 저장을 위한 편의메소드 save(Long userNo, Orders ...orders)를 구현하였다.

5. __JpaOrdersRepositoryTest.java__
    1) test01Save
        + 테스트를 위해 2개 User와 3개 Orders를 생성하였으며 1개의 User에 Orders 3개를 세팅하여 저장한다.
        + OneToMany 단방향(Unidirectional) 에서 다루었지만 저장 후, 업데이트 쿼리가 한 번 더 실행되는 단점을 OneToMany 양방향(Bidirectional)에서도 그대로 가지고 있다.
        + 쿼리로그를 보면 insert 쿼리가 실행된 후, 외래키(FK) Update 쿼리가 실행된 것을 볼 수 있다.
              
            ```
                Hibernate: 
                    /* insert me.kickscar.practices.jpa03.model05.domain.Orders
                        */ insert 
                        into
                            orders
                            (address, name, reg_date, total_price) 
                        values
                            (?, ?, ?, ?)
                Hibernate: 
                    /* create one-to-many row me.kickscar.practices.jpa03.model05.domain.User.orders */ update
                        orders 
                    set
                        user_no=? 
                    where
                        no=?            
            ```
    2) test02UpdateUser
        + 기본키(PK)가 1L인 Orders를 가져와 영속화 시킨다.
        + 기본키(PK)가 2L인("마이콜")인 User를 가져와 영속화 시킨다.
        + Orders에 영속화된 User 엔티티 객체를 세팅하여 업데이트 시킨다.
    
    3) test03UpdateUserResultFails
        + OneToMany 양방향(Biidirectional)에서는 외래키 관리를 두 군데서 하기 때문에 Many(Orders)에서 User(FK)를 변경하는 것을 금지 시키고 One(관계주인, User)에서만 가능하도록 했다.
        + test02에서는 Many(Orders)에 User 엔티티 객체를 세팅하여 업데이트 시켰는데 그 결과를 확인하는 테스트 이다.
        + 테스트 통과 조건은 2L("마이콜")로 변경되지 않아야 한다.
        + 변경되지 않았다. ReadOnly 설정이 정상적으로 작동하는 것을 알 수 있다.
    
    4) 결론
        + OneToMany 양방향(Bidirectional)는 ManyToOne 양방향(Biidirectional)과 완전 동일한 연관관계 이다.
        + OneToMany 단방향과 마찬가지로 One쪽을 주인으로 보지만 RDBMS의 특성상 주인의 관계설정 필드를 갖지 못하고 Many쪽에 두는 특이한 점을 그대로 가진다.
        + Many쪽에서는 JoinColumn 설정을 정상적으로 할 수 있지만, 와래키 관리 포인트가 두군데가 되어 One쪽에 그 설정을 맡기고 ReadOnly 설정을 하게된다.
        + 결론은 OnToMany 단방향에다가 반대편에 탐색을 위한 필드(User)를 하나 추가 한 형태가 되며 그 연관관계는 원칙적으로 존재하지 않는다 보는 것이 맞다.
        + 전반적으로 부자연스러운 매핑 설정을 계속 해야한다.
        + ManyToOne 양방향(Bidirectional) 매핑을 사용하도록 하자.
        
    


