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
navigate to BuyActivity automatically with an authstatelistener. This listener is used in every activity. When the user is not logged in,
the user is navigated to the LoginActivity. The class first retrieves the input, checks if it is not empty and then the function
signInWithEmailAndPassword provided by Firebase is called. When this fails the app toasts what went wrong. Otherwise the user is 
navigated to BuyActivity.

