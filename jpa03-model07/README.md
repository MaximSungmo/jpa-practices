## Model07 : 일대일(OneToOne) - 양방향(Bidirectional), 주테이블에 외래키

### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/37001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/37002.png" width="500px" />
<br>
        
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 회원이 블로그 한 개만을 개설하는 회원제 블로그 서비스인 경우다.
    2) 보통 블로그에 방문할 경우 blog.domain.com/userid 이런 식으로 접근하는 경우가 많다.
    3) userid로 블로그 정보(블로그 이름, 블로그 소개, 카테고리, 포스트등) 가져와야 한다. User -> Blog 연관관계로 User에서 Blog를 참조해야 한다.
    4) 반대방향을 보면, User <- Blog 도 블로그 검색 결과리스트와 같은 기능이 필요하다면 고려해야 한다.  
    5) 따라서 양방향(Bidirectioanl)으로 결정한다. 

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) 회원이 블로그 한 개만을 개설하는 회원제 블로그 서비스이므로 User(1) -> Blog(1)
    2) OneToOne 이다.
       
3. __OneToOne 에서 고려해야 하는 것은 주테이블 또는 대상테이블을 관계주인으로 설정할 수 있다__
    1) 외래키를 양쪽 중 한 곳에 두는 것이 가능하다.
    2) 주테이블에 외래키를 두는 경우
        + 주테이블에 매핑된 엔티티(User)에서 대상테이블에 매핑된 엔티티(Blog)를 참조할 수 있으므로 편하다.
        + 객체지향개발에서 선호한다.
        + JPA에서도 주테이블에 외래키를 두면 여러모로 편리한 점이 많다.
    3) 대상 테이블에 외래키를 두는 경우
        + 일대일(OneToOne) 단방향(Unidirectional)에서는 불가능하지만 일대일(OneToOne) 양방향(Bidirectional)에서는 가능하다.
        + 주로 전통적인 RBMS 스키마 모델링에서 선호한다.
        + 장점은 스키마를 유지하면서 OneToMany로 바꿀 수 있다(블로그를 한 개이상 개설하는 것으로 비즈니스가 변경되는 경우에 쉽게 변경할 수 있을 것이다.)
    4) Model07에서도 주테이블에 왜래키를 둔다.
    
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
        + OneToOne 에서는 Default Fetch Mode는 EAGER 이다. Global Fetch Mode LAZY로 수정했다. 
        + ManyToOne 단방향(Bidirectional)과 유사하다.
        + 일대일(OneToOne) 양방향(Bidirectional)에서는 양쪽 테이블에 외래키를 둘 수 있지만, Model07에서도 주테이블(User)에 외래키를 두었다.
      
    2) OneToOne(Blog 엔티티, 대상테이블)  
        
        ```
             .
             .
            @Id
            @Column(name = "no")
            @GeneratedValue(strategy=GenerationType.IDENTITY)
            private Long no;
             .
             .
            @OneToOne(mappedBy = "blog", fetch = FetchType.LAZY)
            private User user;       
             .
             .
        ```
        + 양방향이므로 연관관계의 주인을 정해야 한다.
        + User 테이블이 FK를 가지고 있으므로 User 엔티티에 있는 User.blog가 연관관계의 주인이다
        + 따라서 반대 매핑인 Blog의 Blog.user는 mappedBy를 선언해서 연관관계의 주인이 아니라고 설정해야 한다.
    
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
        + 스키마 생성 DDL를 보면 OneToOne 단방향(Unidirectional)과 다르지 않다.


### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. 양방향(Bidirectional)에서 관계 필드 변경과 실제 update 쿼리 실행 여부
2. Projection 2가지 방법: @QueryProjection 사용법, Spring Data JPA 지원 Projection 방식


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


#### 2-3. JpaBlogRepository Test : Spring Data JPA 기반 Repository
1. __JpaUserRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

2. __JpaBlogRepository.java__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

