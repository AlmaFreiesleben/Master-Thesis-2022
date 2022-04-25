import pandas as pd
import seaborn as sns
import os
import matplotlib.pyplot as plt
#sns.set(style="darkgrid")
#sns.set(style="whitegrid")
#sns.set_style("white")
sns.set(style="whitegrid",font_scale=1)
import matplotlib.collections as clt
import ptitprince as pt
import matplotlib.colors as mcolors

mono_font = {'fontname':'monospace'}

# read data into dataframe
data = {'Coverage': [0.0,1.4423076923076934,5.608974358974365,8.974358974358978,16.34615384615384,22.275641025641022,30.929487179487182,37.33974358974359,45.993589743589745,52.40384615384615,61.05769230769231,67.46794871794872,75.80128205128204,82.21153846153845,88.30128205128204,92.94871794871796,95.99358974358974,0.0,1.4423076923076934,5.608974358974365,8.974358974358978,16.34615384615384,22.275641025641022,30.929487179487182,37.33974358974359,45.993589743589745,52.40384615384615,61.05769230769231,67.46794871794872,75.80128205128204,82.21153846153845,88.30128205128204,92.94871794871796,95.99358974358974,0.0,1.4423076923076934,5.608974358974365,8.974358974358978,16.34615384615384,22.275641025641022,30.929487179487182,37.33974358974359,45.993589743589745,52.40384615384615,61.05769230769231,67.46794871794872,75.80128205128204,82.21153846153845,88.30128205128204,92.94871794871796,95.99358974358974],
        'Algorithm': ['snake','snake','snake','snake','snake','snake','snake','snake','snake','snake','snake','snake','snake','snake','snake','snake','snake','random','random','random','random','random','random','random','random','random','random','random','random','random','random','random','random','random','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump','wall bump']
        }

df = pd.DataFrame(data, columns = ['Algorithm', 'Coverage'])
print(df)

# create bar plot
save_figs = False
sns.barplot(x = "Algorithm", y = "Coverage", data = df, capsize= .1)
plt.title("Figure P1\n Bar Plot")
if save_figs:
    plt.savefig("box_plot_5x10.png", bbox_inches='tight')

# plotting the clouds
f, ax = plt.subplots(figsize=(7, 5))
dy="Algorithm"; dx="Coverage"; ort="h"; pal = sns.color_palette(n_colors=1)

ax=pt.half_violinplot( x = dx, y = dy, data = df, palette = pal, bw = .2, cut = 0.,
                      scale = "area", width = .6, inner = None, orient = ort)

plt.title("Figure P2\n Basic Rainclouds")
if save_figs:
    plt.savefig('rain_clouds_5x10.png', bbox_inches='tight')

# adding the rain
f, ax = plt.subplots(figsize=(10, 10))
ax=pt.half_violinplot( x = dx, y = dy, data = df, palette = pal, bw = .2, cut = 0.,
                      scale = "area", width = .6, inner = None, orient = ort)
ax=sns.stripplot( x = dx, y = dy, data = df, palette = pal, edgecolor = "white",
                 size = 3, jitter = 0, zorder = 0, orient = ort)

plt.title("Figure P3\n Raincloud Without Jitter")
if save_figs:
    plt.savefig('rain_clouds_5x10.png', bbox_inches='tight')


# adding jitter to the rain
f, ax = plt.subplots(figsize=(7, 5))
ax=pt.half_violinplot( x = dx, y = dy, data = df, palette = pal, bw = .2, cut = 0.,
                      scale = "area", width = .6, inner = None, orient = ort)
ax=sns.stripplot( x = dx, y = dy, data = df, palette = pal, edgecolor = "white",
                 size = 3, jitter = 1, zorder = 0, orient = ort)

plt.title("Figure P4\n Raincloud with Jittered Data")
if save_figs:
    plt.savefig('rain_clouds_5x10.png', bbox_inches='tight')

#adding the boxplot with quartiles
f, ax = plt.subplots(figsize=(7, 5))
ax=pt.half_violinplot( x = dx, y = dy, data = df, palette = pal, bw = .2, cut = 0.,
                      scale = "area", width = .6, inner = None, orient = ort)
ax=sns.stripplot( x = dx, y = dy, data = df, palette = pal, edgecolor = "white",
                 size = 3, jitter = 1, zorder = 0, orient = ort, ax = ax )
ax=sns.boxplot( x = dx, y = dy, data = df, color = "black", width = .15, zorder = 10,\
            showcaps = True, boxprops = {'facecolor':'none', "zorder":10},\
            showfliers=True, whiskerprops = {'linewidth':2, "zorder":10},\
               saturation = 1, orient = ort)

plt.title("Figure P5\n Raincloud with Boxplot", **mono_font)
if save_figs:
    plt.savefig('rain_clouds_5x10.png', bbox_inches='tight')

#adding color
pal = sns.color_palette("Paired")
f, ax = plt.subplots(figsize=(7, 5))

ax=pt.half_violinplot( x = dx, y = dy, data = df, palette = pal, bw = .2, cut = 0.,
                      scale = "area", width = .6, inner = None, orient = ort)
ax=sns.stripplot( x = dx, y = dy, data = df, palette = pal, edgecolor = "white",
                 size = 3, jitter = 1, zorder = 0, orient = ort)
ax=sns.boxplot( x = dx, y = dy, data = df, color = "black", width = .15, zorder = 10,\
            showcaps = True, boxprops = {'facecolor':'none', "zorder":10},\
            showfliers=True, whiskerprops = {'linewidth':2, "zorder":10},\
               saturation = 1, orient = ort)

save_figs=True
plt.title("Tweaking the Colour of Your Raincloud", **mono_font)
if save_figs:
    plt.savefig('rain_clouds_5x10.png', bbox_inches='tight')

#moving the rain below the boxplot
dx = "Algorithm"; dy = "Coverage"; ort = "v"; sigma = .2
f, ax = plt.subplots(figsize=(10, 10))

ax=pt.RainCloud(x = dx, y = dy, data = df, palette = pal, bw = sigma,
                 width_viol = .6, ax = ax, orient = ort, move = .2)

save_figs=False
plt.title("Figure P8\n Rainclouds with Shifted Rain", **mono_font)
if save_figs:
    plt.savefig('rain_clouds_5x10.png', bbox_inches='tight')
