import sys

import socks

from myContact import myContact
import json
from telethon import TelegramClient, sync
from telethon.sessions import StringSession
from telethon.tl.types import InputPeerChannel, PeerChannel
from json import JSONEncoder

api_id = sys.argv[1]
api_hash = sys.argv[2]
sessionPath = sys.argv[3]
groupId = sys.argv[4]

json._default_encoder = JSONEncoder(ensure_ascii=False)
client = TelegramClient(StringSession(sessionPath), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
result = client.get_entity(groupId);
me = client.get_participants(result, aggressive=False)
try:
    for i in me:
        mutualContact = '0';
        deleted = '0'
        if str(i.mutual_contact) == "False":
            mutualContact = '1'
        if str(i.deleted) == "False":
            deleted = '1'

        print(json.dumps(
            myContact(i.username, mutualContact, i.phone, deleted, i.first_name, i.last_name,
                      str(i.id)).__dict__).encode('UTF-8'))
except Exception as e:
    print("{"+'"code":"{}","msg":"{}"'.format(400,e)+"}")
