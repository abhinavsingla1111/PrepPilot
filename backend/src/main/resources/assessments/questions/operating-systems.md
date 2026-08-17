# Operating Systems — 100 Interview MCQs

> Format: Each question has a difficulty tag `[Easy] / [Medium] / [Hard]`, options (or True/False, or one-word), the correct answer, and a one-line justification.

---

## Section A — Easy (Q1–Q35)

**Q1. [Easy]** The primary role of an operating system is to:

- A) Compile programs
- B) Manage hardware resources and provide services to programs
- C) Design circuits
- D) Render graphics only

**Answer:** B — The OS manages resources and acts as an interface between hardware and applications.

**Q2. [Easy] (One word)** A program in execution is called a **\_\_**.

**Answer:** Process — A process is an active instance of a program.

**Q3. [Easy]** Which is NOT a function of an OS?

- A) Memory management
- B) Process scheduling
- C) File management
- D) Writing application source code

**Answer:** D — Writing application code is the developer's job, not the OS's.

**Q4. [Easy]** The core of the operating system is the:

- A) Shell
- B) Kernel
- C) Compiler
- D) Loader

**Answer:** B — The kernel is the central component managing resources.

**Q5. [Easy] (True/False)** A thread is a lightweight unit of execution within a process.

**Answer:** True — Threads share the process's resources but run independently.

**Q6. [Easy]** Which scheduling algorithm serves processes in arrival order?

- A) SJF
- B) FCFS
- C) Round Robin
- D) Priority

**Answer:** B — First-Come-First-Served processes in arrival order.

**Q7. [Easy]** Which memory is fastest and closest to the CPU?

- A) RAM
- B) Registers/Cache
- C) Hard disk
- D) SSD

**Answer:** B — CPU registers and cache are the fastest storage.

**Q8. [Easy] (One word)** The illusion of a large main memory using disk space is called virtual **\_\_**.

**Answer:** Memory — Virtual memory extends RAM using disk.

**Q9. [Easy]** A deadlock is a situation where:

- A) A process finishes quickly
- B) Processes wait forever for resources held by each other
- C) Memory is freed
- D) The CPU idles

**Answer:** B — Circular waiting prevents any involved process from proceeding.

**Q10. [Easy]** Which of these is a non-preemptive scheduling algorithm?

- A) Round Robin
- B) FCFS
- C) Preemptive Priority
- D) SRTF

**Answer:** B — FCFS never preempts a running process.

**Q11. [Easy] (True/False)** RAM is volatile memory.

**Answer:** True — RAM loses its contents when power is off.

**Q12. [Easy]** The component that translates virtual to physical addresses is the:

- A) ALU
- B) MMU
- C) Cache
- D) Bus

**Answer:** B — The Memory Management Unit performs address translation.

**Q13. [Easy]** Which is a valid process state?

- A) Compiled
- B) Ready
- C) Encrypted
- D) Linked

**Answer:** B — Ready, Running, Waiting, etc., are process states.

**Q14. [Easy] (One word)** The data structure holding information about a process is the Process Control **\_\_**.

**Answer:** Block — The PCB stores process metadata.

**Q15. [Easy]** Round Robin scheduling relies on a:

- A) Priority number
- B) Time quantum
- C) Burst prediction
- D) Deadlock

**Answer:** B — Each process gets a fixed time slice (quantum).

**Q16. [Easy]** Which is used for inter-process communication?

- A) Pipes
- B) Compilers
- C) Registers
- D) Linkers

**Answer:** A — Pipes (and message queues, shared memory) enable IPC.

**Q17. [Easy] (True/False)** A context switch saves and restores process state.

**Answer:** True — It saves the current process's context and loads another's.

**Q18. [Easy]** The mode with full hardware access is:

- A) User mode
- B) Kernel mode
- C) Safe mode
- D) Guest mode

**Answer:** B — Kernel (supervisor) mode has privileged access.

