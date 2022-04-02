from flask import Flask
from flask import jsonify
from flask.globals import request
import math
import mysql.connector
import time
import datetime
import iems5722_asyn
#from veri_email import send_verification_email, generate_veri_code
import random

from veri_email import send_verification_email

veri_code = ""

#5-digit Verification Code for email
def generate_veri_code():
    checkcod=''
    for i in range(5): 
        current=random.randrange(0,5)
        if current == i:				
            tmp=chr(random.randint(65,90))	
        else:
            tmp=random.randint(0,9)
        checkcod+=str(tmp)
    return(checkcod)

app = Flask(__name__)

@app.route("/")
def init():
    return "<p>Hello, This is the Welcome Page!</p>"
    #return   

@app.route("/register/",methods=["GET"])
def register():
    mydb = MyDatabase()
    try:
        user_id = request.args.get("id")
        username = request.args.get("name")
        password = request.args.get("pwd")
        input_veriCode = request.args.get("veri")
    except (ValueError,TypeError): 
        flag = "2"
    else:
        if input_veriCode!=veri_code:
            flag = "3"
        else:
            # Check if the account already exists
            query =  "SELECT * FROM accounts where user_id = %s" 
            params = (user_id,)
            mydb.cursor.execute(query,params)
            account = mydb.cursor.fetchall()
            if len(account)!=0:
                flag = "0" 
            else:
                insert_query = "INSERT INTO accounts (user_id,user_name,user_pwd) VALUES (%s,%s,%s);"
                params = (user_id,username,password)
                mydb.cursor.execute(insert_query,params)
                
                mydb.conn.commit()
                flag = "1"
    mydb.close() 
    return flag


@app.route("/login/",methods=["GET"])
def login():
    mydb = MyDatabase()
    try:
        user_id = request.args.get("id")
        password = request.args.get("pwd")
    except (ValueError,TypeError): 
        flag = "2"
    else:
        # Check if the account exists, if not, raise errors
        query =  "SELECT * FROM accounts where user_id = %s" 
        params = (user_id,)
        mydb.cursor.execute(query,params)
        account = mydb.cursor.fetchall()
        if len(account)==0:
            flag = "0" 
        else:
            query =  "SELECT * FROM accounts where user_id = %s" 
            params = (user_id,)
            mydb.cursor.execute(query,params)
            account_pwd = mydb.cursor.fetchall()[0]["user_pwd"]
            if account_pwd!=password:
                flag = "3"
            else:
                flag = "1"

        mydb.conn.commit()
    mydb.close() 
    return flag


@app.route("/verification/",methods=["GET"])
def verification():
    try:
        user_id = request.args.get("id")
    except (ValueError,TypeError): 
        result = "error"
    else:
        #send_verification_email(user_id,veri_code)
        global veri_code
        veri_code = generate_veri_code()
        #send_verification_email(user_id,veri_code)
        iems5722_asyn.send_verification_email.delay(user_id,veri_code)
        print(veri_code)
        result = "sent"
    return result


@app.route("/get_canteens/",methods=["GET"])
def get_canteens():
    mydb = MyDatabase()
    query =  "SELECT * FROM canteens"
    mydb.cursor.execute(query)
    canteens = mydb.cursor.fetchall()
    mydb.close()
    return jsonify(data=canteens,status="OK")

@app.route("/get_posts/",methods=["GET"])
def get_posts():
    mydb = MyDatabase()
    query =  "SELECT * FROM posts  WHERE post_status = 'pending' ORDER BY created_time DESC "
    mydb.cursor.execute(query)
    canteens = mydb.cursor.fetchall()
    mydb.close()
    return jsonify(data=canteens,status="OK")

@app.route("/push_posts/",methods=["POST"])
def push_posts():
    mydb = MyDatabase()
    try:
        time= request.form.get("time")
        dest = request.form.get("dest")
        fees = float(request.form.get("fees"))
        food = request.form.get("food")
        canteen_name = request.form.get("canteen_name")
        orderer_id = request.form.get("orderer")
    except (ValueError,TypeError): 
        res = "2"
    else:
        insert_query =  "INSERT INTO posts (canteen_name,destination,fees,food,expected_time,post_status,orderer_id) VALUES (%s,%s,%s,%s,%s,%s,%s)"
        params = (canteen_name,dest,fees,food,time,"Pending",orderer_id)
        print(params)
        mydb.cursor.execute(insert_query,params)
        mydb.conn.commit()
        mydb.close()
        res = "1"
    return res

