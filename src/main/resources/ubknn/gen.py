import numpy as np

def config(k, sim):
    return "k: {0}\nsimilarity: {1}\n".format(k, sim)
    
kList = np.array([5, 10, 15, 20, 30, 50])
simList = ['cosine', 'pearson']

cnt = 0
for k in kList:
    for sim in simList:
        with open("config{}.yml".format(cnt), 'w') as f:
            f.write(config(k, sim))
        cnt += 1