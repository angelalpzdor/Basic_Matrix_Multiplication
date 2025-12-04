import pandas as pd
import matplotlib.pyplot as plt

# Load CSV
df = pd.read_csv("results/task3_results.csv")

# Filter methods
baseline = df[df["method"] == "baseline"]
parallel_threads = df[df["method"] == "parallel_threads"]
parallel_streams = df[df["method"] == "parallel_streams"]
vectorized = df[df["method"] == "vectorized"]

# -------- Execution Time --------
plt.figure(figsize=(10, 6))

plt.plot(baseline["size"], baseline["time_ms"], label="Baseline")
plt.plot(parallel_streams["size"], parallel_streams["time_ms"], label="Parallel Streams")
plt.plot(vectorized["size"], vectorized["time_ms"], label="Vectorized")

for th in parallel_threads["threads"].unique():
    subset = parallel_threads[parallel_threads["threads"] == th]
    plt.plot(subset["size"], subset["time_ms"], label=f"ThreadPool {th} threads")

plt.xlabel("Matrix size (n x n)")
plt.ylabel("Time (ms)")
plt.title("Execution Time")
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.savefig("results/time_plot.png")
plt.close()

# -------- Speedup --------
plt.figure(figsize=(10, 6))

plt.plot(baseline["size"], baseline["speedup"], label="Baseline")
plt.plot(parallel_streams["size"], parallel_streams["speedup"], label="Parallel Streams")
plt.plot(vectorized["size"], vectorized["speedup"], label="Vectorized")

for th in parallel_threads["threads"].unique():
    subset = parallel_threads[parallel_threads["threads"] == th]
    plt.plot(subset["size"], subset["speedup"], label=f"ThreadPool {th} threads")

plt.xlabel("Matrix size (n x n)")
plt.ylabel("Speedup")
plt.title("Speedup Comparison")
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.savefig("results/speedup_plot.png")
plt.close()

# -------- Efficiency --------
plt.figure(figsize=(10, 6))

for th in parallel_threads["threads"].unique():
    subset = parallel_threads[parallel_threads["threads"] == th]
    plt.plot(subset["size"], subset["efficiency"], label=f"{th} threads")

plt.xlabel("Matrix size (n x n)")
plt.ylabel("Efficiency")
plt.title("Parallel Efficiency")
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.savefig("results/efficiency_plot.png")
plt.close()

print("Plots saved in Task_3/results/")
