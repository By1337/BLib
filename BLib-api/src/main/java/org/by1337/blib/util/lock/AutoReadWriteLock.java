package org.by1337.blib.util.lock;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A utility class that extends {@link ReadWriteLock} by providing
 * auto-locking functionality with {@link AutoLock}, which supports try-with-resources.
 */
public class AutoReadWriteLock implements ReadWriteLock {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Retrieves the {@link Lock} for read operations.
     *
     * @return The read lock.
     */
    @Override
    public @NotNull Lock readLock() {
        return lock.readLock();
    }

    /**
     * Retrieves the {@link Lock} for write operations.
     *
     * @return The write lock.
     */
    @Override
    public @NotNull Lock writeLock() {
        return lock.writeLock();
    }

    /**
     * Creates an {@link AutoLock} for the read lock.
     *
     * @return An {@link AutoLock} instance wrapping the read lock.
     */
    public @NotNull AutoLock autoReadLock() {
        return new AutoLock(lock.readLock());
    }

    /**
     * Creates an {@link AutoLock} for the write lock.
     *
     * @return An {@link AutoLock} instance wrapping the write lock.
     */
    public @NotNull AutoLock autoWriteLock() {
        return new AutoLock(lock.writeLock());
    }

    /**
     * Acquires the read lock and returns it as an {@link AutoLock}.
     *
     * @return An {@link AutoLock} instance wrapping the read lock, already locked.
     */
    public @NotNull AutoLock readLockAndLock() {
        var l = autoReadLock();
        l.lock();
        return l;
    }

    /**
     * Acquires the write lock and returns it as an {@link AutoLock}.
     *
     * @return An {@link AutoLock} instance wrapping the write lock, already locked.
     */
    public @NotNull AutoLock writeLockAndLock() {
        var l = autoWriteLock();
        l.lock();
        return l;
    }

    /**
     * Retrieves the underlying {@link ReentrantReadWriteLock}.
     *
     * @return The source lock.
     */
    public ReentrantReadWriteLock getSource() {
        return lock;
    }

    /**
     * A wrapper around {@link Lock} that implements {@link AutoCloseable}.
     * This allows locks to be used in try-with-resources blocks.
     */
    public static class AutoLock implements Lock, AutoCloseable {
        private final Lock lock;

        /**
         * Constructs an {@code AutoLock} wrapping the given {@link Lock}.
         *
         * @param lock The lock to wrap.
         */
        public AutoLock(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void lock() {
            lock.lock();
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            lock.lockInterruptibly();
        }

        @Override
        public boolean tryLock() {
            return lock.tryLock();
        }

        @Override
        public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
            return lock.tryLock(time, unit);
        }

        @Override
        public void unlock() {
            lock.unlock();
        }

        @Override
        public @NotNull Condition newCondition() {
            return lock.newCondition();
        }

        /**
         * Releases the lock when used in a try-with-resources block.
         */
        @Override
        public void close() {
            unlock();
        }
    }
}
