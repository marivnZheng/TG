import socks

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
api_id = 94575
api_hash = 'a3406de8d171bb422bb6ddf3bbd800e2'
mySession='1BVtsOKwBuwfWWOBlGK2ROBd9qe7CCc-k37hSJC8jUY7bAXLnlwnn0SBnHE3_Qag3E6yP1R1ksipMPP4Gn2SO5ucAHEhNvn5RbEmkgppv_03XDJgpNyoOMDeNW18CuyDmU6AOauw8CGGDcFE_nHS6e2mYbt8ecoMq6hbVGldkCUWO-0mapcis9_-mQZAnqO299AAp_My7cO4hkwz0VH6KGVrXDgE1aEld3FQjqlH2qReS96XQDrbl8GGAEUo95iLpRZ-4ahirk30SyEoFWVrCVMLNaY0bzM5hFyyFEZ009DDKe94zlMUbBOAI6Mi6BL-l94bY22WUVyq8WHX5-ArFaPLXPPwU-bI='

# 创建 Telegram 客户端
client = TelegramClient(StringSession(mySession), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, 'localhost', 4444))
client.connect()

try:

    invite_link = 'https://t.me/addlist/vVvpW5gotZI1YzQx'
    var = client.get_entity(invite_link)
    print(var)
    client(ImportChatInviteRequest(hash=invite_link))
except UserDeactivatedBanError as e:
    print("{" + '"code":"{}","msg":"{}"'.format(444, "账号已经封禁") + "}")
except InviteHashExpiredError as e:
    print("{" + '"code":"{}","msg":"{}"'.format(443, "此私有频道已失效") + "}")
except Exception as e:

    print("{"+'"code":"{}","msg":"{}"'.format(488, e)+"}")

