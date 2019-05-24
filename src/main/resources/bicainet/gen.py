import numpy as np

def config(wr, wc, l, n, s, o):
    return "wr: {0}\nwc: {1}\nlambda: {2}\nmaxNbIt: {3}\nsupIt: {4}\no: {5}\n".format(wr, wc, l, n, s, o)
    
wr = 2
wc = 3
l = 2
n = 100
s = 15
o = 50

cnt = 0
with open("config{}.yml".format(cnt), 'w') as f:
    f.write(config(wr, wc, l, n, s, o))
    cnt += 1
        
