## 1. 연관관계 매핑과 JPQL(QueryDSL), Spring Data 기반 Repository 작성법 

### Module: jpa01-entity-mapping 
#### 1. 다양한 엔티티 매핑 설명
#### 2. 예제 코드: Member 엔티티 

### Module: jpa02-persistence-context 
#### 1. 영속성 켄텍스트(Persistence Context) 설명
#### 2. 영속성 관리 / 엔티티 생명주기 설명
#### 3. 예제 코드: 엔티티 조회/등록/수정/삭제 
#### 4. 준영속성 설명

### Module: jpa03-modelxx 
#### 1. 다루는 내용
1) 다양한 연관관계 매핑 설명
2) JPA 프로그래밍 테크닉
3) Repository(JPQL, QueryDSL, Spring Data JPA 기반) 개별적 구현/테스트
4) 연관관계와 매핑 설정에 따른 성능이슈 설명 
5) 성능 이슈를 해결할 수 있는 Repository 구현 방법
6) Repository 최적화 방법과 JPA 프로그래밍에서 고민해야 하는 것들!

#### 2. 연관관계 모델
 1) 단일(One) - 방명록
 2) 다대일(ManyToOne) 단방향(Unidirectional) - 게시판  [Board -> User]
 3) 다대일(ManyToOne) 양방향(Bidirectional) - 온라인북몰  [Order <-> User]
 4) 일대다(OneToMany) 단방향(Unidirectional) - 게시판  [Board -> Comment]
 5) 일대다(OneToMany) 양방향(Bidirectional) - 온라인북몰  [User <->Order]
 6) 일대일(OneToOne) 단방향(Unidirectional), 주테이블 외래키 - JBlog [User -> Blog]
 7) 일대일(OneToOne) 양방향(Bidirectional), 주테이블 외래키 - JBlog [User <-> Blog]
 8) 일대일(OneToOne) 양방향(Bidirectional), 대상테이블 외래키, 식별관계 - JBlog [User <-> Blog]
 9) 다대다(ManyToMany) 단방향(Unidirectional) - 음반검색 [노래 -> 쟝르]
10) 다대다(ManyToMany) 양방향(Bidirectional) - 음반검색 [노래 <-> 쟝르]
11) 혼합모델(다대다 문제해결, 연결엔티티, 복합키(PK), 식별관계, @EmbeddedId) - 온라인북몰 [User <-> CartItem -> Book]
12) 혼합모델(다대다 문제해결, 연결엔티티, 복합키(PK), 식별관계, @IdClass) - 온라인북몰 [User <-> CartItem -> Book]
13) 혼합모델(다대다 문제해결, 연결엔티티, 새PK, 비식별관계) - 온라인북몰 [User <-> CartItem -> Book]

## 2. 프로젝트 로컬 클론 & 모듈 임포트 & 실행/테스트 환경 설정 (IntelliJ IDEA)
#### 1. Git Repository URL 선택  - **Clone** 
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00001.png" width="600px" />
<br/>  
   
#### 2. Checkout from Version Control - **Yes**  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00002.png" width="600px" />
<br/>  
  
#### 3. Create Project from Existing Sources - **Next**  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00003.png" width="600px" />
<br/>  
  
#### 4. Project Basic Layouts - **Default & Next**  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00004.png" width="600px" />
<br/>  
   
#### 5. Not Empty Folder - **Yes**  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00005.png" width="600px" />
<br/>  
  
#### 6. Project Root Directory 선택 - **Nothing Selected & Finish**  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00006.png" width="600px" />
<br/>  
    
#### 7. Import Module #1  - **Project Structure Window(command + ;)**  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00007.png" width="600px" />
<br/>  
  
#### 8. Import Module #2  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00008.png" width="600px" />
<br/>  
  
#### 9. Project Root Directory 선택 - **OK**  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00009.png" width="600px" />
<br/>  

#### 10. Import Module from External Model - **Gradle & Finish**  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00010.png" width="600px" />
<br/>  

#### 11. Close Project Structure Window - **Apply & OK**  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00011.png" width="600px" />
<br/>  
  
#### 12. Reimport All Gradle Project  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00018.png" width="600px" />
<br/>  
  
#### 13. 잘가져온 모듈 모습!!!  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00013.png" width="600px" />
<br/>  

#### 14. Settings(Command + ,) 에서 Intellij IDEA Native 실행 설정 (Default는 gradle 실행) 
#### 15. *.iml 파일 생성 Diabled  - **Apply & OK** 
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00014.png" width="600px" />
<br/>
  
#### 16. 실행하기(Run)  
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00015.png" width="600px" />
<br/>

#### 17. [Tip] .ignore 플러그인 설치 - **Restart IDE** 
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00016.png" width="600px" />
<br/>
  
#### 18. Hide Ignored files 
<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00017.png" width="600px" />
<br/>

#### 19. 나머지 모듈도 같은 방식으로 !!
1. jpa03-modelxx 모듈들의 QueryDSL Gradle 플러그인 설정은 각 모듈 README.md 참고!!



## 3. 프로젝트 로컬 클론 & 모듈 임포트 & 실행/테스트 환경 설정 (Eclipse)




 
  