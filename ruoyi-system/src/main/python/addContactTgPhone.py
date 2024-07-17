import json
import sys
from json import JSONEncoder

import socks

from myContact import myContact
from telethon import TelegramClient,sync
from telethon.sessions import StringSession
from telethon.tl.functions.contacts import GetContactsRequest, AddContactRequest

api_id = sys.argv[1]
api_hash = sys.argv[2]
sessionPath=sys.argv[3]
userName=sys.argv[4]

json._default_encoder=JSONEncoder(ensure_ascii=False)
client = TelegramClient(StringSession(sessionPath), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()


try:
    var =client.get_entity(userName)
    result = client(AddContactRequest(
        id=userName,
        first_name=str(var.first_name),
        last_name=str(var.last_name),
        phone=str(var.phone),
        add_phone_privacy_exception=True
    ))

    concats = client(GetContactsRequest(hash=0))
    for contact in concats.users:
        if str(contact.id) ==str(var.id):
            mutualContact ='0';
            deleted='0'
            if  str(contact.mutual_contact) == "False" :
                mutualContact='1'
            if str(contact.deleted) == "False" :
                deleted='1'
                print("{"+'"code":"{}","msg":{}'.format(200,json.dumps(myContact(contact.username,mutualContact,contact.phone,deleted,contact.first_name,contact.last_name,str(contact.id)).__dict__))+"}")
except Exception as e:
    print("{"+'"code":"{}","msg":"{}"'.format(400,e.args[0].replace('"','_').replace('\\',"_"))+"}")




