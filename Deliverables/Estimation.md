1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
# Project Estimation  
Authors: Marcelo Coronel, Mostafa Asadollahy, Tommaso Natta, Zissis Tabouras 
Date: 28 april 2021
Version: #1
# Contents
- [Estimate by product decomposition]
- [Estimate by activity decomposition ]
# Estimation approach
<Consider the EZGas  project as described in YOUR requirement document, assume that you are going to develop the project INDEPENDENT of the deadlines of the course>

# Estimate by product decomposition
### 
|             | Estimate                        |             
| ----------- | ------------------------------- |  
| NC =  Estimated number of classes to be developed   |         15                    |             
|  A = Estimated average size per class, in LOC       |           100                 | 
| S = Estimated size of project, in LOC (= NC * A) |1500 |
| E = Estimated effort, in person hours (here use productivity 10 LOC per person hour)  |      150                                |   
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro) |4500 | 
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ) |           1         |               
# Estimate by activity decomposition
### 
|         Activity name    | Estimated effort (person hours)   |             
| ----------- | ------------------------------- | 
| Requirements | 20 |
| Design | 30  |
| Coding | 150 |
| Unit Testing | 20 |
| System Testing | 50 |
###


``` plantuml
@startuml
header Software Engineering
footer Group 28
printscale weekly
title Project Schedule
Project starts the 10th of march 2021
[Requirements] lasts 2 week
then [Design] lasts 2 week
then [Coding] lasts 8 weeks
then [Unit Testing] lasts 2 weeks
then [System Testing] lasts 4 weeks
@enduml
```
###