**Q19. [Easy]** A system call is:

- A) A hardware interrupt only
- B) The interface for programs to request OS services
- C) A compiler directive
- D) A CPU register

**Answer:** B — System calls request kernel services from user programs.

**Q20. [Easy] (One word)** The program that loads the OS at startup is the boot**\_\_**.

**Answer:** loader — The bootloader loads the kernel at boot.

**Q21. [Easy]** Which file system is native to Linux (common)?

- A) NTFS
- B) ext4
- C) FAT32
- D) HFS+

**Answer:** B — ext4 is a common native Linux file system.

**Q22. [Easy]** Thrashing occurs when:

- A) CPU is idle
- B) Excessive paging causes low CPU utilization
- C) Files are deleted
- D) Threads finish

**Answer:** B — Constant page faults spend time swapping, not computing.

**Q23. [Easy] (True/False)** A semaphore can be used for process synchronization.

**Answer:** True — Semaphores coordinate access to shared resources.

**Q24. [Easy]** Which scheduling can cause starvation of low-priority processes?

- A) FCFS
- B) Priority scheduling
- C) Round Robin
- D) None

**Answer:** B — Low-priority processes may never run under strict priority.

**Q25. [Easy]** The technique of dividing memory into fixed-size blocks is:

- A) Segmentation
- B) Paging
- C) Swapping
- D) Fragmentation

**Answer:** B — Paging uses fixed-size pages/frames.

**Q26. [Easy] (One word)** A fixed-size block of physical memory is called a **\_\_**.

**Answer:** Frame — Physical memory is divided into frames.

**Q27. [Easy]** A fixed-size block of virtual memory is called a:

- A) Frame
- B) Page
- C) Segment
- D) Sector

**Answer:** B — Virtual memory is divided into pages.

**Q28. [Easy]** Which is the smallest unit of CPU utilization?

- A) Process
- B) Thread
- C) Program
- D) File

**Answer:** B — A thread is the basic unit scheduled on the CPU.

**Q29. [Easy] (True/False)** A zombie process has completed execution but still has an entry in the process table.

**Answer:** True — It awaits the parent to read its exit status.

**Q30. [Easy]** An orphan process is one whose:

- A) Child died
- B) Parent has terminated before it
- C) Memory is freed
- D) Priority is highest

**Answer:** B — Orphans are adopted by init/systemd when the parent exits.

**Q31. [Easy]** Which command shows running processes in Linux?

- A) ls
- B) ps
- C) cd
- D) grep

**Answer:** B — `ps` lists processes.

**Q32. [Easy] (One word)** The unique identifier of a process is the P**\_\_**.

**Answer:** PID — Process ID uniquely identifies a process.

**Q33. [Easy]** Cache memory is used to:

- A) Store files permanently
- B) Speed up access to frequently used data
- C) Replace the CPU
- D) Manage networks

**Answer:** B — Cache reduces average memory access time.

**Q34. [Easy]** Which is a preemptive scheduling algorithm?

- A) FCFS
- B) Round Robin
- C) Non-preemptive SJF
- D) None

**Answer:** B — Round Robin preempts on quantum expiry.

**Q35. [Easy] (True/False)** Multiprogramming increases CPU utilization by keeping multiple jobs in memory.

**Answer:** True — The CPU switches to another job when one waits.

---

## Section B — Medium (Q36–Q75)

**Q36. [Medium]** The four necessary conditions for deadlock are mutual exclusion, hold-and-wait, no preemption, and:

- A) Starvation
- B) Circular wait
- C) Aging
- D) Paging

**Answer:** B — Circular wait completes the Coffman conditions.

**Q37. [Medium]** The Banker's algorithm is used for:

- A) Deadlock detection
- B) Deadlock avoidance
- C) Deadlock prevention
- D) Memory allocation only

**Answer:** B — It checks safe states to avoid deadlock.

