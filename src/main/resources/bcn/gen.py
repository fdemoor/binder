import numpy as np

def config(l):
    return "level: {0}\n".format(l)
    
lList = np.array([1, 2])

cnt = 0
for l in lList:
	with open("config{}.yml".format(cnt), 'w') as f:
		f.write(config(l))
	cnt += 1
        
