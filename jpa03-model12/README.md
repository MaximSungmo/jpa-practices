## Model12 : 혼합모델(ManyToMany 문제해결, 연결엔티티, 복합키(PK), 식별관계, @IdClass)


### 1. Domain

#### 1-1. 다대다 매핑의 문제 해결, 조인테이블 대신 연결 엔티티 사용

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/31201.png" width="800px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/31202.png" width="800px" />
<br>
        
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 온라인북몰에서 회원과 책과의 관계이다.
    2) 회원은 온라인북몰에서 관심있는 책을 저장(장바구니)할 수 있는 데 이는 User와 Book 사이에 ManyToMany 로 생각할 수 있다. 
    3) 앞의 Model9/10의 관계매핑을 할 경우 문제점이 몇가지 있다. 외래키 두 개 이외에도 amount(수량)과 같은 새로운 칼럼이 더 필요한 경우가 있다.
    4) 앞의 Model9/10 ManyToMany에서는, 조인테이블을 사용해서 편하게 매핑이 가능한 장점이 있었지만 몇가지 문제점을 가지고 있었기 때문에 QueryDSL과 통합해서 JPQL로 직접 메소드를 구현 했었다.
    5) 보통, 실무에서는 ManyToMany 조인테이블을 사용하지 않는다. 연결엔티티를 정의하여 세 개의 엔티티로 매핑하는 복합모델을 사용하게 된다.
    6) 이 경우에는 User <-> CartItem (양방향) 과 CartItem -> Book (단방향)의 복합모델로 ManyToMany 관계를 해결하게 된다.

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) User는 다수의 책을 저장할 수 있기 때문에 User(1) <-> CartItem(\*) 관계다.
    2) OneToMany 이지만 ManyToOne을 많이 선호하기 때문에 CartItem <-> User ManyToOne 양방향을 결정한다.
    3) Book 엔티티와의 관계에서도 다수의 User가 Book 한 권을 장바구니에 담을 수 있기 때문에 CartItem(\*) -> Book(1) 관계다. 
    4) Book에서 User쪽을 탐색하는 드문 경우이기 때문에 CartItem -> Book ManyToOne 단방향으로 결정한다. 
       
3. __복합키 식별관계__
    1) CartItem은 User, Book 엔티티와 각각의 관계에 두 개의 연관필드를 가지게 된다. 
    2) 그리고 이 관계 모두에서 N 다중성을 가지고 있기 때문에 외래키 두 개를 가지게 된다.
    3) 새로운 Id(PK)를 만들 수 있지만, Model11/12에서는 이 외래키 2개을 하나로 묶어 PK로 사용하는 복합키 식별관계로 매핑한다.
    4) JPA에서는 복합키를 @EmebeddedID, @IdClass 이렇게 두 가지 방식을 할 수 있다.
    5) Model12에서는 @IdClass 방식으로 매핑하고 테스트 한다.  
    
