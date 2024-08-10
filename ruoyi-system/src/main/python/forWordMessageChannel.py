import sys

import socks
import telethon
from telethon.errors import UserDeactivatedBanError
from telethon import TelegramClient,sync
from telethon.sessions import StringSession

api_id = sys.argv[1]
api_hash = sys.argv[2]
mySession = sys.argv[3]
targetUser = sys.argv[4]
messageId = sys.argv[5]
charId = sys.argv[6]
client = TelegramClient(StringSession(mySession), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
try:
    user = client.get_entity(targetUser)
    messages = client.get_entity(charId)
    client.forward_messages(user.id, int(messageId), messages.id)
    print("{"+'"code":"{}","msg":"{}"'.format(200,"发送成功")+"}")
except UserDeactivatedBanError as e:
    print("{" + '"code":"{}","msg":"{}"'.format(444, "账号已经封禁") + "}")
except Exception as e:
    print("{"+'"code":"{}","msg":"{}"'.format(400, e)+"}")
