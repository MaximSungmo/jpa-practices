package me.kickscar.practices.jpa03.model11.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCartItemId is a Querydsl query type for CartItemId
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QCartItemId extends BeanPath<CartItemId> {

    private static final long serialVersionUID = -167203524L;

    public static final QCartItemId cartItemId = new QCartItemId("cartItemId");

    public final NumberPath<Long> bookNo = createNumber("bookNo", Long.class);

    public final NumberPath<Long> userNo = createNumber("userNo", Long.class);

    public QCartItemId(String variable) {
        super(CartItemId.class, forVariable(variable));
    }

    public QCartItemId(Path<? extends CartItemId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCartItemId(PathMetadata metadata) {
        super(CartItemId.class, metadata);
    }

}

