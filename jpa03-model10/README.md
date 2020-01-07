## Model10 : 다대다(ManyToMany) 양방향(Biidirectional)


### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/31001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/31002.png" width="500px" />
<br>
        
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 음반검색에서 노래와 쟝르의 관계이다.
    2) 노래 정보를 보여줄 때 그 노래의 쟝르가 필요한 경우다.
    3) 쟝르 검색을 통해 해당 노래를 찾는 등의 기능이 필요한 경우다.
    4) Model10에서는 Song <-> Genre로 양쪽에서 모두 참조가 이루어 지는 양방향(Biidirection)을 매핑한다.

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) Song은 다수의 쟝르에 포함될 수 있다. 쟝르도 해당 쟝르의 노래들이 많다.
    2) Song(\*) <-> Genre(\*)
    3) ManyToMany 이다.
       
3. __다대다 연관관계의 관계형 데이터베이스와 JPA에서의 차이점__
    1) 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다.
    2) 그래서 보통 다대다 관계 를 일대다, 다대일 관계로 풀어내는 연결 테이블을 사용한다.
        
        <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/31003.png" width="800px" />
        <br>    
    3) 객체는 테이블과 다르게 객체 2개로 다대다 관계를 만들 수 있다.
    4) Song 객체는 컬렉션을 사용해서 Genre들을 참조하면 되고 반대로 Genre들도 컬렉션을 사용해서 Song들을 참조하면 된다.
    
#### 1-2. Entity Class: User, Blog
1. __Song 엔티티 매핑 참고__
2. __Jenre 엔티티 매핑 참고__
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
            private Set<Genre> genres = new HashSet<Genre>();
            .
            .
        ```
        + @ManyToMany 와 @JoinTable 을 사용해서 연결 테이블을 바로 매핑한다.
        + Model09와 다르게 Set를 사용하여 컬렉션 연관 필드를 정의했다.
      
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
            @ManyToMany(mappedBy = "genres")
            private List<Song> songs = new ArrayList<Song>();
             .
             .       
        ```
        + ManyToMany 양방향에서는 관계주인이 아닌 엔티티는 mappedBy를 통해 관계의 주인이 아님을 선언한다.
        + 여기서는 Song이 관계의 주인이다.
        + 마찬가지로 Set 타입으로 컬렉션 역관관계 필드를 설정했다.
        
    3) Song 엔티티 클래스에 편의 메소드 추가
        ```
            public void addGenres(Genre genre){
                genres.add(genre);
                genre.getSongs().add(this);
            }
       
            public void removeGenre(Genre genre){
                genres.remove(genre);
                genre.getSongs().remove(this);
            }
        ```
        + 엔티티 양쪽에 연관필드가 있기 때문에 두 곳에 설정을 해주어야 한다.
        + Song에 Genre를 추가할 때 Genre의 Song Collection에 자신을 추가해 준다.
        + 반대로, Song에 Genre를 삭제할 때 Genre의 Song Collection에 자신을 삭제한다.

    4) Genre 엔티티 클래스에 편의 메소드 추가
        ```
            public void addSong(Song song) {
                songs.add(song);
                song.getGenres().add(this);
            }
        
            public void removeSong(Song song){
                song.getGenres().remove(this);
                songs.remove(this);
            }
       
        ```
        + 엔티티 양쪽에 연관필드가 있기 때문에 두 곳에 설정을 해주어야 한다.
        + Genre에 Song을 추가할 때 Song의 Genre Collection에 자신을 추가해 준다.
        + 반대로, Genre에 Song을 삭제할 때 Song의 Genre Collection에서 자신을 삭제한다.
        
    5) 생성 스키마
    
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
        + ManyToMany 단방향(Unidirectional)과 다른점이 없다. 세 개의 테이블을 생성한다.
        + 엔티티 테이블외에 song_genre 연결테이블이 생성 되었다.
        + song_genre 테이블은 다대다 관계를 일대다, 다대일 관계로 풀어내기 위해 필요한 연결 테이블이다.

### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. ManyToMany 컬렉션 연관 필드의 타입은 Set를 사용해야 하는 이유 이해하기


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
1. __JpaSongRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

2. __JpaSongRepositoryTest__
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
    2) test02DeleteGenre
        + ManyToMany 에서 삭제시, 조인테이블의 문제점을 해결한 테스트이다.
        + 조인테이블의 해당 song의 genre를 모두 삭제하고 삭제하려는 genre만 빼고 다시 insert하는 문제를 해결한다.
        + 테스트에서 실행된 삭제 쿼리 로그를 보면,
            ```
                Hibernate: 
                    /* delete collection row me.kickscar.practices.jpa03.model10.domain.Song.genres */ delete 
                        from
                            song_genre 
                        where
                            song_no=? 
                            and genre_no=?

            ```
            1) song_no와 genre_no를 사용하여 조인테이블의 하나의 row만 삭제한다.
            2) 이는 엔티티 클래스의 연관 매핑 필드에 컬렉션 타입을 List대신 Set을 사용하였기 때문이다.


