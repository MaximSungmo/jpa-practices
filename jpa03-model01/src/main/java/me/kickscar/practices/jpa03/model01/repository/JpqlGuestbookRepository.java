package me.kickscar.practices.jpa03.model01.repository;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import me.kickscar.practices.jpa03.model01.dto.GuestbookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class JpqlGuestbookRepository {

    @Autowired
    private EntityManager em;

    // 저장
    public void save(Guestbook guestbook){
        em.persist(guestbook);
    }

    // 삭제
    public Boolean delete(Long no, String password) {
        String qlString = "delete from Guestbook gb where gb.no=:no and gb.password=:password";
        Query query = em.createQuery(qlString);
        query.setParameter("no", no);
        query.setParameter("password", password);
        return query.executeUpdate() == 1;
    }

    // 조회1
    public List<Guestbook> findAll1(){
        String qlString = "select gb from Guestbook gb order by gb.regDate desc";
        TypedQuery<Guestbook> query = em.createQuery(qlString, Guestbook.class);
        return query.getResultList();
    }

    // 조회2
    public List<GuestbookDto> findAll2(){
        String qlString = "select new me.kickscar.practices.jpa03.model01.dto.GuestbookDto(gb.no, gb.name, gb.contents, gb.regDate) from Guestbook gb order by gb.regDate desc";
        TypedQuery<GuestbookDto> query = em.createQuery(qlString, GuestbookDto.class);
        return query.getResultList();
    }

    // count
    public Long count() {
        TypedQuery<Long> query = em.createQuery("select count(gb) from Guestbook gb", Long.class);
        return query.getSingleResult();
    }
}
