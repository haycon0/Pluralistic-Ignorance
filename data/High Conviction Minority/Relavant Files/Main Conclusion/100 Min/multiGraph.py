#This file is to take the data from the txt form from Environment.java and turn it into a csv
#and plot form to make the data easier to read and display
import os
import csv
import matplotlib.pyplot as plt


path = "."
index = []
data = []
index.append("number of trials")
index.append("number of people")
index.append("number of interactions")
index.append("average conviction 1")
index.append("std dev 1")
index.append("average conviction 2")
index.append("std dev 2")
index_bool = True

for file in os.listdir(path):
    if ".txt" in file:
        f = open(file, "r")
        data_points = []
        for line in f:
            if "/" in line:
                if index_bool:
                    index.append(line.split("/")[0].strip())
                data_points.append(float(line.split(":")[1].strip()))
            elif ":" in line:
                if len(line.split(":")[1].strip()) > 0:
                    data_points.append(line.split(":")[1].strip())
        data.append(data_points)
        index_bool = False

labels = []

for x in range(0, len(data)):
    plt.plot(data[x][7:])
    labels.append(data[x][3] + "," + data[x][5] + "," +  data[x][6])

name = "100MinGraph"
title = "Graph of select convictions"
plt.legend(labels, title="Convictions:\n min,maj,std dev")
plt.xlabel("Number of People in Minority Group")
plt.ylabel("Proportion of Trials where mass Pluralistic Ignorance was achieved")
plt.title(title)
plt.savefig(name)
plt.show()