import matplotlib.pyplot as plt
import csv
import os

# Configuration paths
csv_path = 'results/performance.csv'
image_path = 'results/final_report_charts.png'

def plot_results():
    """
    Reads the benchmark CSV and generates plots for Time and Memory usage.
    """
    sizes = []
    times = []
    memories = []

    # Check if data file exists
    # Note: The Java program saves the CSV in the 'results' folder relative to where it ran.
    # Adjust path if running python script from a different directory.
    target_csv = csv_path
    if not os.path.exists(target_csv):
        # Try looking one folder up/down just in case
        if os.path.exists(f"../java/{csv_path}"):
            target_csv = f"../java/{csv_path}"
        else:
            print(f"Error: Could not find {csv_path}")
            return

    try:
        with open(target_csv, 'r') as f:
            reader = csv.reader(f)
            for row in reader:
                # Expecting format: Size, Time, Memory
                if row and len(row) >= 3:
                    sizes.append(int(row[0]))
                    times.append(float(row[1]))
                    memories.append(float(row[2]))

        if not sizes:
            print(" No data found in CSV.")
            return

        # Create a figure with 2 subplots (Time and Memory)
        fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 6))

        # Plot 1: Scalability (Time)
        ax1.plot(sizes, times, marker='o', linestyle='-', color='#1f77b4', linewidth=2)
        ax1.set_title('Scalability Analysis (Time vs Size)', fontsize=12)
        ax1.set_xlabel('Matrix Dimension (NxN)')
        ax1.set_ylabel('Execution Time (ms)')
        ax1.grid(True, linestyle='--', alpha=0.7)

        # Plot 2: Resource Utilization (Memory)
        ax2.plot(sizes, memories, marker='s', linestyle='-', color='#ff7f0e', linewidth=2)
        ax2.set_title('Resource Utilization (Memory vs Size)', fontsize=12)
        ax2.set_xlabel('Matrix Dimension (NxN)')
        ax2.set_ylabel('Memory Usage (MB)')
        ax2.grid(True, linestyle='--', alpha=0.7)

        plt.suptitle('Distributed Matrix Multiplication Report', fontsize=16)
        plt.tight_layout()
        
        # Save output
        plt.savefig(image_path)
        print(f" Success! Charts saved to: {image_path}")

    except Exception as e:
        print(f"Error generating plots: {e}")

if __name__ == "__main__":
    plot_results()