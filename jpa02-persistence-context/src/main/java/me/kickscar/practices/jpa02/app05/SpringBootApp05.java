package me.kickscar.practices.jpa02.app05;

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
public class SpringBootApp05 {

    //엔티티매니저팩토리 주입
    @PersistenceUnit
    EntityManagerFactory emf;

    @Bean
    ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Override
            public void run(ApplicationArguments args) throws Exception {

                // 엔티티매니저 생성
                EntityManager em = emf.createEntityManager();

                // 트랜잭션 객체 얻어오기
                EntityTransaction tx = em.getTransaction();

                // [트랜잭션 시작]
                tx.begin();

                // 엔티티 생성, 비영속 상태
                Member memberA = new Member();
                memberA.setId( "memberA" );
                memberA.setName( "회원A" );

                //영속 상태
                em.persist( memberA );

                //회원 엔티티를 영속성 컨텍스트에서 분리, 준영속 상태
                em.detach( memberA );

                tx.commit();
                // [트랜잭션 종료: insert sql이 실행 되지 않는다.]

                em.close();
                emf.close();
            }
        };
    }

    public static void main(String[] args) {
        try(ConfigurableApplicationContext c = SpringApplication.run(SpringBootApp05.class, args)){}
    }
}
