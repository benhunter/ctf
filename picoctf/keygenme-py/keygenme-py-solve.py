# PicoCTF
# https://play.picoctf.org/practice/challenge/121

import hashlib

def check_key(key, username_trial):
    global key_full_template_trial

    if len(key) != len(key_full_template_trial):
        return False
    else:
        # Check static base key part --v
        i = 0
        for c in key_part_static1_trial:
            if key[i] != c:
                return False
            i += 1

        # TODO : test performance on toolbox container
        # Check dynamic part --v
        if key[i] != hashlib.sha256(username_trial).hexdigest()[4]:
            return False
        else:
            i += 1
        if key[i] != hashlib.sha256(username_trial).hexdigest()[5]:
            return False
        else:
            i += 1
        if key[i] != hashlib.sha256(username_trial).hexdigest()[3]:
            return False
        else:
            i += 1
        if key[i] != hashlib.sha256(username_trial).hexdigest()[6]:
            return False
        else:
            i += 1
        if key[i] != hashlib.sha256(username_trial).hexdigest()[2]:
            return False
        else:
            i += 1
        if key[i] != hashlib.sha256(username_trial).hexdigest()[7]:
            return False
        else:
            i += 1
        if key[i] != hashlib.sha256(username_trial).hexdigest()[1]:
            return False
        else:
            i += 1
        if key[i] != hashlib.sha256(username_trial).hexdigest()[8]:
            return False
        return True


username_trial = "GOUGH"
bUsername_trial = b"GOUGH"

key_part_static1_trial = "picoCTF{1n_7h3_|<3y_of_"
key_part_dynamic1_trial = "xxxxxxxx"
key_part_static2_trial = "}"
key_full_template_trial = key_part_static1_trial + key_part_dynamic1_trial + key_part_static2_trial

# Template key
# picoCTF{1n_7h3_|<3y_of_xxxxxxxx}

digest = hashlib.sha256(bUsername_trial).hexdigest()
print(f'digest: {digest}')

bytes = [4, 5, 3, 6, 2, 7, 1, 8]
digest_reordered = [digest[i] for i in bytes]
print(f'digest_reordered: {digest_reordered}')

key_part_dynamic1_trial = "".join(digest_reordered)
key_full_template_trial = key_part_static1_trial + key_part_dynamic1_trial + key_part_static2_trial
print(f'key_full_template_trial: {key_full_template_trial}')
check_key_result = check_key(key_full_template_trial, bUsername_trial)
print(f'check_key(key_full_template_trial, bUsername_trial): {check_key_result}')

# picoCTF{1n_7h3_|<3y_of_f911a486}
if check_key_result:
    print(f'Key: {key_full_template_trial}')
