# Complete Coverage Path Planning of underwater hull cleaning robot, Lappa, on intricate hulls

## Problem statement 
Hull cleaning is the process of removing fouling from boat hulls, which can be performed as dry dock cleaning and in-water cleaning. As soon as a boat is put into water, it is exposed to fouling, which causes several disadvantages both for the trade shipping industry and for leisure boats, e.g., by slowing down the boat's speed. Using robots for hull cleaning enables cleaning of the boat while in water, which ideally fixes the problem of maintaining the optimal speed of the boat, delays the need for getting the boat out of the water to do dry dock cleaning, and limits the use of anti-fouling paint. The extensive use of anti-fouling paint within hull cleaning today in itself brings a lot of environmental disadvantages since it is composed of highly toxic chemicals.

In order to clean boat hulls with a robot, the robot needs to find a path that ensures that the robot has covered the whole surface to be cleaned; some traditional algorithmic ways of doing coverage path planning (CPP) are random walk, zigzag pattern, Voronoi, and complete coverage D*. These algorithms produce good plans for cleaning when the 3D shape of the boat hulls is “flat” and do not have obstacles. However, there are boats that have intricate hulls with several complex obstacles. Therefore, this project focuses on cleaning such boats with intricate hulls and obstacles such as rudder, keel, and propeller. The robot used in this project is Lappa [1], chosen since it is highly dexterous and is designed to navigate around complex hulls and obstacles.

This project addresses the problem of cleaning hulls of the aforementioned type using Lappa while minimizing the amount of time used to do the cleaning and maximizing the coverage of the hull. The problem is CPP in complex 3D environments, in this case, composed of the shape of the hull and obstacles on the hull. Furthermore, using a robot such as Lappa that has a non-standard design, which enables it to move by an alternating swinging movement of two cleaning devices. The swinging movement leaves a gap in the cleaning trail, which makes it difficult to do full coverage and therefore needs to be considered to ensure coverage. Lastly, to minimize the amount of time used to do the cleaning, the gait strategy that the robot uses needs to be considered as well.

The above leads to answering the following question:
How do you implement and adapt an existing coverage path planning algorithm for the robot Lappa with the aim of executing hull cleaning in an underwater 3D environment composed of an intricate hull with complex obstacles?

## Method 
This project attempts to answer the problem statement by doing the following: 
- Firstly, I will investigate state-of-the-art path planning algorithms such as random walk used by the Roomba robot and then moving onto less randomized CPP algorithms.
- Thereafter, I will implement a simple random walk algorithm to be used as a baseline for comparing to other CPP algorithms.
- Then, I will start out with a model of one boat with an intricate hull e.g. with one keel and one propeller to enable testing in simulation. Aiming at testing the adaptability of the algorithm I will be modeling a few more (3-5) intricate hulls with complex obstacles.

## In short
The goal of this project is to compare and adapt existing CPP algorithms to the specific requirements posed by an underwater environment, the task of hull cleaning, and the movement and cleaning considerations specifically related to Lappa. The main goal is to conquer the difficulty in coverage path planning around complex 3D shapes while doing effective cleaning of the hull. 

## References
[1] Daniel Souto, Andrés Faiña, Fernando López-Peña, and Richard J Duro. Lappa: a new type of robot for underwater non-magnetic and complex hull cleaning. In 2013 IEEE International Conference on Robotics and Automation, pages 3409–3414. IEEE, 2013.
