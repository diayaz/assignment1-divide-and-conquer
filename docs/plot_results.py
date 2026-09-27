# Builds plots from results/results.csv
# Run from the project root: python docs/plot_results.py
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("results/results.csv")
rnd = df[df["input"] == "random"]
algorithms = ["MergeSort", "QuickSort", "Select", "ClosestPair"]

# Plot 1: time vs n
plt.figure(figsize=(8, 5))
for alg in algorithms:
    d = rnd[rnd["algorithm"] == alg]
    plt.plot(d["n"], d["time_ms"], marker="o", label=alg)
plt.xscale("log")
plt.yscale("log")
plt.xlabel("n")
plt.ylabel("time (ms)")
plt.title("Time vs n (random input)")
plt.grid(True, alpha=0.3)
plt.legend()
plt.savefig("docs/plots/time_vs_n.png", dpi=120)

# Plot 2: recursion depth vs n
plt.figure(figsize=(8, 5))
for alg in algorithms:
    d = rnd[rnd["algorithm"] == alg]
    plt.plot(d["n"], d["max_depth"], marker="o", label=alg)
plt.xscale("log")
plt.xlabel("n")
plt.ylabel("max recursion depth")
plt.title("Recursion depth vs n (random input)")
plt.grid(True, alpha=0.3)
plt.legend()
plt.savefig("docs/plots/depth_vs_n.png", dpi=120)

print("Plots saved to docs/plots/")
