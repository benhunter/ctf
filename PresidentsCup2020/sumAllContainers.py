# Practice

from num2words import num2words
import requests

responses = []

for container in range(20):
    container_name = num2words(container)
    responses.append(int(requests.get('http://' + container_name).content))
print(sum(responses))

# 9581004
