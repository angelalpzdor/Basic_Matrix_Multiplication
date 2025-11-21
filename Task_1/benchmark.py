import time
import statistics
import csv
import os
import psutil
from matrix import multiplicar_matrices as multiply_matrices

def estimate_memory_mb(n):
    return 3 * n * n * 8 / (1024 * 1024)

def benchmark(n, runs):
    times = []
    process = psutil.Process(os.getpid())

    for i in range(runs):
        memory_before = process.memory_info().rss / (1024 * 1024)
        start = time.perf_counter()
        multiply_matrices(n)
        end = time.perf_counter()
        memory_after = process.memory_info().rss / (1024 * 1024)
        duration = end - start
        used_memory = memory_after - memory_before
        times.append((duration, used_memory))
        print(f"Running {i+1}/{runs}: {duration:.4f} seconds, Memory used: {used_memory:.2f} MB")

    durations = [t[0] for t in times]
    memory_usages = [t[1] for t in times]

    mean = statistics.mean(durations)
    stdev = statistics.stdev(durations)
    minimum = min(durations)
    maximum = max(durations)
    real_memory = statistics.mean(memory_usages)
    theoretical_memory = estimate_memory_mb(n)

    print("\nResults:")
    print(f"Matrix size: {n}x{n}")
    print(f"Number of runs: {runs}")
    print(f"Average time: {mean:.4f} s")
    print(f"Standard deviation: {stdev:.4f} s")
    print(f"Best time: {minimum:.4f} s")
    print(f"Worst time: {maximum:.4f} s")
    print(f"Average real memory used: {real_memory:.2f} MB")
    print(f"Theoretical memory usage: {theoretical_memory:.2f} MB")

    save_result("python", n, runs, mean, stdev, minimum, maximum, real_memory, theoretical_memory)

def save_result(language, n, runs, mean, stdev, minimum, maximum, real_memory, theoretical_memory):
    folder = "results"
    os.makedirs(folder, exist_ok=True)
    file = os.path.join(folder, "benchmark_results.csv")
    exists = os.path.isfile(file)

    with open(file, "a", newline="") as f:
        writer = csv.writer(f)
        if not exists:
            writer.writerow([
                "language", "size", "runs", "mean (s)", "stdev",
                "best", "worst", "real memory (MB)", "theoretical memory (MB)"
            ])
        writer.writerow([
            language, n, runs,
            f"{mean:.4f}", f"{stdev:.4f}", f"{minimum:.4f}", f"{maximum:.4f}",
            f"{real_memory:.2f}", f"{theoretical_memory:.2f}"
        ])

if __name__ == "__main__":
    n = int(input("Enter matrix size: "))
    runs = int(input("Enter number of runs: "))
    benchmark(n, runs)
