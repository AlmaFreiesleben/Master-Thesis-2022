import matplotlib.pyplot as plt
import numpy as np

c1 = np.genfromtxt("c1.csv", delimiter=",")
t1 = np.genfromtxt("t1.csv", delimiter=",")

c2 = np.genfromtxt("c2.csv", delimiter=",")
t2 = np.genfromtxt("t2.csv", delimiter=",")

x = []
y = []
def compute_mean():
    for i in range(134):
        mean_time = (t1[i] + t2[i]) / 2
        x.append(mean_time)
        mean_cov = (c1[i] + c2[i]) / 2
        y.append(mean_cov)
    x.append(t1[len(t1)-1])
    y.append(c1[len(c1)-1])

plt.scatter(t1, c1, s=1, c='c')
plt.scatter(t2, c2, s=1, c='c')

compute_mean()
plt.plot(x, y, linewidth=2, color='m')

plt.xlabel("Time in minutes")
plt.ylabel("Coverage in percent")
plt.grid()
plt.savefig("graph_zigzag_2_runs.png")
plt.show()