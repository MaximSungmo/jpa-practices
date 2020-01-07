## Model09 : 다대다(ManyToMany) 단방향(Unidirectional)


### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/39001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/39002.png" width="500px" />
<br>
        
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 음반검색에서 노래와 쟝르의 관계이다.
    2) 노래 정보를 보여줄 때 그 노래의 쟝르가 필요한 경우다.
    3) 쟝르 검색을 통해 해당 노래를 찾는 것도 필요하지만 이는 다대다 양방향(Bidirectional)에서 다룬다.
    4) Model09에서는 Song -> Genre로 참조가 이루어 지는 단방향(Unidirection)을 매핑한다.

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) Song은 다수의 쟝르에 포함될 수 있다. 쟝르도 해당 쟝르의 노래들이 많다.
    2) Song(\*) -> Genre(\*)
    3) ManyToMany 이다.
       
3. __다대다 연관관계의 관계형 데이터베이스와 JPA에서의 차이점__
    1) 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다.
    2) 그래서 다대다 관계를 일대다, 다대일 관계로 풀어내는 연결테이블을 사용한다.
        
        <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/39003.png" width="800px" />
        <br>
            
    3) 객체는 테이블과 다르게 객체 2개로 다대다 관계를 만들 수 있다.
    4) Song 객체는 컬렉션을 사용해서 Genre들을 참조하면 되고 반대로 Genre들도 컬렉션을 사용해서 Song들을 참조하면 된다.
    5) 내부적으로는 데이터베이스에서는 조인테이블을 두고 일대다, 다대일 관계로 풀어낸다. 
    
#### 1-2. Entity Class: User, Blog
1. __Song 엔티티 매핑 참고__
2. __Genre 엔티티 매핑 참고__
3. __연관관계 매핑__
    1) ManyToMany(Song 엔티티)
        
        ```
            .
            .
            @Id
            @Column(name = "no")
            @GeneratedValue( strategy = GenerationType.IDENTITY  )
            private Long no;       
            .
            .
            @ManyToMany
            @JoinTable(name = "song_genre", joinColumns = @JoinColumn(name = "song_no"), inverseJoinColumns = @JoinColumn(name = "genre_no"))
            private List<Genre> genres = new ArrayList<Genre>();
            .
            .
        ```
        + @ManyToMany 와 @JoinTable 을 사용해서 연결 테이블을 바로 매핑한다.
        + 노래와 쟝르를 연결하는 노래_쟝르(Song_Genre)엔티티 없이 매핑을 완료할 수 있다.
        + ManyToMany 기본 페치 전략은 LAZY 이다.
        + @JoinTable.name : 연결 테이블을 지정한다. 
        + @JoinTable.joinColumns : 현재 방향인 노래와 매핑할 조인 컬럼 정보를 지정한다. song_no로 지정
        + @JoinTable.inverseJoinColumns : 반대 방향인 쟝릐와 매핑할 조인 컬럼 정보를 지정한다. genre_no로 지정했다.
        + @ManyToMany로 매핑한 덕분에 다대다 관계를 사용할 때는 이 연결테이블을 신경쓰지 않아도 된다.
      
    2) ManyToMany(Genre 엔티티)
        
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
        + ManyToMany 단방향에서는 관계주인이 아닌 엔티티는 어떤 설정도 없다.
    
    3) 생성 스키마
    
        ```
            Hibernate: 
                
                create table genre (
                   no bigint not null auto_increment,
                    abbr_name varchar(5) not null,
                    name varchar(50) not null,
                    primary key (no)
                ) engine=InnoDB
            Hibernate: 
                
                create table song (
                   no bigint not null auto_increment,
                    title varchar(100) not null,
                    primary key (no)
                ) engine=InnoDB
            Hibernate: 
                
                create table song_genre (
                   song_no bigint not null,
                    genre_no bigint not null
                ) engine=InnoDB
            Hibernate: 
                
                alter table song_genre 
                   add constraint FKmdutew4w1ll7a9nd8uhvnajs2 
                   foreign key (genre_no) 
                   references genre (no)
            Hibernate: 
                
                alter table song_genre 
                   add constraint FK5njrlut9t666xo6kp4f339eua 
                   foreign key (song_no) 
                   references song (no)     
       
        ```
        + 세 개의 테이블을 생성한다.
        + 엔티티 테이블외에 song_genre 연결테이블이 생성 되었다.
        + song_genre 테이블은 다대다 관계를 일대다, 다대일 관계로 풀어내기 위해 필요한 연결(조인)테이블이다.


### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. ManyToMany 단방향(Unidirectional)의 Collection Fetch QueryDSL 구현
2. ManyToMany 조인테이블의 문제점 이해와 해결방법
3. Native SQL 사용법

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


#### 2-3. JpaSongRepository Test : Spring Data JPA 기반 Repository
1. __JpaGenreRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스
    2) 테스트를 위한 Genre 엔티티 영속화 목적이기 때문에 별다른 메소드 추가가 없다.

2. __JpaSongRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

3. __JpaSongQryDslRepositry__
    1) Lazy 로딩으로 Genre를 가져오지 않고 fetch join으로 Genre가 포함된 Song을 가져오는 메소드 2개를 정의
        + Song findById2(no) : no(PK)로 Genre가 포함된 Song 객체 한 개를 가져온다.
        + List<Song> findAll2() : Genre가 포함된 Song 객체 리스트를 가져온다. 

4. __JpaSongQryDslRepositryImpl__
    1) findById2, findAll2의 구현
    2) QueryDSL 통합 구현

5. __JpaSongRepositoryTest__
    1) test01Save
        + 쟝르1, 쟝르2와 노래1의 연관관계를 설정했다.
        + 쟝르1, 쟝르4와 노래2의 연관관계를 설정했다.
        + 노래1, 노래2를 각각 저장할 때 연결 테이블에도 값이 저장된다.
        + 노래1이 저장될 때 실행된 SQL
            ```
                Hibernate: 
                    /* insert me.kickscar.practices.jpa03.model09.domain.Song
                        */ insert 
                        into
                            song
                            (title) 
                        values
                            (?)

                Hibernate: 
                    /* insert collection
                        row me.kickscar.practices.jpa03.model09.domain.Song.genres */ insert 
                        into
                            song_genre
                            (song_no, genre_no) 
                        values
                            (?, ?)
                Hibernate: 
                    /* insert collection
                        row me.kickscar.practices.jpa03.model09.domain.Song.genres */ insert 
                        into
                            song_genre
                            (song_no, genre_no) 
                        values
                            (?, ?)                              
            ```    
    2) test02FindById
        + 기본 메소드 findById(no) 테스트
        + Lazy 로딩이기 때문에 Genre 객체를 탐색하기 전까지는 지연 로딩을 한다.
            ```
                Hibernate: 
                    select
                        song0_.no as no1_1_0_,
                        song0_.title as title2_1_0_ 
                    from
                        song song0_ 
                    where
                        song0_.no=?          
            ```
        + genres의 size()를 호출하기 전까지는 프록시 객체 초기화가 되지 않았다.
        + genres의 size()를 호출하면 genre 리스트를 가져오는 쿼리가 실행된다.
            ```
                Hibernate: 
                    select
                        genres0_.song_no as song_no1_2_0_,
                        genres0_.genre_no as genre_no2_2_0_,
                        genre1_.no as no1_0_1_,
                        genre1_.abbr_name as abbr_nam2_0_1_,
                        genre1_.name as name3_0_1_ 
                    from
                        song_genre genres0_ 
                    inner join
                        genre genre1_ 
                            on genres0_.genre_no=genre1_.no 
                    where
                        genres0_.song_no=?          
            ```
         + 마지막에서 프록시 객체가 초기화되어 콜렉션이 채워져 있음을 알수 있다.
    3) test03FindById2
        + test02FindById의 기본 메소드 findById()를 사용하여 Genre의 지연로딩이 필요할 때도 있겠지만 한 번에 Genre Collection을 가져와야 할 때도 있을 것이다.
        + findById2는 QueryDSL를 사용하여 Collection fetch join을 한다.
        + Collection Join은 Collection 필드를 가지고 있는 엔티티가 여러개 페치되는 문제가 있다.
        + 이를 해결하는 방법으로 selectDistinct()를 사용한다.
        + 주의할 것은 조인테이블에 genre가 없으면, join시 select되는 row가 없기 때문에 outer join을 사용해야 한다.
            ```
                return queryFactory
                    .selectDistinct(song)
                    .from(song)
                    .leftJoin(song.genres, genre)
                    .fetchJoin()
                    .where(song.no.eq(no))
                    .fetchOne();          
            ```
        + 쿼리 로그
            ```
                select
                    distinct song0_.no as no1_1_0_,
                    genre2_.no as no1_0_1_,
                    song0_.title as title2_1_0_,
                    genre2_.abbr_name as abbr_nam2_0_1_,
                    genre2_.name as name3_0_1_,
                    genres1_.song_no as song_no1_2_0__,
                    genres1_.genre_no as genre_no2_2_0__ 
                from
                    song song0_ 
                left outer join
                    song_genre genres1_ 
                        on song0_.no=genres1_.song_no 
                left outer join
                    genre genre2_ 
                        on genres1_.genre_no=genre2_.no 
                where
                    song0_.no=?          
            ```
    4) test04FindAll
        + 기본 메소드 findAll()를 사용하여 전체 노래를 가져온다.
        + Lazy 로딩을 확인하는 테스트이다.
    5) test05FindAll2
        + fetch join을 사용하여 한 번에 모두 가져오는 메소드 findAll2()에 대한 테스트 이다.
        + QueryDSL로 구현하였다.
        + 주의할 것은 조인테이블에 genre가 없으면, join시 select되는 row가 없기 때문에 outer join을 사용해야 한다.
            ```
                return queryFactory
                    .selectDistinct(song)
                    .from(song)
                    .leftJoin(song.genres, genre)
                    .fetchJoin()
                    .where(song.no.eq(no))
                    .fetchOne();
          
            ```         
        + 쿼리 로그
            ```
                select
                    distinct song0_.no as no1_1_0_,
                    genre2_.no as no1_0_1_,
                    song0_.title as title2_1_0_,
                    genre2_.abbr_name as abbr_nam2_0_1_,
                    genre2_.name as name3_0_1_,
                    genres1_.song_no as song_no1_2_0__,
                    genres1_.genre_no as genre_no2_2_0__ 
                from
                    song song0_ 
                inner join
                    song_genre genres1_ 
                        on song0_.no=genres1_.song_no 
                inner join
                    genre genre2_ 
                        on genres1_.genre_no=genre2_.no          
            ``` 
    6) test06RemoveGenre1
        + ManyToMany 에서 삭제시, 조인테이블의 문제점을 테스트한다.
        + Song 엔티티 객체에서 Genre를 삭제하는 테스트 이다.
        + song1는 genre1, grenre2가 추가되어 있다.
        + song1에서 genre1를 삭제한다.
        + 쿼리 로그
          
            ```
                Hibernate: 
                    select
                        song0_.no as no1_1_0_,
                        song0_.title as title2_1_0_ 
                    from
                        song song0_ 
                    where
                        song0_.no=?
                Hibernate: 
                    select
                        genre0_.no as no1_0_0_,
                        genre0_.abbr_name as abbr_nam2_0_0_,
                        genre0_.name as name3_0_0_ 
                    from
                        genre genre0_ 
                    where
                        genre0_.no=?
                Hibernate: 
                    select
                        genres0_.song_no as song_no1_2_0_,
                        genres0_.genre_no as genre_no2_2_0_,
                        genre1_.no as no1_0_1_,
                        genre1_.abbr_name as abbr_nam2_0_1_,
                        genre1_.name as name3_0_1_ 
                    from
                        song_genre genres0_ 
                    inner join
                        genre genre1_ 
                            on genres0_.genre_no=genre1_.no 
                    where
                        genres0_.song_no=?
                Hibernate: 
                    /* delete collection me.kickscar.practices.jpa03.model09.domain.Song.genres */ delete 
                        from
                            song_genre 
                        where
                            song_no=?
                Hibernate: 
                    /* insert collection
                        row me.kickscar.practices.jpa03.model09.domain.Song.genres */ insert 
                        into
                            song_genre
                            (song_no, genre_no) 
                        values
                            (?, ?)         
            ```    
            
            1) 첫번째, 두번째 select 쿼리는 삭제 대상이 되는 song, genre 엔티티를 각각의 no(PK)로 찾아 영속화한다.
            2) 세번째 join fetch는 삭제를 위해 해당 song의 genre 콜렉션을 지연 로딩한다.
            3) 네번째는 조인테이블에서 해당 song을 모두 지운다. 주목할 것은 지워야 할 genre의 관계만 삭제되는 것이 아니라 모든 관계가 삭제된다는 것이다. 
            4) 다섯번째는 삭제하지 말아야 할 관계가 3)에서 삭제되었기 때문에 다시 복구를 위해 insert를 한다.
            5) 직접 SQL를 작성해서 이 작업을 한다면, delete from song_genre where song_no=? and genre_no=? 이런 쿼리를 작성했을 것이다.
            6) JPA에서는 이런 쿼리 작성이 객체(엔티티)를 통해서는 원칙적으로 불가능하다.
                - 필드 연관관계 매핑을 통해 연결(조인)테이블을 지정하였고 연결(조인)테이블은 매핑된 엔티티 클래스가 없기 때문이다.
                - 객체 그래프 탐색을 통해 연결(조인)테이블에는 접근할 수 없다.
                - 즉, JPQL를 사용할 수 없다는 것이다.
            7) 해결방법
                - Native SQL를 사용할 수 있다.
                - 쉬운 방법은 컬렉션을 List보다 Set를 사용하는 것이다.(Model10에서 구현하고 테스트 한다.)
                - 가장 좋은 방법은 ManyToMany 보다는 조인테이블에 해당하는 비지니스 엔티티가 있다면 엔티티로 매핑하여 ManyToOne 두 개의 관계로 해결하는 것이다.(Model11, Model12에서 구현하였고 테스트 한다.) 
    7) test06RemoveGenre2
        + test06DeleteGenre1의 문제점을 Native SQL를 사용하여 해결하였다.
        + Native SQL
            1) 어떤 이유로 JPQL를 사용할 수 없을 때, SQL을 직접 사용하는 것이다.
            2) JPQL를 작성하면 JPA가 JPQL를 SQL로 변환하는 데, Native SQL은 개발자가 직접 SQL를 작성하는 것이다.
            3) Native SQL를 사용해도 앤티티 조회는 물론 JPA가 지원하는 영속성 컨텍스트를 그대로 사용할 수 있다.
        + 작성 코드
            ```
                String sqlString = "DELETE from song_genre where song_no=? and genre_no=?";
                Query query = getEntityManager().createNativeQuery(sqlString);
        
                query.setParameter(1, songNo);
                query.setParameter(2, genreNo);
        
                query.executeUpdate();
          
            ```
        + 쿼리 로그
            ```
                /* dynamic native SQL query */ DELETE 
                    from
                        song_genre 
                    where
                        song_no=? 
                        and genre_no=?         
            ```  

#### 2-4. JpaGenreRepository Test : Spring Data JPA 기반 Repository
1. __Song -> Genre 단방향이기 때문에  Genre쪽에서는 객체 탐색은 불가능하다.__
2. __저장, 삭제, 변경, 카운팅 정도의 기본 메소드 사용정도이기 때문에 테스트 생략__


         
