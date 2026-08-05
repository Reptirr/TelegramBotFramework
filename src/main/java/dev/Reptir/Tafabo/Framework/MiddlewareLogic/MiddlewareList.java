package dev.Reptir.Tafabo.Framework.MiddlewareLogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MiddlewareList<T, R> extends ArrayList<Middleware<T, R>> {

    private final Comparator<Middleware<T, R>> comparator =
            Comparator.comparingInt(Middleware::priority);

    @Override
    public boolean add(Middleware<T, R> middleware) {
        int index = Collections.binarySearch(this, middleware, comparator);

        if (index < 0)
            index = -index - 1;

        super.add(index, middleware);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Middleware<T, R>> collection) {
        boolean modified = false;

        for (Middleware<T, R> middleware : collection)
            modified |= add(middleware);

        return modified;
    }

}