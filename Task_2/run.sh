#!/bin/bash

# Always use the directory where run.sh is located
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR" || exit 1

echo "==> Creating results directory..."
mkdir -p results

JAVA_BIN="/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/bin"

echo "==> Compiling Java files..."
$JAVA_BIN/javac *.java
if [ $? -ne 0 ]; then
    echo "[ERROR] Java compilation failed."
    exit 1
fi

echo "==> Running Java benchmark..."
$JAVA_BIN/java BenchmarkTask2

# CSV is already stored inside results/, so we do NOT move it.
echo "CSV stored in results/benchmark_task2_results.csv"

echo "==> Running Python plotting script..."
python3 plot_results_task2.py "results/benchmark_task2_results.csv"

if [ $? -ne 0 ]; then
    echo "[ERROR] Python plotting failed."
    exit 1
fi

echo "==> All tasks completed successfully!"
