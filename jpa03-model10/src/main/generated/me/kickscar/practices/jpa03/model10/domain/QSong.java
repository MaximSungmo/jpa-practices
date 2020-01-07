package me.kickscar.practices.jpa03.model10.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSong is a Querydsl query type for Song
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSong extends EntityPathBase<Song> {

    private static final long serialVersionUID = -448009342L;

    public static final QSong song = new QSong("song");

    public final SetPath<Genre, QGenre> genres = this.<Genre, QGenre>createSet("genres", Genre.class, QGenre.class, PathInits.DIRECT2);

    public final NumberPath<Long> no = createNumber("no", Long.class);

    public final StringPath title = createString("title");

    public QSong(String variable) {
        super(Song.class, forVariable(variable));
    }

    public QSong(Path<? extends Song> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSong(PathMetadata metadata) {
        super(Song.class, metadata);
    }

}