3. __JpaBlogRepositoryTest.java__
    1) test01Save
        + 엔티티 객체 영속화
        + 테스트 데이터 저장
    2) test02UpdateUser
        + DB로 부터 Blog, User 엔티티 객체를 각각 영속화 시킨다.
        + 양방향(Bidirectional) 중 관계의 주인이 아닌 쪽에서 연관관계 필드를 변경한다.  
            ```
                blog.setUser(user);
                    
            ```
    3) test03VerifyUpdateUser01
        + 앞의 test02의 결과를 확인한다.
        + User를 통해 Blog를 탐색해보면 null이다.
        + blog_no(FK)가 update 되지 않았다.
        
    4) test04UpdateUser02
        + 데이터베이스로 부터 Blog, User 엔티티 객체를 각각 영속화 한다.
        + 양방향(Bidirectional) 중 관계의 주인 쪽에서 연관관계 필드를 변경한다.  
            ```
                user.setBlog(blog);
                    
            ```
    5) test05VerifyUpdateUser02
        + 앞의 test04의 결과를 확인한다.
        + User를 통해 Blog를 탐색해서 블로그 이름을 확인해 볼 수 있다.
        + blog_no(FK)가 정상적으로 update 되었다.
        
    6) test06findAllUser Vs test07findAll
        + test06는 관계 주인 필드의 지연로딩을 테스트한다. 연관관계 주인 필드의 글로벌 페치 전략 LAZY는 잘 동작한다.
        + test07는 지연로딩이 되지 않고 N+1이 발생하는 로그를 확인할 수 있다.(@Transacational도 없다)
            1) OnetoOne 양방향에서 mappedBy 선언된 연관 필드의 글로벌 페치 전략 LAZY는 무시된다.
            2) 이는 Proxy의 한계 때문에 발생하는데 반드시 해결해야 하면 Proxy 대신 bytecode instrumentation을 사용한다.
            3) EAGER로 즉시 로딩을 하지만 join도 되지 않는다. 따라서 N+1이 발생한다.
            4) BlogRepository의 findAll을 사용하지 말자. 개선된 메소드를 만들어 사용해야 한다.
    
    7) test08findAll2
        + 앞의 test07에서 문제가 되었던 기본메소드 findAll()를 QueryDSL 페치 조인인 적용된 findAll2()를 테스트한다.
        + 쿼리로그를 보면, 두 테이블에 inner join이 정상적으로 실행된 것을 알 수 있다.
            ```
                select
                    blog0_.no as no1_0_0_,
                    user1_.id as id1_1_1_,
                    blog0_.name as name2_0_0_,
                    user1_.blog_no as blog_no5_1_1_,
                    user1_.join_date as join_dat2_1_1_,
                    user1_.name as name3_1_1_,
                    user1_.password as password4_1_1_ 
                from
                    blog blog0_ 
                inner join
                    user user1_ 
                        on blog0_.no=user1_.blog_no          
            ```
    8) test09findAll3
        + 페치 조인과 함께 BlogDto3 클래스 방식으로 프로젝션하는 findAll3()를 테스트 한다.
        + @QueryProjection 사용하는 데, 기존 방식과 다른 것은 Projections.constructor, Projections.bean, Projections.field 이런 함수를 쓰지 않아도 된다.
        + BlogDto3에 @QueryProjection 적용
            ```
                public class BlogDto3 {
                    private Long no;
                    private String name;
                    private String userId;
                    
                          .
                          .
                          .
                
                    @QueryProjection
                    public BlogDto3(Long no, String name, String userId){
                        this.no = no;
                        this.name = name;
                        this.userId = userId;
                    }
                         .
                         .
                         .
                }              
            ```
            1) Q클래스를 자동 생성해야한다.
            2) JpaConfig.java 에 EntityManagerFactory Bean 설정에 정의한 DTO클래스가 스캐닝될 수 있도록 패키지를 추가해야 한다.
                ```
                    em.setPackagesToScan(new String[] { "me.kickscar.practices.jpa03.model07.domain", "me.kickscar.practices.jpa03.model07.dto" });
               
                ```
            3) QBlogDto3.java 가 자동으로 생성되며 import를 통해 Projection에 사용하면 된다.
                <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/37003.png" width="500px" />
                <br>
        + findAll3() 구현 코드
            ```
                return queryFactory
                        .select(new QBlogDto3(blog.no, blog.name, blog.user.id.as("userId")))
                        .from(blog)
                        .innerJoin(blog.user)
                        .fetch();          
            ```
            1) projection 이기 때문에 fetchJoin()를 사용하지 말아야 한다.
            2) 자동으로 생성된 QBlogDto3 클래스로 projection을 해야 한다.
            3) 비교적 레포지토리 코드가 간결해 졌다.
        + 쿼리로그
            ```
                select
                    blog0_.no as col_0_0_,
                    blog0_.name as col_1_0_,
                    user1_.id as col_2_0_ 
                from
                    blog blog0_ 
                inner join
                    user user1_ 
                        on blog0_.no=user1_.blog_no          
            ```
            1) inner join과 projection이 적용된 SQL문 로그를 확인할 수 있다.
    9) test09findAllByOrderByNoDesc
        + Spring Data JPA에서 지원하는 방식은 쿼리메소드 + DTO Interface 를 사용하는 방식이다.
        + 단점이기도 하고 당연한 것이기도 하지만, 쿼리메소드에서만 사용할 수 있다는 것이다.
        + DTO Interface 정의
            ```
                public interface BlogDto2 {
                    Long getNo();
                    String getName();
                    String getUserId();
                }
            ```
        + 쿼리 메소드 정의
            ```
                public interface JpaBlogRepository extends JpaRepository<Blog, Long>, JpaBlogQryDslRepository {
                    List<BlogDto2> findAllByOrderByNoDesc();
                }          
            ```
        + JPQL Projection에서는 기본적으로 반환하는 타입이 Tuple이다
        + DTO 인터페이스의 구현체 클래스 org.springframework.data.jpa.repository.query.AbstractJpaQuery$TupleConverter$TupleBackedMap을 JPA가 런타임 객체로 생성한다.
        + DTO 인터페이스는 @QueryProjection를 사용한 클래스기반 Projection에서 필요하던 Q클래스 스캐닝이 필요없기 때문에 별다른 설정을 할 필요가 없다.