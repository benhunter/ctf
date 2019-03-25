import os, binascii

from Crypto.Cipher import AES

BLOCK_SIZE = 16


flag = ''  # Solve this
plaintext = """The Song of the Count

You know that I am called the Count
Because I really love to count
I could sit and count all day
Sometimes I get carried away
I count slowly, slowly, slowly getting faster
Once I've started counting it's really hard to stop
Faster, faster. It is so exciting!
I could count forever, count until I drop
1! 2! 3! 4!
1-2-3-4, 1-2-3-4,
1-2, i love couning whatever the ammount haha!
1-2-3-4, heyyayayay heyayayay that's the sound of the count
I count the spiders on the wall...
I count the cobwebs in the hall...
I count the candles on the shelf...
When I'm alone, I count myself!
I count slowly, slowly, slowly getting faster
Once I've started counting it's really hard to stop
Faster, faster. It is so exciting!
I could count forever, count until I drop
1! 2! 3! 4!
1-2-3-4, 1-2-3-4, 1,
2 I love counting whatever the
ammount! 1-2-3-4 heyayayay heayayay 1-2-3-4
That's the song of the Count!
""" + flag  # plus padding to 16 byte block is added

# hex-encoded ciphertext
ciphertext_hex = "9d5c66e65fae92af9c8a55d9d3bf640e8a5b76a878cbf691d3901392c9b8760ebd5c62b22c88dca9d1c55098cbbb644ae9406ba32c8293bdd29139bbc2b4605bba51238f2cb399a9d0894ad9cbb8774be9406ce66fae89a6c8ef7ad9c4b87442ad1470af78e19da6d8c55096d2b9750ea8586fe668a085c2ef8a5e9cd3be6c4bba144ae66ba488e8df84418bceb2650ea84362bf0688dcabd3905d8d87a46d414626be04a58215b84263a620de3203fa4626be08e2940da35c61b82c98201ce15438cd67eb921cf77c28a969de321bf4433ea24ca59216a25b7bb662996106e11639e75ae09015bb4c2fb76d8c254fe15e6ab45cea817391547cab698c6d4ff35039b34df7df599e412fb67fde3200b55432a441f19817b01405962c9d2e1af9556aa447f09f0df75360ad6988241db91129a85deb8559a25b7bb660de084ff1cc3a0e8041bc71d71fb29883931d9d3e8f784ca743b065c91ea386909e1a9100925f4fa742b1718c1efec4d4d609df5bcb3b17e417bd268d5fe6ced4d65b9c40d6305eeb1df03e9050e68bcad241dd15b46453b85dae7cd112b2c3c7ca50dd4ddf2c1ff350f5349c5febcadbd2509c40d6340aad03bd258d5bb2d8cdc647d814d1335efe18f8718651e7c5d6b9609c57d12010fe50e939801ee1dbcbd74cce4739c1eeceba8ef87360b629ed82a6eb9d508ee381bb88e97363bf20a1cfe7a7e07cccf3cea788bd277fb265e9cde4a9b937808aa7ee85f22679a365f5c4ede5f478c0e482ab95bd3c79f731e9c9a8b6ff7cc2e6c0e0c897047fb22ba1e5afa8b778c2ef80abcabd1a37b42af4c2fce5fa60dde582a8c7971a37b42af4c2fce5e475c1f782b7cabd207bb832edd5a4e5e4130a976ad4a2fe86ac99c18491f9f6c86adae59cc4a9f33072f70ca6daede5e40b049272c8e6b980b798c69e9fb7f7891611c7758df0fc82b481d1ca9eb8e2cd5f118f26def6f693d2abc99982bce2855f038175d9e7ebcdf8a4dcca9faab0da1045857eceebed8ab68a89e0bff9f3c60a098426ceedec8daccdce8584bce6cc0d49c065c2f7f797f898c69e9fb5b0e05f019269dd88a8c2f8df89cac5f8b09d00ad16dc760ead7c3518baed47603c5b5251cc269cae93d1f8a4888699aff58942c8529f304af0362143f2bd1e37670d538753992129ff3c6c5befb21e7331590c950ac26917be39644dfba50b2b70117fcbccba57b209ae95721a1c9d36073d934b75014109102be14fb6a044a7cf9e468748976457f6342177f5a9042630622f97d2ba5a8c04a7890d4e5fcb445b7600d7c1be71b711b6b32b4444f078557ef82e4f0559225437b455aa9a0bbaff89c834531a45115125c933d6cd6cdca8f8"