#### 2-4. JpaGernreRepository Test : Spring Data JPA 기반 Repository
         
1. __JpaGenreRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위해 Genre 엔티티 영속화 목적이기 때문에 별다른 쿼리메소드 추가가 없다.

2. __JpaGenreQryDslRepositry__
    1) Lazy 로딩으로 Genre를 가져오지 않고 fetch join으로 Song이 포함된 Genre을 가져오는 메소드 2개를 정의
    2) Genre findById2(no) - no(PK)로 Song이 포함된 Grenre 엔티티 객체 1개를 가져온다.
    3) List<Song> findAll2() - Genre가 포함된 Song 엔티티 객체 리스트를 가져온다. 

3. __JpaGenreQryDslRepositryImpl__
    1) findById2, findAll2의 구현
    2) QueryDSL 통합 구현
    
4. __JpaSongRepositoryTest__
    1) Model09 ManyToMany 단방향에서 했던 JpaSongRepository 테스트를 양방향이니깐 반대편 JpaGenreRepository에서도 똑같이 한다.  
    2) test01Save
        + 쟝르1, 쟝르2와 노래1의 연관관계를 설정했다.
        + 쟝르1, 쟝르4와 노래2의 연관관계를 설정했다.
        + 쟝르1,2,4들을 각각 저장할 때 연결테이블에도 값이 저장된다.
        + ManyToMany 양방향에선 두 엔티티 어느쪽 컬렉션에 값을 담아도 실행 쿼리는 같다.
    
    3) test02FindById
        + 기본 메소드 findById(no) 테스트
        + Lazy 로딩이기 때문에 Song객체를 탐색하기 전까지는 지연 로딩을 한다.
        + 지연 로딩을 확인하는 테스트이다.

    4) test03FindById2
        + test02FindById의 기본 메소드 findById()를 사용하여 Genre의 지연로딩이 필요할 때도 있겠지만 한 번에 Song Collection을 가져와야 할 때도 있을 것이다.(쟝르검색)
        + findById2는 QueryDSL를 사용하여 Collection fetch join을 한다.
        + Collection Join은 Collection 필드를 가지고 있는 엔티티가 여러개 페치되는 문제가 있다.
        + 이를 해결하는 방법으로 selectDistinct()를 사용한다.
        + 주의할 것은 조인테이블에 연결된 song이 없으면, join시 select되는 row가 없기 때문에 outer join을 사용해야 한다.
            ```
                return queryFactory
                        .selectDistinct(genre)
                        .from(genre)
                        .leftJoin(genre.songs, song)
                        .fetchJoin()
                        .where(genre.no.eq(no))
                        .fetchOne();  
            ```
    5) test04FindAll
        + 기본 메소드 findAll()를 사용하여 전체 노래를 가져온다.
        + Lazy 로딩을 확인하는 테스트이다.
    6) test05FindAll2
        + test04FindAll의 지연로딩을 fetch join을 사용하여 한 번에 모두 가져오는 메소드 findAll2()에 대한 테스트 이다.
        + QueryDSL로 구현하였다.
        + 주의할 것은 조인테이블에 genre가 없으면, join시 select되는 row가 없기 때문에 outer join을 사용해야 한다.
            ```
                return queryFactory
                        .selectDistinct(genre)
                        .from(genre)
                        .leftJoin(genre.songs, song)
                        .fetchJoin()
                        .fetch();  
            ```    
    7) test06RemoveSong
        + ManyToMany 에서 삭제시, 조인테이블의 문제점을 해결을 확인하는 테스트이다.
        + 쿼리로그
            ```
             Hibernate: 
                 /* delete collection row me.kickscar.practices.jpa03.model10.domain.Song.genres */ delete 
                     from
                         song_genre 
                     where
                         song_no=? 
                         and genre_no=?         
            ```
            1) song_no와 genre_no를 사용하여 조인테이블의 하나의 row만 삭제한다.
            2) 이는 Genre 엔티티 클래스에도 연관 매핑 필드에 컬렉션 타입을 List대신 Set을 사용하였기 때문이다.
                ```
                    @ManyToMany(mappedBy = "genres")
                    private Set<Song> songs = new HashSet<>();
                   
                ```  