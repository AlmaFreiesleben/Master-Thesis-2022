import matplotlib.pyplot as plt
from numpy import arange, pi, sin, cos, arccos, sqrt

xs = []
ys = []
zs = []
N = 700
phi = 0
s = 3.6/sqrt(N)

for k in range(1,N+1):
    h_k = -1 + ((2*(k-1))/(N-1))
    theta_k = arccos(h_k)
    t = sqrt(1-h_k*h_k)

    if (k == N or k == 1): phi_k = 0
    else: phi_k = (phi + s*t) % 2*pi

    xs.append(cos(theta_k)*2.5)
    ys.append(sin(phi_k)*2.5)
    zs.append(h_k*2.5)

    phi = phi_k

fig = plt.figure()
ax = fig.add_subplot(projection='3d')
ax.scatter(xs, ys, zs)
plt.show()