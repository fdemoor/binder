import numpy as np

def config(k, l):
    return "k: {}\nbiclustering: {}\nminUserSize: {}\nminItemSize: {}\nconsistency: {}\nsize: {}\noverlap: {}\nbin: false\nns: {}\nnd: {}\nsd: {}\n".format(k, l, 3, 3, 0.95, 100, 1, 50, 1000, 3)
    
kList = np.array([5, 10, 15, 20])
algo = "qubic"

cnt = 0
for k in kList:
	with open("config{}.yml".format(cnt), 'w') as f:
		f.write(config(k, algo))
	cnt += 1
    
algo = "xmotif"
for k in kList:
	with open("config{}.yml".format(cnt), 'w') as f:
		f.write(config(k, algo))
	cnt += 1
    
algo = "random"
for k in kList:
	with open("config{}.yml".format(cnt), 'w') as f:
		f.write(config(k, algo))
	cnt += 1

algo = "coclust"
for k in kList:
	with open("config{}.yml".format(cnt), 'w') as f:
		f.write(config(k, algo))
	cnt += 1
