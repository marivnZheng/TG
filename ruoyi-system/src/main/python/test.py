import sys

import socks
import telethon
from telethon.errors import UserDeactivatedBanError
from telethon import TelegramClient,sync
from telethon.sessions import StringSession

api_id = 94575
api_hash = 'a3406de8d171bb422bb6ddf3bbd800e2'
mySession = '1BJWap1wBu26UZpUU1U_fm6jP9uma2WWb5-7BN0m3aF1r_BVUGEC2xuRXk4CPh0Zf5G9Q1SadKNBQsYE7r8LHmzCCCqoxUDMauYAd67yhHfLJXq1QcMaq6MarcpscnwMXENLcgN0dvwyj6aL_32Shsu8P89MjyqHdllSXbK5gyv2YsGWOoXAFDW43hX4OKTfgmw_-5R82idFNuJQ8JNAI_K6_6ltNyyAGu9rkmTFSBVa4ZWf_8MF7S5Gmswi7jZ1Km0BbdfFoIipb6qEOmtNi6mPnfmDxD0JhNHvMTQWQ-ZROAZlfzaQmIZtW26LGUlXIH_IR0ZjoM6YIvjDYOIDW-qCqVNSFeAw='
targetUser = 'test_0820'
messageId = '129778299'
charId = 'buchuhan15'
client = TelegramClient(StringSession(mySession), api_id, api_hash,system_version="4.16.30-vxCUSTOM",proxy=(socks.SOCKS5, '156.232.10.47', 4444))
client.connect()
try:
    user = client.get_entity(targetUser)
    messages = client.get_entity(charId)
    client.forward_messages(user.id, int(messageId), messages.id)
    print("{"+'"code":"{}","msg":"{}"'.format(200,"发送成功")+"}")
except UserDeactivatedBanError as e:
    print("{" + '"code":"{}","msg":"{}"'.format(444, "账号已经封禁") + "}")
except Exception as e:
    print("{"+'"code":"{}","msg":"{}"'.format(400, e)+"}")

