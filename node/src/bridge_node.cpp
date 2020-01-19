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
int i=0; //counter for the number of gamma_i
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
      std::string name = "gamma";  
        
      for(int j=0; j<size; j++){
      //cout<<msg->scene[j].name_config<<"\n"; 
	      if (msg->scene[j].name_config.find("BED")==0){
			//cout<<"ENTRA NEL IF BED" <<"\n"; 
			i++;
	      		item.gamma_i=name+ to_string(i);
			item.degreeBed=msg->scene[j].degreeOrientation;
	 		item.degreeRoof=0;
	 		item.degreeChair=0;
	 		item.degreeNot=0;
	 		item.degreePin=0;
	 		item.degreeTable=0;
			//cout<<item;
			itemsInAScene.push_back(item);

		        //srv.request.test_request.items.push_back(item);
			//itemsInAScene[j]=item;
			//cout<<itemsInAScene;
			if (msg->scene[j].pin!=0){
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

		        relation.gamma_subject=msg->scene[j].name_config;
      		        relation.gamma_object="Pin" + std::to_string(msg->scene[j].pin);
			relation.nameRelation= msg->scene[j].nameRelation;
			relation.degreeRelation= msg->scene[j].legPinRelationDegree;
			relationsInAScene.push_back(relation);
		      
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
				
				//srv.request.test_request.items.push_back(item);
		      }
		      relation.gamma_subject="Pin" + std::to_string(msg->scene[j].pin);
      		      relation.gamma_object= msg->scene[j].table;
		      relation.nameRelation= msg->scene[j].nameRelation;
		      relation.degreeRelation=msg->scene[j].pinTableRelationDegree;
		      relationsInAScene.push_back(relation);
	      }




	      else if (msg->scene[j].name_config.find("ROOF")==0){
			//cout<<"ENTRA NEL IF ROOF" <<"\n";
			i++;
	      		item.gamma_i=name+ to_string(i);
			item.degreeBed=0;
	 		item.degreeRoof=msg->scene[j].degreeOrientation;
	 		item.degreeChair=0;
	 		item.degreeNot=0;
	 		item.degreePin=0;
	 		item.degreeTable=0;
		
			itemsInAScene.push_back(item);

			if (msg->scene[j].pin!=0){
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

		        relation.gamma_subject=msg->scene[j].name_config;
      		        relation.gamma_object="Pin" + std::to_string(msg->scene[j].pin);
			relation.nameRelation= msg->scene[j].nameRelation;
			relation.degreeRelation= msg->scene[j].legPinRelationDegree;
			relationsInAScene.push_back(relation);
		      
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
				
				//srv.request.test_request.items.push_back(item);
		      }
		      relation.gamma_subject="Pin" + std::to_string(msg->scene[j].pin);
      		      relation.gamma_object= msg->scene[j].table;
		      relation.nameRelation= msg->scene[j].nameRelation;
		      relation.degreeRelation=msg->scene[j].pinTableRelationDegree;
		      relationsInAScene.push_back(relation);
		
	      }



	      else if (msg->scene[j].name_config.find("CHAIR")==0){
			//cout<<"ENTRA NEL IF CHAIR" <<"\n";
			i++;
	      		item.gamma_i=name+ to_string(i);
			item.degreeBed=0;
	 		item.degreeRoof=0;
	 		item.degreeChair=msg->scene[j].degreeOrientation;
	 		item.degreeNot=0;
	 		item.degreePin=0;
	 		item.degreeTable=0;
		
			itemsInAScene.push_back(item);

			if (msg->scene[j].pin!=0){
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

		        relation.gamma_subject=msg->scene[j].name_config;
      		        relation.gamma_object="Pin" + std::to_string(msg->scene[j].pin);
			relation.nameRelation= msg->scene[j].nameRelation;
			relation.degreeRelation= msg->scene[j].legPinRelationDegree;
			relationsInAScene.push_back(relation);
		      
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
				
				//srv.request.test_request.items.push_back(item);
		      }
		      relation.gamma_subject="Pin" + std::to_string(msg->scene[j].pin);
      		      relation.gamma_object= msg->scene[j].table;
		      relation.nameRelation= msg->scene[j].nameRelation;
		      relation.degreeRelation=msg->scene[j].pinTableRelationDegree;
		      relationsInAScene.push_back(relation);
	      }




	      else if (msg->scene[j].name_config.find("NOT")==0){
			//cout<<"ENTRA NEL IF NOT" <<"\n";
			i++;
	      		item.gamma_i=name+ to_string(i);
			item.degreeBed=0;
	 		item.degreeRoof=0;
	 		item.degreeChair=0;
	 		item.degreeNot=msg->scene[j].degreeOrientation;
	 		item.degreePin=0;
	 		item.degreeTable=0;
		
			itemsInAScene.push_back(item);

			if (msg->scene[j].pin!=0){
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

		        relation.gamma_subject=msg->scene[j].name_config;
      		        relation.gamma_object="Pin" + std::to_string(msg->scene[j].pin);
			relation.nameRelation= msg->scene[j].nameRelation;
			relation.degreeRelation= msg->scene[j].legPinRelationDegree;
			relationsInAScene.push_back(relation);
		      
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
				
				//srv.request.test_request.items.push_back(item);
		      }
		      relation.gamma_subject="Pin" + std::to_string(msg->scene[j].pin);
      		      relation.gamma_object= msg->scene[j].table;
		      relation.nameRelation= msg->scene[j].nameRelation;
		      relation.degreeRelation=msg->scene[j].pinTableRelationDegree;
		      relationsInAScene.push_back(relation);
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
      //std::cout << frameInstant << std::endl;
          
    for(vector<fuzzy_sit_memory_msgs::SceneItem>::iterator it = itemsInAScene.begin(); it != itemsInAScene.end(); ++it){
    	srv.request.test_request.items.push_back(*it);
	//cout<<*it;
    }

    for(vector<fuzzy_sit_memory_msgs::Relations>::iterator ite = relationsInAScene.begin(); ite != relationsInAScene.end(); ++ite){
    	srv.request.test_request.relations.push_back(*ite);
	//cout<<*it;
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
