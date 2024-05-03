from sympy.ntheory import factorint
from sympy.core.numbers import mod_inverse

def rsa_decrypt(c, n, e):
    # Step 1: Factor N
    print(f'Factor N = {n}')
    factors = factorint(n)
    if len(factors) == 2 and all(val == 1 for val in factors.values()):
        p, q = factors.keys()
    else:
        raise ValueError("Failed to factor N into two distinct primes")

    # p = 1
    # print(f'p = {p}')
    # q = n
    # print(f'q = {q}')

    # Step 2: Compute phi(N)
    phi_n = (p - 1) * (q - 1)

    # Step 3: Compute the modular inverse of e
    d = mod_inverse(e, phi_n)

    # Step 4: Decrypt the message
    m = pow(c, d, n)
    return m

# Given values
c = 964354128913912393938480857590969826308054462950561875638492039363373779803642185
n = 1584586296183412107468474423529992275940096154074798537916936609523894209759157543
e = 65537

# Decrypt the message
try:
    decrypted_message = rsa_decrypt(c, n, e)
    print(f"Decrypted Message: {decrypted_message}")
except Exception as exc:
    print(f"Error: {exc}")