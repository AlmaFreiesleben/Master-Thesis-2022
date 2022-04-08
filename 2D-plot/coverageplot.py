import matplotlib.pyplot as plt
import numpy as np

coverage = np.genfromtxt("coverage_percentage.csv", delimiter=",")
time = np.genfromtxt("time.csv", delimiter=",")

plt.plot(time, coverage)
plt.xlabel("Time in minutes")
plt.ylabel("Coverage in percent")
plt.grid()
plt.savefig("graph.png")
plt.show()