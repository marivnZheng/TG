
import json
import sys
from json import JSONEncoder

import socks
import telethon
from telethon.tl.functions.account import UpdateProfileRequest, UpdateUsernameRequest

from TGHelper import getContactNum, getGroupNumber, initLoginEntity
from telethon import TelegramClient
from telethon.sessions import StringSession
from telethon.tl.functions.users import GetFullUserRequest


api_id = sys.argv[1]
api_hash = sys.argv[2]
sessionPath = sys.argv[3]
firstName = sys.argv[4]
lastName = sys.argv[5]
about = sys.argv[6]
userName = sys.argv[7]


json._default_encoder=JSONEncoder(ensure_ascii=False)
client = TelegramClient(StringSession(sessionPath), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
try:
    client(UpdateProfileRequest(about=about, first_name=firstName, last_name=lastName))
    print("{" + '"code":"{}","msg":"{}"'.format(200, "成功修改") + "}")
except Exception as e:
    print("{" + '"code":"{}","msg":"{}"'.format(400, e) + "}")
