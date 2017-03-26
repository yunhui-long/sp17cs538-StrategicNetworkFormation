import networkx as nx
from networkx.readwrite import json_graph
import json

with open('key-dict.json', 'r') as f_key:
    try:
        dict = json.load(f_key)
    except ValueError:
        dict = {}


G=nx.Graph()

print('adding nodes...')

for key in dict:
    G.add_node(dict[key])

print('adding edges...')

f2 = open('cs538-transactions.txt','r')
curTx = '0000'
inList = []
lineIndex = 0
for line in f2:
    arr = line.split(' ')
    if (arr[0] == curTx): #same transaction
        if (arr[4] == 'in\n'):
            inList.append(dict[arr[2]])
        if (arr[4] == 'out\n'):
            outNode = dict[arr[2]]
            for ele in inList:
                G.add_edge(ele,outNode)
    else:
        inList = []
        curTx = arr[0]
    if lineIndex % 100000 == 0:
        print(str(lineIndex))
    lineIndex += 1
f2.close()

data = json_graph.node_link_data(G)
with open('graph.json', 'w') as f_graph:
    json.dump(data, f_graph)

print(str(G.number_of_nodes()))
print(str(G.number_of_edges()))


