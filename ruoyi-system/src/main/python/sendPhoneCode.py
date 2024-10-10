
import sys

import socks
from telethon import TelegramClient,sync

api_id =  sys.argv[1]
api_hash = sys.argv[2]
phone = sys.argv[3]



client = TelegramClient(phone, api_id, api_hash,proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
if not client.is_user_authorized():
    try:
        phone_code_hash = client.send_code_request(phone).phone_code_hash
        print(phone_code_hash)
    except Exception  as e:
        print(e)