#### 1-2. Entity Class: User, Book, CartItem
1. __User 엔티티 매핑 참고__
2. __Book 엔티티 매핑 참고__
3. __CartItem 엔티티 매핑 참고__
4. __연관관계 매핑__
    1) 복합키를 지원하기 위해 JPA에서는 복합키를 Id(PK)로 잡기 때문에 복합키가 되는 1개 이상의 필드를 가지고 있는 식별자(Id)클래스를 따로 작성해주어야 한다.
    2) CartItemId 식별자 클래스
        ```
            public class CartItemId implements Serializable {
                private Long user;
                private Long book;
                .
                .
                getter/setter
                .
                .
                @Override
                public boolean equals(Object o) {
                    .
                    .
                }
            
                @Override
                public int hashCode() {
                    .
                    .
                }                
       
        ```
        + @EmbeddedId 어노테이션 적용방식과 차이점은 Id에 해당하는 Long타입의 필드 이름이다. userNo, bookNo보다는 user, book을 써야한다. 그 이유는 CartItem의 연관필드의 이름과 같아야 하기 때문이다.
        + 식별자 클래스는 인터페이스 Serializable를 구현해야 한다.
        + 기본 생성자로 생성이 가능해야 한다.
        + equals와 hashCode를 반드시 오버라이딩 해야 한다.
    
    3) User와 Book과 연관 매핑을 하는 CartIetm은 다음과 같다.
        ```
            @Entity
            @IdClass(CartItemId.class)
            @Table(name = "cartitem")
            public class CartItem {
                @Id
                @ManyToOne
                @JoinColumn(name="book_no")
                private Book book;
                
                @Id
                @ManyToOne
                @JoinColumn(name="user_no")
                private User user;
            
                @Column(name = "amount", nullable = false)
                private Integer amount;
                .
                .
                .
            }       
        ```
        + @IdClass(CartItemId.class)를 사용하여 Id(PK)의 복합키 클래스를 지정한다.
        + 복합(Composite)키 임을 설정하기 위해 관계 필드 book, user에 @Id 어노테이션을 붙혔다.
        + CartItem 엔티티는 User 엔티티, Book 엔티티와 ManyToOne 관계를 맺기 때문에 관계 주인 필드를 가진다.
        + book, user가 관계주인 필드이다. 따라서 @JoinColumn를 지정하였으며 @ManyToOne으로 다중성도 지정했다.
        + 중요한 것은 관계필드의 이름과 IdClass의 필드 이름이 같아야 하며 타입은 관계필드의 엔티티 객체의 @Id필드의 타입과 같아야 한다.
      
    4) OneToMany(User 엔티티)
        
        ```
             .
             .
            @Id
            @Column(name = "no")
            @GeneratedValue( strategy = GenerationType.IDENTITY  )
            private Long no;
             .
             .
            @OneToMany(mappedBy = "user", fetch = FetchType.LAZY )
            private List<CartItem> cart = new ArrayList<CartItem>();
             .
             .       
        ```
        + OneToMany ManyToOne 양방향 반대편(OntToMany)에서는 관계주인이 아닌 엔티티는 mappedBy를 통해 관계의 주인이 아님을 선언한다.
        + 여기서는 CartItem.user 필드가 관계의 주인이다.
        + toMany 참조를 위해 컬렉션 매핑을 했다.
        
    5) Book 엔티티는 ManyToOne 단방향 반대편 엔티티기 때문에 연관관계 매핑은 별도로 없다.
    
    6) 생성 스키마
    
        ```
            Hibernate: 
                
                create table book (
                    no bigint not null auto_increment,
                    price integer not null,
                    title varchar(100) not null,
                    primary key (no)
                ) engine=InnoDB
            Hibernate: 
                
                create table cartitem (
                    book_no bigint not null,
                    user_no bigint not null,
                    amount integer not null,
                    primary key (book_no, user_no)
                ) engine=InnoDB
            Hibernate: 
                
                create table user (
                    no bigint not null auto_increment,
                    email varchar(200) not null,
                    name varchar(20) not null,
                    password varchar(128) not null,
                    primary key (no)
                ) engine=InnoDB
            Hibernate: 
                
                alter table cartitem 
                   add constraint FK8wjiwpl7f4veth9llay1tfiov 
                   foreign key (book_no) 
                   references book (no)
            Hibernate: 
                
                alter table cartitem 
                   add constraint FK2bocrqf9dlvblordkxan5ay1l 
                   foreign key (user_no) 
                   references user (no)     
       
        ```
        + ManyToMany 조인테이블과 큰 차이는 없다. 세 개의 엔티티에 대한 세 개의 테이블을 생성한다.
        + 연결 엔티티에 매핑된 cartitem 테이블을 보면 user, book 테이블을 참조하는 두개의 FK가 복합키로 PK로 세팅된 것을 볼 수 있다.
        + 그리고 추가 컬럼 amount가 있는 것을 볼 수 있다.
    
    7) @IdClass 와 @Embedded 차이점
        + @IdClass 와 @EmbeddedId 는 각각 장단점이 있으므로 본인의 취향에 맞는 것을 사용하면 된다.
        + @EmbeddedId 가 @IdClass 와 비교해서 더 객체 지향적이고 중복도 없어서 좋아보인다.
        + 하지만, JPQL 이 @EmbeddedId 방식이 조금 더 길어진다.
        
### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. Model11과 복합키 매핑에 차이가 있고 레포지토리 작성및 이슈는 같다.
2. Model11과 Repository 작성 및 테스트는 동일

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