**Q38. [Medium] (One word)** The scheduling metric measuring time from submission to completion is **\_\_** time.

**Answer:** Turnaround — Turnaround = completion − arrival.

**Q39. [Medium]** Waiting time is calculated as:

- A) Completion − Arrival
- B) Turnaround − Burst
- C) Burst − Arrival
- D) Arrival − Completion

**Answer:** B — Waiting time = turnaround time − CPU burst time.

**Q40. [Medium]** Which algorithm minimizes average waiting time (optimally)?

- A) FCFS
- B) Shortest Job First (SJF)
- C) Round Robin
- D) Priority

**Answer:** B — SJF is provably optimal for average waiting time.

**Q41. [Medium] (True/False)** SJF can cause starvation of long processes.

**Answer:** True — Continually arriving short jobs can starve long ones.

**Q42. [Medium]** The technique to prevent starvation by gradually increasing priority is:

- A) Paging
- B) Aging
- C) Swapping
- D) Spooling

**Answer:** B — Aging raises the priority of waiting processes over time.

**Q43. [Medium]** A race condition occurs when:

- A) Threads run sequentially
- B) The outcome depends on the timing/interleaving of concurrent accesses
- C) Memory is freed
- D) A process finishes

**Answer:** B — Unsynchronized shared access produces nondeterministic results.

**Q44. [Medium]** The critical section problem requires which properties?

- A) Mutual exclusion, progress, bounded waiting
- B) Paging, swapping, caching
- C) Only mutual exclusion
- D) Deadlock and starvation

**Answer:** A — A correct solution ensures mutual exclusion, progress, and bounded waiting.

**Q45. [Medium] (One word)** A synchronization primitive that allows only one thread at a time is a **\_\_** (binary lock).

**Answer:** Mutex — A mutex enforces mutual exclusion.

**Q46. [Medium]** The difference between a binary semaphore and a mutex is:

- A) None
- B) A mutex has ownership (only the locker can unlock); a semaphore does not
- C) Semaphores can't count
- D) Mutexes count resources

**Answer:** B — Mutexes enforce ownership; semaphores are signaling counters.

**Q47. [Medium]** A counting semaphore is used to:

- A) Protect a single resource
- B) Manage a pool of N identical resources
- C) Prevent context switches
- D) Allocate pages

**Answer:** B — It tracks availability of multiple resource instances.

**Q48. [Medium]** In the producer-consumer problem, semaphores typically track:

- A) Only mutual exclusion
- B) Empty slots, full slots, and mutual exclusion
- C) CPU bursts
- D) Page faults

**Answer:** B — `empty`, `full`, and a `mutex` coordinate the bounded buffer.

**Q49. [Medium] (True/False)** Paging eliminates external fragmentation.

**Answer:** True — Fixed-size frames remove external fragmentation (internal remains).

**Q50. [Medium]** Segmentation can suffer from:

- A) Internal fragmentation only
- B) External fragmentation
- C) No fragmentation
- D) Thrashing only

**Answer:** B — Variable-size segments cause external fragmentation.

**Q51. [Medium]** A page fault occurs when:

- A) A page is in memory
- B) A referenced page is not in physical memory
- C) The CPU idles
- D) A file is deleted

**Answer:** B — The required page must be loaded from disk.

**Q52. [Medium]** Which page replacement algorithm can suffer Belady's anomaly?

- A) LRU
- B) FIFO
- C) Optimal
- D) MFU

**Answer:** B — FIFO can have more faults with more frames (Belady's anomaly).

**Q53. [Medium] (One word)** The page replacement algorithm that replaces the page unused for the longest time is **\_\_**.

**Answer:** LRU — Least Recently Used.

**Q54. [Medium]** The Optimal (OPT) page replacement algorithm:

- A) Is implementable in practice
- B) Replaces the page not needed for the longest future time (a theoretical baseline)
- C) Uses FIFO
- D) Causes thrashing

