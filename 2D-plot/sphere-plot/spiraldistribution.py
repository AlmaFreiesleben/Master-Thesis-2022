import matplotlib.pyplot as plt
import math

xs = []
ys = []
zs = []
N = 600
s = 3.6/math.sqrt(N)
dz = 2.0/N
longtitude = 0
z = 1 - dz/2

for i in range(N-1):
    r = math.sqrt(1-z*z)

    x = math.cos(longtitude) * r
    y = math.sin(longtitude) * r

    xs.append(x*2.5)
    ys.append(y*2.5)
    zs.append(z*2.5)

    z = z - dz
    longtitude = longtitude + s/r

fig = plt.figure()
ax = fig.add_subplot(projection='3d')
ax.scatter(xs, ys, zs)
plt.show()