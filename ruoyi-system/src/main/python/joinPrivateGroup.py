import socks
import sys
from telethon import TelegramClient
from telethon.errors import UserDeactivatedBanError, InviteHashExpiredError
from telethon.sessions import StringSession
from telethon.tl.functions.messages import ImportChatInviteRequest
import socks
import telethon
from telethon.errors import UserDeactivatedBanError
from telethon import TelegramClient,sync
from telethon.sessions import StringSession
# 替换为您的 API ID 和 API Hash

api_id = sys.argv[1]
api_hash = sys.argv[2]
sessionPath= sys.argv[3]
link = sys.argv[4]

# 创建 Telegram 客户端
client = TelegramClient(StringSession(sessionPath), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()

try:
    client(ImportChatInviteRequest(hash=link))
    print("{" + '"code":"{}","msg":"{}"'.format(200, "成功") + "}")
except UserDeactivatedBanError as e:
    print("{" + '"code":"{}","msg":"{}"'.format(444, "账号已经封禁") + "}")
except InviteHashExpiredError as e:
    print("{" + '"code":"{}","msg":"{}"'.format(443, "此私有频道已失效") + "}")
except Exception as e:

    print("{"+'"code":"{}","msg":"{}"'.format(488, e)+"}")

