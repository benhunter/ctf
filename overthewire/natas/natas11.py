import urllib.parse
import base64


def repeating_key_xor(text, key):
    return fixed_xor(text, expand_str(key, len(text)))


def fixed_xor(one, two):
    '''
    Returns XOR combination of two equal length bytestrings.
    :param one: Bytes
    :param two: Bytes
    :return: Bytes
    '''

    result = b''

    # print(len(one))
    # test equal len
    if len(one) != len(two):
        raise ValueError('Parameter lengths are not equal.', len(one), len(two), one, two)
    # xor byte by byte

    for i in range(len(one)):
        # print(one, one[i], type(one[i]))
        xor_result = one[i] ^ two[i]
        result += bytes([xor_result])
        # result += format(one[i] ^ two[i], 'x')
        # print(result, type(result))

    # print('fixed_xor:', type(result), result)
    return result


def expand_str(text, length):
    return (text * (length // len(text) + 1))[:length]




# encoded_json = b'{"bgcolor":"#ffffff","showpassword":"no"}'
encoded_json = '{"showpassword":"no","bgcolor":"#ffffff"}'
cookie_url_encoded = 'ClVLIh4ASCsCBE8lAxMacFMZV2hdVVotEhhUJQNVAmhSEV4sFxFeaAw%3D'

cookie_b64 = urllib.parse.unquote(cookie_url_encoded)
print('cookie_b64', cookie_b64)

cookie_xor = base64.b64decode(cookie_b64)

print(cookie_xor)

print('length encoded_json', len(encoded_json))
print('length cookie_xor', len(cookie_xor))

solution = []

for i in range(len(encoded_json)):
    # print(i, cookie_xor[i], encoded_json[i], ord(encoded_json[i]))
    for key in range(256):
        # print(chr(key))
        # print(cookie_xor[i] ^ key)
        if cookie_xor[i] ^ key == ord(encoded_json[i]):
            print(i, cookie_xor[i])
            solution.append(chr(key))
            break

print(solution)
solution_str = ''.join(solution)
print(solution_str)
print(repr(solution_str))

print('\nNow modify cookie\n')

xor_key = b'qw8J'


# evil_encoded_json = '{"bgcolor":"#ffffff","showpassword":"yes"}'
evil_encoded_json = b'{"showpassword":"yes","bgcolor":"#ffffff"}'
# xor, b64, urlencode

evil_xor = repeating_key_xor(evil_encoded_json, xor_key)
evil_b64 = base64.b64encode(evil_xor)
print(evil_b64)
# evil_urlencode = urllib.parse.quote(evil_b64)
# print(evil_urlencode)

