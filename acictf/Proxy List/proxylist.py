# https://github.com/RR2DO2/maxminddb-geolite2
# pip install maxminddb-geolite2

from geolite2 import geolite2

glreader = geolite2.reader()

with open('./Proxy List/ips.txt') as f:
    ip_list = [line.rstrip() for line in f]
# print(ip_list)

data = []
for ip in ip_list:
    data.append(glreader.get(ip))

# print(data)
# print()

# countries = [x['country']['names']['en'] if x['country'] is not None else pass for x in data]
# countries = [x['country'] for x in data if x.get('country')]
# countries = [x['country'] if 'country' in x else None for x in data]

# if 'country' in data[0]:
#     print(True)

countries =[]
for x in data:
    if 'country' in x:
        countries.append(x['country']['names']['en'])

from collections import Counter
print(Counter(countries))

# ACI{Brazil}
