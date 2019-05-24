import numpy as np

def config(k, l, maxiter):
    return "nbUserClusters: {0}\nnbItemClusters: {1}\nnbMaxIterations: {2}\n".format(k, l, maxiter)
    
kList = np.array([1, 2, 3, 5, 10])
lList = kList
maxiter = 30

cnt = 0
for k in kList:
    for l in lList:
        with open("config{}.yml".format(cnt), 'w') as f:
            f.write(config(k, l, maxiter))
        cnt += 1

kList = np.array([15, 20, 25])
lList = kList

for k in kList:
    for l in lList:
        with open("config{}.yml".format(cnt), 'w') as f:
            f.write(config(k, l, maxiter))
        cnt += 1

kList = np.array([15, 20, 25])
lList = np.array([1, 2, 3, 5, 10])

for k in kList:
    for l in lList:
        with open("config{}.yml".format(cnt), 'w') as f:
            f.write(config(k, l, maxiter))
        cnt += 1

kList = np.array([1, 2, 3, 5, 10])
lList = np.array([15, 20, 25])

for k in kList:
    for l in lList:
        with open("config{}.yml".format(cnt), 'w') as f:
            f.write(config(k, l, maxiter))
        cnt += 1
        
with open("config{}.yml".format(cnt), 'w') as f:
    f.write(config(0, 0, maxiter))
cnt += 1

