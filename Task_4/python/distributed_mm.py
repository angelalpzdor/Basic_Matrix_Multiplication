import numpy as np
import time
import os
import csv
import tracemalloc
from multiprocessing import Pool, cpu_count

# --- Configuration ---
SIZES = [200, 400, 600]

# Use absolute paths to guarantee the file is found
CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
RESULTS_DIR = os.path.join(CURRENT_DIR, "results")
RESULTS_FILE = os.path.join(RESULTS_DIR, "performance_python.csv")

def generate_matrix(rows, cols):
    """Generates a matrix filled with random numbers."""
    return np.random.rand(rows, cols)

def worker_task(args):
    """
    Worker function executed by each core.
    Computes the dot product of a single row against the full matrix.
    """
    row_a, matrix_b = args
    return np.dot(row_a, matrix_b)

def run_distributed_execution():
    print(f"--- Starting Distributed Matrix Multiplication (Python) ---")
    print(f"📂 Working Directory: {CURRENT_DIR}")
    
    # 1. Create results directory if it doesn't exist
    if not os.path.exists(RESULTS_DIR):
        try:
            os.makedirs(RESULTS_DIR)
            print(f"✅ Directory created: {RESULTS_DIR}")
        except Exception as e:
            print(f"❌ Error creating directory: {e}")
            return

    # 2. Initialize empty CSV file with headers (Test write permissions)
    try:
        with open(RESULTS_FILE, 'w', newline='') as f:
            writer = csv.writer(f)
            writer.writerow(["Size", "Time_ms", "Memory_MB"])
        print(f"✅ CSV initialized at: {RESULTS_FILE}")
    except Exception as e:
        print(f"❌ Error: Cannot write to CSV file. {e}")
        return

    print(f"⚡ CPU Cores detected: {cpu_count()}")

    # 3. Execute Benchmarks
    for size in SIZES:
        print(f"\nProcessing matrix size: {size}x{size}...")
        
        # Start memory tracking
        tracemalloc.start()
        try:
            # Data Generation
            matrix_a = generate_matrix(size, size)
            matrix_b = generate_matrix(size, size)
            
            start_time = time.time()

            # Partitioning (Map Phase)
            # Create tasks: Each task is (Row of A, Entire Matrix B)
            tasks = [(matrix_a[i], matrix_b) for i in range(size)]

            # Distributed Execution (Simulated using Multiprocessing Pool)
            with Pool(processes=cpu_count()) as pool:
                result = pool.map(worker_task, tasks)

            # Aggregation (Reduce Phase)
            result_matrix = np.array(result)

            end_time = time.time()
            
            # Get memory usage statistics
            current, peak = tracemalloc.get_traced_memory()
            tracemalloc.stop()

            duration_ms = (end_time - start_time) * 1000
            peak_mb = peak / (1024 * 1024)

            print(f" -> Finished in {duration_ms:.2f} ms | Peak RAM: {peak_mb:.2f} MB")

            # Save metrics to CSV
            with open(RESULTS_FILE, 'a', newline='') as f:
                writer = csv.writer(f)
                writer.writerow([size, duration_ms, peak_mb])

        except Exception as e:
            print(f"❌ Error processing size {size}: {e}")
            tracemalloc.stop()

    print(f"\n=== SUCCESS! All results saved to: ===")
    print(RESULTS_FILE)

if __name__ == "__main__":
    # Standard entry point protection for multiprocessing on macOS/Windows
    run_distributed_execution()