**Answer:** B — OPT is optimal but requires future knowledge (used as a benchmark).

**Q55. [Medium]** The TLB (Translation Lookaside Buffer) is:

- A) A disk cache
- B) A cache for page table entries to speed address translation
- C) A CPU register
- D) A file system

**Answer:** B — The TLB caches recent virtual-to-physical mappings.

**Q56. [Medium] (True/False)** A higher TLB hit ratio reduces effective memory access time.

**Answer:** True — Fewer page-table lookups speed translation.

**Q57. [Medium]** Demand paging loads a page:

- A) At program start (all pages)
- B) Only when it is referenced
- C) Never
- D) On shutdown

**Answer:** B — Pages are loaded lazily on first access.

**Q58. [Medium]** Which condition does deadlock PREVENTION target?

- A) Only detection after the fact
- B) Ensuring at least one Coffman condition can never hold
- C) Recovery
- D) Scheduling

**Answer:** B — Prevention negates a necessary condition (e.g., no hold-and-wait).

**Q59. [Medium] (One word)** The classic synchronization problem with five philosophers and forks illustrates deadlock and **\_\_**.

**Answer:** Starvation — Dining Philosophers highlights deadlock/starvation.

**Q60. [Medium]** A monitor (in concurrency) provides:

- A) Only a lock
- B) Mutual exclusion plus condition variables for synchronization
- C) Paging
- D) Scheduling

**Answer:** B — Monitors combine mutual exclusion with condition variables.

**Q61. [Medium]** Which is TRUE about user-level vs kernel-level threads?

- A) User threads are scheduled by the kernel
- B) Kernel threads are managed by the OS and can run in true parallel; user threads are managed in user space
- C) They are identical
- D) User threads always use multiple cores

**Answer:** B — Kernel threads get OS scheduling; user threads are lighter but a blocking call can block all.

**Q62. [Medium] (True/False)** In the many-to-one threading model, a single blocking system call can block the entire process.

**Answer:** True — All user threads map to one kernel thread.

**Q63. [Medium]** Convoy effect is associated with:

- A) Round Robin
- B) FCFS (short jobs wait behind a long job)
- C) SJF
- D) LRU

**Answer:** B — A long job at the front delays short jobs in FCFS.

**Q64. [Medium]** Which scheduling algorithm is best for time-sharing systems?

- A) FCFS
- B) Round Robin
- C) Non-preemptive SJF
- D) Batch

**Answer:** B — Round Robin gives fair, responsive time slices.

**Q65. [Medium] (One word)** The degree of multiprogramming is controlled by the **\_\_**-term scheduler.

**Answer:** long — The long-term (job) scheduler admits jobs.

**Q66. [Medium]** The short-term scheduler (dispatcher) decides:

- A) Which job enters memory
- B) Which ready process runs next on the CPU
- C) Which page to evict
- D) Which file to open

**Answer:** B — It selects the next process for the CPU.

**Q67. [Medium]** Spooling is most associated with:

- A) CPU scheduling
- B) Buffering output for slow devices like printers
- C) Paging
- D) Threading

**Answer:** B — Spooling queues jobs for slow I/O devices.

**Q68. [Medium] (True/False)** Internal fragmentation happens when allocated memory is larger than requested.

**Answer:** True — The unused space within an allocated block is internal fragmentation.

**Q69. [Medium]** In a multilevel feedback queue, a process that uses too much CPU is:

- A) Promoted to a higher priority
- B) Demoted to a lower-priority queue
- C) Terminated
- D) Ignored

**Answer:** B — CPU-bound processes drop to lower-priority queues.

**Q70. [Medium]** Copy-on-write (COW) during `fork()` means:

- A) All memory is copied immediately
- B) Pages are shared until one process writes, then copied
- C) No memory is shared
- D) Only the stack is copied

**Answer:** B — COW defers copying until a write occurs.

