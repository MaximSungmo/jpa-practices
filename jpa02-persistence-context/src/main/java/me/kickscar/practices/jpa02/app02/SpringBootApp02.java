package me.kickscar.practices.jpa02.app02;

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
public class SpringBootApp02 {

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

                try{
                    // [트랜잭션 시작: 엔티티 매니저는 데이터 변경 시 트랜잭션을 시작해야 한다]
                    tx.begin();

                    Member memberA = new Member();
                    memberA.setId( "memberA" );
                    memberA.setName( "회원A" );
                    em.persist( memberA );

                    Member memberB = new Member();
                    memberB.setId( "memberB" );
                    memberB.setName( "회원B" );
                    em.persist( memberB );

                    tx.commit();
                    // [트랜잭션 종료: 커밋하는 순간 데이터베이스에 INSERT SQL를 보낸다: 쿼리 로그 확인]

                } catch(Exception e){
                    e.printStackTrace();
                    tx.rollback();
                }

                em.close();
                emf.close();
            }
        };
    }

    public static void main(String[] args) {
        try(ConfigurableApplicationContext c = SpringApplication.run(SpringBootApp02.class, args)){}
    }
}
