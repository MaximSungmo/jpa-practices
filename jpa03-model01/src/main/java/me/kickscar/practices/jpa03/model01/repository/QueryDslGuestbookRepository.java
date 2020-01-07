package me.kickscar.practices.jpa03.model01.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import me.kickscar.practices.jpa03.model01.dto.GuestbookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import java.util.List;

import static me.kickscar.practices.jpa03.model01.domain.QGuestbook.guestbook;

@Repository
public class QueryDslGuestbookRepository extends QuerydslRepositorySupport {

    @Autowired
    private JPAQueryFactory queryFactory;

    public QueryDslGuestbookRepository(JPAQueryFactory queryFactory) {
        super(Guestbook.class);
    }

    // 저장(영속화)
    public void save(Guestbook guestbook){
        getEntityManager().persist(guestbook);
    }

    // 조회1
    public List<Guestbook> findAll1(){
        return (List<Guestbook>) queryFactory
                .select(guestbook)
                .from(guestbook)
                .orderBy(guestbook.regDate.desc())
                .fetch();
    }

    // 조회2 - 프로젝션
    public List<GuestbookDto> findAll2(){
        return queryFactory
                .select(Projections.constructor(GuestbookDto.class, guestbook.no, guestbook.name, guestbook.contents, guestbook.regDate))
                .from(guestbook)
                .orderBy(guestbook.regDate.desc())
                .fetch();
    }

    // 삭제
    public Boolean delete(Long no, String password) {
        return queryFactory
                .delete(guestbook)
                .where(guestbook.no.eq(no).and(guestbook.password.eq(password)))
                .execute() == 1;
    }

    // count
    public Long count() {
        return queryFactory
                .from(guestbook)
                .fetchCount();
    }
}
