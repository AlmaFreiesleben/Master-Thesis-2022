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
    for i in range(250):
        mean_time = (time_data[0][i] + time_data[1][i] + time_data[2][i] + time_data[3][i] + time_data[4][i] + time_data[5][i] + time_data[6][i] + time_data[7][i] + time_data[8][i] + time_data[9][i]) / 10
        x.append(mean_time)
        mean_cov = (cov_data[0][i] + cov_data[1][i] + cov_data[2][i] + cov_data[3][i] + cov_data[4][i] + cov_data[5][i] + cov_data[6][i] + cov_data[7][i] + cov_data[8][i] + cov_data[9][i]) / 10
        y.append(mean_cov)
    x.append(time_data[5][len(time_data[5])-1])
    y.append(cov_data[5][len(cov_data[5])-1])

def plot_data():
    for i in range(10):
        plt.scatter(time_data[i], cov_data[i], s=1, c='c')

def plot_mean():
    compute_mean()
    plt.plot(x, y, linewidth=2, color='m')

def plot_labels():
    plt.title('Random Walk (5x10)', **mono_font)
    plt.xlabel("Time in minutes", **mono_font)
    plt.ylabel("Coverage in percent", **mono_font)
    plt.grid()
    plt.savefig("graph_random_5x10_10_runs.png")
    plt.show()

plot_data()
plot_mean()
plot_labels()