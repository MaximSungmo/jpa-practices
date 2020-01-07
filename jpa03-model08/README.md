## Model08 : 일대일(OneToOne) - 양방향(Bidirectional), 대상테이블에 외래키, 식별관계


### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/38001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/38002.png" width="500px" />
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
       
3. __식별관계 매핑__
    1) OneToOne 에서는 주테이블 또는 대상테이블에 외래키를 두는 것이 가능하다.
    2) Model08에서는 대상테이블에 외래키(FK)를 두고 동시에 기본키(PK)로도 사용하여 주테이블과 **식별관계**로 매핑한다.
    3) 식별관계 VS 비식별관계
        + 식별 관계(Identifying Relationship)
            1) 식별 관계는 부모 테이블의 기본 키를 내려 받아서 자식 테이블의 기본키+외래키로 사용하는 관계다.
            2) 식별 관계는 부모 테이블의 기본 키를 자식 테이블로 전파하면서 자식 테이블의 기본키 컬럼이 점점 늘어난다. 조인할 때 SQL이 복잡해지고 기본키 인덱스가 불필요하게 커질 수 있다.
            3) 식별 관계는 2개 이상의 컬럼을 합해서 복합 기본키를 만들어야 하는 경우가 많다. JPA에서 복합키 클래스로 지원하지만 복잡하고 약간의 노력이 필요하다.
            4) 식별 관계를 사용할 때 기본키로 비즈니스 의미가 있는 자연키 컬럼을 조합하는 경우가 많다. 비즈니스 요구사 항은 시간이 지남에 따라 언젠가는 변한다. 식별 관계의 자연키 컬럼들이 자식에 손자까지 전파되면 변경하기 힘들다.
        + 비식별 관계(Non-Identifying Relationship)
            1) 비식별 관계는 부모 테이블의 기본키를 받아서 자식 테이블의 외래키로만 사용하는 관계다.(전통적으로 데이터베이스 스키마 모델링에서는 비식별 관계를 선호한다.)
            2) 비식별 관계의 기본키는 주로 대리 키를 사용하는데 JPA에서는 @GenerateValue 처럼 대리키를 생성하기 위한 편리한 방법을 제공한다.
        + 권고 사항
            1) 추천하는 방법은 될 수 있으면 비식별관계를 사용하고 기본키는 Long 타입의 대리키를 사용한다.
            2) 대리키(후보키, Alternate Key)는 비즈니스와 아무 관련이 없다. 따라서 비즈니스가 변경되어도 유연하게 대처할 수 있다.
            3) JPA는 @GenerateValue 를 통해 간편하게 대리키를 생성할 수 있다. 그리고 식별자 컬럼이 하나여서 쉽게 매핑할 수 있다.
            4) 식별자의 데이터 타입은 Long을 추천한다.
            5) 필수적 비식별 관계를 사용하는 것이 좋다. 선택적인 비식별 관계는 NULL 을 허용하므로 조인할 때에 외부 조인을 사용해야 한다. 반면에 필수적 관계는 NOT NULL 로 항상 관계가 있다는 것을 보장하므로 내부 조인만 사용해도 된다. 

    
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
            @OneToOne(mappedBy="user", fetch=FetchType.LAZY)
            private Blog blog;
            .
            .
        ```
        + OneToOne 에서는 Default Fetch Mode는 EAGER 이다. Global Fetch Mode LAZY로 수정했다. 
        + 외래키 관리를 대상테이블이 하기 때문에 관계 주인이 아님을 mappedBy를 통해 선언하고 반대편 엔티티(Blog)의 연관관계 필드를 지정했다.
      
    2) OneToOne(Blog 엔티티, 대상테이블)  
        
        ```
             .
             .
            @Id
            @Column(name = "id")
            private String id;
             .
             .
            @MapsId
            @OneToOne(fetch=FetchType.LAZY)
            @JoinColumn(name="id")
            private User user;       
             .
             .
        ```
        + @Id로 PK 설정을 했다. 식별관계로 부모테이블(User)의 PK를 사용할 것이기 때문에 String으로 타입을 바꿨다.
        + 외래키 관리를 해야 하기때문에 @JoinColumn(name="id")를 통해 외래키 설정을 하였다. 식별관계 설정을 위해 PK와 필드 이름이 같다.
        + 여기에 @MapsId를 통해 실제적인 PK 필드 id와 매핑을 해야 한다. 
    
    3) 생성 스키마
    
        ```
            Hibernate: 
                
                create table blog (
                    id varchar(24) not null,
                    name varchar(200) not null,
                    open_date datetime not null,       
                    primary key (id)
                ) engine=InnoDB
            Hibernate: 
                
                create table user (
                    id varchar(24) not null,
                    join_date datetime not null,
                    name varchar(24) not null,
                    password varchar(64) not null,
                    primary key (id)
                ) engine=InnoDB
            Hibernate: 
                
                alter table blog 
                   add constraint FKqx432k8povl16musk8wt5cn7y 
                   foreign key (id) 
                   references user (id)       
       
        ```
        + 두 테이블의 PK필드의 타입이 같고 FK설정을 통해 식별관계가 설정되어 있음을 알 수 있다.
        + Model07과 다르게 페이징을 위해 blog.openDate(개설날짜, 컬럼 open_date)을 추가 하였다.


### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. Model02[CrudRepository.save() 오해]와  Model06[String 기본키 사용 시 유의할 점] 함께 이해하기
2. Page<T> 를 반환하는 레포지토리 메소드 작성하기
3. QueryDSL Dynamic 조건절 작성 방법 vs Spring Data JPA Specification(명세, Criteria 기반)


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
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스
    2) 테스트를 위한 목적이기 때문에 메소드 추가가 없다.

2. __JpaUserRepositoryTest__
    1) test01Save
        + 순수객체(영속화되지 않은 객체, 엔티티매니저 관리 대상이 아닌 객체)를 save()로 전달하여 영속화 시키는 테스트이다.
        + CrudRepository.save(entity)호출 시, 외부에서 전달하는 entity 객체는 대부분 영속화되서 다음 코드에서 영속화 객체로 사용할 수 있다.
        + 대부분이 그렇고 아닌 경우도 있다. 다음 코드를 보자
            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/32008.png" width="800px" />
            <br>
            ```
                public boolean isNew(T entity) {
                
                    ID id = getId(entity);
                    Class<ID> idType = getIdType();
                
                    if (!idType.isPrimitive()) {
                        return id == null;
                    }
                
                    if (id instanceof Number) {
                        return ((Number) id).longValue() == 0L;
                    }
                
                    throw new IllegalArgumentException(String.format("Unsupported primitive id type %s!", idType));
                }          
            ```    
            1) entityInformation.isNew(entity)를 통해서 새롭게 영속화해야 할 객체인지 아니면 준영속객체로 merge를 해야할 지 결정하는 코드가 핵심이다.
            2) 앞의 대부분 Model들의 테스트에서는 "새롭게 영속화해야 객체"였기 때문에 그냥 파라미터로 전달된 엔티티 객체를 영속화된 객체로 간주하고 다음 코드에서 사용해도 큰 문제가 없었다.
            3) isNew() 함수를 보면, "새롭게 영속화해야 객체" 기준이 Id 타입임을 알 수 있다. 그리고 기본 데이터 타입(Primitive, int, long, ...) 이다.
            4) Model06, Model07, Model08은 Id(PK) 타입을 String을 쓰고 있기 때문에 "새롭게 영속화해야 객체" 로 인식하지 않는다.
            5) merge 대상이 되어 merge() 메소드가 반환하는 새롭게 영속화 된 객체를 save()가 반환하기 때문에 외부에서는 save()가 반환하는 객체를 사용하는 것이 안전하다.
        + 테스트에서는 save() 메소드에 파라미터로 전달되는 객체와 반환되는 객체의 영속화 유무를 확인하고 있다.       
    2) test02Save
        + save()가 insert SQL을 실행하는지 여부를 확인하는 테스트다.
        + test01에서 저장한 엔티티 객체와 같은 PK를 가지는 객체를 생성해 영속화 시켜본다.
        + @GeneratedValue 정책을 사용하지 않기 때문에 save()를 호출하면 select 쿼리가 먼저 실행된다.
        + 존재하지 않으면 insert 쿼리가 실행된다.
        + 존재하면 페치하여 엔티티 객체로 영속화 시키고 파라미터로 넘어온 순수객체의 값들로 변경할 것이다. 변경여부가 탐지되면 update 쿼리가 실행된다.
        + 로그를 확인해 보자.
            ```
                Hibernate: 
                    /* load me.kickscar.practices.jpa03.model08.domain.User */ select
                        user0_.id as id1_1_0_,
                        user0_.join_date as join_dat2_1_0_,
                        user0_.name as name3_1_0_,
                        user0_.password as password4_1_0_ 
                    from
                        user user0_ 
                    where
                        user0_.id=?
                Hibernate: 
                    /* load me.kickscar.practices.jpa03.model08.domain.Blog */ select
                        blog0_.id as id1_0_0_,
                        blog0_.name as name2_0_0_ 
                    from
                        blog blog0_ 
                    where
                        blog0_.id=?
                Hibernate: 
                    /* update
                        me.kickscar.practices.jpa03.model08.domain.User */ update
                            user 
                        set
                            join_date=?,
                            name=?,
                            password=? 
                        where
                            id=?              
            ```
            1) update 쿼리가 실행되었음을 알 수 있다.
            2) 두번째 쿼리를 보면 설정과 다르게 Lazy Loading을 하지 않고 EAGER로 즉시 로딩한 것을 발견할 수 있다.
                ```
                    @OneToOne(mappedBy="user", fetch=FetchType.LAZY)
                    private Blog blog;                        
                ```
                - OnetoOne Bidirectional 에서 mappedBy 선언된 연관 필드의 글로벌 페치 전략 LAZY는 무시된다.
                - 이는 Proxy의 한계 때문에 발생하는데 반드시 해결해야 하면, Proxy 대신 bytecode instrumentation을 사용하면 된다.
                - fetch join 하도록 QueryDSL로 작성해서 튜닝하는 방법도 있겠지만, OneToOne에서는 크게 문제되지 않을 것 같다.
                        
#### 2-4. JpaBlogRepository Test : Spring Data JPA 기반 Repository
1. __JpaBlogRepository__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스
    2) 쿼리 메소드 Page<Blog> findAll(Specification<Blog> spec, Pageable pageable) 가 정의되어 있다.
        + 쿼리 메소드는 함수 이름뿐만 아니라 반환타입 Page, 파라미터 Pageable 등을 유연하게 지원한다.
        + Spring Data JPA에서 지원하는 Specification(명세) 파라미터로 받고 있다.
            1) Specification
                - 간단히 설명하면, where절의 다양한 제약 조건들을 쉽게 조합하여 사용할 수 있는 기능이다.
                - POJO 클래스에 Specification 인터페이스를 구현한 객체를 반환하는 static 메소드를 적당한 이름으로 작성하면 Specification 구현은 끝난다.
                - 사용하기 위해서는 예제처럼 쿼리 메소드의 파라미터로 static 메소드를 호출하기만 하면 된다.
                - 문제는 쿼리 메소드 안에서 전달받은 Specification의 where(), and(), or()를 적절한 Criteria 함수 where(), and(), or()로 변한해야 한다.
                - 이는 직접 구현하지 않고 JpaSpecificationExecutor<Entity> 인터페이스르 쿼리메소드를 정의한 인터페이스에 상속하면 된다.
                    ```
                         public interface JpaBlogRepository extends JpaRepository<Blog, Long>, JpaBlogQryDslRepository, JpaSpecificationExecutor<Blog> {
                             public Page<Blog> findAll(Specification<Blog> spec, Pageable pageable);
                         }                 
                    ```  
            2) 구현 코드
                ```
                    public static Specification<Blog> withNameContains(String keyword) {
                        return new Specification<Blog>() {
                            @Override
                            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                                if(StringUtils.isEmpty(keyword)){
                                    return null;
                                }
                
                                return criteriaBuilder.like(root.<String>get("name"), "%"+keyword+"%");
                            }
                        };
                    }               
                ```
                + Criteria 기반 like 검색 구현
                + null를 리턴하면 where(), and(), or()는 무시한다.
                + 사용법은 테스트 코드 참고
            
3. __JpaBlogQryDslRepository Interface, JpaBlogQryDslRepositoryImpl class__ 
    1) findAll2(..), findAll3(...), findAll4(...) 메소드는 다음과 같은 것들이 구현되어 있다.
        + Page 객체를 반환한다. fetch()결과 List를 Page객체로 변환한다.
        + @QueryProjection을 사용하여 BlogDto로 Projection한다.
        + 글로벌 페치 전략 Lazy을 적용하지 않고 QueryDSL 기반의 inner join을 한다.
        + Pageable를 사용하여 QueryDSL 페이징을 구현하고 있다.
        + Optional 파라미터를 사용하여 QueryDSL 기반 동적 조건절이 구현되어 있다. 
    2) 3개의 메소드가 다른 점은 QueryDSL기반 동적 조건절 작성에 차이가 있다. 테스트에서 자세히 설명한다. 

2. __JpaBlogRepositoryTest__
    1) test01Save
        + 엔티티 객체 영속화
        + 외래키를 관리하는 Blog Entity의 user 필드를 User 엔티티 객체로 세팅해야 한다.(식별관계여서 PK필드를 세팅하는 것이 아니다. 착각금지. 예외 발생함) 
        + 테스트 데이터 저장
    2) test02findAll2
        + QueryDSL 동적 쿼리 예제1
        + findAll2(...) 메소드는 BooleanBuilder를 사용하고 where()에 파라미터로 BooleanBuilder 객체를 넘겨준다.
        + 쉬운 방법이나 코드가 길고 지저분하다.
        + name에 "블"이 있거나 블로그 주인이름에 "희"가 있는 블로그를 검색해서 첫페이지를 반환한다.
    3) test03findAll3
        + QueryDSL 동적 쿼리 예제2
        + findAll3(...) 메소드는 QueryDSL where(Predicate..)을 사용한다. 가변 파라미터에 조건을 사용해주면 되는데 null이면 무시하기 때문에 삼한연산자를 사용하면 코드가 길어지고 지저분해 지는 것을 피할 수 있다.
        + name에 "블"이 있는 블로그를 모두 검색해서 첫페이지를 반환한다. Optional.empty() 사용해서 userName에 null를 설정하는 테스트이다.
    4) test04findAll4
        + QueryDSL 동적 쿼리 예제2
        + findAll4(...) 메소드는 QueryDSL where(Predicate..)을 사용한다. 가변 파라미터가 아니고 Predicate를 연결해서 사용한다. 마찬가지로 null이면 무시하지만 삼항연산자를 사용하면 가변 파라미터를 사용하는 것에 피해 복잡해 보인다.
        + name에 "블"이 있는 블로그를 모두 검색해서 첫페이지를 반환한다. Optional.empty() 사용해서 userName에 null를 설정하는 테스트이다.
    5) test04findAll5
        + Spectification을 사용하는 퀄리 메소드 findAll(...)의 테스트이다.
            ```
                import static me.kickscar.practices.jpa03.model08.domain.specs.BlogSpecs.withNameContains;
                import static me.kickscar.practices.jpa03.model08.domain.specs.BlogSpecs.withUserNameContains;          
            ```
                    
            ```
                Page<Blog> pageBlogDto = blogRepository.findAll(
                    where( withNameContains("블") ).and( withUserNameContains("희") ), 
                    PageRequest.of(page++, size, Sort.Direction.DESC, "openDate"));
            ```
            1) Specification 클래스의 static 메소드를 static import하면 훨씬 더 편하고 이름만 잘 지으면 이해하기 편하게 사용할 수 있다.
            2) null을 리턴하는 코드를 함수 안에 두면 메소드 호출에 null체크를 하지 않아도 된다.
            3) 전반적으로 이해하기 쉽고 깔끔하다.
                
        + 장점은 이해하기가 쉽고 조건을 Specification 클래스의 함수 이름으로 조금 유연하게 작성할 수 있다.
        + 그리고 조합해서 사용할 수 있다는 점과 복잡성을 피할 수 있는 점도 장점이라 할 수 있다.
        + 주로 쿼리메소드에 사용할 수 있다. 만일 성능 또는 구현불가 이유로 QueryDSL 통합하여 메소드를 직접 구현해야 한다면, 불가능한 것은 아니지만 구현이 복잡해 질 것이다.
        + 이런 점을 고려해서 Specification과 QueryDSL동적 쿼리를 적절하게 혼용하면 된다.
         
