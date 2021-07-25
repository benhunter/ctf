What is Santa giving to Josh Wright?
Unswirl the photo https://2020.kringlecon.com/textures/billboard.png


Front Lawn outside Castle

1. Kiosk
 - Escape to shell by selecting option 4 - cowsay. Enter: test;/bin/bash

shinny@4b7786e43929:/home/elf$ ./runtoanswer ejm96
Sorry, that answer is incorrect. Please try again!
shinny@4b7786e43929:/home/elf$ ./runtoanswer -h   
D'aww, people hardly ever ask me for help! Nice to meet you!
The usage is really easy, we promise! Just pass your answer as a commandline argument, lik
e:
    ./runtoanswer MyWonderfulAnswer
Or just run ./runtoanswer by itself, and it'll ask for your answer!
Bye now!
shinny@4b7786e43929:/home/elf$ 

 - Option: "plant"
cat /opt/plant.txt
Enter choice [1 - 5] plant
  Hi, my name is Jason the Plant!
  ( U
   \| )
  __|/
  \    /
   \__/ ejm96
Sleeping for 10 seconds..

/opt/directory.txt
Name:                 Floor:   Room:
Ribb Bonbowford       1        Dining Room
Noel Boetie           1        Wrapping Room
Ginger Breddie        1        Castle Entry
Minty Candycane       1.5      Workshop
Angel Candysalt       1        Great Room
Tangle Coalbox        1        Speaker UNPreparedness
Bushy Evergreen       2        Talks Lobby
Holly Evergreen       1        Kitchen
Bubble Lightington    1        Courtyard
Jewel Loggins                  Front Lawn
Sugarplum Mary        1        Courtyard
Pepper Minstix                 Front Lawn
Bow Ninecandle        2        Talks Lobby
Morcel Nougat         2        Speaker UNPreparedness
Wunorse Openslae      R        NetWars Room
Sparkle Redberry      1        Castle Entry
Jingle Ringford                NJTP
Piney Sappington      1        Castle Entry
Chimney Scissorsticks 2        Talks Lobby
Fitzy Shortstack      1        Kitchen
Alabaster Snowball    R        NetWars Room
Eve Snowshoes         3        Santa's Balcony
Shinny Upatree                 Front Lawn
Tinsel Upatree        3        Santa's Office



Investigate S3 Bucket

elf@cbce7726eaec:~/bucket_finder$ ./bucket_finder.rb --log-file bucket.out wordlist 
http://s3.amazonaws.com/kringlecastle
Bucket found but access denied: kringlecastle
http://s3.amazonaws.com/wrapper
Bucket found but access denied: wrapper
http://s3.amazonaws.com/santa
Bucket santa redirects to: santa.s3.amazonaws.com
http://santa.s3.amazonaws.com/
        Bucket found but access denied: santa
elf@cbce7726eaec:~/bucket_finder$ less bucket_finder.rb 

Add wrapper3000 to wordlist.
download: http://s3.amazonaws.com/wrapper3000/package

package.txt:
North Pole: The Frostiest Place on Earth