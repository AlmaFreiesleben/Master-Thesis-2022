import matplotlib.pyplot as plt
import csv

mono_font = {'fontname':'monospace'}

cov_data = []
with open('coverage.csv') as csvfile:
    reader = csv.reader(csvfile, quoting=csv.QUOTE_NONNUMERIC)
    for row in reader:
        cov_data.append(row)

time_data = []
with open('time.csv') as csvfile:
    reader = csv.reader(csvfile, quoting=csv.QUOTE_NONNUMERIC)
    for row in reader:
        time_data.append(row)

def plot_mean():
    plt.plot(time_data[0], cov_data[0], linewidth=2, color='darkcyan')

def plot_labels():
    plt.title('Random Walk', **mono_font)
    plt.xlabel("Time in minutes", **mono_font)
    plt.ylabel("Coverage in percent", **mono_font)
    plt.grid()
    plt.savefig("graph_random_sphere.png")
    plt.show()

plt.xlim(right=100)
plot_mean()
plot_labels()