#### 2-3. JpaCartItemRepository Test : Spring Data JPA 기반 Repository
1. __JpaCartItemRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) ManyToMany 조인테이블 대신 연결엔티티로 매핑하였기 때문에 편리한 두 엔티티(User, Book)간의 관계에 쿼리메소드를 사용할 수 있다.
    3) findAllByUserNo(userNo) 사용자 번호로 장바구니 리스트를 가져올 수 있다.
    4) deleteByUserNoAndBookNo(userNo, bookNo) 사용자 번호와 도서번호로 장바구니에서 삭제를 한다.
    5) 편리하긴 하지만, 테스트와 퉈리 로그를 보면 성능에 문제점이 있기 때문에 QueryDsl를 사용한 메소드를 만드는 것이 보통이다.

2. __JpaCartItemQryDslRepositry__
    1) 성능에 문제가 있는 기본메소드, 쿼리메소드를 대체할 메소드를 정의한다.
       + findAllByUserNo2(userNo)
       + findAllByUserNo3(userNo)
       + deleteByUserNoAndBookNo2(userNo, bookNo)

3. __JpaCartItemQryDslRepositryImpl__
    1) findAllByUserNo2, findAllByUserNo3, deleteByUserNoAndBookNo2 구현
    2) QueryDSL 통합 구현
    
