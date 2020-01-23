#include "ros/ros.h"
#include "std_msgs/String.h"
#include <iostream>  
#include <string> 
#include <sstream>
//Include the messages in the folder msgs
#include <fuzzy_sit_memory_msgs/Scene.h>
//#include <fuzzy_sit_memory_msgs/FuzzyDegree.h>
#include <fuzzy_sit_memory_msgs/SceneItem.h>
#include <fuzzy_sit_memory_msgs/Relations.h>
//The header file generated from the srv file where there is the service
#include <fuzzy_sit_memory_msgs/TestServiceDirective.h>
#include <vision/Configuration.h>
#include <vision/SceneTable.h>
#include <vector>
using namespace std;
ros::ServiceClient *clientPtr; //pointer for a client (The client is instantiated in the main function, but it needs to be used in the callback function. I made a ros::ServiceClient pointer at a global level (w.r.t. the node) and then gave it the address of the client. In the callback function I dereference the client pointer and use it to request a service.)
int i=0; //counter for the number of gamma_i for the leg
int p=0; //counter for the number of gamma_i for the pin
//Boolean variables to check if a table and a pin has been already put in the message (only one of each must to send to the service)
bool table=false;
bool pin=false;

void SUBSCRIBE_CALLBACK_FUNCTION (const vision::SceneTable::ConstPtr& msg)
{ 

      int size= msg->scene.size(); 
      fuzzy_sit_memory_msgs::TestServiceDirective srv;
      fuzzy_sit_memory_msgs::Scene::Ptr sceneToSend(new fuzzy_sit_memory_msgs::Scene);
      //fuzzy_sit_memory_msgs::SceneItem itemsInAScene[size];
      vector<fuzzy_sit_memory_msgs::SceneItem> itemsInAScene;
      vector<fuzzy_sit_memory_msgs::Relations> relationsInAScene;
      fuzzy_sit_memory_msgs::SceneItem item;
      fuzzy_sit_memory_msgs::Relations relation;
      int sizeForFuzzyDegreeArray=size*2;
      int frameInstant=0;
      //fuzzy_sit_memory_msgs::FuzzyDegree arrayOfFuzzyDegree[sizeForFuzzyDegreeArray];
      //i++;
      string gamma_leg_for_relation;
      string gamma_pin_for_relation;

        
      for(int j=0; j<size; j++){
      //cout<<msg->scene[j].name_config<<"\n";



	//////////ITEMS PART OF THE MESSAGE///////////////////////////////////////////////////

 
	      if (msg->scene[j].name_config.find("BED")==0){
			//cout<<"ENTRA NEL IF BED" <<"\n"; 
			i++;
			gamma_leg_for_relation="g_"+ to_string(i);
	      		item.gamma_i= gamma_leg_for_relation;
			item.degreeBed=msg->scene[j].degreeOrientation;
	 		item.degreeRoof=0;
	 		item.degreeChair=0;
	 		item.degreeNot=0;
	 		item.degreePin_1=0;
			item.degreePin_2=0;
			item.degreePin_3=0;
			item.degreePin_4=0;
			item.degreePin_5=0;
			item.degreePin_6=0;
			item.degreePin_7=0;
			item.degreePin_8=0;
			item.degreePin_9=0;
			item.degreePin_10=0;
			item.degreePin_11=0;
			item.degreePin_12=0;
		 	item.degreeTable=0;
			//cout<<item;
			itemsInAScene.push_back(item);

		        //srv.request.test_request.items.push_back(item);
			//itemsInAScene[j]=item;
			//cout<<itemsInAScene;
			
	      }




	      else if (msg->scene[j].name_config.find("ROOF")==0){
			//cout<<"ENTRA NEL IF ROOF" <<"\n";
			i++;
			gamma_leg_for_relation="g_"+ to_string(i);
	      		item.gamma_i=gamma_leg_for_relation;
			item.degreeBed=0;
	 		item.degreeRoof=msg->scene[j].degreeOrientation;
	 		item.degreeChair=0;
	 		item.degreeNot=0;
	 		item.degreePin_1=0;
			item.degreePin_2=0;
			item.degreePin_3=0;
			item.degreePin_4=0;
			item.degreePin_5=0;
			item.degreePin_6=0;
			item.degreePin_7=0;
			item.degreePin_8=0;
			item.degreePin_9=0;
			item.degreePin_10=0;
			item.degreePin_11=0;
			item.degreePin_12=0;
	 		item.degreeTable=0;
		
			itemsInAScene.push_back(item);	      
		        
		
	      }



	      else if (msg->scene[j].name_config.find("CHAIR")==0){
			//cout<<"ENTRA NEL IF CHAIR" <<"\n";
			i++;
	      		gamma_leg_for_relation="g_"+ to_string(i);
	      		item.gamma_i=gamma_leg_for_relation;
			item.degreeBed=0;
	 		item.degreeRoof=0;
	 		item.degreeChair=msg->scene[j].degreeOrientation;
	 		item.degreeNot=0;
	 		item.degreePin_1=0;
			item.degreePin_2=0;
			item.degreePin_3=0;
			item.degreePin_4=0;
			item.degreePin_5=0;
			item.degreePin_6=0;
			item.degreePin_7=0;
			item.degreePin_8=0;
			item.degreePin_9=0;
			item.degreePin_10=0;
			item.degreePin_11=0;
			item.degreePin_12=0;
	 		item.degreeTable=0;
		
			itemsInAScene.push_back(item);	      
		        
		      
	      }




	      else if (msg->scene[j].name_config.find("NOT")==0){
			//cout<<"ENTRA NEL IF NOT" <<"\n";
			i++;
	      		gamma_leg_for_relation="g_"+ to_string(i);
	      		item.gamma_i=gamma_leg_for_relation;
			item.degreeBed=0;
	 		item.degreeRoof=0;
	 		item.degreeChair=0;
	 		item.degreeNot=msg->scene[j].degreeOrientation;
	 		item.degreePin_1=0;
			item.degreePin_2=0;
			item.degreePin_3=0;
			item.degreePin_4=0;
			item.degreePin_5=0;
			item.degreePin_6=0;
			item.degreePin_7=0;
			item.degreePin_8=0;
			item.degreePin_9=0;
			item.degreePin_10=0;
			item.degreePin_11=0;
			item.degreePin_12=0;
	 		item.degreeTable=0;
		
			itemsInAScene.push_back(item);				
				
		      
	      }
      		//if (msg->scene[j].name_config.find('_')!=0){
		      
		      /*if (msg->scene[j].pin!=0){
			//cout<<"ENTRA NEL IF PIN" <<"\n";		
			if (!pin){
				pin=true;			
				i++;
		      		item.gamma_i=name+ to_string(i);
				item.degreeBed=0;
		 		item.degreeRoof=0;
		 		item.degreeChair=0;
		 		item.degreeNot=0;
		 		item.degreePin=1;
		 		item.degreeTable=0;
				//cout<<item;
				itemsInAScene.push_back(item);
				//srv.request.test_request.items.push_back(item);
			  }
	      		}

		      if ((msg->scene[j].table.find("Table")==0) && !table){
				//cout<<"ENTRA NEL IF TABLE" <<"\n";
				table=true;
				i++;
		      		item.gamma_i=name+ to_string(i);
				item.degreeBed=0;
		 		item.degreeRoof=0;
		 		item.degreeChair=0;
		 		item.degreeNot=0;
		 		item.degreePin=0;
		 		item.degreeTable=1;
				//cout<<item;
				itemsInAScene.push_back(item);
				relation.gamma_subject=msg->
      				relation.gamma_object
				//srv.request.test_request.items.push_back(item);
		      }*/
		//}		
      		//else{
	      
			//cout<<"ENTRA NEL IF LEG SENZA TIPO" <<"\n";
      		//}
      
      		if (msg->scene[j].pin!=0){
			//cout<<"ENTRA NEL IF PIN" <<"\n";		
			//if (!pin){
				//pin=true;			
				p++;
				switch (msg->scene[j].pin)
				{
					case 1:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=1;
						item.degreePin_2=0;
						item.degreePin_3=0;
						item.degreePin_4=0;
						item.degreePin_5=0;
						item.degreePin_6=0;
						item.degreePin_7=0;
						item.degreePin_8=0;
						item.degreePin_9=0;
						item.degreePin_10=0;
						item.degreePin_11=0;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 2:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=1;
						item.degreePin_3=0;
						item.degreePin_4=0;
						item.degreePin_5=0;
						item.degreePin_6=0;
						item.degreePin_7=0;
						item.degreePin_8=0;
						item.degreePin_9=0;
						item.degreePin_10=0;
						item.degreePin_11=0;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 3:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=0;
						item.degreePin_3=1;
						item.degreePin_4=0;
						item.degreePin_5=0;
						item.degreePin_6=0;
						item.degreePin_7=0;
						item.degreePin_8=0;
						item.degreePin_9=0;
						item.degreePin_10=0;
						item.degreePin_11=0;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 4:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=0;
						item.degreePin_3=0;
						item.degreePin_4=1;
						item.degreePin_5=0;
						item.degreePin_6=0;
						item.degreePin_7=0;
						item.degreePin_8=0;
						item.degreePin_9=0;
						item.degreePin_10=0;
						item.degreePin_11=0;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 5:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=0;
						item.degreePin_3=0;
						item.degreePin_4=0;
						item.degreePin_5=1;
						item.degreePin_6=0;
						item.degreePin_7=0;
						item.degreePin_8=0;
						item.degreePin_9=0;
						item.degreePin_10=0;
						item.degreePin_11=0;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 6:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=0;
						item.degreePin_3=0;
						item.degreePin_4=0;
						item.degreePin_5=0;
						item.degreePin_6=1;
						item.degreePin_7=0;
						item.degreePin_8=0;
						item.degreePin_9=0;
						item.degreePin_10=0;
						item.degreePin_11=0;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 7:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=0;
						item.degreePin_3=0;
						item.degreePin_4=0;
						item.degreePin_5=0;
						item.degreePin_6=0;
						item.degreePin_7=1;
						item.degreePin_8=0;
						item.degreePin_9=0;
						item.degreePin_10=0;
						item.degreePin_11=0;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 8:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=0;
						item.degreePin_3=0;
						item.degreePin_4=0;
						item.degreePin_5=0;
						item.degreePin_6=0;
						item.degreePin_7=0;
						item.degreePin_8=1;
						item.degreePin_9=0;
						item.degreePin_10=0;
						item.degreePin_11=0;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 9:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=0;
						item.degreePin_3=0;
						item.degreePin_4=0;
						item.degreePin_5=0;
						item.degreePin_6=0;
						item.degreePin_7=0;
						item.degreePin_8=0;
						item.degreePin_9=1;
						item.degreePin_10=0;
						item.degreePin_11=0;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 10:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=0;
						item.degreePin_3=0;
						item.degreePin_4=0;
						item.degreePin_5=0;
						item.degreePin_6=0;
						item.degreePin_7=0;
						item.degreePin_8=0;
						item.degreePin_9=0;
						item.degreePin_10=1;
						item.degreePin_11=0;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 11:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=0;
						item.degreePin_3=0;
						item.degreePin_4=0;
						item.degreePin_5=0;
						item.degreePin_6=0;
						item.degreePin_7=0;
						item.degreePin_8=0;
						item.degreePin_9=0;
						item.degreePin_10=0;
						item.degreePin_11=1;
						item.degreePin_12=0;
				 		item.degreeTable=0;
					break;
					case 12:
						gamma_pin_for_relation="p_"+ to_string(p);
						item.gamma_i= gamma_pin_for_relation;
						item.degreeBed=0;
				 		item.degreeRoof=0;
				 		item.degreeChair=0;
				 		item.degreeNot=0;
				 		item.degreePin_1=0;
						item.degreePin_2=0;
						item.degreePin_3=0;
						item.degreePin_4=0;
						item.degreePin_5=0;
						item.degreePin_6=0;
						item.degreePin_7=0;
						item.degreePin_8=0;
						item.degreePin_9=0;
						item.degreePin_10=0;
						item.degreePin_11=0;
						item.degreePin_12=1;
				 		item.degreeTable=0;
					break;

					default:
						ROS_ERROR("INVALID PIN (Pin: %d)!!!!!!!!!!!!!!!!", pin);
					break;
				}

		      		
				//cout<<item;
				itemsInAScene.push_back(item);
				//srv.request.test_request.items.push_back(item);
			  //}
	      		}
			
		        if ((msg->scene[j].table.find("Table")==0) && !table){
				//cout<<"ENTRA NEL IF TABLE" <<"\n";
				table=true;
		      		item.gamma_i="t";
				item.degreeBed=0;
		 		item.degreeRoof=0;
		 		item.degreeChair=0;
		 		item.degreeNot=0;
		 		item.degreePin_1=0;
				item.degreePin_2=0;
				item.degreePin_3=0;
				item.degreePin_4=0;
				item.degreePin_5=0;
				item.degreePin_6=0;
				item.degreePin_7=0;
				item.degreePin_8=0;
				item.degreePin_9=0;
				item.degreePin_10=0;
				item.degreePin_11=0;
				item.degreePin_12=0;
		 		item.degreeTable=1;
				//cout<<item;
				itemsInAScene.push_back(item);
				
				//srv.request.test_request.items.push_back(item);
		      }

			///////////////////////////////////////////////////////////////////////////////



			////RELATION PART OF THE MESSAGE///////////////////////////////////////////////
		        relation.gamma_subject=gamma_leg_for_relation;
      		        relation.gamma_object=gamma_pin_for_relation;
			relation.nameRelation= msg->scene[j].nameRelation;
			relation.degreeRelation= msg->scene[j].legPinRelationDegree;
			relationsInAScene.push_back(relation);

			relation.gamma_subject=gamma_pin_for_relation;
      		      	relation.gamma_object= "t";
		      	relation.nameRelation= msg->scene[j].nameRelation;
		      	relation.degreeRelation=msg->scene[j].pinTableRelationDegree;
		      	relationsInAScene.push_back(relation);
  
		      	/////////////////////////////////////////////////////////////////////////////
		      
      frameInstant=msg->frame;

     /*arrayOfFuzzyDegree[j].valueChair=msg->scene[j].name_config;
      arrayOfFuzzyDegree[j].degreeChair=msg->scene[j].degreeOrientation;
      //arrayOfFuzzyDegree[j].degreeChair=0.9;
      arrayOfFuzzyDegree[j].valuePin=std::to_string(msg->scene[j].pin);
      arrayOfFuzzyDegree[j].degreePin=1.0;
      arrayOfFuzzyDegree[j].table=msg->scene[j].table;
      arrayOfFuzzyDegree[j].degreeTable=1.0;
      arrayOfFuzzyDegree[j].nameRelation=msg->scene[j].nameRelation;
      arrayOfFuzzyDegree[j].pinTableRelationDegree=msg->scene[j].pinTableRelationDegree;
      arrayOfFuzzyDegree[j].legPinRelationDegree=msg->scene[j].legPinRelationDegree;
      //std::cout << arrayOfFuzzyDegree[j] << std::endl;
      item.gamma_i=name + std::to_string(i);
      item.degrees.push_back(*(arrayOfFuzzyDegree+j)); 
      frameInstant=msg->frame;*/
         
      }
      //sceneToSend.items.push_back(item); 
      //std::cout << item << std::endl;
	//std::cout<<itemsInAScene;
      //std::cout << relation << std::endl;*/
      std::cout << "Frame:" << frameInstant << std::endl;
          cout <<"\n";
    for(vector<fuzzy_sit_memory_msgs::SceneItem>::iterator it = itemsInAScene.begin(); it != itemsInAScene.end(); ++it){
    	//srv.request.test_request.items.push_back(*it);
	cout<<*it;
	cout<<"\n";
    }

    for(vector<fuzzy_sit_memory_msgs::Relations>::iterator ite = relationsInAScene.begin(); ite != relationsInAScene.end(); ++ite){
    	//srv.request.test_request.relations.push_back(*ite);
	cout<<*ite;
	cout<<"\n";
    }
      //srv.request.test_request.items.push_back(itemsInAScene);
      //srv.request.test_request.items.push_back(relation);
      //srv.request.test_request.items.push_back(itemsInAScene);
      srv.request.test_request.frame=frameInstant;
      //srv.request.test_request.items.push_back( );

      ros::ServiceClient client = (ros::ServiceClient)*clientPtr; //dereference the clientPtr

      if(client.call(srv)) //request service from the client
      {
          ROS_INFO("Success");
          
      }   
      else
      {
          ROS_ERROR("Error");    
      }

}

int main(int argc, char **argv)
{
    ros::init(argc, argv, "bridge_node");
    ros::NodeHandle n;

    ros::ServiceClient client = n.serviceClient<fuzzy_sit_memory_msgs::TestServiceDirective>("memory_service"); //create the client

    clientPtr = &client; //give the address of the client to the clientPtr

    ros::Subscriber sub = n.subscribe<vision::SceneTable>("scene_data", 1000, SUBSCRIBE_CALLBACK_FUNCTION); //subscribing

    ros::spin();
/*while(n.ok()) {

        ros::spinOnce();

    }*/
    return 0;
}
