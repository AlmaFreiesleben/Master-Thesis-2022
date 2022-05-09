import matplotlib.pyplot as plt
from numpy import arange, pi, sin, cos, arccos, sqrt

xs = []
ys = []
zs = []
N = 600
r = 1
L = sqrt(N*pi)

for k in range(1,N):
    h_k = 1 - (2*k-1)/N
    phi_k = arccos(h_k)
    theta_k = L*phi_k

    xs.append(h_k*2.5)
    ys.append(phi_k*2.5)
    zs.append(theta_k*2.5)

fig = plt.figure()
ax = fig.add_subplot(projection='3d')
ax.scatter(xs, ys, zs)
plt.show()