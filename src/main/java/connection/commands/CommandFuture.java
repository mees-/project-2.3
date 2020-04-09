package connection.commands;

import connection.commands.response.StandardResponse;

import java.util.concurrent.*;

public class CommandFuture<T extends StandardResponse> implements Future<T> {
    private ArrayBlockingQueue<T> responseQueue = new ArrayBlockingQueue<>(1);
    private T response = null;

    void setResponse(T response) {
        try {
            responseQueue.put(response);
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
        return this.response != null;
    }

    @Override
    public T get() throws InterruptedException {
        if (isDone()) {
            return response;
        } else {
            response = responseQueue.take();
            return response;
        }
    }

    @Override
    public T get(long l, TimeUnit timeUnit) throws InterruptedException {
        if (isDone()) {
            return response;
        } else {
            response = responseQueue.poll(l, timeUnit);
            return response;
        }
    }
}