**Q71. [Medium] (One word)** The signal sent by `kill -9` that cannot be caught or ignored is SIG**\_\_**.

**Answer:** KILL — `SIGKILL` (9) forcibly terminates a process.

**Q72. [Medium]** A trap (software interrupt) is caused by:

- A) Hardware only
- B) A program instruction (e.g., system call or exception)
- C) A disk failure
- D) A power outage

**Answer:** B — Traps are synchronous, program-generated interrupts.

**Q73. [Medium]** Which disk scheduling algorithm services the closest request first?

- A) FCFS
- B) SSTF (Shortest Seek Time First)
- C) SCAN
- D) C-LOOK

**Answer:** B — SSTF picks the request with the least seek distance.

**Q74. [Medium] (True/False)** The SCAN (elevator) algorithm moves the disk head in one direction servicing requests, then reverses.

**Answer:** True — SCAN sweeps like an elevator.

**Q75. [Medium]** Which best describes a monolithic kernel?

- A) All OS services run in kernel space
- B) Services run as separate user processes
- C) No kernel exists
- D) Only drivers run

**Answer:** A — Monolithic kernels run most services in kernel space.

---

## Section C — Hard (Q76–Q100)

**Q76. [Hard]** A microkernel differs from a monolithic kernel by:

- A) Running more services in kernel space
- B) Keeping the kernel minimal and running services (drivers, FS) in user space
- C) Having no system calls
- D) Being slower to boot only

**Answer:** B — Microkernels minimize kernel code, improving modularity/reliability at some IPC cost.

**Q77. [Hard]** The effective access time with TLB hit ratio h, TLB time t, and memory time m (one-level page table) is approximately:

- A) h·m + (1−h)·2m
- B) h·(t+m) + (1−h)·(t+2m)
- C) t only
- D) m only

**Answer:** B — Hit: one memory access; miss: page-table access plus data access (plus TLB time).

**Q78. [Hard] (True/False)** Peterson's algorithm provides a software solution to the two-process critical section problem.

**Answer:** True — It ensures mutual exclusion, progress, and bounded waiting for two processes.

**Q79. [Hard]** Priority inversion is when:

- A) A high-priority task waits on a resource held by a low-priority task
- B) Priorities never change
- C) All tasks have equal priority
- D) The CPU idles

**Answer:** A — A low-priority holder blocks a high-priority waiter (possibly worsened by a mid-priority task).

**Q80. [Hard]** Priority inversion is commonly mitigated by:

- A) Aging only
- B) Priority inheritance
- C) FCFS
- D) Paging

**Answer:** B — The lock holder temporarily inherits the waiter's higher priority.

**Q81. [Hard] (One word)** The condition where two or more processes continuously change state in response to each other without progressing is called **\_\_**.

**Answer:** Livelock — Processes act but make no progress.

**Q82. [Hard]** In a multi-level page table, the main advantage over a single-level table is:

- A) Faster translation always
- B) Reduced memory for sparse address spaces
- C) No page faults
- D) Larger pages

**Answer:** B — It avoids allocating page-table space for unused regions.

**Q83. [Hard]** An inverted page table has one entry per:

- A) Virtual page
- B) Physical frame
- C) Process
- D) Segment

**Answer:** B — It maps physical frames, saving space for large virtual spaces.

**Q84. [Hard] (True/False)** The working set model estimates the set of pages a process is actively using to reduce thrashing.

**Answer:** True — It keeps the current locality resident to avoid excessive paging.

**Q85. [Hard]** Belady's anomaly specifically refers to:

- A) More frames always reducing faults
- B) More frames sometimes increasing page faults (in FIFO)
- C) LRU inefficiency
- D) TLB misses

**Answer:** B — FIFO can paradoxically fault more with additional frames.

**Q86. [Hard]** Which page replacement algorithms are "stack algorithms" (immune to Belady's anomaly)?

- A) FIFO
- B) LRU and OPT
- C) Second-chance only
- D) None

