# Programmeerproject

## Problem defenition
Marktplaats is not a product specicif site. Products such as old and rare wines require extra searching criteria such as region or year. Furthermore user experience is not optimal. After wanting to buy an item on markplaats, you have to email or text the seller. There is no in-application chat.(update -> there is :/ )

## Problem solving
The application will let users upload a bottle of wine for sale. Buyers can find these bottles, and start a conversation with the seller in a private (in-app) chat. Users have an overview of their chats. 

## Data sets
Users will be registered on firebase, and upload products from their account. Therefore for each product the seller will be known, and users can start a chat with that seller in a firebase realtime database chat. 

## Parts of application
* Login/register
* Add product 
* Search for products
* Chat

All products added by any user will be available for users when searching. Users can start a chat with another user when they would like to buy/discuss a product. 
Extra function: distance between buyer and seller with the google maps api
(Extra Extra function: let users search for region in search screen by clicking on part of map, not sure)

## Technical problems
Problems that could arise from now on are most likely centered around the realtime chat. Initially the database structure could have produced some difficulties, but (by trial and error) the database is becoming more and more efficient.

## Similar applications
* Marktplaats (mentioned earlier)
* Vivino. Found this application when a friend searched if my original idea of a wine buying app wasn't already made. Almost identical design of results as i originally planned. Really great and clean design, useful functions. Is used to find good wines, review the wines and tell friends about certain wines.

Update: this picture of activities is not up to date anymore, will be changed as soon as possible
![alt tag](https://github.com/koenzijlstra/Programmeerproject/blob/master/docs/PP_eerste_opzet.jpg)

## UI
Below are some pictures to give an idea of how the user interface will look. The activites Buyfullinfo and Sellfullinfo have a dialog theme.

![alt tag](https://github.com/koenzijlstra/WineRetry/blob/master/docs/For%20sale%2C%20dinsdagmiddag%2017.PNG)
![alt tag](https://github.com/koenzijlstra/WineRetry/blob/master/docs/loginvrijdag.PNG)




