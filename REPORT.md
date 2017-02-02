#Finewine  

Created by Koen Zijlstra  
Student nr: 10741615  
University of Amsterdam  
Coarse: Programmeerproject  

### Introduction
The aim of this application is to provide a product-specific Marktplaats/eBay like app for people looking to buy or sell rare or 
old bottles of wine. FineWine has a realtime-chat function for the communication between the seller of a wine bottle and a user that
is interested in (buying) the bottle. User-authentication and database management is done with the use of Firebase. There are 
three main/overview activities which mirror the three different parts in functionality: buy, sell and chat. Furthermore there
are two authentication activities to register to FineWine and to log in. Additionally there are two activities (with a dialog theme)
that display information about a specific bottle (which is shown depends on the previous activity) and an activity with a dialog
theme that displays information about the activity the user was previously in.    

Below is a screenshot of the BuyActivity, where a filter of only red whines from between the year 1960 and 1989 is applied. 

<img src="https://github.com/koenzijlstra/WineRetry/blob/master/docs/final2.PNG" width="250">  

## Technical design
#### Overview
There are three main/overview activities which mirror the three different parts in functionality: buy, sell and chat. Furthermore there
are two authentication activities to register to FineWine and to log in. Additionally there are two activities (with a dialog theme)
that display information about a specific bottle (which is shown depends on the previous activity) and an activity with a dialog
theme that displays information about the activity the user was previously in.    

#### Classes
When the app is opened for the first time after installing, LoginActivity will be opened. There is a button to navigate to SignupActivity, 
which is necessary if the user does not have an account yet. 

**SignupActivity** lets the user register with a username, an email and a password. The createUserWithEmailAndPassword method provided by
Firebase is used to create a new user. When registering is unsuccessful, toast what went wrong (with getexeption). When registering is
successful, the user is navigated to BuyActivity. The user can also click a button to navigate to
LoginActivity when already registered.

**LoginActivity** is opened when the application is started. It lets the user log in to FineWine. When the user is already logged in,
navigate to BuyActivity automatically with an authstatelistener. This listener is used in every activity. When the user is not logged in, the user is navigated to the LoginActivity. The class first retrieves the input, checks if it is not empty and then the function
signInWithEmailAndPassword provided by Firebase is called. When this fails the app toasts what went wrong. Otherwise the user is 
navigated to BuyActivity.  

**ForgotpasswordActivity** is started from LoginActivity. The user enters his email adress and a mail to reset
 the password is sent to the email adress. This is done with the use of the sendPasswordResetEmail function, which is
 provided by Firebase. The user is then navigated back to LoginActivity.   
 
**BuyActivity** is the default activity when the user is logged in for the authstatelistener as previously mentioned. It displays all the bottles that are for sale. Custom listadapter ListadapterBottles is used to fill the listview with some of the info of each WineObject. When a Wineobject/item in the listview is clicked BuyfullinfoActivity is started.  

**WineObject** is an object that contains a title, the region the wine comes from, the year, additional information about the bottle (story), the id of the user that sells the bottle (sellerid), an unique ID for the bottle and a tag (red/white etc). This object is created at NewSellActivity and used for SellActivity and BuyActivity. Furthermore the info of the objects is given to BuyfullinfoActivity and SellfullinfoActivity.  

**ListadapterBottles** extends the arrayadapter. It uses a list of WineObjects as input, and a custom listitem xml file as
 layout for each item. It fills the needed textviews of each listitem with the title, year and region of the Wine Object. This adapter is used by BuyActivity as well as SellActivity.
 
**BuyfullinfoActivity** displays most of the data of a WineObject. This data is retrieved via the hashmap that was given to the intent (no object was given to the hashmap as a result of some bugs). When the current user is the seller of the bottle the chat button is hidden (so no chat can be created) and the textview that displays the seller has a different message. Otherwise the name of the seller is displayed and the chat button navigates to a chat with the seller of the bottle (ChatActivity).   

**SellActivity** can be accessed via the navigation buttons at the top of the screen. It displays all winebottles that the current user sells. This is done by first retrieving all ID's of bottles that the user sells, and then comparing these ID's with the bottleID's of the bottles under root/wines. This way the complete wineobjects can be retrieved that the user sells. As mentioned earlier ListadapterBottles is used as custom listadapter. When the user clicks an item in the listview, the user is navigated to SellfullInfo.  

**Sellfullinfo** is an activity with a dialog theme and displays most the information of a bottle that the current user sells. The bottle can be deleted from here. When the delete button is clicked an alertdialog is shown. When the deleting is confirmed by the user the bottle is deleted an the user is navigated back to SellActivity. The complete bottle is deleted under root/wines and the bottleID is also deleted under the users wines. This is done both with the use of removeValue, provided by Firebase.

**AllchatsActivity** is also accessible with the navigation buttons atop. It displays all usernames of otherusersobjects that current user has a chat with. This is done with the use of the custom listadapter ListadapterChats. When the user clicks on a name, ChatActivity
is started and the OtherID of the otheruserobject that was clicked on is given to the intent.

**OtheruserObject** is an object that contains the username and ID of the other user (the user that the current user has a chat with). The name is used for displaying the names of all people that current user has a chat with, and the id is used for opening the
correct chat in ChatActivity.  

**ChatActivity** displays all the messages (chatmessageobjects) between two users with the use of the Firebaselistadapter. This adapter uses a custom listitem xml file called message. The messages are updated live, it is a realtime chat. The user can write a message, and the newly created chatmessageobject will be written in both duplicate chats. This activity can be accessed from Buyfullinfo or from AllchatsActivity. 

**ListadapterChats**
**ChatMessageObject**
**InfoActivity**
**BaseActivity**
**Signout**




