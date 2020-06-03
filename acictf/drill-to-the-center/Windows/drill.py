# drill.py

alphabet = "ABCDEFGHIJKHIJKLMNOPQRSTUVWXYZ"

param = "HDtbn7plJIn9CpzKEvw25KI0ku668nb"

solution = ''


for pos, char in enumerate(param):
    print("position:", pos, "char:", char, "ord:", ord(char))
    print(ord(char) % len(alphabet), alphabet[ord(char) % len(alphabet)])
    solution += alphabet[ord(char) % len(alphabet)]

print(solution)

# IIWIQVSOKJQXHSCLJYZQTLJONXUUWQI
# ACI{IIWIQVSOKJQXHSCLJYZQTLJONXUUWQI}