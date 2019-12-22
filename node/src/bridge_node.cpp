#include "ros/ros.h"
#include "std_msgs/String.h"
#include <iostream>  

#include <sstream>
//Include the messages in the folder msgs
#include <fuzzy_sit_memory_msgs/Scene.h>
#include <fuzzy_sit_memory_msgs/FuzzyDegree.h>
#include <fuzzy_sit_memory_msgs/SceneItem.h>
//The header file generated from the srv file where there is the service
#include <fuzzy_sit_memory_msgs/TestServiceDirective.h>
#include <vision/Configuration.h>
#include <vision/SceneTable.h>

ros::ServiceClient *clientPtr; //pointer for a client (The client is instantiated in the main function, but it needs to be used in the callback function. I made a ros::ServiceClient pointer at a global level (w.r.t. the node) and then gave it the address of the client. In the callback function I dereference the client pointer and use it to request a service.)
int i=0; //counter for the number of gamma_i


void SUBSCRIBE_CALLBACK_FUNCTION (const vision::SceneTable::ConstPtr& msg)
{ 

      int size= msg->scene.size(); 
      fuzzy_sit_memory_msgs::TestServiceDirective srv;
      fuzzy_sit_memory_msgs::Scene::Ptr sceneToSend(new fuzzy_sit_memory_msgs::Scene);
      fuzzy_sit_memory_msgs::SceneItem itemsInAScene[size];
      fuzzy_sit_memory_msgs::SceneItem item;
      int sizeForFuzzyDegreeArray=size*2;
      fuzzy_sit_memory_msgs::FuzzyDegree arrayOfFuzzyDegree[sizeForFuzzyDegreeArray];
      i++;
      std::string name = "g";
      for(int j=0; j<size; j++){
      arrayOfFuzzyDegree[j].valueChair=msg->scene[j].name_config;
      arrayOfFuzzyDegree[j].degreeChair=0.9;
      arrayOfFuzzyDegree[j].valuePin=std::to_string(msg->scene[j].pin);
      arrayOfFuzzyDegree[j].degreePin=1.0;
      //std::cout << arrayOfFuzzyDegree[j] << std::endl;
      item.gamma_i=name + std::to_string(i);
      item.degrees.push_back(*(arrayOfFuzzyDegree+j)); 
         
      }
      //sceneToSend.items.push_back(item); 
      std::cout << item << std::endl;
      
      srv.request.test_request.items.push_back(item);
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
