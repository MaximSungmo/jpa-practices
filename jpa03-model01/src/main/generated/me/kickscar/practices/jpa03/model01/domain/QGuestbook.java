package me.kickscar.practices.jpa03.model01.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QGuestbook is a Querydsl query type for Guestbook
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGuestbook extends EntityPathBase<Guestbook> {

    private static final long serialVersionUID = 1144079058L;

    public static final QGuestbook guestbook = new QGuestbook("guestbook");

    public final StringPath contents = createString("contents");

    public final StringPath name = createString("name");

    public final NumberPath<Long> no = createNumber("no", Long.class);

    public final StringPath password = createString("password");

    public final DateTimePath<java.util.Date> regDate = createDateTime("regDate", java.util.Date.class);

    public QGuestbook(String variable) {
        super(Guestbook.class, forVariable(variable));
    }

    public QGuestbook(Path<? extends Guestbook> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGuestbook(PathMetadata metadata) {
        super(Guestbook.class, metadata);
    }

}

