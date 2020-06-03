"""
The following Python implementation of Shamir's Secret Sharing is
released into the Public Domain under the terms of CC0 and OWFa:
https://creativecommons.org/publicdomain/zero/1.0/
http://www.openwebfoundation.org/legal/the-owf-1-0-agreements/owfa-1-0

See the bottom few lines for usage. Tested on Python 2 and 3.
"""

from __future__ import division
from __future__ import print_function

import random
import functools
import base64

# 12th Mersenne Prime
# (for this application we want a known prime number as close as
# possible to our security level; e.g.  desired security level of 128
# bits -- too large and all the ciphertext is large; too small and
# security is compromised)
_PRIME = 2 ** 127 - 1
# 13th Mersenne Prime is 2**521 - 1

_RINT = functools.partial(random.SystemRandom().randint, 0)

def _eval_at(poly, x, prime):
    """Evaluates polynomial (coefficient tuple) at x, used to generate a
    shamir pool in make_random_shares below.
    """
    accum = 0
    for coeff in reversed(poly):
        accum *= x
        accum += coeff
        # accum %= prime
    return accum

def make_random_shares(minimum, shares, prime=_PRIME):
    """
    Generates a random shamir pool, returns the secret and the share
    points.
    """
    if minimum > shares:
        raise ValueError("Pool secret would be irrecoverable.")
    poly = [_RINT(prime - 1) for i in range(minimum)]
    points = [(i, _eval_at(poly, i, prime))
              for i in range(1, shares + 1)]
    return poly[0], points

def _extended_gcd(a, b):
    """
    Division in integers modulus p means finding the inverse of the
    denominator modulo p and then multiplying the numerator by this
    inverse (Note: inverse of A is B such that A*B % p == 1) this can
    be computed via extended Euclidean algorithm
    http://en.wikipedia.org/wiki/Modular_multiplicative_inverse#Computation
    """
    x = 0
    last_x = 1
    y = 1
    last_y = 0
    while b != 0:
        quot = a // b
        a, b = b, a % b
        x, last_x = last_x - quot * x, x
        y, last_y = last_y - quot * y, y
    return last_x, last_y


def _divmod(num, den, p):
    """Compute num / den modulo prime p

    To explain what this means, the return value will be such that
    the following is true: den * _divmod(num, den, p) % p == num
    """
    inv, _ = _extended_gcd(den, p)
    return num * inv


def _lagrange_interpolate(x, x_s, y_s, p):
    """
    Find the y-value for the given x, given n (x, y) points;
    k points will define a polynomial of up to kth order.
    """
    k = len(x_s)
    assert k == len(set(x_s)), "points must be distinct"

    def PI(vals):  # upper-case PI -- product of inputs
        accum = 1
        for v in vals:
            accum *= v
        return accum

    nums = []  # avoid inexact division
    dens = []
    for i in range(k):
        others = list(x_s)
        cur = others.pop(i)
        nums.append(PI(x - o for o in others))
        dens.append(PI(cur - o for o in others))
    den = PI(dens)
    num = sum([_divmod(nums[i] * den * y_s[i] % p, dens[i], p)
               for i in range(k)])

    # [(nums[i] * den * y_s[i]) / dens[i] for i in range(k)]

    # num = sum([
    #         (nums[i] * den * y_s[i]) / dens[i]
    #         for i in range(k)]
    #     )

    return (_divmod(num, den, p) + p) % p
    # return num / den

def _lagrange_interpolate_noprime(x, x_s, y_s):
    """
    Find the y-value for the given x, given n (x, y) points;
    k points will define a polynomial of up to kth order.
    """
    k = len(x_s)
    assert k == len(set(x_s)), "points must be distinct"

    def PI(vals):  # upper-case PI -- product of inputs
        accum = 1
        for v in vals:
            accum *= v
        return accum

    nums = []  # avoid inexact division
    dens = []
    for i in range(k):
        others = list(x_s)
        cur = others.pop(i)
        nums.append(PI(x - o for o in others))
        dens.append(PI(cur - o for o in others))
    den = PI(dens)
    # num = sum([_divmod(nums[i] * den * y_s[i] % p, dens[i], p)
    #            for i in range(k)])

    # [(nums[i] * den * y_s[i]) / dens[i] for i in range(k)]

    num = sum([
            (nums[i] * den * y_s[i]) / dens[i]
            for i in range(k)]
        )

    # return (_divmod(num, den, p) + p) % p
    return num / den


