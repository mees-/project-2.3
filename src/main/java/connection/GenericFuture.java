package connection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class GenericFuture<T> implements Future<T> {
    private ArrayBlockingQueue<T> valueQueue = new ArrayBlockingQueue<>(1);
    private T value = null;

    public void resolve(T value) {
            try {
                valueQueue.put(value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public boolean cancel(boolean b) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return this.value != null;
    }

    @Override
    public T get() throws InterruptedException {
        if (isDone()) {
            return value;
        } else {
            value = valueQueue.take();
            return value;
        }
    }

    @Override
    public T get(long l, TimeUnit timeUnit) throws InterruptedException {
        if (isDone()) {
            return value;
        } else {
            value = valueQueue.poll(l, timeUnit);
            return value;
        }
    }

    public void waitForResolve() throws InterruptedException {
        get();
    }
}
