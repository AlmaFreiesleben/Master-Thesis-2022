from math import pi, sin, cos, floor, degrees, atan2
from os import umask
import random
import matplotlib.pyplot as plt
import numpy as np

#### CONSTANTS ####

R = 2                               # distance between champers
W = 20                              # width of arena
H = 20                              # height of arena
is_left_fixed = 1                   # start with left chamber
world = np.full((W,H), False)       # create decomposition of world
world[10][10] = True                # left chamber has cleaned
world[12][10] = True                # right chamber has cleaned
n_dots = 12                         # chambers around fixed chamber

### ROBOT CHAMBER COORDS ###
current_left_x = 0
current_left_y = 0
current_right_x = 0
current_right_y = 2
x = 0
y = 0

left_x = [0]
left_y = [0]
right_x = [0]
right_y = [2]

def plot(is_first):
    plt.plot(left_x, left_y, "ro", markersize=12)
    plt.plot(right_x, right_y, "go", markersize=12)
    #plt.plot(current_left_x, current_left_y, "ro")
    #plt.plot(current_right_x, current_right_y, "go")
    plt.plot([W/2,-W/2,-W/2,W/2,W/2],[H/2,H/2,-H/2,-H/2,H/2], "b--")
    #plt.plot([20,0,0,20,20],[20,20,0,0,20], "b--")
    if (not is_first): plt.savefig("simulation.png")
    plt.show()

def plot_path():
    angs = np.linspace(0, 2*np.pi, n_dots)                   # angles to the dots
    if (is_left_fixed): cx, cy = (current_left_x, current_left_y)  # center of circle
    else: cx, cy = (current_right_x, current_right_y)
    xs, ys = [], []                                          # for coordinates of points to plot

    for ang in angs:                                         # compute (x,y) for each point
        x = cx + R * cos(ang)
        y = cy + R * sin(ang)
        xs.append(x)   
        ys.append(y)   

    plt.scatter(xs, ys, c = 'blue')

def collision_detection():
    return x > W/2 or y > H/2 or x < -(W/2) or y < -(H/2)
    #return x > W or y > H or x < 0 or y < 0

def robot_step():
    global current_right_x, current_right_y, current_left_x, current_left_y, is_left_fixed, x, y
    a = random.choice([pi/6, pi/3, pi/2, (2*pi)/3, (5*pi)/6, pi, (7*pi)/6, (4*pi)/3, (3*pi)/2, (5*pi)/3, (11*pi)/6, 2*pi])
    #dir = random.choice([0,1])
    #if (dir): a = -a

    if (is_left_fixed):   
        x = current_left_x + R * cos(a)
        y = current_left_y + R * sin(a)
    else:
        x = current_right_x + R * cos(a)
        y = current_right_y + R * sin(a)

def random_walk():
    global current_right_x, current_right_y, current_left_x, current_left_y, is_left_fixed, x, y
    
    robot_step()

    while (collision_detection()):
        robot_step()

    if (is_left_fixed):
        right_x.append(x)
        right_y.append(y)
        update_all_covered_points()
        current_right_x = x 
        current_right_y = y
        is_left_fixed = 0        
    else:
        left_x.append(x)
        left_y.append(y)
        update_all_covered_points()
        current_left_x = x
        current_left_y = y
        is_left_fixed = 1

def update_coverage():
    print("x: ", floor(x))
    print("y: ", floor(y))
    world_x = floor(x) + 10 % 19
    world_y = floor(y) + 10 % 19
    world[world_x, world_y] = True

def is_covered():
    return np.all(world)

def update_all_covered_points():
    if (is_left_fixed):
        a1 = get_angle((x,y), (current_left_x, current_left_y), (current_right_x, current_right_y))
        a2 = get_angle((current_right_x, current_right_y), (current_left_x, current_left_y), (current_left_x+1, current_left_y))
        angs = np.linspace(a2, a1, 10)
        for a in angs:
            xx = current_left_x + R * cos(a)
            yy = current_left_y + R * sin(a)
            world_x = floor(xx) + 10 % 19
            world_y = floor(yy) + 10 % 19
            if (world_x < 20 and world_y < 20): 
                world[world_x, world_y] = True
    else:
        a1 = get_angle((x,y), (current_right_x, current_right_y), (current_left_x, current_left_y))
        a2 = get_angle((current_left_x, current_left_y), (current_right_x, current_right_y), (current_right_x+1, current_right_y))
        angs = np.linspace(a2, a1, 20)
        for a in angs:
            xx = current_right_x + R * cos(a)
            yy = current_right_y + R * sin(a)
            world_x = floor(xx) + 10 % 19
            world_y = floor(yy) + 10 % 19
            if (world_x < 20 and world_y < 20): 
                world[world_x, world_y] = True

def get_angle(a, b, c):
    ang = degrees(atan2(c[1]-b[1], c[0]-b[0]) - atan2(a[1]-b[1], a[0]-b[0]))
    a = ang + 360 if ang < 0 else ang
    return a * pi / 180

def print_world():
    print("world:\n", world.astype(int)) 

def simulation():
    plot(True)

    while (not is_covered()):
    #for _ in range(10):
        random_walk()
        #plot(False)
    
    plot(False)
    print_world()   

simulation()