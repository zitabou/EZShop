# Design assessment


```
<The goal of this document is to analyse the structure of your project, compare it with the design delivered
on April 30, discuss whether the design could be improved>
```


# Levelized structure map
```
<Applying Structure 101 to your project, version to be delivered on june 4, produce the Levelized structure map,
with all elements explosed, all dependencies, NO tangles; and report it here as a picture>
```
<img src="./images/structure101/LevelizedStructureMap.PNG">

# Structural over complexity chart
```
<Applying Structure 101 to your project, version to be delivered on june 4, produce the structural over complexity chart; and report it here as a picture>
```
<img src="./images/structure101/structuralComplexity.PNG">


# Size metrics

```
<Report here the metrics about the size of your project, collected using Structure 101>
```



| Metric                                    | Measure |
| ----------------------------------------- | ------- |
| Packages                                  |   8      |
| Classes (outer)                           |  59       |
| Classes (all)                             |  59       |
| NI (number of bytecode instructions)      |  15K       |
| LOC (non comment non blank lines of code) |  6K       |



# Items with XS

```
<Report here information about code tangles and fat packages>
```

| Item | Tangled | Fat  | Size | XS   |
| ---- | ------- | ---- | ---- | ---- |
| it.polito.ezshop.data     |         | 143     |  4133    |    664  |
| it.polito.ezshop.classesDAO     |         |  19    | 189     |  39    |



# Package level tangles

```
<Report screen captures of the package-level tangles by opening the items in the "composition perspective" 
(double click on the tangle from the Views->Complexity page)>
```
<img src="./images/structure101/packageTangles.PNG">

# Summary analysis
```
<Discuss here main differences of the current structure of your project vs the design delivered on April 30>
<Discuss if the current structure shows weaknesses that should be fixed>
```
If we were to put the classes contained in the classes package into the data package the tangles would be 0