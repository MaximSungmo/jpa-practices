## Model04 : 일대다(OneToMany) - 단방향


### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/34001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/34002.png" width="500px" />
<br>
   
1. __게시판 게시물(Board)의 댓글(Comment)__
    1) 예를 들어, 게시판의 게시물 보기 페이지에서 게시물 내용 아래에 달린 댓글들과 게시물과의 관계이다. 게시물 쪽에서 댓글들이 탐색이 가능해야 한다.  
       따라서, Board -> Comment Navigational Association 관계가 있다.  
    2) 고려해야 할 반대 방향의 비즈니스 로직은 없다. 댓글에서 게시글을 참조해야 할 경우는 없다.
        Board -> Comment 관계매핑은 단방향(Unidirectional)이다.  

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) Board(1) -> Comment(N)
    2) OneToMany 이다.
  
3. OneToMany
    1) toMany 참조 필드의 타입으로 Map, Set, List, Collection 등을 사용하게 된다.
    2) JPA 2.0 부터 지원하는 관계 매핑이다.

#### 1-2. Entity Class: Board, Comment
1. __Board 엔티티 매핑 참고__
2. __Comment 엔티티 매핑 참고__
3. __연관관계 매핑__
    1) OneToMany(Board 엔티티)
        ```
            .
            .
            @OneToMany(fetch = FetchType.LAZY)
            @JoinColumn(name = "board_no")
            private List<Comment> comments;
            .
            .
        ```
    2) OneToMany 단방향의 특이점  
        - 외래키 관리를 Many쪽(Comment)에서 해야하는데, 단방향이기 때문에 Comment에 Board를 참조하는 매핑 필드가 없는 것이 문제다. 
        - 관계 주인 필드인 Board.comments가 외래키 관리를 해야하고 @JoinColumn name에 Comment 엔티티의 FK(board_no)를 지정해야 한다.  
        - 하지만, 데이터베이스의 스키마 생성 DDL를 보면 당연하지만, Many쪽인 Comment 테이블에 Board에 대한 외래키(FK)가 있다. 
            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/34004.png" width="500px" />
            <br>
        - @JoinColumn를 하지 않으면 JPA의 Join Table 전략이 적용되어 연결 테이블을 중간에 두고 연관관계를 관리하게 된다.    
        - OneToMany 에서는 Default Fetch Mode는 LAZY 이다.  
    3) 객체 관계 설정에 주의 할점
        - 단점을 분명하게 인지할 것: 외래키 관리를 다른 테이블에서 한다. (Many 쪽에서 하지 않는다.) 이는 다른 테이블에 FK가 있으면 insert 작업 시, 추가적으로 update를 해야 한다.
        - OneToMany Unidirectional(단방향)을 써야 하다면 ManyToOne Bidirectional(양방향)를 것을 보통 추천한다.  


### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. OneToMany Unidirectional(단방향) 단점 이해
2. Global Fetch 전략 LAZY에 대한 이해
3. OneToMany Unidirectional(단방향) 관계 매핑, 객체 탐색, 컬렉션 조인
4. 기존 Board, User 엔티티에 Comment 엔티티가 하나 더 추가된 모델로 레포지토리를 작성하였다. 다소 복잡해졌기 때문에 성능 및 비지니스 요구에 대한 레포지토리 최적화 방법등을 이해

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

#### 2-3. JpaBoardRepository Test : Spring Data JPA 기반 Repository
1. __JpaBoardRepository.java__
    1) 기본메소드와 쿼리메소드 

2. __JpaBoardQryDslRepository.java__
    1) 추가/성능 개선이 필요한 쿼리메소드 정의

3. __JpaBoardQryDslRepositoryImp.java__
    1) 추가/성능 개선이 필요한 쿼리메소드 구현

