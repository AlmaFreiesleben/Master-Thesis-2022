from pyparsing import col
import shapely
from shapely.geometry import LinearRing, LineString, Point
from numpy import sin, cos, pi, sqrt
import random
import numpy as np
import matplotlib.pyplot as plt

#### CONSTANTS ####

R = 1  # distance between champers in meters
W = 20  # width of arena
H = 20  # height of arena
is_left = 1 # start with left chamber

### VALID MOVES ###
chamber_actions = [2/pi, pi, 3*pi/2, 2*pi]

### ROBOT CHAMBER COORDS ###
current_left_x = [0]
current_left_y = [0]
current_right_x = [0]
current_right_y = [1]

left_x = [0]
left_y = [0]
right_x = [0]
right_y = [1]

def plot(is_first):
    plt.plot(left_x, left_y, marker="o", markersize=5, markeredgecolor="green", markerfacecolor="green")
    plt.plot(right_x, right_y, marker="o", markersize=5, markeredgecolor="blue", markerfacecolor="blue")
    plt.plot([W/2,-W/2,-W/2,W/2,W/2],[H/2,H/2,-H/2,-H/2,H/2], "b--")
    if (is_first): plt.savefig("simulation.png")
    plt.show()

def collision_detection(x, y):
    return x > W/2 or y > H/2 or x < -(W/2) or y < -(H/2)

def robot_step():
    global current_right_x, current_right_y, current_left_x, current_left_y, is_left
    #randomly move a chamber one of four legal actions
    action = random.choice([0,1,2,3])

    #choose direction of movement
    dir = random.choice([0,1])
    if (dir):
        a = chamber_actions[action]
    else:
        a = -chamber_actions[action]

    if (is_left):   
        x = current_right_x + R * cos(a)
        y = current_right_y + R * sin(a)
    else:
        x = current_left_x + R * cos(a)
        y = current_left_y + R * sin(a)

    return x, y

def random_walk():
    global current_right_x, current_right_y, current_left_x, current_left_y, is_left
    x = 0
    y = 0
    
    if (is_left):
        #compute x,y for right chamber
        x, y = robot_step()

        #check collision, if collision choose other action
        while (collision_detection(x, y)):
            x, y = robot_step()

        right_x.append(x)
        right_y.append(y)
        current_right_x = x 
        current_right_y = y
        is_left = 0        
    else:
        #compute x,y for left chamber
        x, y = robot_step()

        #check collision, if collision choose other action
        while (collision_detection(x, y)):
            x, y = robot_step()

        left_x.append(x)
        left_y.append(y)
        current_left_x = x
        current_left_y = y
        is_left = 1

def simulation():
    plot(True)

    for _ in range(100000):
        random_walk()
    
    plot(False)    

simulation()