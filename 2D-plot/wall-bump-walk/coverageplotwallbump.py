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
    for i in range(60):
        mean_time = (time_data[0][i] + time_data[1][i] + time_data[2][i] + time_data[3][i] + time_data[4][i] + time_data[5][i] + time_data[6][i] + time_data[7][i] + time_data[8][i] + time_data[9][i]) / 10
        x.append(mean_time)
        mean_cov = (cov_data[0][i] + cov_data[1][i] + cov_data[2][i] + cov_data[3][i] + cov_data[4][i] + cov_data[5][i] + cov_data[6][i] + cov_data[7][i] + cov_data[8][i] + cov_data[9][i]) / 10
        y.append(mean_cov)

    for i in range(100):
        j = 70
        mean_time = (time_data[0][i+j] + time_data[7][i+j] + time_data[4][i+j]) / 3
        x.append(mean_time)
        mean_cov = (cov_data[0][i+j] + cov_data[7][i+j] + cov_data[4][i+j]) / 3
        y.append(mean_cov)

    x.append(time_data[0][len(time_data[0])-1])
    y.append(cov_data[0][len(cov_data[0])-1])

def plot_data():
    for i in range(10):
        plt.scatter(time_data[i], cov_data[i], s=1, c='hotpink')

def plot_mean():
    compute_mean()
    plt.plot(x, y, linewidth=2, color='darkslateblue')

def plot_labels():
    plt.title('Wall Bumping Walk (3x6)', **mono_font)
    plt.xlabel("Time in minutes", **mono_font)
    plt.ylabel("Coverage in percent", **mono_font)
    plt.grid()
    plt.savefig("graph_wallbump_3x6_10_runs_95.png")
    plt.show()

plt.xlim(right=400)
plot_data()
plot_mean()
plot_labels()
