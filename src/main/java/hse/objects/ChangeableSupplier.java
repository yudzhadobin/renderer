package hse.objects;

import java.util.function.Supplier;

/**
 * Created by Yura on 09.01.2017.
 */
public class ChangeableSupplier<T> implements Supplier<T> {
    T instance;


    public ChangeableSupplier(T instance) {
        this.instance = instance;
    }

    @Override
    public T get() {
        return instance;
    }


    public void set(T value) {
        instance = value;
    }
}
