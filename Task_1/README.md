# Cross-Language Matrix Benchmarking with Memory Profiling

This repository contains a benchmarking suite that compares the performance of **matrix multiplication** across three programming languages — **C++**, **Java**, and **Python** — under a unified methodology.  
The benchmark evaluates **execution time** and **memory usage** for matrices of arbitrary size and produces reproducible results stored in CSV files.

---

##  Overview

Each implementation performs a basic dense matrix multiplication with cubic time complexity **O(n³)**.  
The programs prompt the user for:
- **Matrix size** `n`
- **Number of runs**

For each run, they record:
- Execution time per iteration  
- Measured and theoretical memory usage (in MB)  
- Statistical metrics such as mean, standard deviation, best, and worst time  

All results are appended to a shared CSV file in the `results/` directory.

---

##  Implementations

| Language | File | Description |
|-----------|------|-------------|
| **C++** | [`Benchmark.cpp`](Benchmark.cpp) & [`Matrix.hpp`](Matrix.hpp) | Fully compiled benchmark using `<chrono>` for timing and `getrusage()` for memory profiling. Results stored in `results/benchmark_results.csv`. |
| **Java** | [`Benchmark.java`](Benchmark.java) & [`Matrix.java`](Matrix.java) | JVM-based implementation using `System.nanoTime()` for precision timing and the `Runtime` API for heap memory measurement. |
| **Python** | [`benchmark.py`](benchmark.py) & [`matrix.py`](matrix.py) | Interpreted version using `time.perf_counter()` and `psutil` to track process memory usage dynamically. |

---

##  Theoretical Memory Model

Each implementation uses three dense matrices `A`, `B`, and `C`, each of size `n × n` containing 64-bit floating-point numbers.  
Thus, the **theoretical memory usage** is:

\[
\text{Memory} = 3 \times n^2 \times 8\ \text{bytes}
\]

This serves as a baseline to compare against the measured process memory obtained at runtime.

---

##  How to Run

### 1. **C++**

```bash
clang++ -std=c++17 -O3 Benchmark.cpp -o benchmark
./benchmark
```
Then enter:
```yaml
Then enter:

Enter matrix size: 512
Enter number of runs: 5
```
Results will be appended to : 
```bash 
results/benchmark_results.csv 
```
### 2. **Java**
```bash
javac Benchmark.java Matrix.java
java Benchmark
```
Follow the same prompts.

### 3. **Python**
Install psutil if not present:
```bash
pip install psutil
```
Then run:
```bash
python3 benchmark.py
```

## Output Format
All three programs append results to a shared CSV file located in:
```bash
results/benchmark_results.csv
```
Each row has the following columns:

| Column                    | Description                          |
| ------------------------- | ------------------------------------ |
| `language`                | Programming language used            |
| `size`                    | Matrix dimension (n × n)             |
| `runs`                    | Number of repetitions                |
| `mean (s)`                | Average execution time               |
| `stdev`                   | Standard deviation of execution time |
| `best`                    | Fastest run                          |
| `worst`                   | Slowest run                          |
| `real memory (MB)`        | Average measured memory usage        |
| `theoretical memory (MB)` | Estimated ideal memory usage         |


Example:
```scss
language,size,runs,mean (s),stdev,best,worst,real memory (MB),theoretical memory (MB)
cpp,512,5,0.021107,0.000112,0.020985,0.021354,12.10,6.00
python,512,5,4.501235,0.076132,4.421001,4.610922,45.02,6.00
java,512,5,0.132450,0.004231,0.128990,0.137811,7.12,6.00
```

## Data Interpretation

The benchmark highlights the impact of language execution models on performance and resource usage:

- C++ achieves the fastest execution time due to native compilation and direct memory access.
It maintains high CPU utilization and a small, predictable memory footprint.

- Java demonstrates a remarkably low memory footprint, aided by its optimized heap and compact array representation.
Although CPU usage appears lower, the JIT compiler dynamically optimizes code paths, resulting in efficient execution.

- Python is significantly slower and consumes the most memory, reflecting the cost of its interpreted nature and dynamic object model.
Despite high CPU utilization, much of that time is spent managing interpreter overhead rather than performing arithmetic operations.

## Folder Structure
```bash
matrix-bench/
├── Benchmark.cpp
├── Matrix.hpp
├── Benchmark.java
├── Matrix.java
├── benchmark.py
├── matrix.py
└── results/
    └── benchmark_results.csv
```
## Notes

- All tests are single-threaded for comparability.

- Each implementation uses double precision (64-bit) floating-point numbers.

- Real memory measurements may vary slightly depending on the operating system and runtime garbage collection behavior.

- The CSV file grows cumulatively with each run, allowing long-term comparative analysis.

## Interpretation Summary

| Language   | Execution Time | Memory Efficiency | CPU Utilization | Key Insight                               |
| ---------- | -------------- | ----------------- | --------------- | ----------------------------------------- |
| **C++**    | Fastest      | Moderate       | ~100%        | Excellent balance of speed and efficiency |
| **Java**   | Near C++     | Best           | Low (≈15%)   | Managed runtime, high memory efficiency   |
| **Python** | Slowest     | Highest        | ~100%        | Interpreter overhead dominates runtime    |



