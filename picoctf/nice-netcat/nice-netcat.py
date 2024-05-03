# PicoCTF
# https://play.picoctf.org/practice/challenge/156?page=1
# nc mercury.picoctf.net 22902 | python3 nice-netcat.py

# with open('out.txt') as f:
    # data = [int(line.rstrip()) for line in f]
    # print(data)

import sys

data = [int(line) for line in sys.stdin]
print(data)

for c in data:
    print(chr(c), end='')

flag = [chr(c) for c in data]
print(flag)
flag = ''.join(flag)
print(flag)