4. __JpaCartItemRepositoryTest__
    1) test01Save
        + 테스트를 위한 User, Book, CartItem 데이터를 저장한다.
        + ManyToMant 조인테이블 대신 연결엔티티(CartItem)을 사용하기 때문에 CartItemRepository를 통해 쉽게 관계를 설정할 수 있다.
        
            ```
                .
                .
                CartItem cartItem = new CartItem();
                cartItem.setUser(user);
                cartItem.setBook(book);
                cartItem.setAmount(1);
                cartItemRepository.save(cartItem);
                .
                .   
            ```
    2) test02FindAllByUserNo
        + 특정 사용자의 번호로 장바구니(cart)안의 항목(CartItem) 리스트를 가져온다.
        + 2가지 방식으로 가져 올 수 있다.
            1) User 객체 그래프 탐색 지연로딩을 사용해 가져오기(UserRepositoryTest test02FindById 참고)
                - UserRepositoryTest.test02FindById
                    ```
                        User user = userRepository.findById(1L).get();
                        List<CartItem> cart = user.getCart();
                    ```
                - CartItem을 가져오기 위해 실행된 쿼리 로그
                    ```
                         Hibernate: 
                             select
                                 cart0_.user_no as user_no2_1_0_,
                                 cart0_.book_no as book_no1_1_0_,
                                 cart0_.book_no as book_no1_1_1_,
                                 cart0_.user_no as user_no2_1_1_,
                                 cart0_.amount as amount3_1_1_,
                                 book1_.no as no1_0_2_,
                                 book1_.price as price2_0_2_,
                                 book1_.title as title3_0_2_ 
                             from
                                 cartitem cart0_ 
                             inner join
                                 book book1_ 
                                     on cart0_.book_no=book1_.no 
                             where
                                 cart0_.user_no=?                 
                    ```
                    1) 조인 쿼리가 실행되어 CartItem 콜렉션을 별문제 없이 받아 올수 있다.
                    2) 문제는 CartItem 콜렉션(cart) 내용이다.
                    
                    ```
                        CartItem{user=User{no=1, name='둘리', email='dooly@gmail.com', password='1234'}, book=Book{no=1, title='책1', price=1000}, amount=1}
                        CartItem{user=User{no=1, name='둘리', email='dooly@gmail.com', password='1234'}, book=Book{no=2, title='책2', price=1000}, amount=2}
                       
                    ```
                    3) 크게 문제 될 것은 없지만 장바구니 내용을 구성하기에 필요없는 User 내용이 전부 있는 것이 불만이다.
                    
            2) CartItemRepository 쿼리메소드를 사용해서 User의 번호를 파라미터로 전달하여 가져오기(이 테스트에 해당된다.)
                - 쿼리메소드 findAllByUserNo(no)를 사용했다
                - 실행된 쿼리 로그
                    ```
                        Hibernate: 
                                select
                                    cartitem0_.book_no as book_no1_1_,
                                    cartitem0_.user_no as user_no2_1_,
                                    cartitem0_.amount as amount3_1_ 
                                from
                                    cartitem cartitem0_ 
                                left outer join
                                    user user1_ 
                                        on cartitem0_.user_no=user1_.no 
                                where
                                    user1_.no=?
                        Hibernate: 
                            select
                                book0_.no as no1_0_0_,
                                book0_.price as price2_0_0_,
                                book0_.title as title3_0_0_ 
                            from
                                book book0_ 
                            where
                                book0_.no=?
                        Hibernate: 
                            select
                                user0_.no as no1_2_0_,
                                user0_.email as email2_2_0_,
                                user0_.name as name3_2_0_,
                                user0_.password as password4_2_0_ 
                            from
                                user user0_ 
                            where
                                user0_.no=?
                        Hibernate: 
                            select
                                book0_.no as no1_0_0_,
                                book0_.price as price2_0_0_,
                                book0_.title as title3_0_0_ 
                            from
                                book book0_ 
                            where
                                book0_.no=?                  
                    ```
                    1) 최악이다. 일단 CartItem을 가져오기 위해 User와 조인을 한 쿼리가 실행된다.
                    2) CartItem 내용(엔티티 객체)을 채우기 위해 개별적 User, Book 쿼리가 실행됨을 알 수 있다.
                    
        + 결론은 사용자의 번호로 장바구니(cart)안의 항목(CartItem) 리스트를 가져오기 위해 QueryDSL을 사용하지 않고 기본메소드, 쿼리메소드만 사용할 생각이면 User 엔티티 객체를 통한 그래프 탐색이 좋다.

    3) test03FindAllByUserNo2
        + 사용자의 번호로 장바구니(cart)안의 항목(CartItem) 리스트를 가져오기 위해 QueryDSL로 작성한 JpaCartItemQryDslRepository.findAllByUserNo2(userNo) 테스트이다.
        + QueryDSL 코드
            ```
                return  queryFactory
                        .select(cartItem)
                        .from(cartItem)
                        .innerJoin(cartItem.user)
                        .fetchJoin()
                        .innerJoin(cartItem.book)
                        .fetchJoin()
                        .where(cartItem.user.no.eq(userNo))
                        .fetch();          
            ```
            innerJoin과 fetchJoin을 사용한다.  
            
        + 실행된 쿼리
            ```
                 Hibernate: 
                         select
                             cartitem0_.book_no as book_no1_1_0_,
                             cartitem0_.user_no as user_no2_1_0_,
                             user1_.no as no1_2_1_,
                             book2_.no as no1_0_2_,
                             cartitem0_.amount as amount3_1_0_,
                             user1_.email as email2_2_1_,
                             user1_.name as name3_2_1_,
                             user1_.password as password4_2_1_,
                             book2_.price as price2_0_2_,
                             book2_.title as title3_0_2_ 
                         from
                             cartitem cartitem0_ 
                         inner join
                             user user1_ 
                                 on cartitem0_.user_no=user1_.no 
                         inner join
                             book book2_ 
                                 on cartitem0_.book_no=book2_.no 
                         where
                             cartitem0_.user_no=?         
            ```    
            1) CartItem, User, Book 세 테이블에 조인 쿼리가 실행되었음을 알 수가 있다.
            2) 주의할 것은 조인쿼리가 개별적으로 나눠서 실행하는 select쿼리 보다 성능이 반드시 좋다고 볼 수는 없다.
            3) 성능(시간)에 큰 영향을 주는 것은 오히려 projection일 수 있다.
            
    4) test04FindAllByUserNo3
        + 세 테이블에 대한 조인과 프로젝션을 적용한 findAllByUserNo3(no)의 테스트이다.
        + QueryDSL 코드
            ```
                return  queryFactory
                        .select(new QCartItemDto(cartItem.book.no, cartItem.book.title, cartItem.book.price, cartItem.amount))
                        .from(cartItem)
                        .innerJoin(cartItem.user)
                        .innerJoin(cartItem.book)
                        .where(cartItem.user.no.eq(userNo))
                        .fetch();          
            ```    
            1) 프로젝션에서는 fetchJoin()을 사용할 수 없다.
            2) @QueryProjection 를 사용한 DTO 클래스를 사용했다. Q클래스 생성 필요
            
        + 실행된 쿼리
            ```
                select
                    cartitem0_.book_no as col_0_0_,
                    book2_.title as col_1_0_,
                    book2_.price as col_2_0_,
                    cartitem0_.amount as col_3_0_ 
                from
                    cartitem cartitem0_ 
                inner join
                    user user1_ 
                        on cartitem0_.user_no=user1_.no 
                inner join
                    book book2_ 
                        on cartitem0_.book_no=book2_.no 
                where
                    cartitem0_.user_no=?         
            ```
            세 테이블에 조인과 프로젝션이 적용된 쿼리가 실행됨을 알 수 있다.  
            
        + 결과 CartItem Collection(cart) 내용
            ```
                CartItemDto{bookNo=1, bookTitle='책1', bookPrice=1000, amount=1}
                CartItemDto{bookNo=2, bookTitle='책2', bookPrice=1000, amount=2}          
            ```    
        + 성능(시간) 비교
            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/31203.png" width="800px" />
            <br>            
          
            test02FindAllByUserNo, test03FindAllByUserNo2, test04FindAllByUserNo3 테스트의 실행 시간을 보면, 프로젝션과 조인을 함께 실행한 test04FindAllByUserNo3이 확연히 빠름을 알 수 있다.  
    
    5) test05DeleteByUserNoAndBookNo
        + 쿼리메소드 JpaCartItemRepository.deleteByUserNoAndBookNo(userNo, bookNo)의 테스트이다.
        + Model10 ManyToMany 조인테이블과 달리, 연결엔티티의 레포지토리의 쿼리메소드로 편리하게 삭제를 할 수 있다.
        + 하지만, 쿼리 로그를 보면 생각보다 많은 쿼리가 실행됨을 알 수 있다.
            ```
                Hibernate: 
                    select
                        cartitem0_.book_no as book_no1_1_,
                        cartitem0_.user_no as user_no2_1_,
                        cartitem0_.amount as amount3_1_ 
                    from
                        cartitem cartitem0_ 
                    left outer join
                        user user1_ 
                            on cartitem0_.user_no=user1_.no 
                    left outer join
                        book book2_ 
                            on cartitem0_.book_no=book2_.no 
                    where
                        user1_.no=? 
                        and book2_.no=?
                Hibernate: 
                    select
                        book0_.no as no1_0_0_,
                        book0_.price as price2_0_0_,
                        book0_.title as title3_0_0_ 
                    from
                        book book0_ 
                    where
                        book0_.no=?
                Hibernate: 
                    select
                        user0_.no as no1_2_0_,
                        user0_.email as email2_2_0_,
                        user0_.name as name3_2_0_,
                        user0_.password as password4_2_0_ 
                    from
                        user user0_ 
                    where
                        user0_.no=?
                Hibernate: 
                    /* delete me.kickscar.practices.jpa03.model11.domain.CartItem */ delete 
                        from
                            cartitem 
                        where
                            book_no=? 
                            and user_no=?          
            ```
            1) 삭제하기 전, 삭제 대상이 되는 CartItem의 연관필드 엔티티 객체 User, Book을 영속화 시키기 위해 쿼리가 부수적으로 실행된 모습이다.
            2) 쿼리 자체로 보면 큰 부담이 없고 삭제 대상은 아니지만 연관된 두 엔티티 객체를 영속화 하는 것이 필요한 경우도 있을 것이다.
            3) 2)의 경우가 아니라면 바로 삭제 쿼리가 실행되는 것이 불만이 없을 것이다.

    5) test06DeleteByUserNoAndBookNo2
        + QueryDSL를 사용해서 바로 삭제 쿼리가 실행될 수 있도록 작성한 deleteByUserNoAndBookNo2 메소드 테스트이다.
        + QueryDSL 코드
            ```
                queryFactory
                        .delete(cartItem)
                        .where(cartItem.user.no.eq(userNo).and(cartItem.book.no.eq(bookNo)))
                        .execute();          
            ```
        + 실행된 쿼리
            ```
            Hibernate: 
                delete 
                    from
                        cartitem 
                where
                    user_no=? 
                    and book_no=?          
            ```
        + 성능(시간) 비교
            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/31204.png" width="800px" />
            <br>            
          
        + 시간상 큰 차이는 없지만, 캐쉬, 메모리 등을 비교해 보면 더 차이가 있을 수 있으며 많은 수의 삭제에는 문제의 소지가 충분히 있다.
        + 연관된 엔티티의 영속화가 필요없거나 또는 드문드문 적은 수로 삭제를 하는 경우를 제외하고 QueryDSL를 사용해 삭제 쿼리만 실행하는 것이 좋을 것이다. 