import matplotlib.pyplot as plt
import csv
import os

csv_path = 'results/performance_python.csv'
image_path = 'results/python_report_charts.png'

def plot_results():
    sizes = []
    times = []
    memories = []

    if not os.path.exists(csv_path):
        print(f"❌ Error: Not found {csv_path}")
        return

    try:
        with open(csv_path, 'r') as f:
            reader = csv.reader(f)
            next(reader) # Skip header
            for row in reader:
                if row:
                    sizes.append(int(row[0]))
                    times.append(float(row[1]))
                    memories.append(float(row[2]))

        # Create 2 subplots
        fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 6))

        # Plot 1: Time
        ax1.plot(sizes, times, 'o-', color='green', linewidth=2)
        ax1.set_title('Python Performance (Time)')
        ax1.set_xlabel('Size (N)')
        ax1.set_ylabel('Time (ms)')
        ax1.grid(True, linestyle='--')

        # Plot 2: Memory
        ax2.plot(sizes, memories, 's-', color='purple', linewidth=2)
        ax2.set_title('Python Memory Usage')
        ax2.set_xlabel('Size (N)')
        ax2.set_ylabel('Memory (MB)')
        ax2.grid(True, linestyle='--')

        plt.tight_layout()
        plt.savefig(image_path)
        print(f"✅ Python charts saved to: {image_path}")

    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    plot_results()