import networkx as nx
from networkx.readwrite import json_graph
import json
import matplotlib.pyplot as plt

print('reading graph...')
with open('graph.json', 'r') as f_graph:
    try:
        data = json.load(f_graph)
    except ValueError:
        data = {}

print('reconstructing graph...')
G = json_graph.node_link_graph(data) # load graph from file

'''
print('analyzing degreee...')
degree = nx.degree(G)

print('writing to file...')

f_degree = open('degree.txt','w')
i = 0

for key in degree:
	f_degree.write(str(degree[key]) + '\n')
	if(i%10000 == 0):
		print(str(i))
	i += 1

f_degree.close()
'''

'''
print('ploting graph...')

nx.draw(G)
plt.savefig("path.png")
'''

print('generating largest cc...')
largest_cc = max(nx.connected_component_subgraphs(G), key=len)
print(str(len(largest_cc)))

print('generating largest cc subgraph...')
for node in G.nodes_iter():
	if node not in largest_cc:
		G.remove_node(node)

print(str(G.number_of_nodes()))
print(str(G.number_of_edges()))

print('save to file...')
data_largest = json_graph.node_link_data(G)
with open('graph_largest.json', 'w') as f_graph_largest:
    json.dump(data_largest, f_graph_largest)
