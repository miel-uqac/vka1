# SPDX-FileCopyrightText: 2021 Neradoc NeraOnGit@ri1.fr
#
# SPDX-License-Identifier: MIT
"""
This file was automatically generated using Circuitpython_Keyboard_Layouts
"""


__version__ = "0.0.0-auto.0"
__repo__ = "https://github.com/Neradoc/Circuitpython_Keyboard_Layouts.git"


from adafruit_hid.keyboard_layout_base import KeyboardLayoutBase
class KeyboardLayoutFR(KeyboardLayoutBase):
    ASCII_TO_KEYCODE = (
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x2a'  # BACKSPACE
        b'\x2b'  # '\t'
        b'\x28'  # '\n'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x29'  # ESC
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x00'
        b'\x2c'  # ' '
        b'\x38'  # '!'
        b'\x20'  # '"'
        b'\x20'  # '#'
        b'\x30'  # '$'
        b'\xb4'  # '%'
        b'\x1e'  # '&'
        b'\x21'  # "'"
        b'\x22'  # '('
        b'\x2d'  # ')'
        b'\x31'  # '*'
        b'\xae'  # '+'
        b'\x10'  # ','
        b'\x23'  # '-'
        b'\xb6'  # '.'
        b'\xb7'  # '/'
        b'\xa7'  # '0'
        b'\x9e'  # '1'
        b'\x9f'  # '2'
        b'\xa0'  # '3'
        b'\xa1'  # '4'
        b'\xa2'  # '5'
        b'\xa3'  # '6'
        b'\xa4'  # '7'
        b'\xa5'  # '8'
        b'\xa6'  # '9'
        b'\x37'  # ':'
        b'\x36'  # ';'
        b'\x64'  # '<'
        b'\x2e'  # '='
        b'\xe4'  # '>'
        b'\x90'  # '?'
        b'\x27'  # '@'
        b'\x94'  # 'A'
        b'\x85'  # 'B'
        b'\x86'  # 'C'
        b'\x87'  # 'D'
        b'\x88'  # 'E'
        b'\x89'  # 'F'
        b'\x8a'  # 'G'
        b'\x8b'  # 'H'
        b'\x8c'  # 'I'
        b'\x8d'  # 'J'
        b'\x8e'  # 'K'
        b'\x8f'  # 'L'
        b'\xb3'  # 'M'
        b'\x91'  # 'N'
        b'\x92'  # 'O'
        b'\x93'  # 'P'
        b'\x84'  # 'Q'
        b'\x95'  # 'R'
        b'\x96'  # 'S'
        b'\x97'  # 'T'
        b'\x98'  # 'U'
        b'\x99'  # 'V'
        b'\x9d'  # 'W'
        b'\x9b'  # 'X'
        b'\x9c'  # 'Y'
        b'\x9a'  # 'Z'
        b'\x22'  # '['
        b'\x25'  # '\\'
        b'\x2d'  # ']'
        b'\x26'  # '^'
        b'\x25'  # '_'
        b'\x00'  # '`' (Dead key)
        b'\x14'  # 'a'
        b'\x05'  # 'b'
        b'\x06'  # 'c'
        b'\x07'  # 'd'
        b'\x08'  # 'e'
        b'\x09'  # 'f'
        b'\x0a'  # 'g'
        b'\x0b'  # 'h'
        b'\x0c'  # 'i'
        b'\x0d'  # 'j'
        b'\x0e'  # 'k'
        b'\x0f'  # 'l'
        b'\x33'  # 'm'
        b'\x11'  # 'n'
        b'\x12'  # 'o'
        b'\x13'  # 'p'
        b'\x04'  # 'q'
        b'\x15'  # 'r'
        b'\x16'  # 's'
        b'\x17'  # 't'
        b'\x18'  # 'u'
        b'\x19'  # 'v'
        b'\x1d'  # 'w'
        b'\x1b'  # 'x'
        b'\x1c'  # 'y'
        b'\x1a'  # 'z'
        b'\x21'  # '{'
        b'\x23'  # '|'
        b'\x2e'  # '}'
        b'\x00'  # '~' (Dead key)
        b'\x00'
    )
    NEED_ALTGR = '#@[\\]^{|}¤€'
    HIGHER_ASCII = {
        0xe9: 0x1f,  # 'é'
        0xe8: 0x24,  # 'è'
        0xe7: 0x26,  # 'ç'
        0xe0: 0x27,  # 'à'
        0xb0: 0xad,  # '°'
        0x20ac: 0x08,  # '€'
        0xa3: 0xb0,  # '£'
        0xa4: 0x30,  # '¤'
        0xf9: 0x34,  # 'ù'
        0xb2: 0x35,  # '²'
        0xb5: 0xb1,  # 'µ'
        0xa7: 0xb8,  # '§'
    }
    COMBINED_KEYS = {
        0xe3: 0x1fe1,  # 'ã'
        0xc3: 0x1fc1,  # 'Ã'
        0xf1: 0x1fee,  # 'ñ'
        0xd1: 0x1fce,  # 'Ñ'
        0xf5: 0x1fef,  # 'õ'
        0xd5: 0x1fcf,  # 'Õ'
        0x7e: 0x1fa0,  # '~'
        0xe0: 0x24e1,  # 'à'
        0xe8: 0x24e5,  # 'è'
        0xec: 0x24e9,  # 'ì'
        0xf2: 0x24ef,  # 'ò'
        0xf9: 0x24f5,  # 'ù'
        0xc0: 0x24c1,  # 'À'
        0xc8: 0x24c5,  # 'È'
        0xcc: 0x24c9,  # 'Ì'
        0xd2: 0x24cf,  # 'Ò'
        0xd9: 0x24d5,  # 'Ù'
        0x60: 0x24a0,  # '`'
        0xe2: 0x2f61,  # 'â'
        0xea: 0x2f65,  # 'ê'
        0xee: 0x2f69,  # 'î'
        0xf4: 0x2f6f,  # 'ô'
        0xfb: 0x2f75,  # 'û'
        0xc2: 0x2f41,  # 'Â'
        0xca: 0x2f45,  # 'Ê'
        0xce: 0x2f49,  # 'Î'
        0xd4: 0x2f4f,  # 'Ô'
        0xdb: 0x2f55,  # 'Û'
        0x5e: 0x2f20,  # '^'
        0xe4: 0xaf61,  # 'ä'
        0xeb: 0xaf65,  # 'ë'
        0xef: 0xaf69,  # 'ï'
        0xf6: 0xaf6f,  # 'ö'
        0xfc: 0xaf75,  # 'ü'
        0xff: 0xaf79,  # 'ÿ'
        0xc4: 0xaf41,  # 'Ä'
        0xcb: 0xaf45,  # 'Ë'
        0xcf: 0xaf49,  # 'Ï'
        0xd6: 0xaf4f,  # 'Ö'
        0xdc: 0xaf55,  # 'Ü'
        0xa8: 0xaf20,  # '¨'
    }
