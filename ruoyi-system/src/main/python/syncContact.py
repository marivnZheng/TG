import sys

import socks as socks

from myContact import myContact
import json
from telethon.tl.functions.contacts import GetContactsRequest, ImportContactsRequest, AddContactRequest
from telethon import TelegramClient, sync
from telethon.sessions import StringSession
from json import JSONEncoder
from telethon.errors import UserDeactivatedBanError

api_id =  sys.argv[1]
api_hash =  sys.argv[2]
sessionPath =  sys.argv[3]

json._default_encoder = JSONEncoder(ensure_ascii=False)
client = TelegramClient(StringSession(sessionPath), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
try:
    var = client(GetContactsRequest(hash=0))
    for i in var.users:
        mutualContact = '0';
        deleted = '0'
        if str(i.mutual_contact) == "False":
            mutualContact = '1'
        if str(i.deleted) == "False":
            deleted = '1'
        print(json.dumps(
            myContact(i.username, mutualContact, i.phone, deleted, i.first_name, i.last_name,
                      str(i.id)).__dict__).encode('UTF-8'))
except UserDeactivatedBanError as e:
    print("{" + '"code":"{}","msg":"{}"'.format(444, "账号已经封禁") + "}")
except Exception as e:
    print("{"+'"code":"{}","msg":"{}"'.format(400,e)+"}")
