import numpy as np

def config(factorizer, nbFeatures, Lambda, nbIterations):
    return "factorizer: {0}\nnbfeatures: {1}\nlambda: {2}\nnbiterations: {3}\n".format(factorizer, nbFeatures, Lambda, nbIterations)
    
factorizer = "alswr"
nbIterations = 25
LambdaList = np.arange(0.1, 1, 0.1)
nbFeaturesList = np.arange(3, 22, 5)

cnt = 0

for nbFeatures in nbFeaturesList:
    for Lambda in LambdaList:
        with open("config{}.yml".format(cnt), 'w') as f:
            f.write(config(factorizer, nbFeatures, Lambda, nbIterations))
        cnt += 1
        
LambdaList = np.arange(0.01, 0.1, 0.01)
for nbFeatures in nbFeaturesList:
    for Lambda in LambdaList:
        with open("config{}.yml".format(cnt), 'w') as f:
            f.write(config(factorizer, nbFeatures, Lambda, nbIterations))
        cnt += 1
 
factorizer = "svd++"
nbIterations = 25
nbFeaturesList = np.arange(3, 22, 5)

for nbFeatures in nbFeaturesList:
	with open("config{}.yml".format(cnt), 'w') as f:
		f.write(config(factorizer, nbFeatures, 0, nbIterations))
	cnt += 1
	
factorizer = "sgd"
for nbFeatures in nbFeaturesList:
	with open("config{}.yml".format(cnt), 'w') as f:
		f.write(config(factorizer, nbFeatures, 0, nbIterations))
	cnt += 1