4. __JpaBoardRepositoryTest.java__
    1) test01Save
        - 코멘트 저장하는 방법 이해(How to Persist Many Side's Entity for OneToMany Unidirectional Model)
            ```
                User user = new User();
                user.setName("둘리");
                user.setPassword("1234");
                user.setEmail("dooly@kickscar.me");
                user.setGender(GenderType.MALE);
                user.setRole(RoleType.USER);
                userRepository.save(user1);
       
                Board board = new Board();
                board.setTitle("제목");
                board.setContents("내용");
                board.setUser(user);
                boardRepository.save(board);
       
                Comment comment = new Comment();
                comment.setUser(user);
                comment.setContents("댓글");
       
                commentRepository.save(comment);
                board.getComments().add(comment);
            ```
            1) Many쪽 Comment 엔티티가 외래키 관리를 하지 않기 때문에 Insert(Save)후, FK Update를 해야하며 반대편 Board 엔티티를 통해 한다.  
      
        - 실제 코드는 기본 메소드 save(entity)를 오버로딩한 JpaCommentQryDslRepositoryImp.save(boardNo, comments) 이다.  
          
            ```
                commentRepository.save(1L, new Comment(user1, "댓글1"));
                commentRepository.save(2L, new Comment("댓글2"), new Comment(user2, "댓글3"));
            ```
            1) save(boardNo, comments) 메소드를 보면, Comment 엔티티에 board의 no를 세팅할 필드가 없고 당연히 setter 자체가 없기 때문에 두 개로 나눠 함께 전달한다.  
            2) 테스트를 위해서 comments 파라미터는 Variable Arguments(Varargs)를 사용하여 여러 Comment 엔티티 객체를 전달할 수 있도록 하였다.
            3) 코멘트를 작성한 User 영속객체를 전달해야 한다.  
            4) 실행된 Update 쿼리 로그    
         
                ```
                    Hibernate: 
                    /* create one-to-many row me.kickscar.practices.jpa03.model04.domain.Board.comments */ update
                        comment 
                    set
                        board_no=? 
                    where
                        no=?         
                ```

        - OneToMain Unidirectional의 단점은 다음 ManyToOne Bidirectional 으로 바꿨을 때 예상되는 코드와 비교해 보면 명확히 알 수 있다.

            ```
                Comment comment = new Comment();
                comment.setBoard(board);
                comment.setUser(user);
                comment.setContents("댓글");
       
                commentRepository.save(comment);
            ```
    2) test02SaveEagerProblem
        - Comment 엔티티 저장에서 ManyToOne 기본 페치전략 EAGER의 문제점 테스트
        - 글로벌 Fecth 전략 - LAZY(지연로딩)
            1) JPA에서 기본 페치 전략
                + EAGER : ManyToOne, OnetoOne
                + LAZY : OneToMany
            2) 보통 실무에서는 ManyToOne, OneToOne 에서는 글로벌 페치 전략 Lazy로 변경한다.
            3) 하지만, 기본을 EAGER로 한 이유가 있을 것이다. 이를 이해하고 비즈니스와 상황에 따라 EAGER도 적용할 수 있어야 한다.
        - 테스트 코드
            ```
                Board board = em.find(Board.class, boardNo);    
            ```
            
            데이터베이스로 부터 Board 엔티티 객체를 Fetch하여 영속화 하기위해서 실행된 쿼리는,   
            
            ```
                Hibernate: 
                    select
                        board0_.no as no1_0_0_,
                        board0_.contents as contents2_0_0_,
                        board0_.hit as hit3_0_0_,
                        board0_.reg_date as reg_date4_0_0_,
                        board0_.title as title5_0_0_,
                        board0_.user_no as user_no6_0_0_,
                        user1_.no as no1_2_1_,
                        user1_.email as email2_2_1_,
                        user1_.gender as gender3_2_1_,
                        user1_.name as name4_2_1_,
                        user1_.password as password5_2_1_,
                        user1_.role as role6_2_1_ 
                    from
                        board board0_ 
                    left outer join
                        user user1_ 
                            on board0_.user_no=user1_.no 
                    where
                        board0_.no=?            
            ```
            1) Board -> User(ManyToOne Unidirectional) 기본 Fetch 모드가 EAGER이기 때문에 User Entity와 Outer Join이 자동으로 실행된 것을 알 수 있다.
            2) 이는 comment 테이블의 와래키(FK) board_no 세팅을 위한 쿼리로는 조금은 부담스럽다.  
            
        - LAZY로 바꾸고 실행된 쿼리 로그를 보자.
            1) Entity 매핑 수정
             
                ```
                    @ManyToOne(fetch = FetchType.LAZY)
                    @JoinColumn( name = "user_no" )
                    private User user;
                ```
            
            2) 테스트 실행 후, 쿼리로그 확인     
             
                ```
                Hibernate: 
                    select
                        board0_.no as no1_0_0_,
                        board0_.contents as contents2_0_0_,
                        board0_.hit as hit3_0_0_,
                        board0_.reg_date as reg_date4_0_0_,
                        board0_.title as title5_0_0_,
                        board0_.user_no as user_no6_0_0_ 
                    from
                        board board0_ 
                    where
                        board0_.no=?
                ```
            
            3) Join이 실행되지 않았으며 Comment 엔티티 객체 저장에 아무런 문제가 없다.
            4) 글로벌 페치 전략 LAZY 적용은 이 상황에서는 성능 향상을 기대할 수 있다. 
    3) test03BoardListLazyProblem
        - 쿼리메소드 JpaBoardRepository.findAllByOrderByRegDateDesc()를 사용해서 게시물 리스트를 가져오는 테스트이다. 
        - 테스트 코드는 LAZ Y로딩 시, 발생하는 Lazy의 N+1 문제를 확인해 본다.
        - 쿼리로그를 보면, 게시판 리스트를 가져오기 위한 쿼리(1) 그리고 게시글이 있는 User 이름을 가져오기 위한 쿼리(2 == N)이 실행됨을 알 수 있다. 즉, LAZY로딩의 N+1 문제가 발생한다.  
            1) EAGER -> LAZY로 페치 전략 수정 후, 코멘트 저장 시 발생한 성능 이슈 1개가 해결되었다고 성능이 개선되었을 것이라 기대하면 안된다. 
            2) 페치전략 수정으로 게시판 리스트, 글보기 등의 다른 비즈니스에서 N+1 문제로 또 다른 성능 문제가 발생할 수 있기 때문이다.
    4) test04BoardListLazyProblemSolved()
        - N+1 문제를 해결하기 위한 성능 및 비지니스 요구 따른 레포지토리 최적화를 다루는 테스트이다.
        - JpaBoardQryDslRepositoryImpl.findAll3(pageable) 메소드를 테스트한다.
        - 일단은 JPQL(QueryDSL)로 해결할 수 있다. 
            1) QueryDSL 경우에는 innerJoin(), fetchJoin()를 사용해 개선할 수 있다(jpa03-model02 참고)
            2) 게시판 리스트에서는 User 엔티티의 전체 필드 보다는 이름과 번호 정도의 필드만 필요 할 것이다. 따라서 Projection과 DTO를 함께 고려하면 성능(속도) 현저하게 빨라 질 것이다.
            3) 문제는 Projection을 하면 join fetch를 할 수 없다는 점이다.
            4) 이유는 join fetch은 기본적으로 select에 1개 이상의 Entity가 올 수 없다.(join fetch 뒤의 Entity에 별칭을 줄 수 없는 이유이기도 하다)
            5) 하지만, QueryDSL에서는 innerJoin() 만으로도 join fetch으로 생성된 쿼리와 같은 쿼리 실행이 가능하다.(jpa03-model02 참고)
        - 테스트 실행과 쿼리 로그 
            
            ```
                select
                    board0_.no as col_0_0_,
                    board0_.hit as col_1_0_,
                    board0_.title as col_2_0_,
                    board0_.contents as col_3_0_,
                    board0_.reg_date as col_4_0_,
                    user1_.name as col_5_0_ 
                from
                    board board0_ 
                inner join
                    user user1_ 
                        on board0_.user_no=user1_.no 
                order by
                    board0_.reg_date desc limit ?                  
            ```
          
    5) test05BoardViewLazyProblem(01~02)
        - 이 테스트도 성능 및 비지니스 요구 따른 레포지토리 최적화를 다루는 테스트이다.
        - 이 테스트에서는 가능한 쿼리 1회로 게시글 보기 페이지에 필요한 모든 데이터를 가져오는 데 집중하는 것이지만, 성능과 최적화를 다룰 때는 유의할 점이 있다.
            1) 복잡한 조인 쿼리가 여러번 나눠서 가져오는 select(Lazy)보다 성능 향상을 늘 보장하지 않는다는 것이다.
            2) 너무 View에 맞춘 Repository 메소드 작성은 Repository 계층과 Presentation(View) 계층의 결합도가 높아지는 단점이 있다.
            3) Service 계층에서 적당한 Lazy를 사용해서 Presentation(View)에 맞는 DTO를 제공하는 것이 더 성능면서 이점을 가질 수 있다.
        - JPA와 QuryDSL 테크닉 연습을 위해 쿼리 1회로 모두 가져올 수 있도록 시도해 보고 위에서 언급한 1) 2) 3)은 개발시 고려해야 할 사항으로 남겨둔다.
            1) test05BoardViewLazyProblem01 - 기본메소드 findByID(no) 사용
                + 게시글 가져오기
                    ```
                        Board board = boardRepository.findById(1L).get();
                        assertEquals("제목1", board.getTitle());
               
                    ```            
                    실행된 쿼리  
                    ```
                        Hibernate: 
                            select
                                board0_.no as no1_0_0_,
                                board0_.contents as contents2_0_0_,
                                board0_.hit as hit3_0_0_,
                                board0_.reg_date as reg_date4_0_0_,
                                board0_.title as title5_0_0_,
                                board0_.user_no as user_no6_0_0_ 
                            from
                                board board0_ 
                            where
                                board0_.no=?
                    ```
                
                + 사용자 가져오기
                    ```
                        User user = board.getUser();
                        assertEquals("둘리", user.getName());
                  
                    ```
                    실행된 쿼리  
                    ```
                        Hibernate: 
                            select
                                user0_.no as no1_2_0_,
                                user0_.email as email2_2_0_,
                                user0_.gender as gender3_2_0_,
                                user0_.name as name4_2_0_,
                                user0_.password as password5_2_0_,
                                user0_.role as role6_2_0_ 
                            from
                                user user0_ 
                            where
                                user0_.no=?
                  
                    ```
                + 코멘트 가져오기
                    ```
                        List<Comment> comments = board.getComments();
                        assertEquals(2L, comments.size());
                        
                    ```
                    실행된 쿼리
                    ```
                        Hibernate: 
                            select
                                comments0_.board_no as board_no5_1_0_,
                                comments0_.no as no1_1_0_,
                                comments0_.no as no1_1_1_,
                                comments0_.contents as contents2_1_1_,
                                comments0_.reg_date as reg_date3_1_1_,
                                comments0_.user_no as user_no4_1_1_,
                                user1_.no as no1_2_2_,
                                user1_.email as email2_2_2_,
                                user1_.gender as gender3_2_2_,
                                user1_.name as name4_2_2_,
                                user1_.password as password5_2_2_,
                                user1_.role as role6_2_2_ 
                            from
                                comment comments0_ 
                            left outer join
                                user user1_ 
                                    on comments0_.user_no=user1_.no 
                            where
                                comments0_.board_no=?                          
                    ```
                    1) ManyToOne Fecth.EAGER 기본 상태의 테스트이기 때문에 Comment와 User entity가 자동으로 join 되었다.      
                    2) Service 계층은 @Transactional을 적용할 수 있는 계층으로 지금 테스트 케이스 실행타임과 같다.
                    3) 따라서 Service 계층,에서 LAZY를 사용해서 데이터를 모아 DTO에 담아 Controller, View로 전달하는 것이 보통이다.
                    4) 성능이 더 나아질 것이라 보장은 못하지만, join을 사용해서 쿼리 실행 회수를 줄여보자. 
                    5) JPA 개발에서 쿼리로그, DBMS 로그 또는 메모리 모니터링을 통해 지속적인 튜닝작업을 해야하는 것도 잊지 말자.
                    6) 테스트 작성도 잊지 말아야 한다. 성능 개선을 위해 추가하거나 리팩토링 된 코드가 이미 작업을 마친 다른 코드에 영향을 줄 수 있기 때문에 이전 코드 테스트도 신경써야 한다. 
        
            2) test06BoardViewLazyProblem02 - QueryDSL 통합 findById3(no) 사용
                + 메소드 코드
                    ```
                        return queryFactory
                                .selectDistinct(board)
                                .from(board)
                                .innerJoin(board.user)
                                .fetchJoin()
                                .innerJoin(board.comments)
                                .fetchJoin()
                                .where(board.no.eq(no))
                                .fetchOne();    
                    ```
                    1) LAZY로딩이 발생하는 부분을 fetch join으로 해결하고 있는 모습이다.
                    2) Model03의 Board -> Comment OneToMany(기본 페치 전략 Lazy)의 Collection Join의 문제를 해결하기 위해 selectDistinct()도 사용하고 있다.
                    3) 추가된 부분은 Board -> User ManyToOne 클로벌 페치 전략 Lazy 로딩 부분을 Fetch Join으로 수정한 부분이다.
                    
                + 실행된 쿼리
                    ```
                        Hibernate: 
                                select
                                    distinct board0_.no as no1_0_0_,
                                    user1_.no as no1_2_1_,
                                    comments2_.no as no1_1_2_,
                                    board0_.contents as contents2_0_0_,
                                    board0_.hit as hit3_0_0_,
                                    board0_.reg_date as reg_date4_0_0_,
                                    board0_.title as title5_0_0_,
                                    board0_.user_no as user_no6_0_0_,
                                    user1_.email as email2_2_1_,
                                    user1_.gender as gender3_2_1_,
                                    user1_.name as name4_2_1_,
                                    user1_.password as password5_2_1_,
                                    user1_.role as role6_2_1_,
                                    comments2_.contents as contents2_1_2_,
                                    comments2_.reg_date as reg_date3_1_2_,
                                    comments2_.user_no as user_no4_1_2_,
                                    comments2_.board_no as board_no5_1_0__,
                                    comments2_.no as no1_1_0__ 
                                from
                                    board board0_ 
                                inner join
                                    user user1_ 
                                        on board0_.user_no=user1_.no 
                                inner join
                                    comment comments2_ 
                                        on board0_.no=comments2_.board_no 
                                where
                                    board0_.no=?
                        Hibernate: 
                            select
                                user0_.no as no1_2_0_,
                                user0_.email as email2_2_0_,
                                user0_.gender as gender3_2_0_,
                                user0_.name as name4_2_0_,
                                user0_.password as password5_2_0_,
                                user0_.role as role6_2_0_ 
                            from
                                user user0_ 
                            where
                                user0_.no=?
                        Hibernate: 
                            select
                                user0_.no as no1_2_0_,
                                user0_.email as email2_2_0_,
                                user0_.gender as gender3_2_0_,
                                user0_.name as name4_2_0_,
                                user0_.password as password5_2_0_,
                                user0_.role as role6_2_0_ 
                            from
                                user user0_ 
                            where
                                user0_.no=?
                    ```
                    1) Board, User(게시글 작성자), Comment 엔티티와 selectDistinct() 가 사용되어 게시글 번호에 맞는 Board 엔티티 객체가 Comment Collection(List) 까지 잘 Fecth 되었다.
                    2) 문제는 List에 있는 Comment 엔티티 객체의 User를 EAGER로 가져올 때 발생한다. Comment 수(N) 만큼 쿼리가 실행되는 EAGER N+1 문제가 발생한다.(댓글이 1000개 이상 달린 인기 있는 글이라 생각해보면...)
                    3) Comment 2개에 대한 User 엔티티를 가져오기 위해 select 쿼리 2번이 실행된 것을 확인 할 수 있다.
                      
    6) test07BoardViewLazySolved01
        - test05BoardViewLazyProblem(01~02)의 문제는 SQL에서는 Board, User(글작성자), Comment, User(코멘트작성자)를 Join하고 Board가 중복되기 때문에 Distinct를 사용하면 된다.
        - 복잡성을 피하기 위해 서브쿼리를 생각해 볼 수 있지만 서브쿼리 자체는 성능에 좋지 않다.
        - QueryDSL 서브쿼리는 select, where, having에만 허용하고 from 절에 놓을 수가 없는 점도 유의해야 한다.
        - Native SQL을 사용해도 된다. Native SQL은 비즈니스에 특화된 성능을 고려하지 않아도 되는 쿼리 작성에 맞다. 여기에 사용할 정도로 복잡하거나 특별나지도 않다.
        - 하지만, 복잡성을 피하기 위해 일단, BoardDto와 List<CommentDto> 를 조인 페치하는 2개의 메소드로 나누어 테스트하고 최종적으로 합친다.    
        - DTO 정의
            1) BoardDto
               ```
                public class BoardDto {
                        .
                        .
                    private Long no;
                    private Integer hit;
                    private String title;
                    private String contents;
                    private Date regDate;
                    private String userName;
                    private List<CommentDto> comments;
                        .
                        .
                }
                ```
            2) CommentDto
                ```
                    public class CommentdDto {
                             .
                             .
                        private Long no;
                        private String contents;
                        private Date regDate;
                        private String userName;
                             .
                             .
                    }
                ```
               
        - JpaBoardQryDslRepositoryImpl.findById3(no) 테스트
            1) 메소드 코드
                ```
                    return queryFactory
                        .select(Projections.fields(BoardDto.class, board.no, board.hit, board.title, board.contents, board.regDate, board.user.name.as("userName")))
                        .from(board)
                        .innerJoin(board.user)
                        .where(board.no.eq(no))
                        .fetchOne();
                
                ```
                    + Board, User 엔티티를 innerJoin과 Projection을 사용한다.
                    + 다시 얘기하지만, fetchJoin()은 Projection()과 함께 사용할 수 없다.

            2) 실행 쿼리
                ```
                    select
                        board0_.no as col_0_0_,
                        board0_.hit as col_1_0_,
                        board0_.title as col_2_0_,
                        board0_.contents as col_3_0_,
                        board0_.reg_date as col_4_0_,
                        user1_.name as col_5_0_ 
                    from
                        board board0_ 
                    inner join
                        user user1_ 
                            on board0_.user_no=user1_.no 
                    where
                        board0_.no=?
                       
                ```
            
        - JpaBoardQryDslRepositoryImpl.findCommentsByNo(no) 테스트
            1) 메소드 코드
                ```
                    return queryFactory
                        .select(Projections.bean(CommentDto.class, comment.no, comment.contents, comment.user.name.as("userName")))
                        .from(board)
                        .innerJoin(board.comments, comment)
                        .innerJoin(comment.user)
                        .where(board.no.eq(no))
                        .fetch();
                ```
                    + Comment Collection을 가져오지만 OneToMany Unidirectional이기 때문에 board no값을 파라미터를 이용할 때는 Comment 엔티티를 사용할 수 없다.
                    + innerJoin(board.comments, comment)에서 comment는 별칭이라 보면 된다.(별칭으로 사용할 쿼리 타입)
                    + JPQL로 작성했을 때 코드를 보면 훨씬 이해가 쉽다.(CommentDto 프로젝션 생략)
                        ```
                            String qlString = "select b.comments from Board b join fetch b.comments c join fetch c.user where b.no=:no"
                            
                        ```
                    + Projections.bean, Projections.fields, Projections.constructor 중에 bean을 생성하는 방법(생성자, setter 모두 지원)으로 프로젝션한다.
                    + comment.user.name.as("userName") 에서 commentDto의 필드에 맞게 Alias하는 것도 잊지말자.
            
            2) 실행 쿼리
            ```
                select
                    comments1_.no as col_0_0_,
                    comments1_.contents as col_1_0_,
                    user2_.name as col_2_0_ 
                from
                    board board0_ 
                inner join
                    comment comments1_ 
                        on board0_.no=comments1_.board_no 
                inner join
                    user user2_ 
                        on comments1_.user_no=user2_.no 
                where
                    board0_.no=?
            ```
        - BoardDto 완성하기
            ```
                board.setComments(comments);

            ```
            출력결과           
            ```
                BoardDto{no=3, hit=0, title='제목3', contents='내용3', regDate=2019-10-19 11:59:59.59, userName='마이콜', comments=[CommentDto{no=4, contents='댓글4', regDate=null, userName='둘리'}, CommentDto{no=5, contents='댓글5', regDate=null, userName='마이콜'}, CommentDto{no=6, contents='댓글6', regDate=null, userName='또치'}]}

            ```
        - 글로벌 페치모드가 LAZY이지만 @Transactional을 사용하지 않고 있다는 점도 주목하자.
                          
    7) test08BoardViewLazySolved02
        - JpaBoardQryDslRepositoryImpl.findById3(no), JpaBoardQryDslRepositoryImpl.findCommentsByNo(no) 두 메소드의 QueryDSL를 합치는 것은 사실상 불가능하다.
            1) Projection 2개를 합치는 것은 사실상 from절에 두 메소드 결과중 하나가 있어야 하는 서브쿼리인데, QueryDSL은 from절 서브쿼리를 지원하지 않는다.
            2) Distinct는 OneToMany findById3()에 한 번만 적용되는데, findCommentsByNo(no) 결과도 Board 엔티티로 Distinct를 해야 하는데 불가능하다.
        - 하지만, JpaBoardQryDslRepositoryImpl.findById(no)를 보면, Projection을 통한 DTO 객체대신 매핑 엔티티를 DB로부터 영속화 시키면 가능하다.(JPA가 해준다) 
            1) 구현코드
                ```
                    return queryFactory
                            .selectDistinct(board)
                            .from(board)
                            .innerJoin(board.user)
                            .fetchJoin()
                            .leftJoin(board.comments, comment)
                            .fetchJoin()
                            .leftJoin(comment.user)
                            .fetchJoin()
                            .where(board.no.eq(no))
                            .fetchOne();               
                ```
                + 주의 할 것은 comment가 없는 게시글도 나와야 하기 때문에 innerJoin() 대신 leftJoin() Outer Join을 사용했다.
                + Projection이 아니기 때문에 join()과 fetchJoin()를 함께 사용해야 한다.
            2) 실행쿼리
                ```
                    select
                                distinct board0_.no as no1_0_0_,
                                user1_.no as no1_2_1_,
                                comments2_.no as no1_1_2_,
                                user3_.no as no1_2_3_,
                                board0_.contents as contents2_0_0_,
                                board0_.hit as hit3_0_0_,
                                board0_.reg_date as reg_date4_0_0_,
                                board0_.title as title5_0_0_,
                                board0_.user_no as user_no6_0_0_,
                                user1_.email as email2_2_1_,
                                user1_.gender as gender3_2_1_,
                                user1_.name as name4_2_1_,
                                user1_.password as password5_2_1_,
                                user1_.role as role6_2_1_,
                                comments2_.contents as contents2_1_2_,
                                comments2_.reg_date as reg_date3_1_2_,
                                comments2_.user_no as user_no4_1_2_,
                                comments2_.board_no as board_no5_1_0__,
                                comments2_.no as no1_1_0__,
                                user3_.email as email2_2_3_,
                                user3_.gender as gender3_2_3_,
                                user3_.name as name4_2_3_,
                                user3_.password as password5_2_3_,
                                user3_.role as role6_2_3_ 
                            from
                                board board0_ 
                            inner join
                                user user1_ 
                                    on board0_.user_no=user1_.no 
                            left outer join
                                comment comments2_ 
                                    on board0_.no=comments2_.board_no 
                            left outer join
                                user user3_ 
                                    on comments2_.user_no=user3_.no 
                            where
                                board0_.no=?                
                ``` 
                + 모든 필드를 가져오는 것은 불만이지만 쿼리 1회로 모든 엔티티를 로딩할 수 있다.
            3) 테스트 통과 조건 
                + 4L번 글은 코멘트가 없어도 페치가 되어야 하기 때문에 null이 아니여야 한다.
                + 1L번 글에 대해서 작성자및 코멘트등의 데이터 검증을 한다.
    8) 결론
        - 다시 말하지만, LAZY로 select를 여러번 하는 것보다 join으로 단순히 쿼리 횟수를 줄인 것이 성능과 속도면에서 반드시 좋은 것은 아니다.
        - 잘못된 EAGER 페치 전략으로 발생할 수 있는 N+1 이 더 큰 문제 일 수 있다.
        - 그렇다고 LAZY가 만사일 순 없다. 게시판 리스트 같은 경우에는 LAZY가 더 문제일 수 있다. 이럴 때는 Join을 사용한다.
        - 이럴 땐 이렇게 저럴 땐 저렇게... 케이스마다 상황별 대처가 다르고 어떤 규칙이 있는 것도 딱히 아니다.
        - JPA는 사전에 충분한 경험이 있는 팀이 수행해서 사전에 성능 이슈를 발견하고 최적화 작업과 지속적인 모니터링 프로파일링 등이 중요하다.
        - 이런 이슈에 제대로 대처하기 위해서는 다음과 같은 지식과 경험이 있어야 하는 것 같다.
          1) 다양한 모델에 대한 기본적인 엔티티, 연관관계 매핑 지식은 기본으로 갖춰야 한다.
          2) 다양한 객체지향쿼리 작성법을 숙지해야 한다. 특히, JPQL 기본지식은 반드시 있어야 하며 QueryDSL를 잘 다루어야 한다. 이것도 기본적으로 갖춰야 한다.
          3) 영속성컨텍스트와 트랜잭션 개념 이해도 물론 기본이다.
          4) SQL 작성과 RDBMS 스키마 모델링 경험도 당연히 있어야 한다. 이것도 기본이다.
          5) 객체지향에대한 이해와 객체지향개발 그리고 디자인(설계) 경험이 필요한데 이것은 시간이 쫌 필요할 것이다.
  
#### 2-4. JpaCommentRepository Test : Spring Data JPA 기반 Repository
1. __다음과 같은 인터페이스와 클래스로 구현되어 있다__
    1) JpaCommentRepository.java
    2) JpaCommentQryDslRepository.java
    3) JpaCommentQryDslRepositoryImp.java
    4) JpaCommentRepositoryTest.java
2. __OneToMany Unidirectional(단방향)의 Many쪽 엔티티에는 별다른 메소드를 두지 않는다.__
    1) 연관 필드가 없기 때문에 toOne쪽 엔티티 객체 탐색은 불가능하다.
    2) 저장, 삭제, 수정 정도의 메소드 구현이 가능하다.
