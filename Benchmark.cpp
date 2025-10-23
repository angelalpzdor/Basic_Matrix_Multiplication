#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <chrono>
#include <algorithm>
#include <filesystem>
#include <sys/resource.h>
#include "Matrix.hpp"

double estimate_memory_MB(int n) {
    return 3.0 * n * n * 8 / (1024.0 * 1024.0);
}

double get_memory_MB() {
    struct rusage usage;
    getrusage(RUSAGE_SELF, &usage);
    return usage.ru_maxrss / 1024.0;
}

double mean(const std::vector<double>& v) {
    double sum = 0.0;
    for (double x : v) sum += x;
    return sum / v.size();
}

double stdev(const std::vector<double>& v, double m) {
    if (v.size() <= 1) return 0.0;
    double sum = 0.0;
    for (double x : v) sum += std::pow(x - m, 2);
    return std::sqrt(sum / (v.size() - 1));
}

int main() {
    int n, runs;
    std::cout << "Enter matrix size: ";
    std::cin >> n;
    std::cout << "Enter number of runs: ";
    std::cin >> runs;

    std::vector<double> times;
    std::vector<double> memory_usages;

    for (int i = 0; i < runs; i++) {
        double memory_before = get_memory_MB();
        auto start = std::chrono::high_resolution_clock::now();
        multiply_matrices(n);
        auto end = std::chrono::high_resolution_clock::now();
        double memory_after = get_memory_MB();

        std::chrono::duration<double> duration = end - start;
        double used_memory = std::max(0.0, memory_after - memory_before);

        times.push_back(duration.count());
        memory_usages.push_back(used_memory);

        std::cout << "Running " << (i + 1) << "/" << runs
                  << ": " << duration.count()
                  << " seconds, Memory used: " << used_memory << " MB\n";
    }

    double mean_time = mean(times);
    double sd_time = stdev(times, mean_time);
    double min_time = *std::min_element(times.begin(), times.end());
    double max_time = *std::max_element(times.begin(), times.end());
    double real_memory = mean(memory_usages);
    double theoretical_memory = estimate_memory_MB(n);

    std::cout << "\nResults:\n";
    std::cout << "Matrix size: " << n << "x" << n << "\n";
    std::cout << "Number of runs: " << runs << "\n";
    std::cout << "Average time: " << mean_time << " s\n";
    std::cout << "Standard deviation: " << sd_time << " s\n";
    std::cout << "Best time: " << min_time << " s\n";
    std::cout << "Worst time: " << max_time << " s\n";
    std::cout << "Average real memory used: " << real_memory << " MB\n";
    std::cout << "Theoretical memory usage: " << theoretical_memory << " MB\n";

    std::filesystem::create_directory("results");
    std::ofstream file("results/benchmark_results.csv", std::ios::app);
    if (file.tellp() == 0)
        file << "language,size,runs,mean (s),stdev,best,worst,real memory (MB),theoretical memory (MB)\n";

    file << "cpp," << n << "," << runs << ","
         << mean_time << "," << sd_time << ","
         << min_time << "," << max_time << ","
         << real_memory << "," << theoretical_memory << "\n";

    file.close();


    return 0;
}
