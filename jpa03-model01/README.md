## Model01 : 단일 엔티티


### 1. Domain
#### 1-1 Scheme 

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30001.png" width="600px" />

#### 1-2 Entity Class: Guestbook
1. me.kickscar.practices.jpa03.model01.domain.Guestbook.java 엔티티 매핑 참고  


### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. JPQL 그리고 QueryDSL, Spring Data JPA 기반의 각각의 레포지토리 구현을 이해한다.
2. 각각의 레포지토리를 작성하기 위한 설정 방법을 이해한다.
3. 레포지토리에서 영속객체를 다루는 방법과 트랜잭션과의 관계를 이해한다.
4. 연관관계가 없는 단일 엔티티 레포지토리 구현이기 때문에 CRUD 메소드 작성 방법을 각각의 레포지토리 별로 이해한다.
  
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


#### 2-3. JpqlGuestbookRepository Test : JPQL 기반 Repository
1. __JpqlConfig.java__
    1) Datasource Bean 설정  
    2) **TransactionManager 설정**
    3) PersistenceExceptionTranslationPostProcessor JPA 예외 전환 설정  
    4) LocalContainerEntityManagerFactoryBean 엔티티매니저팩토리 설정 (Repository에서 엔티티매니저 빈을 주입받기 위해)  
    5) EntityManager 빈 등록(Repository 빈에 주입)
    6) JPA Properties (jpa02-persistence-context 모듈의 appication.yml의 JPA 섹션과 비교해 보면 이해가 쉽다.)  

2. __JPA 트랜잭션 관리에 관해서...(중요개념)__
    1) 트랜잭션과 영속성켄텍스트
        - 객체 저장, 수정, 삭제, 탐색(특히, Fetch.LAZY)는 영속성켄텍스트 안의 객체를 대상으로 한다.
        - JPA에서는 트랜잭션이 시작할 때 영속성컨텍스트를 생성하고 트랜잭션이 끝날 때 영속성컨텍스트를 종료한다.(트랜잭션 범위 == 영속성 컨텍스트의 생존범위)
        - 이는 트랜잭션이 시작과 끝에 많은 경우의 JPA 작업을 해야 한다는 뜻이다.
    2) Spring Conatiner 에서의 트랜잭션과 영속성컨텍스트 
        - Spring Conatiner는 하나의 쓰레드에 하나의 트랜잭션을 할당한다.(중요)
        - SpringMVC에서는 하나의 요청에 한 개의 쓰레드가 할당 된다.
        - 이 쓰레드에서, 서비스와 레포지토리를 거치면서 다수의 엔티티매니저가 객체의 영속성에 관여할 수 있으나 하나의 쓰레드 즉, 트랜잭션이 같기 때문에 영속성컨텍스트는 같다.
        - 반대로, **여러 요청에 대한 여러 쓰레드에서 같은 엔티티매니저를 쓰는 경우는 SpringMVC에서는 흔한 일이다. 이런 경우는 영속성컨텍스트가 다르기 때문에 멀티쓰레드에서는 안전하다.**
        - 스프링 MVC에서는 비즈니스 로직을 시작하는 서비스 계층에서 AOP가 적용된 @Transactional으로 트랜잭션을 시작하는 것이 보통이다.
    3) 결론은 Spring Container가 알아서 쓰레드와 연관된 트랜잭션과 영속성켄텍스트의 관리를 맡아주니 개발자는 @Transactional과 비즈니스 로직에만 집중하면 된다.
    4) 하지만, 이 내용을 숙지하지 못하면 지연로딩이나 프록시초기화 등에 문제가 발생하면 해결에 많은 노력을 기울여야 하기 때문에 꼭 이해해야 할 개념이다. 
  
3. __JpqlGuestbookRepository.java__  
    1) JPQL 기반으로 작성
        - 객체지향쿼리의 핵심은 JPQL이다. JPQL이 **가장 기본**이고 **제일 중요**하다 
        - Criteria, QueryDSL은 문자열 쿼리 기반의 JPQL를 객체지향쿼리로 쓰기 위한 일종의 Helper Wrapping 라이브러리이다.
        - 쿼리로그를 보면 JPQL과 SQL이 나오는데, 이는 JPQL로 변환된 것이 최종적으로 SQL로 변환되기 때문이다.     
    2) 영속화 
    3) TypedQuery 객체 사용
    4) Projection 및 Order by 지원
    5) 집합함수: 통계 쿼리 스칼라 타입 조회
      
