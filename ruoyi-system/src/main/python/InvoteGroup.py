
import json
import sys
from json import JSONEncoder

import socks
from telethon import TelegramClient,sync
from telethon.sessions import StringSession
from telethon.tl.functions.channels import InviteToChannelRequest
from telethon.tl.types import PeerChannel

api_id = sys.argv[1]
api_hash = sys.argv[2]
sessionPath=sys.argv[3]
channelId=sys.argv[4]
sysContactUserName=sys.argv[5]

json._default_encoder=JSONEncoder(ensure_ascii=False)
client = TelegramClient(StringSession(sessionPath), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
if client.is_user_authorized() :
    try:
        result = client.get_entity(channelId);
        var = client(InviteToChannelRequest(
            chat_id=result,  # chat_id
            user_id =[sysContactUserName],  # 被邀请人id
        ))
        print("{" + '"code":"{}","msg":"{}"'.format(200, "邀请成功") + "}")
    except Exception as e:
        print("{"+'"code":"{}","msg":"{}"'.format(400,e)+"}")
else:
    print("{"+'"code":"{}","msg":"{}"'.format(304,"账号登入状态失效")+"}")