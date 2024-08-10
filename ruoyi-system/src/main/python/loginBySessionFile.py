

import json
import sys
import redis
import socks
import logging
from TGHelper import getContactNum, getGroupNumber, initLoginEntity
from telethon import TelegramClient, events
from telethon.sessions import StringSession
from telethon.tl.functions.users import GetFullUserRequest

api_id = sys.argv[1]
api_hash = sys.argv[2]
sessionPath = sys.argv[3]

r = redis.Redis(host='localhost', port=6379, db=0)
logging.basicConfig(level=logging.INFO)
client = TelegramClient(sessionPath, api_id, api_hash,proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()
if  client.is_user_authorized():
    MySession = StringSession.save(client.session)
    ContactNum = getContactNum(client)
    groupnum = getGroupNumber(client)
    result = client.get_me()
    full =  client(GetFullUserRequest(result))
    loginEntity = initLoginEntity(result.phone, result.phone, MySession,ContactNum, groupnum,result.username,result.first_name,result.last_name,full.full_user.about)
    result = "{"+'"code":"{}","msg":{}'.format(200,json.dumps(loginEntity.__dict__))+"}"
    sys.stdout.flush()
    r.set(sessionPath, result)
    client.run_until_disconnected()

else :
    result = "{"+'"code":"{}","msg":"{}"'.format(304,"账号登入状态失效")+"}"

    r.set(sessionPath, result)