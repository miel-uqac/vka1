import adafruit_ble
from adafruit_ble.advertising.standard import ProvideServicesAdvertisement
from adafruit_ble.services.standard.hid import HIDService
import usb_cdc
import time
import board
import digitalio
from adafruit_hid.keyboard import Keyboard
from adafruit_hid.keyboard_layout_us import KeyboardLayoutUS

# Configuration des LEDs
led_red = digitalio.DigitalInOut(board.LED_RED)
led_green = digitalio.DigitalInOut(board.LED_GREEN)
led_blue = digitalio.DigitalInOut(board.LED_BLUE)
led_red.direction = digitalio.Direction.OUTPUT
led_green.direction = digitalio.Direction.OUTPUT
led_blue.direction = digitalio.Direction.OUTPUT

def set_leds(blue, green, red):
    led_blue.value = not blue
    led_green.value = not green
    led_red.value = not red

# BLE HID Setup
ble = adafruit_ble.BLERadio()
hid = HIDService()
advertisement = ProvideServicesAdvertisement(hid)
advertisement.complete_name = "VKA Keyboard"

# Initialisation HID Keyboard
keyboard = Keyboard(hid.devices)
keyboard_layout = KeyboardLayoutUS(keyboard)

# Pas de saut de ligne
def write(text):
    for char in text:
        keyboard_layout.write(char)

# Démarre la publicité BLE
print("Starting BLE advertising...")
set_leds(blue=True, green=False, red=False)
ble.start_advertising(advertisement)

# Boucle principale
while True:
    try:
        if usb_cdc.console.connected and usb_cdc.console.in_waiting > 0:
            command = usb_cdc.console.readline().decode("utf-8").strip()
            print(f"Command received via USB: {command}")

            if ble.connected:
                set_leds(blue=False, green=True, red=False)
                print(f"Sending command via HID: {command}")
                write(command)
            else:
                set_leds(blue=False, green=False, red=True)
                print("BLE not connected.")

        if not ble.connected and not ble.advertising:
            print("BLE disconnected. Restarting advertising...")
            set_leds(blue=True, green=False, red=False)
            ble.start_advertising(advertisement)

    except Exception as e:
        print(f"Error: {e}")
        set_leds(blue=False, green=False, red=True)
        time.sleep(1)