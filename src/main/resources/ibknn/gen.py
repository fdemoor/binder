import numpy as np

def config(sim):
    return "similarity: {}\n".format(sim)
    
simList = ['cosine', 'pearson', 'log']

cnt = 0
for sim in simList:
	with open("config{}.yml".format(cnt), 'w') as f:
		f.write(config(sim))
	cnt += 1
