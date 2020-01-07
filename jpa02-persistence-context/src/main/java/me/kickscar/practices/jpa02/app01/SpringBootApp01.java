package me.kickscar.practices.jpa02.app01;

import me.kickscar.practices.jpa02.domain.Member;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

@SpringBootApplication
// 엔티티 클래스 자동스캐닝 베이스 패키지 지정
@EntityScan( basePackages = { "me.kickscar.practices.jpa02.domain" } )
public class SpringBootApp01 {

    //엔티티매니저팩토리 주입
    @PersistenceUnit
    EntityManagerFactory emf;

    @Bean
    ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Override
            public void run(ApplicationArguments args) throws Exception {

                // 엔티티매니저 생성
                EntityManager em1 = emf.createEntityManager();
                EntityManager em2 = emf.createEntityManager();

                // 트랜잭션 객체 얻어오기
                EntityTransaction tx = em1.getTransaction();

                try{
                    // [트랜잭션 시작]
                    tx.begin();

                    testInsert( em1 );
                    testFind01( em1 );
                    testIdentity( em1 );

                    tx.commit();
                    // [트랜잭션 종료]

                    testFind02( em2 );

                } catch(Exception e){
                    e.printStackTrace();
                    tx.rollback();
                }

                em1.close();
                em2.close();

                emf.close();
            }

            public void testInsert( EntityManager em ) {
                Member member = new Member();

                // 1차 캐시에 저장됨
                member.setId( "member1" );
                member.setName( "회원1" );

                em.persist( member );
            }


            public void testFind01( EntityManager em ) {
                // 1차 캐시에서 조회(쿼리 로그 확인)
                Member findMember = em.find( Member.class, "member1" );
                System.out.println( findMember );
            }

            public void testIdentity( EntityManager em ) {
                Member a = em.find( Member.class, "member1" );
                Member b = em.find( Member.class, "member1" );
                System.out.println( a == b );
            }

            public void testFind02( EntityManager em ) {
                //  DB에서 조회(쿼리 로그 확인)
                Member findMember = em.find( Member.class, "member1" );
                System.out.println( findMember );
            }
        };
    }

    public static void main(String[] args) {
        try(ConfigurableApplicationContext c = SpringApplication.run(SpringBootApp01.class, args)){}
    }
}
