from math import pi, sin, cos
import random
import matplotlib.pyplot as plt

#### CONSTANTS ####

R = 1  # distance between champers in meters
W = 20  # width of arena
H = 20  # height of arena
is_left = 1 # start with left chamber

### ROBOT CHAMBER COORDS ###
current_left_x = [0]
current_left_y = [0]
current_right_x = [0]
current_right_y = [1]
x = [0]
y = [0]

def plot(is_first):
    plt.plot(current_left_x, current_left_y, marker="o", markersize=5, markeredgecolor="green", markerfacecolor="green")
    plt.plot(current_right_x, current_right_y, marker="o", markersize=5, markeredgecolor="blue", markerfacecolor="blue")
    plt.plot([W/2,-W/2,-W/2,W/2,W/2],[H/2,H/2,-H/2,-H/2,H/2], "b--")
    if (is_first): plt.savefig("simulation.png")
    plt.show()

def collision_detection():
    return x > W/2 or y > H/2 or x < -(W/2) or y < -(H/2)

def robot_step():
    global current_right_x, current_right_y, current_left_x, current_left_y, is_left, x, y
    #randomly move a chamber one of four legal actions
    a = random.choice([2/pi, pi, (3*pi)/2, 2*pi])

    if (is_left):   
        #x = current_right_x + R * cos(a)
        #y = current_right_y + R * sin(a)
        x = cos(a)
        y = sin(a)
    #else:
    #    x = current_left_x + R * cos(a)
    #    y = current_left_y + R * sin(a)

def random_walk():
    global current_right_x, current_right_y, current_left_x, current_left_y, is_left, x, y
    
    if (is_left):
        #compute x,y for right chamber
        robot_step()

        #check collision, if collision choose other action
        while (collision_detection()):
            robot_step()

        current_right_x = x 
        current_right_y = y
        #is_left = 0        
    #else:
    #    #compute x,y for left chamber
    #    x, y = robot_step()
    #
    #    #check collision, if collision choose other action
    #    while (collision_detection(x, y)):
    #        x, y = robot_step()
    #
    #    current_left_x = x
    #    current_left_y = y
    #    is_left = 1

def simulation():
    plot(True)

    for _ in range(10):
        random_walk()
        plot(False)
    
    #plot(False)    

simulation()