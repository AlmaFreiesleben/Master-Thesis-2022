import matplotlib.pyplot as plt
import math

xs = []
ys = []
zs = []
samples = 10
phi = math.pi * (3. - math.sqrt(5.))  # golden angle in radians

for i in range(samples):
    y = 1 - (i / float(samples - 1)) * 2  # y goes from 1 to -1
    radius = math.sqrt(1 - y * y)  # radius at y

    theta = phi * i  # golden angle increment

    x = math.cos(theta) * radius
    z = math.sin(theta) * radius

    xs.append(x*2.5)
    ys.append(y*2.5)
    zs.append(z*2.5)

fig = plt.figure()
ax = fig.add_subplot(projection='3d')
ax.scatter(xs, ys, zs)
plt.show()