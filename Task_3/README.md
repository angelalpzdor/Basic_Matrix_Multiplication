# Task 3 – Parallel and Vectorized Matrix Multiplication
Big Data – ULPGC  
Ángela López Dorta

## Overview
This task evaluates different techniques to accelerate matrix multiplication using parallel computing and manual vectorization. The goal is to compare the sequential baseline with several parallel approaches and analyze execution time, speedup, and efficiency.

Implemented methods:
- **Baseline (sequential O(n³))**
- **ThreadPool (2, 4, 8 threads)**
- **Parallel Streams**
- **Vectorized (SIMD-style loop unrolling)**

Matrices tested:
200 × 200
300 × 300
400 × 400
500 × 500


---

## File Structure

│── src/
│ ├── BaselineMatrix.java
│ ├── ParallelMatrix.java
│ ├── ParallelStreamMatrix.java
│ ├── VectorizedMatrix.java
│ └── BenchmarkTask3.java
│
│── results/
│ ├── task3_results.csv
│ ├── time_plot.png
│ ├── speedup_plot.png
│ └── efficiency_plot.png
│
│── plot_results_task3.py
│── README.md


---

## How to Compile and Run

### 1) Compile Java files

From inside `Task_3/src/`:

```bash
javac *.java
2) Run the benchmark
java BenchmarkTask3
```

This generates:

Task_3/results/task3_results.csv

3) Generate the plots

From inside Task_3:

python3 plot_results_task3.py


This creates:

time_plot.png

speedup_plot.png

efficiency_plot.png

Results Summary
Execution Time

Baseline is the slowest.

ThreadPool performance increases with thread count.

Parallel Streams achieves the best overall time.

Vectorized improves the sequential method slightly.

Speedup

Parallel Streams has the highest speedup (~5.8×).

ThreadPool with 8 threads performs consistently well.

Vectorized yields small speedups (~1.1×).

Efficiency

Efficiency increases with matrix size.

Some values exceed 1.0 (superlinear speedup), a known effect caused by caching and JVM behavior.

Conclusions

Parallel computing greatly accelerates matrix multiplication compared to the sequential baseline.

Main conclusions:

Parallel Streams = best performance + simplest implementation.

ThreadPool = good scalability and control over threads.

Vectorized = slight improvement but far from true parallelism.

Larger matrices benefit more from parallelism because thread overhead becomes less relevant.

Author

Ángela López Dorta
Grado en Ciencia e Ingeniería de Datos – ULPGC
