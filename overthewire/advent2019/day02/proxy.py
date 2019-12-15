# ncat 3.93.128.89 12021

import socket


def hexdump(src_bytes, length=16):
    '''
    Dump hexadecimal
    :param src_bytes: Bytes, Bytearray, or compatible object
    :param length: Length of each output line
    :return:
    '''
    result = []
    digits = 4  # python2 code, all python3 strings are Unicode: if isinstance(src, unicode) else 2

    # for i in xrange(0, len(src), length):
    for i in range(0, len(src_bytes), length):
        s = src_bytes[i:i + length]
        hexa = ' '.join(["%0*X" % (digits, x) for x in s])
        text = ''.join([chr(x) if 0x20 <= x < 0x7F else '.' for x in s])
        result.append('%04X   %-*s   %s' % (i, length * (digits + 1), hexa, text))

    print('\n'.join(result))


# Fake Server
fakeServer_host = "3.93.128.89"
fakeServer_port = 12022
fakeServer = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
fakeServer.connect((fakeServer_host, fakeServer_port))
fakeServer_response = fakeServer.recv(4096)
print('\nFake Server:')
print(fakeServer_response)

input('Enter the Fake Server ID in the client then press Enter to continue.')

# Real Server
realServer_host = "3.93.128.89"
realServer_port = 12021
realServer = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
realServer.connect((realServer_host, realServer_port))
realServer_response = realServer.recv(4096)
print('\nReal Server:')
print(realServer_response, 'length:', len(realServer_response))
print('Hexdump:')
hexdump(realServer_response)


while True:
    # send the real server to fake server
    print('sending real->fake')
    fakeServer.send(realServer_response)
    print('receiving fake')
    fakeServer_response = fakeServer.recv(4096)
    print('fakeServer_response', fakeServer_response)
    hexdump(fakeServer_response)

    # send the fake server to real server
    print('sending fake->real')
    realServer.send(fakeServer_response)
    print('receiving real')
    realServer_response = realServer.recv(4096)  # might need more than 4096
    print('realServer_response', realServer_response)
    hexdump(realServer_response)
