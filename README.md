# ExpandableRecyclerview
An ExpandableRecycleradapter with Recyclerview

#Screenshots
![effict](/sample-screen.gif)
  

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ExpandableRecyclerview-green.svg?style=true)](https://android-arsenal.com/details/1/3903)

##Gradle
 ```
 compile 'com.zaihuishou:expandablerecycleradapter:1.0.0'
 ```
 
##Maven
 
 ```
 <dependency>
   <groupId>com.zaihuishou</groupId>
   <artifactId>expandablerecycleradapter</artifactId>
   <version>1.0.0</version>
   <type>pom</type>
 </dependency>
 ```
 
#Useage

  * Expandable item:</br>
  
    data model class must `implement` [ParentListItem](https://github.com/zaihuishou/ExpandableRecyclerview/blob/master/expandablerecycleradapter/src/main/java/com/zaihuishou/expandablerecycleradapter/model/ParentListItem.java),and viewhold class must `extend` [AbstractParentAdapterItem](https://github.com/zaihuishou/ExpandableRecyclerview/blob/master/expandablerecycleradapter/src/main/java/com/zaihuishou/expandablerecycleradapter/viewholder/AbstractParentAdapterItem.java)
    
       example:`public class Company implements ParentListItem` and `public class CompanyItem extendsAbstractParentAdapterItem`
   
* Normal item</br>

   viewholder `extend` [AbstractAdapterItem](https://github.com/zaihuishou/ExpandableRecyclerview/blob/master/expandablerecycleradapter/src/main/java/com/zaihuishou/expandablerecycleradapter/viewholder/AbstractAdapterItem.java)</br>
   

   example:`public class EmployeeItem extends AbstractAdapterItem`
    
Extends AbstractParentAdapterItem or AbstractAdapterItem,if you want item can expandable ,you should use first and implement 
 
 
#Thanks
* [bignerdranch/expandable-recycler-view](https://github.com/bignerdranch/expandable-recycler-view)
* [zaihuishou/RcvAdapter](https://github.com/zaihuishou/RcvAdapter)
 
##License
 
 ```
 Copyright 2016 zaihuishou
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressor implied.See the License for the specific language governing permissions and limitations under the License.
 ```