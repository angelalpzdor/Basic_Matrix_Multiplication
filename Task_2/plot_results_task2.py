import pandas as pd
import matplotlib.pyplot as plt

plt.style.use("seaborn-v0_8-whitegrid")
plt.rcParams["figure.figsize"] = (9, 5)
plt.rcParams["font.size"] = 12

# ======================================================
# Load CSV containing all runs (dense + sparse)
# ======================================================
df = pd.read_csv("results/benchmark_task2_results.csv", header=None)
df.columns = ["lang", "algorithm", "n", "runs", "mean", "best", "worst"]

dense = df[df["algorithm"].isin(["basic", "cache", "tiled16", "tiled32", "tiled64"])]
dense = dense.sort_values("n")

# ======================================================
# 1. Time vs matrix size (ALL n)
# ======================================================
plt.figure()

for method in ["basic", "cache", "tiled16", "tiled32", "tiled64"]:
    sub = dense[dense["algorithm"] == method]
    if not sub.empty:
        plt.plot(sub["n"], sub["mean"], marker="o", linewidth=2, label=method)

plt.xlabel("Matrix size n")
plt.ylabel("Execution time (s)")
plt.title("Dense Matrix Multiplication – Time vs n")
plt.legend()
plt.tight_layout()
plt.savefig("results/dense_time_vs_n.png")
plt.close()

# ======================================================
# 2. Speedup vs basic (ALL n)
# ======================================================
basic_times = dense[dense["algorithm"] == "basic"].groupby("n")["mean"].mean()

plt.figure()
for method in ["cache", "tiled16", "tiled32", "tiled64"]:
    sub = dense[dense["algorithm"] == method].groupby("n")["mean"].mean()
    common = basic_times.index.intersection(sub.index)
    if len(common) > 0:
        speedup = basic_times.loc[common] / sub.loc[common]
        plt.plot(common, speedup, marker="o", linewidth=2, label=method)

plt.xlabel("Matrix size n")
plt.ylabel("Speedup vs basic")
plt.title("Speedup Comparison")
plt.legend()
plt.tight_layout()
plt.savefig("results/dense_speedup_vs_n.png")
plt.close()

# ======================================================
# 3. Dense vs sparse (global)
# ======================================================
dense_mean = dense["mean"].mean()

sparse = df[df["algorithm"] == "sparseCSR"]
if not sparse.empty:
    sparse_mean = sparse["mean"].mean()
    labels = ["Dense avg", "Sparse (CSR)"]
    values = [dense_mean, sparse_mean]

    plt.figure()
    plt.yscale("log")
    plt.plot(labels, values, marker="o", linewidth=2)
    for l, v in zip(labels, values):
        plt.text(l, v, f"{v:.2e}", ha="center", va="bottom")
    plt.ylabel("Execution time (log scale)")
    plt.title("Dense vs Sparse (Global Comparison)")
    plt.tight_layout()
    plt.savefig("results/dense_vs_sparse.png")
    plt.close()

print(" All plots generated in results/:")
print("   - dense_time_vs_n.png")
print("   - dense_speedup_vs_n.png")
print("   - dense_vs_sparse.png")