4. __JpqlGuestbookRepositoryTest.java__
    1) test01Save
        - JpqlGuestbookRepository.save(Guestbook)
        - 객체 영속화

    2) test02FindAll1
       - JpqlGuestbookRepository.findAll1()
       - TypedQuery 객체 사용
       - Order By 지원 
         
    3) test03FindAll2
       - JpqlGuestbookRepository.findAll2()
       - TypedQuery 객체 사용
       - Projection: 방명록 리스트에서는 모든 정보를 담고 있는 Guestbook Entity로 받을 필요가 없다. 이런 경우 DTO(VO) 객체에 필요한 컬럼만 프로젝션한다.  
       - Order By 지원 
         
    4) test04Delete
       - JpqlGuestbookRepository.delete(Long, String)
       - TypedQuery 객체 사용  
       - 이름 기반 파라미터 바인딩

#### 2-4. QueryDslGuestbookRepository Test : QueryDSL 기반 Repository
1. __JpqlConfig.java__
    1) JPQL과 설정파일 동일
    2) QueryDSL은 JPQL을 사용하기 쉽게, 특히 Criteria 대용의 JPQL래퍼 라이브러리 이다.
    3) 쿼리를 문자열 기반이 아닌 코드로 작성한다.(다양한 쿼리함수 사용법을 익혀야 한다.)
    4) QueryDSL Repository에 JPAQueryFactory를 주입하기 위한 빈설정이 추가적으로 필요하다.
     
        ```
          @PersistenceContext
          private EntityManager entityManager;
       
        ```
 
        ```
         @Bean
         public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory( entityManager );
         }
      
        ```

2. __QueryDslGuestbookRepository.java__  
    1) QueryDSL를 편하게 쓰기 위해 JPAQueryFactory Bean을 주입 받는다.
    2) 영속화 관리를 위해 EntityManager 주입 받을 필요는 없다. 부모 클래스 QuerydslRepositorySupport의 getEntityManager()를 사용한다.
    3) **컴파일 오류**
       
        ```
         import static me.kickscar.practices.jpa03.model01.domain.QGuestbook.guestbook;  
            .  
            .  
            .  
 
            @Transactional
            public Boolean remove( Guestbook parameter ) {
               return queryFactory
                  .delete( guestbook )
                  .where( guestbook.no.eq( parameter.getNo() ).and( guestbook.password.eq( parameter.getPassword() ) ) )
                  .execute() == 1;
            }
        ```
        **쿼리타입 클래스 QGuestbook가 없기 때문에 발생!!!**  

3. __QueryDSL를 위한 쿼리타입 QClass(쿼리용 클래스, Q로 시작) QGuestbook 생성하기__
   
    1) querydsl plugin for gradle를 build.gradle 에 설정하고 Build Task의 build 와 clean 실행을 통해 생성과 삭제를 한다. 
    2) querydsl plugin 설정 (build.gradle)
     
        - dependency 추가
         
            ```
             dependencies {
    
               compile('org.springframework.boot:spring-boot-starter-web:2.1.8.RELEASE')
               compile('org.springframework.boot:spring-boot-starter-data-jpa:2.1.8.RELEASE')
               compile('org.hibernate:hibernate-entitymanager:5.4.4.Final')
               compile('mysql:mysql-connector-java:8.0.16')
               runtimeOnly('com.h2database:h2:1.4.197')
               testCompile('junit:junit:4.12')
    
               compile('com.querydsl:querydsl-jpa:4.1.4') // querydsl-jpa 추가
               compile('com.mysema.querydsl:querydsl-apt:3.7.4') // querydsl-apt 추가 
    
               // annotationProcessor 추가
               annotationProcessor(
                  'com.querydsl:querydsl-apt:4.1.4:jpa',
                  'org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final',
                  'javax.annotation:javax.annotation-api:1.3.2'
               )
             }     
            ```
         
        - querydsl gradle plugin 설정
       
            ```
              /* querydsl PlugIn Configuration */
    
              def querydslGenDirectory = 'src/main/generated'
    
              sourceSets {
                main.java.srcDirs += [ querydsldslGenDirectory ]
              }
    
              tasks.withType(JavaCompile) {
                 options.encoding = 'UTF-8'
                 options.annotationProcessorGeneratedSourcesDirectory = file(querydslGenDirectory)
              }
    
              clean.doLast {
                 file(querydslGenDirectory).deleteDir()
              }  
            ```
            설정은 gradle 버젼에 매우 민감하다(gradle 5.4 기준이다. 프로젝트의 gradle wrapper를 빌드에 사용하면 큰 문제가 없다.)   
     
    3) Build Task의 build 또는 Other Task의 compileJava 함수 실행  
  
        <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30002.png" width="400px" />
        <br/>

        생성되었다!!!  
        <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30003.png" width="400px" />
  
        - Build Task clean 함수 실행으로 삭제할 수 있다.
     
