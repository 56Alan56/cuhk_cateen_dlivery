from flask import Flask
from flask import jsonify
from flask.globals import request
import math
import mysql.connector
import datetime
import time


import os,sys
import requests
from bs4 import BeautifulSoup



'''To init the database '''

def create_canteens():
    canteen_list = bs_crawl_from_web()
    mydb = MyDatabase()
    insert_query = "INSERT INTO canteens (canteen_name,location,open_hours,special,opening_status) VALUES (%s,%s,%s,%s,%s)"
    mydb.cursor.executemany(insert_query,canteen_list)
    mydb.conn.commit()
    mydb.close()
    return None


def create_messages():
    mydb = MyDatabase()
    delete_query = "DELETE FROM messages"
    mydb.cursor.execute(delete_query)
    for chatroom_id in range(1,4):
        message_list = []
        for id in range(1,30):
            now = datetime.datetime.now()
            now = now.strftime("%Y-%m-%d %H:%M:%S")
            time.sleep(0.5)
            message = (chatroom_id,0,"localtest","test"+str(id),now)
            message_list.append(message)
        insert_query = "INSERT INTO messages (chatroom_id,user_id,name,message,message_time) VALUES (%s,%s,%s,%s,%s)"
        mydb.cursor.executemany(insert_query,message_list)
        mydb.conn.commit()
    
    mydb.close()
    return None


def bs_crawl_from_web():
    URL = "https://www.cuhk.edu.hk/english/campus/accommodation.html"
    page = requests.get(URL)
    soup = BeautifulSoup(page.text, "html.parser")
    tables = soup.find_all('table')
    
    name_list = []
    loc_list = []
    time_list = []
    special_list = []
    opening_status = []
    for canteen in tables[-3].find_all("tr")[1:]:
        name_list.append(canteen.find("th").text)
        loc_list.append(canteen.find_all("td")[1].text)
        time_list.append(canteen.find_all("td")[2].text)
        if "Closed" in canteen.find_all("td")[2].text:
            opening_status.append("Closed")
        else:
            opening_status.append("Opening")
        special_list.append("Today's special of " + canteen.find("th").text)

    return list(zip(name_list,loc_list,time_list,special_list,opening_status))






'''Database Class Define'''
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
            database = 'group28',
            auth_plugin = 'mysql_native_password'
        )
        self.cursor = self.conn.cursor(dictionary = True)
        return 

    def close(self):
        self.cursor.close()
        self.conn.close()


if __name__=="__main__":
    create_canteens()
    #app.run()
    #debug
    #app.run(debug=True,host="0.0.0.0")