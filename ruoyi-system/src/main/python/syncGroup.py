import sys

import socks
from telethon.tl.types import ChatBannedRights
from myGroup import myGroup
import json
from TGHelper import escapeSpecialCharacters
from telethon.errors import UserDeactivatedBanError
from telethon import TelegramClient, sync
from telethon.sessions import StringSession
from json import JSONEncoder
import telethon.tl.functions as _fn

api_id = sys.argv[1]
api_hash = sys.argv[2]
sessionPath = sys.argv[3]


json._default_encoder = JSONEncoder(ensure_ascii=False)
client = TelegramClient(StringSession(sessionPath), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()

for d in client.iter_dialogs():
    if (not d.is_channel): continue
    link = 'null'
    try:
        en = d.entity
        is_group = d.is_group
        rights = '',
        sendPhotos = '',
        inviteUsers = '',
        sendMessages = '',
        participantsCount='',
        id = str(en.id)
        title = escapeSpecialCharacters(en.title)
        isPrivate = not en.access_hash
        participantsCount=en.participants_count
        if en.admin_rights != 'None':
            rights = en.default_banned_rights
        elif en.default_banned_rights != 'None':
            rights = en.default_banned_rights
        if type(rights) is not ChatBannedRights:
            sendMessages = True
            inviteUsers = True
            sendPhotos = True
        else:
            sendPhotos = rights.send_photos
            sendMessages = rights.send_messages
            inviteUsers = rights.invite_users
        public = hasattr(en, 'username') and en.username
        is_chat = d.is_group and not d.is_channel and not en.deactivated
        admin = en.creator or (en.admin_rights and en.admin_rights.invite_users)
        if not d.is_channel or is_chat: continue
        if en.usernames:
            link = f'{en.usernames[0].username}'
            tmpJson = json.dumps(
                myGroup(link, inviteUsers, sendPhotos, sendMessages, is_group, id, title,participantsCount,isPrivate).__dict__).encode('utf-8')
            print(json.dumps(
                myGroup(link, inviteUsers, sendPhotos, sendMessages, is_group, id, title,participantsCount,isPrivate).__dict__).encode('utf-8'))
        else:
            if public:
                link = f'{en.username}'
                print(json.dumps(
                    myGroup(link, inviteUsers, sendPhotos, sendMessages, is_group, id, title,participantsCount,isPrivate).__dict__).encode('utf-8'))

            elif admin:
                if is_chat:
                    r = client(_fn.messages.GetFullChatRequest(en.id))
                else:
                    r = client(_fn.channels.GetFullChannelRequest(en))

                link = r.full_chat.exported_invite
                print(json.dumps(
                    myGroup(link.link, inviteUsers, sendPhotos, sendMessages, is_group, id, title,participantsCount,isPrivate).__dict__).encode('utf-8'))

            else:
                print(json.dumps(
                    myGroup(link, inviteUsers, sendPhotos, sendMessages, is_group, id, title,participantsCount,isPrivate).__dict__).encode('utf-8'))
    except UserDeactivatedBanError as e:
        print("{" + '"code":"{}","msg":"{}"'.format(444, "账号已经封禁") + "}")
    except Exception as e:
        print("{"+'"code":"{}","msg":"{}"'.format(400,e)+"}")





