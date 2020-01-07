## Model06 : 일대일(OneToOne) - 단방향(Unidirectional), 주테이블에 외래키

### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/36001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/36002.png" width="500px" />
<br>
        
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 회원이 블로그 한 개만을 개설하는 회원제 블로그 서비스인 경우다.
    2) 보통 블로그에 방문할 경우 blog.domain.com/userid 이런 식으로 접근하는 경우가 많다.
    3) userid로 블로그 정보(블로그 이름, 블로그 소개, 카테고리, 포스트등) 가져와야 한다. User -> Blog 연관관계로 User에서 Blog를 참조해야 한다.
    4) 반대방향을 보면, User <- Blog 도 블로그 검색 결과리스트와 같은 기능이 필요하다면 고려해야 한다.  
    5) Model06에서는 단방향(Unidirectioanl)으로 결정하고 Model07에서 일대일(OneToOne) 양방향(Bidirectional)에서 적용해 본다. 

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) 회원이 블로그 한 개만을 개설하는 회원제 블로그 서비스이므로 User(1) -> Blog(1)
    2) OneToOne 이다.
       
3. __OneToOne 에서 고려해야 하는 것은 와래키를 두는 엔티티(관계주인)가 주테이블(User) 또는 대상테이블(Blog) 일 수 있다.__
    1) 주테이블에 외래키를 두는 경우
        + 주테이블에 매핑된 엔티티(User)에서 대상테이블에 매핑된 엔티티(Blog)를 참조할 수 있으므로 편하다.
        + 객체지향개발에서 선호한다.
        + JPA에서도 주테이블에 외래키를 두면 여러모로 편리한 점이 많다.
    2) 대상 테이블에 외래키를 두는 경우
        + 일대일(OneToOne) 단방향(Unidirectional)에서는 대상 테이블에  외래키를 둘 수 없다.
        + 당연히 JPA에서도 지원하지 않는다.
        + 하지만 일대일(OneToOne) 양방향(Bidirectional)에서는 가능하다.
        + 주로 전통적인 RBMS 스키마 모델링에서 선호한다.
        + 장점은 스키마를 유지하면서 OneToMany로 바꿀 수 있다(블로그를 한 개이상 개설하는 것으로 비즈니스가 변경되는 경우에 쉽게 변경할 수 있을 것이다.)

#### 1-2. Entity Class: User, Blog
1. __User 엔티티 매핑 참고__
2. __Blog 엔티티 매핑 참고__
3. __연관관계 매핑__
    1) OneToOne(User 엔티티, 주테이블)
        
        ```
            .
            .
            @Id
            @Column(name = "id", nullable = false, length = 24)
            private String id;       
            .
            .
	        @OneToOne(fetch = FetchType.LAZY)
	        @JoinColumn(name="blog_no")
	        private Blog blog; 
            .
            .
        ```
        + OneToOne 에서는 Default Fetch Mode는 LAZY로 유지한다.
        + ManyToOne 단방향(Unidirectional)과 유사하다.
        + 일대일(OneToOne) 단방향(Unidirectional)에서는 외래키를 주테이블에 두어야 한다.
      
    2) OneToOne(Blog 엔티티, 대상테이블)  
        
        ```
             .
             .
            @Id
            @Column(name = "no")
            @GeneratedValue( strategy = GenerationType.IDENTITY  )
            private Long no;
             .
             .
        ```
        + 일대일(OneToOne) 단방향(Unidirectional)에서는 대상테이블에 외래키를 둘 수 없다.
    
    3) 생성 스키마
    
        ```
            Hibernate: 
                
                create table blog (
                   no bigint not null auto_increment,
                    name varchar(200) not null,
                    primary key (no)
                ) engine=InnoDB
            Hibernate: 
                
                create table user (
                   id varchar(24) not null,
                    join_date datetime not null,
                    name varchar(24) not null,
                    password varchar(64) not null,
                    blog_no bigint,
                    primary key (id)
                ) engine=InnoDB
            Hibernate: 
                
                alter table user 
                   add constraint FK1g7a1d0no76mnqmr01ys1a40k 
                   foreign key (blog_no) 
                   references blog (no)       
       
        ```


### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. 키본키 생성 전략(String 기본키 사용 시, 유의할 점) 
2. Lazy Loading과 Proxy 객체 이해 


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
1. __JpaUserRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

2. __JpaBlogRepository.java__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

3. __JpaUserRepositoryTest.java__
    1) test01Save
        + 앞의 ManyToOne Many쪽 엔티티 저장과 같다.
        + 차이점이 있다면, 기본키(PK)를 String 사용자 아이디로 매핑하였고 애플리케이션이 직접 할당하고 unique를 유지하겠다고 설정한 것이다.
        + 따라서 @GeneratedValue 적용하지 않았다. 문자열 기본키에 적용하면 무시되거나 예외가 발생한다.
        + 당연히, DBMS에게 위임하는 @GeneratedValue(strategy=GenerationType.IDENTITY)도 무시된다.   
        + 문제는 기본키 전략을 애플리케이션 직접 할당으로 설정한 경우, JPA가 엔티티 객체를 저장시 실행하는 쿼리다.
              
            ```
                Hibernate: 
                    select
                        user0_.id as id1_1_0_,
                        user0_.blog_no as blog_no5_1_0_,
                        user0_.join_date as join_dat2_1_0_,
                        user0_.name as name3_1_0_,
                        user0_.password as password4_1_0_ 
                    from
                        user user0_ 
                    where
                        user0_.id=?
                Hibernate: 
                    insert 
                        into
                            user
                            (blog_no, join_date, name, password, id) 
                        values
                            (?, ?, ?, ?, ?)
            ```
            1) insert 앞에 해당 user를 찾는다.
            2) 이는 @GeneratedValue 생략하면 기본키(PK)의 unique를 위해 JPA가 영속화할 엔티티를 아이디로 미리 찾는 작업이다.
            3) 참고로 @JoinColum의 nullable=false 가 기본이기 때문에 User.blog는 세팅안해도 저장하는 데는 문제가 없다.
            4) 애플리케이션이 String 기본티(PK)을 직접 할당하고 unique를 잘 유지하면 문제는 없지만, 툭별한 케이스가 아니면 @GeneratedValue와 데이터베이스에에 맞는 자동생성 전략을 사용할 것을 JPA는 권고한다.
             
    2) test02SaveBlog
        + Blog 엔티티 객체를 저장한다.
        + 이 블로그의 주인 user를 데이터베이스로부터 영속화 시킨다. 
        + 쿼리로그를 보면, user table의 외래키(FK) blog_no을 update 한다.
    
    3) test03FindById
        + 영속화된 User 엔티티 객체로 부터 Blog 엔티티 객체를 가져오는 테스트이다.
        + 글로벌 페치 전략 LAZY로 바꿨기 때문에 Proxy 객체 확인 테스트를 한다.
        + 기본 페치 전략 EAGER를 유지하고 있다면, blog 테이블과 outer join 퀄리가 실행 되었을 것이다. 이는 User.blog가 null를 허용하고 있기 때문이다.
        + 테스트 코드를 보면 첫번째 Proxy 객체 확인에서는 초기화 되지 않은 Proxy객체로 지연로딩 중인 것을 확인할 수 있다. 
        + 블로그의 이름을 반환한 이후에는 Proxy객체가 초기화되어 있음을 확인할 수 있다.