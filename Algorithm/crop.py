import cv2
import numpy as np
import os
import matplotlib.pyplot as plt

imgPath = '/home/sandeep/PycharmProjects/demo/Clipped_NDVI'
imgPathToSave = '/home/sandeep/PycharmProjects/demo/adaptiveThreshold'

xValue = []
avgPixel = []
i=0
for filename in os.listdir(imgPath):
    i = i + 1
    tempFile = cv2.imread(imgPath+"/"+filename,cv2.IMREAD_GRAYSCALE)
    cv2.imwrite(imgPathToSave + "/" + str(i) + "orig" + ".png", tempFile)
    ret, tempFile = cv2.threshold(tempFile,np.mean(tempFile),255,cv2.THRESH_BINARY)
    tempFile = cv2.bitwise_not(tempFile)
    avgPixel.append(cv2.countNonZero(tempFile))
    xValue.append(i)
    cv2.imwrite(imgPathToSave+"/"+str(i)+".png",tempFile)
plt.plot(xValue,avgPixel,'ro-')
plt.savefig("mygraphThreshold.png")
