# decrypt.py

import binascii

enc = b''

with open('./Over Time Paid/document.encrypted') as f:
    hexenc = f.readlines()

for line in hexenc:
    enc += binascii.unhexlify(line.strip())
print(enc)

# decoded = enc.decode('utf-8')
# print(decoded)


INTRO = b"The following encoded individuals are to be given a $27.3k bonus:".ljust(63) + b"\n"
# 10 lines 64 bytes long random
# ' ' * 63 + '\n'
OUTRO = "Furthermore, the FLAG is:".ljust(63) + "\n"
# flag.ljust(63) + '\n'

# print()

OTP_LENGTH = 64
otp = bytearray(b'\x00' * OTP_LENGTH)


# xor cipher with known plaintext = OTP
def decrypt(data: bytes) -> bytes:
    global otp
    decrypted = []

    # known plaintext attack against OTP
    for i in range(0, OTP_LENGTH):
        xored = data[i] ^ INTRO[i % len(otp)]
        otp[i % len(otp)] = xored

    otp = bytes(otp)

    for i in range(OTP_LENGTH, len(data)):
        xored = data[i] ^ otp[i % len(otp)]
        decrypted.append(xored)
    
    return bytes(decrypted)

d = decrypt(enc)
print(d)
