load('degree.txt')
nonzero_nodes = degree(degree~=0);

avDegree = mean(nonzero_nodes);
minDegree = min(nonzero_nodes);
maxdegree = max(nonzero_nodes);
singleConnection = sum(degree==1);
degree100 = sum(degree>100);
degree1000 = sum(degree>1000);
degree10000 = sum(degree>10000);