#!/usr/bin/python3
import argparse
import socket
import base64
import binascii

# 'argparse' is a very useful library for building python tools that are easy
# to use from the command line.  It greatly simplifies the input validation
# and "usage" prompts which really help when trying to debug your own code.
# parser = argparse.ArgumentParser(description="Solver for 'All Your Base' challenge")
# parser.add_argument("ip", help="IP (or hostname) of remote instance")
# parser.add_argument("port", type=int, help="port for remote instance")
# args = parser.parse_args()
ip = 'challenge.acictf.com'
port = 47912

# This tells the computer that we want a new TCP "socket"
socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# This says we want to connect to the given IP and port
# socket.connect((args.ip, args.port))
socket.connect((ip, port))

# This gives us a file-like view of the connection which makes reading data
# easier since it handles the buffering of lines for you.
f = socket.makefile()

# while True:
    # line = f.readline().strip()
    # This iterates over data from the server a line at a time.  This can cause
    # some unexpected behavior like not seeing "prompts" until after you've sent
    # a reply for it (for example, you won't see "answer:" for this problem).
    # However, you can still send data and it will be handled correctly.

    # Handle the information from the server to extact the problem and build
    # the answer string.
    # pass # Fill this in with your logic

    # Send a response back to the server
    # answer = "Clearly not the answer..."
    # socket.send((answer + "\n").encode()) # The "\n" is important for the server's
                                     # interpretation of your answer, so make
                                     # sure there is only one sent for each
                                     # answer.

def raw_dec(x):
    e = x.encode()
    b = bytes(e)
    i = int.from_bytes(b, byteorder='big')
    return i

def b64_dec(x):
    b = base64.b64decode(x)
    i = int.from_bytes(b, byteorder='big')
    return i

def hex_dec(x):
    # return int(binascii.unhexlify(x))
    i = int(x, 16)
    return i

def oct_dec(x):
    d = int(x, 8)
    return d

def bin_dec(x):
    d = int(x, 2)
    return d

def dec_raw(x):
    # return str(x)
    i = int(x).to_bytes(int(x).bit_length(), byteorder='big').strip(b'\x00')
    return i.decode()

def dec_b64(x):
    by = x.to_bytes((x.bit_length() + 7) // 8, byteorder='big').strip(b'A')
    b64 = base64.b64encode(by)
    return b64.decode()

def dec_hex(x):
    # by = x.to_bytes(x.bit_length(), byteorder='big')
    # h = binascii.hexlify(by)
    h = hex(x)
    return h[2:]

def dec_oct(x):
    o = oct(x)
    s = str(o)
    return s[2:]

def dec_bin(x):
    b = bin(x)
    s = str(b)
    return s[2:]

# def read_to_dash():
#    pass


while True:
    line = f.readline().strip()
    if len(line) > 1 and line[0] == '-':
        break

while True:
    line = f.readline().strip().split()
    print(line)

    encode = line[0]
    decode = line[2]
    print(encode, decode)

    src = f.readline().strip()
    print(src)

    # src to dec
    if encode == 'raw':
        dec = raw_dec(src)
    elif encode == 'b64':
        dec = b64_dec(src)
    elif encode == 'hex':
        dec = hex_dec(src)
    elif encode == 'dec':
        dec = int(src)
    elif encode == 'oct':
        dec = oct_dec(src)
    elif encode == 'bin':
        dec = bin_dec(src)
    
    # dec to target
    if decode == 'raw':
        target = dec_raw(dec)
    elif decode == 'b64':
        target = dec_b64(dec)
    elif decode == 'hex':
        target = dec_hex(dec)
    elif decode == 'dec':
        target = str(dec)
    elif decode == 'oct':
        target = dec_oct(dec)
    elif decode == 'bin':
        target = dec_bin(dec)


    # answer = "Clearly not the answer..."
    socket.send((target + "\n").encode())  # The "\n" is important for the server's
                                     # interpretation of your answer, so make
                                     # sure there is only one sent for each
                                     # answer.

    line = f.readline().strip()
    print(line)
    line = f.readline().strip()
    print(line)
    line = f.readline().strip()
    print(line)
    if 'incorrect' in line:
        print('hold up')
    line = f.readline().strip()
    print(line)

# ACI{for_great_justice_618c35ec}
