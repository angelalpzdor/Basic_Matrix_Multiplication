# Task 2 – Performance Benchmark of Matrix Multiplication

This project implements and benchmarks several dense and sparse matrix multiplication
methods in Java. It includes automated execution, CSV result generation, and Python
plotting scripts to visualize performance differences across algorithms and matrix sizes.

---

##  Project Structure

```text
Task_2/
│
├── BenchmarkTask2.java
├── CSRMatrix.java
├── OptimizedMatrix.java
├── MatrixMarketReader.java
│
├── run.sh
├── plot_results_task2.py
│
├── data/
│ └── mc2depi.mtx
│
└── results/
├── benchmark_task2_results.csv
├── dense_time_vs_n.png
├── dense_speedup_vs_n.png
└── dense_vs_sparse.png
```

---

##  Overview

### Dense multiplication methods implemented:
- **Baseline (ijk)**  
- **Cache-friendly variant**  
- **Tiled multiplication** with block sizes:  
  - 16 × 16  
  - 32 × 32  
  - 64 × 64  

### Sparse multiplication:
- Implemented using **CSR (Compressed Sparse Row)**.
- Benchmarked with the real-world sparse dataset:  
  **`mc2depi.mtx`** (Matrix Market format).

### Output:
- CSV results saved in `results/benchmark_task2_results.csv`.
- Summary plots generated automatically into `results/`.

---

##  Requirements

### Java
- JDK 17 or newer (OpenJDK recommended)

### Python (optional, only for plots)
- Python 3.8+
- Required packages:
    - numpy
    - pandas
    - matplotlib

Install them with:

```bash
pip install numpy pandas matplotlib
```



## How to Run

### From the root directory of the project (Task_2/):

1. Make the script executable
```bash
chmod +x run.sh
```

2. Run the entire pipeline
```bash
./run.sh
```

### The script will automatically:

- Create the results/ directory (if missing)

- Compile all .java files

- Run all dense and sparse benchmarks

- Generate:

    - results/benchmark_task2_results.csv

    - performance plots inside results/

## Generating Plots Manually (Optional)

If you want to regenerate plots without running benchmarks again:
```bash
python3 plot_results_task2.py
```

This will:

- Read results/benchmark_task2_results.csv

- Create all figures inside results/

## Dataset

### The project requires the file:
```bash
data/mc2depi.mtx
```

This is a large real-world sparse matrix (~2.1M lines) used to evaluate CSR performance.

If missing, download it from the Matrix Market repository:
https://sparse.tamu.edu/Williams/mc2depi

## Output Files

After running the project, you will obtain:

### CSV:

- results/benchmark_task2_results.csv

### Plots:

- dense_time_vs_n.png

- dense_speedup_vs_n.png

- dense_vs_sparse.png

### These figures are used in the LaTeX report.

