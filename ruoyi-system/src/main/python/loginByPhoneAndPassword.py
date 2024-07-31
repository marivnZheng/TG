
import json
import sys

import socks
import telethon
import logging
import redis
from sql_insert import insertSysAccount
from TGHelper import getContactNum, getGroupNumber, initLoginEntity
from telethon import TelegramClient, events
from telethon.sessions import StringSession
from telethon.tl.functions.users import GetFullUserRequest

logging.basicConfig(level=logging.INFO)
api_id = sys.argv[1]
api_hash = sys.argv[2]
phone = sys.argv[3]
phoneCode = sys.argv[4]
password = sys.argv[5]
phoneCodeHash =sys.argv[6]

r = redis.Redis(host='localhost', port=6379, db=0)
client = TelegramClient(phone, api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
if not client.is_user_authorized():
    try:
        client.sign_in(phone, phoneCode, phone_code_hash=phoneCodeHash)
    except telethon.errors.SessionPasswordNeededError:
        client.sign_in(password=password)
        MySession = StringSession.save(client.session)
        ContactNum = getContactNum(client)
        groupnum = getGroupNumber(client)
        result = client.get_me()
        full =  client(GetFullUserRequest(result))
        loginEntity = initLoginEntity(phone, phone, MySession,ContactNum, groupnum,result.username,result.first_name,result.last_name,full.full_user.about)
        result = "{" + '"code":"{}","msg":{}'.format(200, json.dumps(loginEntity.__dict__)) + "}"
        r.set(phone, result)
        @client.on(events.NewMessage(pattern='/start'))
        async def handler(event):
            if 'Telegram' in event.sender.username:
                await event.respond('Yes, this was me.')
        client.run_until_disconnected()
    except Exception as e:
        result = "{"+'"code":"{}","msg":"{}"'.format(400,e)+"}"
        r.set(phone, result)