4. __QueryDSLGuestbookRepositoryTest.java__  
    1) test01Save
        - QueryDSLGuestbookRepository.save(Guestbook)
        - 객체 영속화
         
    2) test02FindAll1
        - QueryDSLGuestbookRepository.findAll1()
        - from(), orderBy(), fetch()
         
    3) test03Delete
        - QueryDSLGuestbookRepository.delete(Long, String)
        - delete(), where(), eq(), end(), execute()  

    4) test04FindAll2
        - QueryDSLGuestbookRepository.findAll2()
        - Projection을 위한 QueryDsl Projection Projections.constructor(..) 사용법
         
    5) QueryDSLGuestbookRepository.count() 메소드
        - fetchCount()


#### 2-5. JpaGuestbookRepository Test : Spring Data JPA 기반 Repository
1. __JpaConfig.java__
    1) JPQL(QueryDSL포함) 설정 클래스인 JpqlConfig.java와 다르다.
    2) 설정 클래스에 @EnableJpaRepositories 어노테이션으로 JPA Repositories 활성화해야 한다.
    3) JPA Repositories 활성화: JpaRepository 인터페이스를 상속받은 Repository Interface 에 대한 구현체 생성을 애플리케이션 실행 처음에 Spring Data JPA가 자동으로 하는 것

        ```
          @Configuration
          @EnableTransactionManagement
          @EnableJpaRepositories(basePackages = { "me.kickscar.practices.jpa03.model01.repository" })
          public class JpaConfig {
                 .  
                 .
                 .    
          }
            
        ```
        ****
      
2. __JpaGuestbookRepository.java__
    
    1) JpaRepository Interface 
        - Spring Data JPA에서 제공하는 인테페이스로 상속받은 Repoitory Interface에 기본적인 CRUD 메서드를 제공한다.         
        - 구현체는 애플리케이션 처음 시작 시, Spring Data JPA가 생성해서 제공해 준다.  
        - 즉, **데이터 접근 계층(DAO, Repository) 개발할 떄 구현 클래스 없이 인터페이스만 작성해도 개발을 완료할 수 있다.**  
     
    2) 기본적으로 JpaRepository를 상속하는 Repository 인터페이스를 생성한다.  
     
        ```java
          public interface JpaGuestbookRepository extends JpaRepository<Guestbook, Long> {
          }
        ```    
        
        - 이 코드로도 다음과 같은 메소드를 직접 불러 사용할 수 있다.   
            1) save(S), findOne(Id), exists(Id), count(), detete(T), deleteAll() - CRUD 기능  
            2) findAll(Sort), findAll(Pageable) - 정열 및 패이징  
        - 더 막강한 기능은 **쿼리메소드** 기능이다. (메소드이름으로 내부에서 JPQL를 생성해서 호출, 예제코드 참고)  
        - JPA NamedQuery 작성이 가능하다.   
        - QueryDSL과 통합이 가능하다  
        - Specification를 통해 검색조건을 다양하게 조립하여 사용할 수 있다.  
  
4. __JpaGuestbookRepositoryTest.java__
    1) 방명록에 직접 사용하지 않지만 기본적으로 제공되는 메소드와 쿼리메소드(2걔) 테스트
    2) Repositry 인터페이스 상속 Hierarchy  
        - JpaGuestbookRepository -> JpaRepository -> PagingAndSortingRepository -> CrudRepository -> Repository
        - 상위 인터페이스 JpaRepository, PagingAndSortingRepository, CrudRepositor 들의 메소드들을 별다른 구현없이 사용 가능하다.
    3) test01Save()
        - CrudRepository.save(S)
    4) test02FindAll   
        - JpaRepository.findAll()
    5) test03FindAll()  
        - PagingAndSortingRepository.findAll(Sort)
    6) test04FindAll()  
        - PagingAndSortingRepository.findAll(Pageable)
    7) test05FindAllByOrderByRegDateDesc
        - JpaGuestbookRepository.findAllByOrderByRegDateDesc()
        - JpaGuestbookRepository **쿼리메소드 예시**
    8) test06FindByIdAndDelete()  
        - CrudRepository.findById(ID)
        - CrudRepository.delete(S)
    9) test07FindDeleteById()  
        - CrudRepository.deleteById(id)
   10) test08DeleteByNoAndPassword
        - JpaGuestbookRepository.deleteByNoAndPassword(id, password)
        - JpaGuestbookRepository **쿼리메소드 예시**