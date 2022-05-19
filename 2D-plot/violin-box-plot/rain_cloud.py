import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
sns.set(style="whitegrid",font_scale=1)
import ptitprince as pt

plt.rcParams.update({'font.family':'Helvetica'})

# read data into dataframe
#s = ['snake'] * 10
r = ['random'] * 50
w = ['wall bump'] * 50
#s.extend(r)
r.extend(w)

#t1 = [239.25,239.25,239.25,239.25,239.25,239.25,239.25,239.25,239.25,239.25]
t2 = [220.12314,221.86296,172.23334,155.10185,210.75462,188.9898,182.05,276.02408,351.7,236.09814,277.76947,182.29538,234.22128,257.99258,237.71666,164.71205,298.8037,194.27872,437.19077,262.31387,219.16943,193.40834,197.87129,309.5,210.94629,198.2426,167.05276,245.96388,317.41205,206.36667,176.13148,162.87871,162.28241,173.60463,147.99443,204.44167,216.78334,200.85463,148.94353,192.72223,225.96295,282.4278,151.28334,215.96295,217.77592,247.39815,234.51295,201.00093,192.2861,277.54538]
t3 = [128.14166,124.075,109.796295,114.63241,114.73333,112.643524,107.75833,103.51296,145.59166,112.078705,128.76852,132.7787,113.73241,128.05833,125.74167,118.962036,128.67593,146.82224,120.643524,105.416664,118.85093,108.61945,123.010185,125.8787,100.05555,114.356476,107.08055,116.123146,108.687035,97.17778,117.30555,141.5676,101.41389,131.3926,125.828705,112.82407,141.57777,119.577774,137.90648,106.84445,135.99167,128.48148,129.37593,110.24167,122.268524,107.3074,133.5176,135.2139,135.27963,137.73705]
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
