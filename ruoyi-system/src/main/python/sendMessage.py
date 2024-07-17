import sys

import socks
import telethon
from telethon import TelegramClient,sync
from telethon.sessions import StringSession

api_id = sys.argv[1]
api_hash = sys.argv[2]
mySession = sys.argv[3]
targetUser = sys.argv[4]
message = sys.argv[5]
filePath = sys.argv[6]
client = TelegramClient(StringSession(mySession), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
try:
    user = client.get_entity(targetUser)
    if  message == "None":
        message = ""
    if filePath != "None":
        result = client.send_file(user,filePath, caption=message)
        print("{"+'"code":"{}","msg":"{}"'.format(200,"发送成功")+"}")
    else:
        result = client.send_message(user, message)
        print("{"+'"code":"{}","msg":"{}"'.format(200,"发送成功")+"}")
except Exception as e:

    print("{"+'"code":"{}","msg":"{}"'.format(400, e)+"}")
