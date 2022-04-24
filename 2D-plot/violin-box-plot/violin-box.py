import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import csv

mono_font = {'fontname':'monospace'}

def box_plot(data, edge_color, fill_color):
    bp = ax.boxplot(data, patch_artist=True, widths=0.25)
    
    for element in ['boxes', 'whiskers', 'fliers', 'means', 'medians', 'caps']:
        plt.setp(bp[element], color=edge_color)
        plt.setp(bp[element], linewidth=1)

    for patch in bp['boxes']:
        patch.set(facecolor=fill_color)
        
    return bp

def violin_plot(data, fill_color):
    violin_parts = ax.violinplot(data, showmeans=False, showmedians=True, showextrema=False)
    
    for vp in violin_parts['bodies']:
        vp.set_facecolor(fill_color)
        vp.set_edgecolor(fill_color)
        vp.set_linewidth(1)
        vp.set_alpha(0.5)


#alg1 = np.genfromtxt('c.csv', delimiter=",")
data = pd.DataFrame({"Alg1": np.random.uniform(0, 100, 100), "Alg2": np.random.uniform(0, 100, 100), "Alg3": np.random.uniform(0, 100, 100)})
#data = pd.DataFrame({"Alg1": alg1, "Alg2": alg1, "Alg3": alg1})

fig, ax = plt.subplots()

violin_plot(data, 'deeppink')
box_plot(data, 'darkolivegreen', 'yellowgreen')

# add title and axis labels
ax.set_title('violin plot', **mono_font)
ax.set_ylabel('Coverage percentage', **mono_font)
yticklabels = [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]
ax.set_yticks([0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100])
ax.set_yticklabels(yticklabels, **mono_font)

# add x-tick labels
xticklabels = ["random walk", "wall bump walk", "snake walk"]
ax.set_xticks([1,2,3])
ax.set_xticklabels(xticklabels, **mono_font)

# add horizontal grid lines
ax.yaxis.grid(True)

# show the plot
plt.show()
