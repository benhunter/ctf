#!/usr/bin/env python3
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes

import sys
import zlib
import binascii


from firmware_util import *

'''
Firmware class encapsulates all the information needed to 
create a firmware image. It is also responsible for actually
generating the image
'''

class firmware(object):
    def __init__(self):
        self.revision = '2'
        self.name = 'V.i.r.t.u.a.l.K.E.Y'
        self.vin = ''
        self.user_title = ''

        self.firmware_code = open("./car_code.bin", "rb").read()[0:64]
        self.aes_key = None
        self.device_key = open("./flag", "r").read()

    def generate_image(self):
        img = "Rev:%s::Vin:%s::DeviceKey:%s::Name:%s::User_Title:%s::Code:%s" % (
                                           self.revision,       # 2
                                           self.vin,            # '00000000000000000'
                                           self.device_key,     # ACI{?}
                                           self.name,           # 'V.i.r.t.u.a.l.K.E.Y'
                                           self.user_title,     # "My Car" (could override to ' ')
                                           self.firmware_code)  # '' or 64 bytes
        
        
        img = "Rev:2::Vin:00000000000000000::DeviceKey:%s::Name:V.i.r.t.u.a.l.K.E.Y::User_Title:My Car::Code:" % (
            self.device_key)     # ACI{?}  # ASCII characters, digits, underscores
        
        
        #sys.stderr.write(img)
        #sys.stderr.write("\n")
        compressed_img = zlib.compress(img.encode('utf-8'), level=9)

        cipher = AES.new(self.aes_key, AES.MODE_GCM)  # nonce not passed - 16 byte nonce generated automatically
        # not reusing nonce...
        # https://pycryptodome.readthedocs.io/en/latest/src/cipher/aes.html
        final = cipher.encrypt_and_digest(compressed_img)[0]

        #sys.stderr.write("Legnth: %s" % len(final))

        if (len(final) > 229):
            print("[X] Fatal Error: Final Firmware Image too large: (%d bytes)" % len(final))
            sys.stdout.flush()
            return
        
        return final

class firmware_server(object):
    def __init__(self):
        self.flag = open("./flag", "rb").read()
        self.aes_key = get_random_bytes(16)  # 16*8 = 128 bit key
        
        self.fw = firmware()
        self.final_img = None

    def handle_create_firmware(self):
        self.fw.aes_key = self.aes_key

        print("Enter Vehicle VIN: ")
        vin = input()

        if not vin.isalnum() or len(vin) != 17:
            print("Invalid VIN")
            return 

        self.fw.vin = vin
        
        print("Vehicle Name (Blank for default):")
        name = input()

        if name == '':
            name = "My Car"
        
        self.fw.user_title = name

        print("Reflash Code? (y/n)")
        code = input()

        if code == 'n':
            self.fw.firmware_code = ''

        f = self.fw.generate_image()
        self.final_img = f
        print("Firmware Created Succesfully")
        sys.stdout.flush()

    def decrypt_firmware(self):
        print("Input HEX Encoded Firmware: ")
        fw_img = input()

        print("Input Encryption Nonce: ")
        nonce = input()

        print("Input Tag: ")
        tag = input()

        try:
            fw_img = binascii.unhexlify(fw_img)
            nonce = binascii.unhexlify(nonce)
            tag = binascii.unhexlify(tag)
        except Exception as e:
            #sys.stderr.write(str(e))
            print("Firmware Image must be Hex encoded")
            return

        try:
            cipher = AES.new(self.aes_key, AES.MODE_GCM, nonce=nonce)
            compressed = cipher.decrypt_and_verify(fw_img, tag)
        except:
            print("Decryption Failed!")
            return
    
        try:
            plain = zlib.decompress(compressed)
        except:
            print("Decompressing Failed")
            return
        
        return plain

    def handle_read_firmware(self):
        p = self.decrypt_firmware()
        
        if not p:
            return
        print(p)

    def handle_write_firmware(self):
        if not self.final_img:
            print("Firmware Image not created yet!")
            return

        print(binascii.hexlify(self.final_img))

    def serve(self):
        while True:
            print(MENU)
            msg = input()
            
            if msg.startswith("1"):
                self.handle_create_firmware()
            elif msg.startswith("2"):
                self.handle_read_firmware()
            elif msg.startswith("3"):
                self.handle_write_firmware()
            else:
                pass

if __name__ == '__main__':
    s = firmware_server()
    s.serve()
