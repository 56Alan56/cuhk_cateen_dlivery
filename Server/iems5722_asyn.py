from celery import Celery
from flask import Flask
from pyfcm import FCMNotification
import mysql.connector

import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.header import Header
import random


api_key = "AAAAyaepncg:APA91bGG28LSD3lUiUnr4_WxjZEm85N7BV7JXOutd0gYJZeLA741KUKnJczUh2OKiSsm6y2MO_m5ApplDAhfwtBqj_tAumMtux8btlxUJ7vM3Eyvd6sdUKo7bPxL0vx5T0FoJmif_Ce5"
push_service = FCMNotification(api_key = api_key)


# def multicast_push():
#     mydb = MyDatabase()
#     mydb.connect()
#     token_query = "SELECT token FROM push_tokens;"
#     mydb.cursor.execute(token_query)
#     token_list = []
#     rows = mydb.cursor.fetchall()
#     for row in rows:
#         token_list.append(row["token"])

#     #registration_id = "<device registration_id>"
#     message_title = "Ben sends you a message!"
#     message_body = "Hello from Ben!"
#     result = push_service.notify_multiple_devices(
#                                             registration_ids = token_list,
#                                             message_title = message_title,
#                                             message_body = message_body)
#     print(token_list[1])
#     return(result)

def make_celery(app):
    celery = Celery(
                    app.import_name,
                    backend=app.config['CELERY_RESULT_BACKEND'],
                    broker=app.config['CELERY_BROKER_URL']
                    )
    celery.conf.update(app.config)
    class ContextTask(celery.Task):
        def __call__(self, *args, **kwargs):
            with app.app_context():
                return self.run(*args, **kwargs)
    
    celery.Task = ContextTask
    return celery

app = Flask(__name__)
app.config.update(
            CELERY_BROKER_URL='amqp://guest@localhost//',
            CELERY_RESULT_BACKEND='redis://localhost:6379'
            )


celery = make_celery(app)



@celery.task()
def multicast_push(user_id,user_name, message,chatroom_id):
    # Connect the database
    mydb = MyDatabase()
    mydb.connect()

    # Fetch Tokens except for the message sender from Table 'push_tokens'
    token_query = "SELECT token FROM push_tokens where user_id <> %s" % user_id
    mydb.cursor.execute(token_query)
    token_list = []
    rows = mydb.cursor.fetchall()
    for row in rows:
        token_list.append(row["token"])
    
    # Fetch Chatroom Name to Display on Android's action bar
    chatroom_query = "SELECT name FROM chatrooms WHERE id= %s" % chatroom_id
    mydb.cursor.execute(chatroom_query)
    chatroom_name = mydb.cursor.fetchone()["name"]

    # Data Message
    data_message = {
                        "chatroom_id" : chatroom_id,
                        "chatroom_name" : chatroom_name,
                    }

    # Notification Message
    message_title = "%s sends you a message!" % user_name
    message_body = message
    
    # Push to FCM
    result = push_service.notify_multiple_devices(
                                            registration_ids = token_list,
                                            message_title = message_title,
                                            message_body = message_body,
                                            data_message = data_message)

    # Check on celery
    print("result status:%s" % result)
    print(data_message)
    
    mydb.close()
    return(result)

@celery.task()
def single_push(sender_id,receiver_id,message):
    # Connect the database
    mydb = MyDatabase()
    mydb.connect()

    # Fetch Tokens except for the message sender from Table 'push_tokens'
    token_query = "SELECT token FROM push_tokens where user_id = %s" % receiver_id
    mydb.cursor.execute(token_query)
    token_list = []
    rows = mydb.cursor.fetchall()
    for row in rows:
        token_list.append(row["token"])

    # Data Message
    data_message = {
                        "receiver_id" : receiver_id,
                        "message" : message,
                    }

    # Notification Message
    message_title = "%s sends you a message!" % sender_id
    message_body = message
    
    # Push to FCM
    result = push_service.notify_multiple_devices(
                                            registration_ids = token_list,
                                            message_title = message_title,
                                            message_body = message_body,
                                            data_message = data_message)

    # Check on celery
    print("result status:%s" % result)
    print(data_message)
    
    mydb.close()

    return(result)

@celery.task()
def send_verification_email(user_id,veri_code):
    # Connect the database
    receiver = user_id + "@link.cuhk.edu.hk"    
    smtpserver = 'smtp.qq.com'
    username = '371782140@qq.com'
    password = 'txzhohzesuxgcaif'
    sender = username 
    print("done1")


    subject = Header("Verification Email for the CUHK Canteen App", 'utf-8').encode()
    msg = MIMEMultipart('mixed')
    msg['Subject'] = subject
    msg['From'] = 'Group#28'
    msg['To'] = receiver
    print("done2")
    #构造文字内容
    text = "Hi there!\n Welcome to the CUHK Canteen APP! \nThis is your verification code: {}, expiring in 5 minutes".format(veri_code)
    text_plain = MIMEText(text,'plain', 'utf-8')
    msg.attach(text_plain)
    print("done3")

    smtp = smtplib.SMTP_SSL(smtpserver)
    smtp.connect(smtpserver,465)
    #用set_debuglevel(1)可以打印出和SMTP服务器交互的所有信息。
    #smtp.set_debuglevel(1)
    smtp.login(username, password)
    smtp.sendmail(sender, receiver, msg.as_string())
    smtp.quit()
    print("result status:%s" % "Sent!")




class MyDatabase:
    conn = None
    cursor = None

    def __init__(self):
        self.connect()
        return
    
    def connect(self):
        self.conn = mysql.connector.connect(
            host = 'localhost',
            port = 3306,
            user = 'dbuser',
            password = 'password',
            database = 'iems5722',
            auth_plugin = 'mysql_native_password'
        )
        self.cursor = self.conn.cursor(dictionary = True)
        return 

    def close(self):
        self.cursor.close()
        self.conn.close()

