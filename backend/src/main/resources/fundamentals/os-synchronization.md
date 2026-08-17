# Synchronization and Race Conditions

> Shared mutable state is safe only when every access follows a correct synchronization protocol.

## Race condition

A race occurs when the outcome depends on an uncontrolled interleaving. `count++` is read, modify and write—not one indivisible operation—so two threads can lose an update.

## Critical-section requirements

A correct solution aims for:

1. **Mutual exclusion:** only one participant executes the critical section.
2. **Progress:** threads outside the section cannot block a decision forever.
3. **Bounded waiting:** a waiting participant is not postponed indefinitely.

## Main primitives

| Primitive | Use |
| --- | --- |
| Mutex | Exclusive ownership; the locker unlocks it |
| Semaphore | Counter of permits; models a finite resource pool |
| Read-write lock | Many readers or one writer |
| Condition variable | Sleep until a protected predicate may be true |
| Atomic operation | Lock-free indivisible update for a small value |
| Monitor | Shared state plus methods, lock and conditions |

## Producer–consumer flow

```mermaid
sequenceDiagram
    participant P as "Producer"
    participant Q as "Bounded queue"
    participant C as "Consumer"
    P->>Q: lock
    P->>Q: wait while full
    P->>Q: enqueue item
    P->>Q: signal not-empty; unlock
    C->>Q: lock
    C->>Q: wait while empty
    C->>Q: dequeue item
    C->>Q: signal not-full; unlock
```

Condition predicates must be checked in a loop because wakeups can be spurious and another thread may consume the condition before the awakened thread reacquires the lock.

## Semaphore versus mutex

A mutex represents ownership of a critical section. A counting semaphore represents available permits and can allow several concurrent holders. Treating a semaphore as a mutex can obscure ownership mistakes.

## Memory visibility

Synchronization is also about visibility and ordering. Without a happens-before relationship, one core may not promptly observe another core's write or may observe operations in an unexpected order. Locks, atomics and language memory-model constructs establish the necessary guarantees.

## Common failures

- Holding a lock during network or disk I/O.
- Locking the same resources in inconsistent order.
- Protecting one field with different locks.
- Using `sleep` instead of signaling.
- Assuming a thread-safe collection makes a multi-step workflow atomic.

## Interview checklist

State what data is shared, name the invariant, minimize the critical section, choose a primitive, and explain liveness as well as safety.