encrypted = b"TEST"
# print(encrypted.encode("hex"))
# print(binascii.hexlify(encrypted))
ciphertext = binascii.unhexlify(ciphertext_hex)
print('ciphertext', ciphertext)
print('len(ciphertext)', len(ciphertext))
print('type(ciphertext)', type(ciphertext))
num_blocks = len(ciphertext) // BLOCK_SIZE
print('number of blocks', num_blocks, ', remainder ', len(ciphertext) % BLOCK_SIZE)

def groups(seq, length):
    '''
    Yield groups of specified length from a sequence. The final yield will provide whatever data is left in the
    sequence, without padding. Useful in a for statement:
        for pair in groups('abcdefg', 2):
            do_the_thing(pair)
    :param seq: A slicable object like string or list.
    :param length: The length of each group (ie. 2 for pairs)
    :return:
    '''
    for i in range(0, len(seq), length):
        # print(i)
        yield seq[i:i + length]

def aes_ecb_encrypt(data, key):
    '''
    Encrypt bytes using AES ECB algorithm.
    :param data:
    :param key:
    :return:
    '''
    diff = BLOCK_SIZE - (len(data) % BLOCK_SIZE)
    padding = bytearray([diff for x in range(diff)])
    return AES.new(key=key, mode=AES.MODE_ECB).encrypt(data + padding)


def random_aes_key():
    # return secrets.token_bytes(16)
    return os.urandom(16)


def test_decrypt_ecb_byte_at_time():
    global plaintext
    detected_block_length = 16 # block size 16
    random_key = random_aes_key()

    solved_plaintext = b''


    for block in range((len(ciphertext) // detected_block_length) + 1):
        for position in range(detected_block_length, 0, -1):
            short_block = b'A' * (position - 1)
            print('1. block:', block, 'short_block:', short_block, 'length:', len(short_block))
            known_crypt = b''

            # build lookup table for unknown last character
            lastchar_dict = {}
            for i in range(256):
                my_str = short_block + solved_plaintext + chr(i).encode()
                # print('2. my_str', my_str)
                lastchar_dict[aes_ecb_encrypt(my_str, random_key)[
                              detected_block_length * block:detected_block_length * (block + 1)]] = chr(i)

            # print('3. lastchar_dict')
            # pprint(lastchar_dict)
            # print('length lastchar_dict:', len(lastchar_dict))

            # print('4. plaintext before oracle:', short_block + base64.b64decode(unknown_str), 'length:',
            #       len(short_block + base64.b64decode(unknown_str)))

            crypt_block = aes_ecb_encrypt(short_block + ciphertext, random_key)[
                          detected_block_length * block:detected_block_length * (block + 1)]
            print('5. crypt_block:', crypt_block, 'len(crypt_block):', len(crypt_block))

            # if last byte of block is \x01, padding may be in use
            if lastchar_dict[crypt_block] == '\x01':
                print('Possible padding detected by last character.')
                break
            try:
                print('6. char:', lastchar_dict[crypt_block], ord(lastchar_dict[crypt_block]), 'type:',
                      type(lastchar_dict[crypt_block]))
            except KeyError as ke:
                print('Possible padding detected by KeyError.')
                print(ke)
                break

            solved_plaintext += lastchar_dict[crypt_block].encode()
            print('7. plaintext solved', solved_plaintext, 'len(plaintext)', len(solved_plaintext))

        print('8. plaintext:', plaintext)
    # assert plaintext == b"Rollin' in my 5.0\nWith my rag-top down so my hair can blow\nThe girlies on standby waving just to say hi\nDid you stop? No, I just drove by\n"
    # assert plaintext == ciphertext

# test_decrypt_ecb_byte_at_time()

def pad(data):
    pad_byte = 16 - len(data) % 16

    chr_pad_byte = chr(pad_byte)
    byte_chr_pad_byte = bytes(chr_pad_byte, 'utf8')
    padding = (chr_pad_byte * pad_byte)
    padded_data = data + padding
    return padded_data


def worker_function(block):
    global counter
    key_stream = aes.encrypt(pad(str(counter)))
    result = xor_string(block, key_stream)
    counter += 1
    return result


def possible_keys():
    global num_blocks
    keys = []
    for i in range(num_blocks):
        padded = pad(str(i))
        # print(padded, bytes(padded, 'utf8'))
        bytes_padded = bytes(padded, 'utf8')
        keys.append(bytes_padded)
    return keys


first_block = next(groups(ciphertext, BLOCK_SIZE))
print('first_block', first_block)
print('type(first_block)', type(first_block))

keys = possible_keys()
print(type(keys[0]))
print()
for key in keys:
    plainblock = AES.new(key, AES.MODE_ECB).decrypt(first_block)
    # print(plainblock)
    try:
        print(binascii.unhexlify(plainblock))
    except binascii.Error:
        pass




