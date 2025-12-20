import numpy as np
import time
import csv
import os
import tracemalloc
from multiprocessing import Pool, cpu_count

# --- Configuration ---
# Sizes to test. We include a larger one (800) to better see the distributed advantage
SIZES = [200, 400, 600, 800]
OUTPUT_FILE = 'results/benchmark_comparison.csv'

def generate_matrix(size):
    """Generates a random NxN matrix."""
    return np.random.rand(size, size)

# --- 1. Serial Implementation (Basic) ---
def serial_multiply(matrix_a, matrix_b):
    """
    Standard matrix multiplication running on a single core.
    """
    return np.dot(matrix_a, matrix_b)

# --- 2. Distributed Implementation (Parallel) ---
def worker_task(args):
    """
    Helper function for the worker node: multiplies a row by the matrix.
    """
    row_a, matrix_b = args
    return np.dot(row_a, matrix_b)

def distributed_multiply(matrix_a, matrix_b, pool):
    """
    Distributes rows of Matrix A across available CPU cores.
    """
    size = matrix_a.shape[0]
    # Map: Create tasks (Row A, Matrix B)
    tasks = [(matrix_a[i], matrix_b) for i in range(size)]
    # Reduce: Collect results
    result = pool.map(worker_task, tasks)
    return np.array(result)

# --- Benchmark Runner ---
def run_benchmark():
    # Ensure results directory exists
    os.makedirs('results', exist_ok=True)
    
    # Initialize CSV
    with open(OUTPUT_FILE, 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['Size', 'Type', 'Time_ms', 'Memory_MB'])

    print(f"=== Starting Comparison Benchmark (Cores: {cpu_count()}) ===")
    
    # Prepare the pool for distributed tests
    pool = Pool(processes=cpu_count())

    for size in SIZES:
        print(f"\n--- Testing Size: {size}x{size} ---")
        
        # Generate Data
        A = generate_matrix(size)
        B = generate_matrix(size)

        # 1. Test Serial (Basic)
        tracemalloc.start()
        start_seq = time.time()
        _ = serial_multiply(A, B)
        end_seq = time.time()
        _, peak_seq = tracemalloc.get_traced_memory()
        tracemalloc.stop()
        
        time_seq = (end_seq - start_seq) * 1000
        mem_seq = peak_seq / (1024 * 1024)
        print(f" [Serial]      Time: {time_seq:.2f} ms | RAM: {mem_seq:.2f} MB")

        # 2. Test Distributed
        tracemalloc.start()
        start_dist = time.time()
        _ = distributed_multiply(A, B, pool)
        end_dist = time.time()
        _, peak_dist = tracemalloc.get_traced_memory()
        tracemalloc.stop()
        
        time_dist = (end_dist - start_dist) * 1000
        mem_dist = peak_dist / (1024 * 1024)
        print(f" [Distributed] Time: {time_dist:.2f} ms | RAM: {mem_dist:.2f} MB")

        # Save both results
        with open(OUTPUT_FILE, 'a', newline='') as f:
            writer = csv.writer(f)
            writer.writerow([size, 'Serial', time_seq, mem_seq])
            writer.writerow([size, 'Distributed', time_dist, mem_dist])

    pool.close()
    pool.join()
    print(f"\n=== Benchmark Finished. Results saved to {OUTPUT_FILE} ===")

if __name__ == "__main__":
    run_benchmark()