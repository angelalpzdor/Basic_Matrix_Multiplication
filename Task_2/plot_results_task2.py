import pandas as pd
import matplotlib.pyplot as plt

# ---------------------------------------------------------------------
# Global plot style
# ---------------------------------------------------------------------
plt.style.use("seaborn-v0_8-whitegrid")
plt.rcParams["figure.figsize"] = (9, 5)
plt.rcParams["font.size"] = 12

# ---------------------------------------------------------------------
# Load CSV
# ---------------------------------------------------------------------
df = pd.read_csv("results/benchmark_task2_results.csv", header=None)
df.columns = ["lang", "algorithm", "n", "runs", "mean", "best", "worst"]

# ---------------------------------------------------------------------
# FIX: Convert n to int ONLY for dense methods
# ---------------------------------------------------------------------
dense_mask = df["algorithm"].isin(["basic", "cache", "tiled16", "tiled32", "tiled64"])
df.loc[dense_mask, "n"] = df.loc[dense_mask, "n"].astype(int)

# Extract dense methods
dense = df[dense_mask].sort_values("n")

# ---------------------------------------------------------------------
# 1. TIME VS N
# ---------------------------------------------------------------------
plt.figure()
for method in ["basic", "cache", "tiled16", "tiled32", "tiled64"]:
    sub = dense[dense["algorithm"] == method]
    if not sub.empty:
        plt.plot(sub["n"], sub["mean"], marker="o", linewidth=2, label=method)

plt.xlabel("Matrix size (n)")
plt.ylabel("Mean execution time (s)")
plt.title("Dense Matrix Multiplication – Time vs n")
plt.legend()
plt.tight_layout()
plt.savefig("results/dense_time_vs_n.png")
plt.close()

# ---------------------------------------------------------------------
# 2. SPEEDUP VS N
# ---------------------------------------------------------------------
basic_times = dense[dense["algorithm"] == "basic"].groupby("n")["mean"].mean()
sorted_ns = sorted(basic_times.index)

plt.figure()
for method in ["cache", "tiled16", "tiled32", "tiled64"]:
    sub = dense[dense["algorithm"] == method].groupby("n")["mean"].mean()
    common = [n for n in sorted_ns if n in sub.index]
    if len(common) > 0:
        speedup = [basic_times.loc[n] / sub.loc[n] for n in common]
        plt.plot(common, speedup, marker="o", linewidth=2, label=method)

plt.xlabel("Matrix size (n)")
plt.ylabel("Speedup vs basic")
plt.title("Speedup Comparison")
plt.legend()
plt.tight_layout()
plt.savefig("results/dense_speedup_vs_n.png")
plt.close()

# ---------------------------------------------------------------------
# 3. DENSE VS SPARSE
# ---------------------------------------------------------------------
dense_mean = dense["mean"].mean()
sparse = df[df["algorithm"] == "sparseCSR"]

if not sparse.empty:
    sparse_mean = sparse["mean"].mean()
    labels = ["Dense avg", "Sparse CSR"]
    values = [dense_mean, sparse_mean]

    plt.figure()
    plt.yscale("log")
    plt.plot(labels, values, marker="o", linewidth=2)
    for l, v in zip(labels, values):
        plt.text(l, v, f"{v:.2e}", ha="center", va="bottom")

    plt.ylabel("Execution time (log scale)")
    plt.title("Dense vs Sparse (CSR)")
    plt.tight_layout()
    plt.savefig("results/dense_vs_sparse.png")
    plt.close()

print("All plots successfully generated!")
