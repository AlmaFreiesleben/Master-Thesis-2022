import matplotlib.pyplot as plt
import numpy as np

mono_font = {'fontname':'monospace'}

cc1 = np.genfromtxt("c1.csv", delimiter=",")
tt1 = np.genfromtxt("t1.csv", delimiter=",")
zero_array_1 = np.zeros(cc1.shape, dtype=cc1.dtype)
c1 = np.maximum(cc1, zero_array_1)
t1 = np.maximum(tt1, zero_array_1)

cc2 = np.genfromtxt("c2.csv", delimiter=",")
tt2 = np.genfromtxt("t2.csv", delimiter=",")
zero_array_2 = np.zeros(cc2.shape, dtype=cc2.dtype)
c2 = np.maximum(cc2, zero_array_2)
t2 = np.maximum(tt2, zero_array_2)

cc3 = np.genfromtxt("c3.csv", delimiter=",")
tt3 = np.genfromtxt("t3.csv", delimiter=",")
zero_array_3 = np.zeros(cc3.shape, dtype=cc3.dtype)
c3 = np.maximum(cc3, zero_array_3)
t3 = np.maximum(tt3, zero_array_3)

cc4 = np.genfromtxt("c4.csv", delimiter=",")
tt4 = np.genfromtxt("t4.csv", delimiter=",")
zero_array_4 = np.zeros(cc4.shape, dtype=cc4.dtype)
c4 = np.maximum(cc4, zero_array_4)
t4 = np.maximum(tt4, zero_array_4)

cc5 = np.genfromtxt("c5.csv", delimiter=",")
tt5 = np.genfromtxt("t5.csv", delimiter=",")
zero_array_5 = np.zeros(cc5.shape, dtype=cc5.dtype)
c5 = np.maximum(cc5, zero_array_5)
t5 = np.maximum(tt5, zero_array_5)

x = []
y = []
def compute_mean():
    for i in range(330):
        mean_time = (t1[i] + t2[i] + t3[i] + t4[i] + t5[i]) / 5
        x.append(mean_time)
        mean_cov = (c1[i] + c2[i] + c3[i] + c4[i] + c5[i]) / 5
        y.append(mean_cov)
    x.append(t5[len(t5)-1])
    y.append(c5[len(c5)-1])

plt.scatter(t1, c1, s=1, c='hotpink')
plt.scatter(t2, c2, s=1, c='hotpink')
plt.scatter(t3, c3, s=1, c='hotpink')
plt.scatter(t4, c4, s=1, c='hotpink')
plt.scatter(t5, c5, s=1, c='hotpink')

compute_mean()
plt.plot(x, y, linewidth=2, color='darkslateblue')

plt.title('Wall Bumping Walk', **mono_font)
plt.xlabel("Time in minutes", **mono_font)
plt.ylabel("Coverage in percent", **mono_font)
plt.grid()
plt.savefig("graph_zigzag_8x8_5_runs.png")
plt.show()