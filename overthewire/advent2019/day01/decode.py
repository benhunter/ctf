import csv
# import os

# read sms.csv and output the message

KEYMAP = {'0': " 0",
                '1': ".,'?!\"1-()@/:",
                '2': "abc2",
                '3': "def3",
                '4': "ghi4",
                '5': "jkl5",
                '6': "mno6",
                '7': "pqrs7",
                '8': "tuv8",
                '9': "wxyz9",
                '10': "@/:_;+&%*[]{}"}
# CONTROL_KEYS = 


def decode_key(key, next=''):
    try:
        value = KEYMAP[key]
        # print('value', value)
        if next:
            # print('next', next)
            value = value[value.find(next)+1]
            # need to iterate circularly

        else:
            value = value[0]
    except KeyError:
        # print("Key not found:", key)
        value = key
    finally:
        # print('value', value)
        return value
    

def decode_message(keylog):
    message = []
    # print(message)
    for position, key in enumerate(keylog):
        if position == 0:
            message.append(decode_key(key[1]))
            continue

        # print('key', key)
        # look back
        value = decode_key(key[1])

        if value == '101':
            # message.pop()  # remove last item, backspace
            message.append(value)
            continue

        # compare raw button to previous and make sure the keypress was within 1000 milliseconds of the last
        if key[1] == keylog[position-1][1] and int(key[0]) - int(keylog[position-1][0]) < 1000:
            message[-1] = decode_key(key[1], message[-1])
        else:
            message.append(value)
        # message.append(value)
    return message



if __name__ == "__main__":
    # print(os.getcwd())
    with open("sms4.csv") as csvfile:
        smsreader = csv.reader(csvfile)
        sms = [line for line in smsreader]
    # print(sms)
    decoded = decode_message(sms)
    print(''.join(decoded))
