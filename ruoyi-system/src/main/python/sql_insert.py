import mysql.connector
from mysql.connector import errorcode
from datetime import datetime
import pytz

# import loginEntity
host = "localhost"
user = "root"
password = "ZPzp!@#123456Tg"
database = "tg"

def insert(query,data_list):
    try:
        conn = mysql.connector.connect(
            host=host,
            user=user,
            password=password,
            database=database
        )
        cursor = conn.cursor()
        for data in data_list:
            cursor.execute(query, data)
        conn.commit()
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)
    finally:
        cursor.close()
        conn.close()


def insertSysGroup():
    query = """
        INSERT INTO sys_group (
        sys_group_id, sys_user_id, sys_group_title, sys_group_link, sys_group_detail, sys_group_send_photo, sys_group_send_message,sys_group_invite, sys_account_string_session
        )
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
    """
    group_data = [(1,1,'亦如梦','yirumeng','false',0,1,0,'sadhiu')]
    insert(query,group_data)

def insertSysContact():
    query = """
        INSERT INTO sys_contact (
        sys_contact_id, sys_account_id, sys_user_id, sys_contact_name, sys_contact_user_name, sys_mutual_contact, sys_status,sys_contact_phone
        )
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
    """
    contact_data = [(1,45,1,'亦如梦','yirumeng',1,1,'')]
    insert(query,contact_data)

def insertSysAccount(loginEntity):
    query = """
       INSERT INTO sys_account (
           sys_user_id,sys_account_session_file, sys_account_phone, sys_account_string_session, sys_account_concats_number, sys_account_group_number, sys_account_name, sys_account_first_name, sys_account_last_name, sys_account_about,sys_account_create_time,sys_account_create_timeZone,sys_account_status,sys_account_online,sys_account_login_status
       ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
       """
    utc_timezone = pytz.timezone('Etc/UTC')
    current_time = datetime.now(utc_timezone)
    user_data = [
        (1,loginEntity.sessionFilePath, loginEntity.phoneNumber, loginEntity.sessionString, loginEntity.concatNumber, loginEntity.groupNumber, loginEntity.username, loginEntity.firstname, loginEntity.lastname, loginEntity.about,current_time,"Etc/UTC",0,0,0)
    ]
    #user_data=[(1, '923115605926', '923115605926', '1BJWap1wBu67EYOcGN0ygQgjzAsn7yIU_Z0jtQ9EaP7RF6tLnfIeQNsd2gHJMZbKyHePRjk4ArACYuDPp4Vjfr7rkTpv-MGidmedqAus7rG9qgweTvopACNciqOWCSjdkn0doQNiXtr-1YrXAKZzvHSnRZkalIZEcEAOOo5bLR8VwbECFuRfVkYIjPbpDdUKX-63SOrdk_T0WTS8yFzvaBE6mZ-BNN9MXfYKjGvVtI6pg1I7TlYqgventZt9ZWOn1zYOgjQqcrRmn6Ql_7jlwMxQxCYVcvKLX-84GixuwlsNUblG3IF9EoD-cZF_c85q2CijIeabLsxAmryXctgq8Brl6DT3FpdM=', 37, 20, 'HRDcoco', 'COCO', None, '1️⃣寻志同道合之人2️⃣寻竭忠尽智之人3️⃣寻深思远虑之人4️⃣寻勤恳至诚之人 🌐公司頻道：@BC_coco 🤝交流群：@Flbylq', current_time, 'Etc/UTC', 0, 0, 0)]
    insert(query, user_data)
