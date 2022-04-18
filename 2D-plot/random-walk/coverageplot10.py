import matplotlib.pyplot as plt
import numpy as np

c1 = np.genfromtxt("c1.csv", delimiter=",")
t1 = np.genfromtxt("t1.csv", delimiter=",")

c2 = np.genfromtxt("c2.csv", delimiter=",")
t2 = np.genfromtxt("t2.csv", delimiter=",")

c3 = np.genfromtxt("c3.csv", delimiter=",")
t3 = np.genfromtxt("t3.csv", delimiter=",")

c4 = np.genfromtxt("c4.csv", delimiter=",")
t4 = np.genfromtxt("t4.csv", delimiter=",")

c5 = np.genfromtxt("c5.csv", delimiter=",")
t5 = np.genfromtxt("t5.csv", delimiter=",")

c6 = np.genfromtxt("c6.csv", delimiter=",")
t6 = np.genfromtxt("t6.csv", delimiter=",")

c7 = np.genfromtxt("c7.csv", delimiter=",")
t7 = np.genfromtxt("t7.csv", delimiter=",")

c8 = np.genfromtxt("c8.csv", delimiter=",")
t8 = np.genfromtxt("t8.csv", delimiter=",")

c9 = np.genfromtxt("c9.csv", delimiter=",")
t9 = np.genfromtxt("t9.csv", delimiter=",")

c10 = np.genfromtxt("c10.csv", delimiter=",")
t10 = np.genfromtxt("t10.csv", delimiter=",")

x = []
y = []
def compute_mean():
    for i in range(198):
        mean_time = (t1[i] + t2[i] + t3[i] + t4[i] + t5[i] + t6[i] + t7[i] + t8[i] + t9[i] + t10[i]) / 10
        x.append(mean_time)
        mean_cov = (c1[i] + c2[i] + c3[i] + c4[i] + c5[i] + c6[i] + c7[i] + c8[i] + c9[i] + c10[i]) / 10
        y.append(mean_cov)
    x.append(t1[len(t1)-1])
    y.append(c1[len(c1)-1])

plt.scatter(t1, c1, s=1, c='c')
plt.scatter(t2, c2, s=1, c='c')
plt.scatter(t3, c3, s=1, c='c')
plt.scatter(t4, c4, s=1, c='c')
plt.scatter(t5, c5, s=1, c='c')
plt.scatter(t6, c6, s=1, c='c')
plt.scatter(t7, c7, s=1, c='c')
plt.scatter(t8, c8, s=1, c='c')
plt.scatter(t9, c9, s=1, c='c')
plt.scatter(t10, c10, s=1, c='c')

compute_mean()
plt.plot(x, y, linewidth=2, color='m')

plt.xlabel("Time in minutes")
plt.ylabel("Coverage in percent")
plt.grid()
plt.savefig("graph_10_runs.png")
plt.show()