@app.route("/get_profiles/",methods=["GET"])
def get_profiles():
    mydb = MyDatabase()
    try:
        user_id = request.args.get("id")
    except (ValueError,TypeError): 
        res = 2
    else:
        query =  "SELECT * FROM posts WHERE (orderer_id = %s or deliverer_id=%s) ORDER BY created_time DESC "
        params = (user_id,user_id)
        mydb.cursor.execute(query,params)
        posts = mydb.cursor.fetchall()
        mydb.close()
    return jsonify(data=posts,status="OK")

@app.route("/start/",methods=["POST"])
def delivery_start():
    mydb = MyDatabase()
    try:
        deliverer_id = request.form.get("deliverer_id")
        food = request.form.get("food")
        food = food.replace("%20"," ")
    except (ValueError,TypeError): 
        res = 2
    else:
        query1 =  "UPDATE posts SET deliverer_id = %s WHERE food = %s "
        params1 = (deliverer_id,food)
        mydb.cursor.execute(query1,params1)
        query2 =  "UPDATE posts SET post_status= \"In Progress\" WHERE food = %s "
        params2 = (food,)
        mydb.cursor.execute(query2,params2)
        mydb.conn.commit()
        mydb.close()
    return jsonify(status="OK")

@app.route("/get_messages/",methods=["GET"])
def get_message_p2p():
    mydb = MyDatabase()
    try:
        id = request.args.get("id")
    except (ValueError,TypeError): 
        res = 2
    else:
        query1 =  "SELECT * FROM messages WHERE sender_id=%s or receiver_id=%s"
        params1 = (id,id)
        mydb.cursor.execute(query1,params1)
        messages = mydb.cursor.fetchall()
        suc_res_dict = {"messages":messages}
        mydb.close()
    return jsonify(data=suc_res_dict,status="OK")

@app.route("/send_message/",methods=["POST"])
def send_message_p2p():
    mydb = MyDatabase()
    try:
        receiver_id = request.form.get("receiver_id")
        sender_id = request.form.get("sender_id")
        message = request.form.get("message")
    # Query ERROR
    except (ValueError,TypeError): 
        result = jsonify(message="Query ERROR:Please check the Form Data!",status="error")
    else:

        #iems5722_asyn.multicast_push.delay(user_id,name,message,chatroom_id) #send to the fcm asynchronously
        insert_query = "INSERT INTO messages(sender_id,receiver_id,message) VALUES (%s,%s,%s)" 
        params = (sender_id,receiver_id,message)
        mydb.cursor.execute(insert_query,params)
        mydb.conn.commit()
        result = jsonify(status="OK")
    
    mydb.close()
    return result

@app.route("/finish/",methods=["POST"])
def delivery_finished():
    mydb = MyDatabase()
    try:
        deliverer_id = request.form.get("deliverer")
        orderer_id = request.form.get("orderer")
        food = request.form.get("food")
        food = food.replace("%20"," ")
    except (ValueError,TypeError): 
        res = 2
    else:
        query1 =  "UPDATE posts SET post_status= \"Finished\" WHERE (food = %s AND deliverer_id=%s AND orderer_id=%s) "
        params1 = (food,deliverer_id,orderer_id)
        mydb.cursor.execute(query1,params1)
        mydb.conn.commit()
        mydb.close()
    return jsonify(status="OK")

@app.route("/submit_push_token/",methods=["POST"])
def push_tokens():
    mydb = MyDatabase()
    try:
        user_id = int(request.form.get("user_id"))
        token = request.form.get("token")
    except (ValueError,TypeError): 
        result = jsonify(message="Parameters Error!",status="error")
    else:
        # Check if there's a new token for user id
        token_insert_query = "INSERT INTO push_tokens (user_id,token) VALUES (%s,%s);"
        #token_insert_query = "UPDATE push_tokens SET token=%s where user_id=%s"
        #params = (token,user_id)
        params = (user_id,token)
        mydb.cursor.execute(token_insert_query,params)
        mydb.conn.commit()
        result = jsonify(status="OK")
    mydb.close()
    return result





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
    #app.run()
    #debug
    app.run(debug=True,host="0.0.0.0") 
