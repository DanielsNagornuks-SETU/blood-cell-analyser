Blood Cell Analyser
-------------------

Application built with JavaFX that detects and marks blood cell clusters from an image of a prepared/stained microscopic blood sample. The application marks each blood cell cluster with a rectangular frame (green for red blood cell, red for white blood cell) and a number.

There is also a tooltip at each cluster, stating approximately how many blood cells are in the cluster. If there is more than one blood cell in a cluster, the frame color is blue. The user can make adjustments for hue, saturation and brightness, as well as set minimum and maximum blood cell sizes. 

The user can also view stats for the image and toggle various options mentioned above, as well as an option to see the tricolor version of the image used for analysis.
<img width="1732" height="1064" alt="Screenshot_20250806_221236" src="https://github.com/user-attachments/assets/bc12484c-b688-4e49-8951-2d703b4c22c1" />

Build
-----
The following dependencies are required:
  - Java (JDK 21 or higher recommended)
  - Maven

Clone the repository, change current directory into it and use Maven run the project:
```
git clone https://github.com/DanielsNagornuks-SETU/blood-cell-analyser.git
cd blood-cell-analyser
mvn javafx:run
```
