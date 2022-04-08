import matplotlib.pyplot as plt
import numpy as np

coverage = np.genfromtxt("coverage_percentage.csv", delimiter=",")
time = np.genfromtxt("angle.csv", delimiter=",")

plt.plot(time, coverage)
plt.grid()
plt.savefig("graph.png")
plt.show()