def recover_secret(shares, prime=_PRIME):
    """
    Recover the secret from share points
    (x, y points on the polynomial).
    """
    if len(shares) < 2:
        raise ValueError("need at least two shares")
    x_s, y_s = zip(*shares)
    return _lagrange_interpolate(0, x_s, y_s, prime)

def recover_secret_noprime(shares):
    """
    Recover the secret from share points
    (x, y points on the polynomial).
    """
    if len(shares) < 2:
        raise ValueError("need at least two shares")
    x_s, y_s = zip(*shares)
    return _lagrange_interpolate_noprime(0, x_s, y_s)


def recover_ctf():
    shares = [
        (1, 17870330202029034097213593538499510188566476749593017613688108594674466005074684028857900869061774723488629527762023298115201407881821228772474275941016002),
        (3, 215989352922700286649929361116996115814653979395320906636047757613334444364570802876847548317251913770823706737660321581482056076553326019573376997751727388),
        (5, 804324829089228747032780267845145277086215604128778699231769090990838539183332149138461396224926389520272393211212965671712719951862327213252031737720492838),
        (2, 81181182658410481546235862320115426694441067053416281566701505703131144726924801099299917958520184004624634801912174540138893756528465484769758891830451563),
        (4, 448351636548454393711156644701289943846235422259158007598734430613248773117033111507801228790854727712551180989322055074541170698237560655096433108212516729),
        ]
    # shares = [
    #     '1-0155345ac0739a5b76d982a11853e915a311fb6bde2ee880a93d2af5ac73f0a9c00ebdca6311ee981da0af0ce0c1ef733747111e513b7429e2ed2a2bfd14c761c2',
    #     '2-060e055ae7947c49b246fe0b6efef63a99087654198f0803d021392ba57ef17739ecf93d4a569b57f914a2a51e3afa1f180b61c3fe90790120c6ac57f287f09d6b',
    #     '3-101bf5f7242c0784ece38d91bd332a948582d3c27c9da35be9f95e47c17d2bd9d0994d43dac85d822148e0c4d0d1bc99515a4ee32d8fefbb6fb96ffdee068b251c',
    #     '4-2170892625039dc7614a4c86bc2289490c2076c0d1d7ff5b6c12cdefd6cac942e6d1117fbe2e541fc1f13637aa87a03f3109d237cc93872d51bb2d67c60b740779',
    #     '5-3bfd41de98e4a0cb4a16563d24ff157dd080c258e3bb60d4cbbabbc9bbc3f323df519d929e4f9e3a06c16fc95d5e0e6e04efe57dc9f4ee2b48c19cdf5111885326']

    # prime = 2147483647

    s = recover_secret(shares[:-1])
    print(s)
    print(recover_secret(shares[1:]))
    s = recover_secret(shares)
    print(s)
    print()

    s = recover_secret_noprime(shares[:-1])
    print(s)
    print(recover_secret_noprime(shares[1:]))
    s = recover_secret_noprime(shares)
    print(s)

    return s


def main():
    """Main function"""
    secret, shares = make_random_shares(minimum=3, shares=6)

    print('Secret:                                                     ',
          secret)
    print('Shares:')
    if shares:
        for share in shares:
            print('  ', share)

    print('Secret recovered from minimum subset of shares:             ',
          recover_secret(shares[:3]))
    print('Secret recovered from a different minimum subset of shares: ',
          recover_secret(shares[-3:]))
    
    
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


if __name__ == '__main__':
    # main()
    s = recover_ctf()
    print(s)

'''
s
76979881306162460288581436086217659456
dec_hex(s)
'39e9c92d9da2d3faa9a39103fc98d440'
dec_raw(s)
UnicodeDecodeError: 'utf-8' codec can't decode byte 0xe9 in position 1: invalid continuation byte
dec_b64(s)
'OenJLZ2i0/qpo5ED/JjUQA=='
dec_oct(s)
'717234445547321323765246434420177446152100'
dec_bin(s)
'111001111010011100100100101101100111011010001011010011111110101010100110100011100100010000001111111100100110001101010001000000'
'''