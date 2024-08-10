
import sys

import socks
from telethon.errors import UserDeactivatedBanError
from telethon import TelegramClient,sync
from telethon.sessions import StringSession
from telethon.tl.types import PeerChannel

api_id = sys.argv[1]
api_hash = sys.argv[2]
mySession = sys.argv[3]
targetUser = sys.argv[4]
message = sys.argv[5]
filePath = sys.argv[6]
client = TelegramClient(StringSession(mySession), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
try:
    channel = client.get_entity(targetUser)
    if filePath != "None":
        client.send_file(channel, filePath, caption=message,parse_mode='html')
    else:
        message = client.send_message(channel, message,parse_mode='html')
    print("{" + '"code":"{}","msg":"{}"'.format(200, "发送成功") + "}")
except UserDeactivatedBanError as e:
    print("{" + '"code":"{}","msg":"{}"'.format(444, "账号已经封禁") + "}")
except Exception as e:
    print("{"+'"code":"{}","msg":"{}"'.format(400,e)+"}")
