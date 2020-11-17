#This file is to take the data from the txt form from Environment.java and turn it into a csv
#and plot form to make the data easier to read and display
#
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
    print(file)
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

#with open('data.csv', 'w', newline='') as file:
#    writer = csv.writer(file)
#    writer.writerow(index)
#    for row in data:
#        writer.writerow(row)
for x in range(0, len(data)):
    plt.plot(data[x][7:])
    plt.title("Conviction of Minority:" + data[x][3]+ " Conviction of Majority:"+ data[x][5] + " Std Dev:" + data[x][6] +
              "\n# of trials: " + data[x][0] + " # of people:" + data[x][1] + " # of interactions:" + data[x][2])
    plt.xlabel("Number of People in Minority Group")
    plt.ylabel("Proportion of Trials where mass Pluralistic Ignorance was achieved")
    name = "t"+ data[x][0] + "p" + data[x][1]+ "i" + data[x][2] + "a1-" + data[x][3] + " a2-" + data[x][5] + "s"+ data[x][6] +".png"
    plt.savefig(name)
    plt.clf()
