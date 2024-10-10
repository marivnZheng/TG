

import telethon
from telethon import TelegramClient, sync
from telethon.sessions import Session, StringSession
from telethon.tl.functions.contacts import GetContactsRequest
from telethon.tl.types import InputPeerChannel
from loginEntity import loginEntity



def escapeSpecialCharacters(original_string):
    # 使用转义字符
    escaped_string = original_string.replace("\\", "")  # 转义反斜杠
    escaped_string = escaped_string.replace("\"", "")  # 转义双引号
    escaped_string = escaped_string.replace("'", "")     # 转义单引号
    escaped_string = escaped_string.replace("\n", "")    # 转义换行符
    escaped_string = escaped_string.replace("\t", "")    # 转义制表符
    escaped_string = escaped_string.replace("{", "")  # 转义制表符
    escaped_string = escaped_string.replace("}", "")  # 转义制表符
    return escaped_string


def loginByPhoneNumberAndPassWord(api_id,api_hash,phone,code,codeHash,password):
    client = TelegramClient(phone, api_id, api_hash)
    client.connect()
    if not client.is_user_authorized():
        try:
            client.sign_in(phone, code,phone_code_hash=codeHash)
            print(client.get_me())
        except telethon.errors.SessionPasswordNeededError:
            client.sign_in(password=password)
            MySession = StringSession.save(client.session)
            print(MySession)
            return MySession



def loginBySessionFile(api_id,api_hash,mySession):
    client = TelegramClient(mySession, api_id, api_hash)
    client.connect()
    return client

def getStringSession(api_id,api_hash,sessionPath):
    client = TelegramClient(sessionPath, api_id, api_hash)
    client.connect()
    if client.is_user_authorized():
        return StringSession.save(client.session)
    else:
        return 'session is Invalid'

def loginBySessionString(api_id,api_hash,myString):
    client = TelegramClient(StringSession(myString), api_id, api_hash)
    client.connect()
    return client

def sendCodeToPhoneNumber(api_id,api_hash,phone):
    client = TelegramClient(phone, api_id, api_hash)
    client.connect()
    if not client.is_user_authorized():
        phone_code_hash = client.send_code_request(phone).phone_code_hash
        return  phone_code_hash

def sendMessage(mySession,message,targetUser):
    client =loginBySessionFile(mySession)
    user =client.get_entity(targetUser)
    message = client.send_message(user,message)
    return message

def initLoginEntity(sessionFilePath,phoneNumber,sessionString,concatNumber,groupNumber,username,firsname,lastname,about):
    return loginEntity(sessionFilePath,phoneNumber,sessionString,concatNumber,groupNumber,username,firsname,lastname,about)

def getContactNum(client):
    Contacts = client(GetContactsRequest(hash=0))
    ContactNum = 0
    # get concat total
    for Contacts in Contacts.users:
        if not Contacts.deleted:
            ContactNum = ContactNum + 1
    return  ContactNum

def getGroupNumber(client):
    chats = client.get_dialogs()
    groupNum = 0
    for chat in chats:
        if isinstance(chat.input_entity, InputPeerChannel):
            groupNum = groupNum + 1
    return  groupNum








