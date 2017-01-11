# Design document

## titel voor plaatje

![alt tag](https://github.com/koenzijlstra/Programmeerproject/blob/master/docs/Knipsel.PNG)
 
## Classes
#### Activities 
* login: checks input for error, check if currentuser = null, else go to home. Get firebase auth instane, signinwithemailandpassword
* register: checks input for errors, get firebase auth instance, createuserwithemailandpassword
* Home: User can navigate to sells, allchats or buy activity or logout.
* Sell: user can search for wines, displayed are random wines
* Results: the results of the search key
* Product: all info of wine, user can navigate to chat activity
* Sells: overview of all wines that the user sells. user can navigate to newsell activity
* New sell: user can upload a new product for sale
* allChats: overview of all chats
* realtime chat with other user (not part of mvp)

#### Other
* Database manager: gets database reference, write method, read method etc
* Maybe helper for the realtime chat? 

## APIs, frameworks or plugins
The platform Firebase will be used for the funcionality of the app.

## Data sources
Users will provide the data. Later the api of a store can be added.

## Database tables and fields
Firebase works with JSON. When a user uploads a whine, it needs to be in his personal sells overview, as well as in the list of all wines that users can find. I will have to be careful to not safe too much data double. (Another thing to think of is duplicates of whines, same wine by different sellers)

* users
&nbsp;&nbsp;&nbsp;&nbsp;user1
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wine1
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wine2
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wine3
&nbsp;&nbsp;&nbsp;&nbsp;user2


