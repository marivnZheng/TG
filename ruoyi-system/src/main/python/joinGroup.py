
import json
import sys
from json import JSONEncoder

from telethon.errors import UserDeactivatedBanError
import socks
from telethon import TelegramClient,sync
from telethon.sessions import StringSession
from telethon.tl.functions.channels import JoinChannelRequest

api_id = sys.argv[1]
api_hash = sys.argv[2]
sessionPath= sys.argv[3]
link = sys.argv[4]

json._default_encoder=JSONEncoder(ensure_ascii=False)
client = TelegramClient(StringSession(sessionPath), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
try:

    var =client.get_entity(link)
    client(JoinChannelRequest(var))
    print("{"+'"code":"{}","msg":"{}"'.format(200, "成功加入")+"}")
except UserDeactivatedBanError as e:
    print("{" + '"code":"{}","msg":"{}"'.format(444, "账号已经封禁") + "}")
except Exception as e:
    print("{"+'"code":"{}","msg":"{}"'.format(400, e)+"}")