**Answer:** B — Stack algorithms like LRU/OPT never exhibit Belady's anomaly.

**Q87. [Hard] (One word)** The clock (second-chance) algorithm approximates which replacement policy?

**Answer:** LRU — Second-chance approximates LRU using a reference bit.

**Q88. [Hard]** In the readers-writers problem, a "readers-preference" solution can cause:

- A) Deadlock always
- B) Writer starvation
- C) Reader starvation
- D) No issues

**Answer:** B — Continuous readers can starve waiting writers.

**Q89. [Hard]** A safe state (Banker's algorithm) guarantees:

- A) No process ever waits
- B) There exists an ordering allowing all processes to complete
- C) Maximum CPU utilization
- D) Zero page faults

**Answer:** B — A safe sequence exists so every process can obtain resources and finish.

**Q90. [Hard] (True/False)** Deadlock detection allows deadlocks to occur and then recovers, unlike prevention/avoidance.

**Answer:** True — Detection periodically checks for cycles and recovers.

**Q91. [Hard]** In demand paging, the effective access time with page-fault rate p, memory access m, and page-fault service time f is:

- A) m + p·f
- B) (1−p)·m + p·(f)
- C) (1−p)·m + p·f (approximately, f ≫ m)
- D) p·m only

**Answer:** C — Weighted average of normal access and fault-service time dominates when f ≫ m.

**Q92. [Hard]** Which is TRUE about `fork()` return values?

- A) Returns 0 to both
- B) Returns child PID to parent and 0 to the child
- C) Returns -1 always
- D) Returns the same value to both

**Answer:** B — Parent gets the child's PID; the child gets 0.

**Q93. [Hard] (One word)** The system call that replaces the current process image with a new program is exec (the **\_\_** family).

**Answer:** exec — The `exec*()` family loads a new program image.

**Q94. [Hard]** A spinlock is preferable to a blocking mutex when:

- A) The critical section is very long
- B) The wait is expected to be very short and context-switch cost exceeds spin cost
- C) On single-core with no preemption benefit
- D) Never

**Answer:** B — Spinning avoids context-switch overhead for brief waits (multi-core).

**Q95. [Hard]** The C-SCAN disk scheduling algorithm improves fairness over SCAN by:

- A) Reversing at each end
- B) Servicing in one direction then jumping to the start (treating the disk as circular)
- C) Random seeks
- D) Ignoring requests

**Answer:** B — C-SCAN returns to the beginning without servicing on the way back, giving uniform wait.

**Q96. [Hard] (True/False)** Memory-mapped I/O maps device registers into the address space so normal load/store instructions access devices.

**Answer:** True — Devices are accessed like memory locations.

**Q97. [Hard]** DMA (Direct Memory Access) benefits the system by:

- A) Making the CPU copy every byte
- B) Transferring data between device and memory without CPU involvement per byte
- C) Disabling interrupts
- D) Increasing page faults

**Answer:** B — DMA offloads bulk transfers, freeing the CPU.

**Q98. [Hard]** A translation with a two-level page table on a TLB miss requires how many memory accesses just for the page tables?

- A) 1
- B) 2
- C) 0
- D) 4

**Answer:** B — Two levels mean two page-table memory accesses (plus the final data access).

**Q99. [Hard] (One word)** The scheduling anomaly where increasing the time quantum in Round Robin toward infinity makes it behave like **\_\_**.

**Answer:** FCFS — A very large quantum degenerates Round Robin into FCFS.

**Q100. [Hard]** Which best explains why context switching is pure overhead?

- A) It executes user code faster
- B) The CPU does no useful user work while saving/restoring state and flushing caches/TLB
- C) It frees memory
- D) It prevents interrupts

**Answer:** B — Switching consumes CPU cycles without progressing user computation (and can cause cache/TLB misses).
