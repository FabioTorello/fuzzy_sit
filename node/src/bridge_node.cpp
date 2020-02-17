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
//#include <vision/Configuration.h>
//#include <vision/SceneTable.h>
#include <vision/SceneToSIT.h>
#include <vision/Configuration_SIT.h>
#include <vector>
using namespace std;
ros::ServiceClient *clientPtr; //pointer for a client (The client is instantiated in the main function, but it needs to be used in the callback function. I made a ros::ServiceClient pointer at a global level (w.r.t. the node) and then gave it the address of the client. In the callback function I dereference the client pointer and use it to request a service.)




void SUBSCRIBE_CALLBACK_FUNCTION (const vision::SceneToSIT::ConstPtr& msg)
{ 

      ros::ServiceClient client = (ros::ServiceClient)*clientPtr; //dereference the clientPtr

      int size_items = msg->items.size(); 
      int size_relations = msg->relations.size(); 
      fuzzy_sit_memory_msgs::TestServiceDirective srv;
      //fuzzy_sit_memory_msgs::Scene::Ptr sceneToSend(new fuzzy_sit_memory_msgs::Scene);
      //fuzzy_sit_memory_msgs::SceneItem itemsInAScene[size];
      vector<fuzzy_sit_memory_msgs::SceneItem> itemsInAScene;
      vector<fuzzy_sit_memory_msgs::Relations> relationsInAScene;
      fuzzy_sit_memory_msgs::SceneItem item;
      fuzzy_sit_memory_msgs::Relations relation;
      //int sizeForFuzzyDegreeArray=size*2;
      long frameInstant;
      std::string scene_name;
    
      std::cout<<"THIS IS THE SERVICE RESPONSE: "<< srv.response.test_response<<"\n";
      frameInstant=msg->frame;

      //If the bagfile is not finished
      if(frameInstant!=-1){
	      //////////ITEMS PART OF THE MESSAGE///////////////////////////////////////////////////
	      for(int j=0; j<size_items; j++){
	      	
		item.gamma_i= msg->items[j].gamma_i;
		item.type= msg->items[j].type;
		item.degree= msg->items[j].degree;
		itemsInAScene.push_back(item);


	      }

	      //////////RELATIONS PART OF THE MESSAGE///////////////////////////////////////////////////
	      for(int j=0; j<size_relations; j++){
	      
	      	relation.gamma_subject=msg->relations[j].gamma_subject;
		relation.gamma_object=msg->relations[j].gamma_object;
		relation.nameRelation= msg->relations[j].nameRelation;
		relation.degreeRelation= msg->relations[j].degreeRelation;
		relationsInAScene.push_back(relation);
	  

	      }

      	      scene_name=msg->scene_name;	      
      


   
      
	    //Send the items part of the message
	    for(vector<fuzzy_sit_memory_msgs::SceneItem>::iterator it = itemsInAScene.begin(); it != itemsInAScene.end(); ++it){
	    	srv.request.test_request.items.push_back(*it);
		//cout<<*it;
		//cout<<"\n";
	    }
	    //Send the relations part of the message
	    for(vector<fuzzy_sit_memory_msgs::Relations>::iterator ite = relationsInAScene.begin(); ite != relationsInAScene.end(); ++ite){
	    	srv.request.test_request.relations.push_back(*ite);
		//cout<<*ite;
		//cout<<"\n";
	    }
      
	      srv.request.test_request.frame=frameInstant;
	      srv.request.test_request.scene_name=scene_name;

	      
      }

      //If bagfile is finished so there is no more information
      if(frameInstant==-1){
	//vectors for relations and for items and scene_name will not be sent but only the frameInstant will be 
	srv.request.test_request.frame=frameInstant;
	
	
	
      }
     
      if(client.call(srv)) //request service from the client
      {
		ROS_INFO("Success");
		std::cout<<"THIS IS THE SERVICE RESPONSE: "<< srv.response.test_response<<"\n";
		  
      }   
      else
      {
          ROS_ERROR("Error");
    
      }
      
      itemsInAScene.clear();
      relationsInAScene.clear();
      
      

}

int main(int argc, char **argv)
{
    ros::init(argc, argv, "bridge_node");
    
    ros::NodeHandle n;

    ros::ServiceClient client = n.serviceClient<fuzzy_sit_memory_msgs::TestServiceDirective>("memory_service"); //create the client

    clientPtr = &client; //give the address of the client to the clientPtr

    ros::Subscriber sub = n.subscribe<vision::SceneToSIT>("sceneSIT_data", 1000, SUBSCRIBE_CALLBACK_FUNCTION); //subscribing

    ros::spin();
/*while(n.ok()) {

        ros::spinOnce();

    }*/
    return 0;
}
