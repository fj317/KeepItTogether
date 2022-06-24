# KeepItTogether
This work is a collaborative effort to create a program that would help people sharing a house plan and track their household chores and spendings. It aims to make it immensely easier for housemates to know what has been done or purchased, and to split their responsibilities evenly, making maintaining the household more efficient and organised. In this way, communication between housemates would be improved and, at the same time, misunderstandings would be minimised. A database is used to provide data storage, as well as security and authentication for users.

## Getting Started
1. Clone the repo

` git clone https://github.com/fj317/KeepItTogether.git `

2. Run the IP server.
3. Using Android Studio, select the app via the toolbar and select the Android Virtual Device to run upon (or connect your Android device to the development machine via USB).
4. Click run on Android Studio.

## Usage
- The app uses a login page to provide authenticy and security to each user. An account can also be registered and created if you do not have one. To login, simply enter the login details and submit them to the app. If the credentials are valid, the main menu will be displayed. 
- The main menu gives several options:
  - Viewing the house's unique page. The house's unique page displays information related to the house the user is currently part of, including its members and the house's join code. 
  - Adding additional chores/spendings to the house account.
  - Viewing the chores and spendings that are currently unresolved.
  - Viewing the user's specific chores and spendings. 
  
An demo video of the application is available [here](https://drive.google.com/file/d/1Kr3VCAy-QFgoxrU6CK3K3_n7cwnDGr0C/view?usp=sharing).

## Database
The project makes use of a central multi-threaded java server that allows users to connect via sockets. Once the client has connected the server sends the request it receives to the database, gets the response and passes any information it receives back. The application makes use of the client class that allows it to connect to the server. The client class comes with two methods: select and modify. Select allows you to get data from the database and modify allows you to change the database e.g. with the sql commands UPDATE and DELETE. The database uses SQLite as the query language to communicate with the database. The database has several tables that are used to store the data for the users, houses, chores and transactions. The database relationship diagram shows the different entities in the database and helps visualise the datbaase's functionality:

![Relationship diagram](https://github.com/fj317/KeepItTogether/blob/main/images/Database.png)
