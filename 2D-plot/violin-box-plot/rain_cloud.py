import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
sns.set(style="whitegrid",font_scale=1)
import ptitprince as pt

plt.rcParams.update({'font.family':'Helvetica'})

# read data into dataframe
#s = ['snake'] * 10
r = ['random'] * 10
w = ['wall bump'] * 10
#s.extend(r)
r.extend(w)

#t1 = [239.25,239.25,239.25,239.25,239.25,239.25,239.25,239.25,239.25,239.25]
t2 = [344.98334,673.9667,753.2592,748.91486,359.2287,711.3991,489.35092,252.93796,432.61295,399.6509]
t3 = [327.86115,400.8148,308.50742,383.42685,337.39908,404.14166,330.94815,319.26205,387.71854,359.70648]
#t1.extend(t2)
t2.extend(t3)

data = {
    'Time in minutes': t2,
    'Algorithm': r
        }

df = pd.DataFrame(data, columns = ['Algorithm', 'Time in minutes'])

#adding color
pal = sns.color_palette("Paired")
f, ax = plt.subplots(figsize=(7, 5))
dy="Algorithm"
dx="Time in minutes"
ort="h"
pal = sns.color_palette(pal)

ax=pt.half_violinplot( x = dx, y = dy, data = df, palette = pal, bw = .2, cut = 0.,
                      scale = "area", width = .6, inner = None, orient = ort)
ax=sns.stripplot( x = dx, y = dy, data = df, palette = pal, edgecolor = "m",
                 size = 3, jitter = 1, zorder = 0, orient = ort)
ax=sns.boxplot( x = dx, y = dy, data = df, color = "darkolivegreen", width = .20, zorder = 10,\
            showcaps = True, boxprops = {'facecolor':'none', "zorder":10},\
            showfliers=False, whiskerprops = {'linewidth':2, "zorder":10},\
               saturation = 1, orient = ort)

save_figs=True
#plt.title("2D algorithms: snake, random, and wall bump walk")
if save_figs:
    plt.savefig('rain_clouds_6x11.png', bbox_inches='tight')
