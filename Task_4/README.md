# Task 4: Distributed Matrix Multiplication & Scalability Analysis

## Project Overview
This project implements a Distributed Matrix Multiplication system designed to process large datasets efficiently. The solution is implemented in two languages to demonstrate different parallel computing paradigms:
1.  **Java (Hazelcast):** Simulates a distributed cluster using `IExecutorService` and in-memory data grids.
2.  **Python (Multiprocessing):** Utilizes local CPU cores via `multiprocessing.Pool` to simulate MapReduce behavior.

The project analyzes **Scalability** (Execution Time vs. Input Size) and **Resource Utilization** (Memory Usage) to evaluate the performance of distributed algorithms against computational complexity ($O(N^3)$).

---

## Project Structure

```bash
Task_4/
├── java/
│   ├── lib/
│   │   └── hazelcast-5.3.6.jar       # Core dependency for distributed execution
│   ├── src/
│   │   ├── DistributedMatrixMultiplication.java  # Main entry point (Driver)
│   │   ├── RowBlockTask.java                     # Distributed task (Worker logic)
│   │   └── MatrixUtils.java                      # Helper for generation & file I/O
│   ├── results/
│   │   ├── performance.csv           # Raw benchmark data
│   │   └── final_report_charts.png   # Generated Scalability & Memory plots
│   └── plot_results_task4_java.py    # Python script to visualize Java results
│
├── python/
│   ├── distributed_mm.py             # Main distributed implementation (MapReduce style)
│   ├── benchmark_task4_py.py         # Comparison: Serial vs. Distributed
│   ├── plot_results_task4_py.py      # Script to visualize Python results
│   └── results/
│       ├── performance_python.csv    # Raw benchmark data
│       └── python_report_charts.png  # Generated Scalability & Memory plots
│
└── README.md                         # Project Documentation
```

Prerequisites
For Java Implementation
Java Development Kit (JDK): Version 8 or higher.

Hazelcast: Version 5.3.6 (Included in lib/).

RAM: Minimum 4GB free (required for large matrix sizes like 600x600+).

For Python Implementation
Python: Version 3.8+.

Libraries:
 
```Bash 
pip install numpy matplotlib
```

## Part 1: Java Implementation (Hazelcast)
This implementation uses Hazelcast to create a cluster of nodes. The matrix multiplication is split into tasks using a Row-Partitioning Strategy:
- Map Phase: Matrix $A$ is split into rows.
- Process: Each row is sent to a worker node along with Matrix $B$.
- Reduce Phase: The main node collects calculated rows to form Matrix $C$.How to RunNavigate to the Java directory:

### How to Run
Navigate to the Java directory
```Bash 
cd java
```
Compile the code (linking the Hazelcast JAR):
```Bash 
javac -cp ".:lib/hazelcast-5.3.6.jar" src/*.java -d bin
```
Execute the Driver (with increased Heap Memory): Note: The -Xmx4G flag is crucial to prevent OutOfMemoryError during large matrix operations.

```Bash

java -Xms2G -Xmx4G -cp "bin:lib/hazelcast-5.3.6.jar" DistributedMatrixMultiplication
```
Visualize Results: Once execution is finished, generate the charts:

``` Bash
python3 plot_results_task4_java.py
```
Output: Check java/results/final_report_charts.png

## Part 2: Python Implementation (Multiprocessing)
This version utilizes Python's multiprocessing library to bypass the GIL (Global Interpreter Lock) and utilize all available CPU cores.
### How to run
Navigate to the Python directory:
``` Bash
cd python
```
Run the Distributed Simulation: This script measures Time and Peak Memory usage.
``` Bash
python3 distributed_mm.py
```
Run the Benchmark Comparison (Serial vs. Distributed): 

``` Bash
python3 benchmark_task4_py.py
```

Visualize Results:
``` Bash
python3 plot_results_task4_py.py
```
Output: Check python/results/python_report_charts.png.
