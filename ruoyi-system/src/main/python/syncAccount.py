import sys

import socks
from telethon.tl.functions.users import GetFullUserRequest

from TGHelper import getContactNum, getGroupNumber, initLoginEntity
from myContact import myContact
import json
from telethon.tl.functions.contacts import GetContactsRequest, ImportContactsRequest, AddContactRequest
from telethon import TelegramClient, sync
from telethon.sessions import StringSession
from json import JSONEncoder

api_id =  sys.argv[1]
api_hash =  sys.argv[2]
sessionPath =  sys.argv[3]

json._default_encoder = JSONEncoder(ensure_ascii=False)
client = TelegramClient(StringSession(sessionPath), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
try:
    result = client.get_me()
    full =  client(GetFullUserRequest(result))
    ContactNum = getContactNum(client)
    groupnum = getGroupNumber(client)
    loginEntity = initLoginEntity(result.phone, result.phone, sessionPath,ContactNum, groupnum,result.username,result.first_name,result.last_name,full.full_user.about)

    print(json.dumps(loginEntity.__dict__).encode("UTF-8"))
except Exception as e:
    print("{"+'"code":"{}","msg":"{}"'.format(400,e)+"}")


