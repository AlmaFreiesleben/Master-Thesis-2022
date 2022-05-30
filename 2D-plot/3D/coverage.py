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

x = []
y = []
def compute_mean():
    for i in range(40):
        mean_time = (time_data[0][i] + time_data[1][i] + time_data[2][i]) / 3
        x.append(mean_time)
        mean_cov = (cov_data[0][i] + cov_data[1][i] + cov_data[2][i]) / 3
        y.append(mean_cov)
    x.append(time_data[1][len(time_data[1])-1])
    y.append(cov_data[1][len(cov_data[1])-1])  

def plot_data():
    for i in range(3):
        plt.plot(time_data[i], cov_data[i], linewidth=2, color='hotpink')

def plot_mean():
    compute_mean()
    plt.plot(x, y, linewidth=2, color='yellowgreen')

def plot_labels():
    plt.title('Random Walk', **mono_font)
    plt.xlabel("Time in minutes", **mono_font)
    plt.ylabel("Coverage in percent", **mono_font)
    plt.grid()
    plt.savefig("graph_random_sphere_3_runs.png")
    plt.show()

plt.xlim(right=100)
plot_data()
#plot_mean()
plot_labels()