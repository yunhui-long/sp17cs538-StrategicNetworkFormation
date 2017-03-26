import networkx as nx
from networkx.readwrite import json_graph
import json


print('generating dictionary...')

dict = {}

f1 = open('cs538-transactions.txt', 'r')
index = 0

for line in f1:
    arr = line.split(' ')

    if(not arr[2] in dict):
        dict[arr[2]] = index
        index += 1
f1.close()

with open('key-dict.txt', 'w') as f_key:
    json.dump(dict, f_key)
