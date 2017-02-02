# Proccesbook 
Below the most important challenges, decisions and fixes in the month Januari for the FineWine application are shown. 

## Day 3 (Wednesday 11)
* Finished design document
* Still not sure about data structure
* Made some stupid mistakes with creating login and register, already worked but copied a wrong xml file

## Day 4 (Thursday 12)
* AVG security deleted some tools, had to install android studio again
* Fixed login and register with firebase

## Day 5 (Friday 13)
* Got rid of home screen with 3 big buttons to navigate. Instead have 3 buttons for navigations (and a logout button) in linear vertical layout at the top of each activity (so no home screen)

## Day 8 (Monday 16)
* Added a unique id for each wine bottle. This is ID is saved under root/wines, and under this id all info is saved. This ID is also saved under  root/users/userid/wines. This is to know what wines each user sells.

## Day 9 (Tuesday 17)
* Read all wines from root/wines, create wineobjects of all wines and display them in a listview. On tuesday wineobject consisted of "year", "title", "region" and "story".

## Day 10 (Wednesday 18)
* Read all wines that current user sells via bottleids, create wineobjects of these wines and display them in a listview.
* Created an onitemclicklistener for the listview on BuyActivity

## Day 11 (Thursday 19)
* Name of seller in buyfullinfo was null first time. Fixed by creating a separate function to get sellername and calling it in ondatachange
* Created delete function. user can now delete its whine at root/wines, not yet under root/users/uid/wines

## day 15 (Monday 23)
* Completed the delete function
* Numberpicker to select the year of a new bottle
* Realised that searching on firebase is not possible (without programs as elasticsearch etc). Only option for searching is with exact queries, which is undesirable

## day 18 (Tuesday 24)
* Show errors instead of a toast at Login and Signup activities. These show what went wrong.
* Implemented hide keys (of the searchbar. UPDATE searchbar was eventually deleted and the hide key function with it)

## day 17 (Wednesday 25)
* Created the open chat! 

## day 18 and 19 (Thursday 26 and Friday 27)
* Made the chats private, but still struggling with the datastructure. Goal is to always enter the same chat with other user
* Started with the counter for unread messages, got halfway but started with other functionality
* Created the duplicate (otherway around) cat ID's

## day 20 and 21 (Saturday 28 and Sunday 29)
* Fixed some bugs, e.g. listview doubling the chat names because the arraylist was not cleared or correctly updated

## day 22 (Monday 30)
* Added last functionality such as alertdialogs

## day 23 (Tuesday 31)
* Just focussed on cleaning code

## day 24 (Wednesday 1)
* Fixed last bug, needed to notify the adapter that the arraylist changed
* Improved code

## day 25 (Thursday 2)
* Finished commenting and cleaning all code
* Wrote the report, added license and updated readme


