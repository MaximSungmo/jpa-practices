package me.kickscar.practices.jpa02.app03;

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
public class SpringBootApp03 {

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

                beforeTest( em1 );
                deleteTest( em2 );

                em1.close();
                em2.close();

                emf.close();
            }

            public void beforeTest( EntityManager em ){
                // 트랜잭션 객체 얻어오기
                EntityTransaction tx = em.getTransaction();

                // [트랜잭션 시작]
                tx.begin();

                Member memberA = new Member();
                memberA.setId( "memberA" );
                memberA.setName( "회원A" );
                em.persist( memberA );

                tx.commit();
                // [트랜잭션 종료]
            }

            public void deleteTest( EntityManager em ){
                // 트랜잭션 객체 얻어오기
                EntityTransaction tx = em.getTransaction();

                // [트랜잭션 시작]
                tx.begin();

                // 삭제 대상 엔티티 조회
                Member memberA = em.find( Member.class, "memberA" );

                // 엔티티 삭제
                em.remove( memberA );

                tx.commit();
                // [트랜잭션 종료]
            }

        };
    }

    public static void main(String[] args) {
        try(ConfigurableApplicationContext c = SpringApplication.run(SpringBootApp03.class, args)){}
    }
}
